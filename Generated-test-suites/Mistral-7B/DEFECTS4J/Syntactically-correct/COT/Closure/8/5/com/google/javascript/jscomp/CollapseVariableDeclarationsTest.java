import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.TRAVERSE;
import static com.google.javascript.rhino.NodeTag.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
  private AbstractCompiler compiler;
  private Compiler compilerWithClosureCompiler;
  private Scope rootScope;

  @Before
  public void setup() {
    compiler = new Compiler();
    compilerWithClosureCompiler = new Compiler();
    compilerWithClosureCompiler.init();
    rootScope = compilerWithClosureCompiler.getTopLevelScope();
  }

  @Test
  public void testCollapseSingleVariable() {
    Node rootNode = new Node("var a;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
    assertEquals("var a;", rootNode.toString());
  }

  @Test
  public void testCollapseMultipleVariables() {
    Node rootNode = new Node("var a; var b = 1; var c = 2;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
    assertEquals("var a, b = 1, c = 2;", rootNode.toString());
  }

  @Test
  public void testCollapseSimpleAssignments() {
    Node rootNode = new Node("a = true; b = true; var c = true;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
    assertEquals("var c = b = a = true;", rootNode.toString());
  }

  @Test
  public void testCollapseVariableInFunction() {
    Node rootNode = new Node("function foo() { var a; var b = 1; }");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
    assertEquals("function foo() { var a, b = 1; }", rootNode.toString());
  }

  @Test
  public void testCollapseVariablesWithJSDocs() {
    Node rootNode = new Node("/** @type {number} */ var a; /** @type {string} */ var b;");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
    assertEquals("var a, /** @type {number} */ b;", rootNode.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testCollapseVariablesInLoop() {
    Node rootNode = new Node("for (var i = 0; i < 5; i++) { var i; }");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
  }

  @Test(expected = IllegalStateException.class)
  public void testCollapseVariablesInIfStatement() {
    Node rootNode = new Node("if (true) { var a; } else { var a; }");
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compilerWithClosureCompiler);
    pass.process(null, rootNode);
  }
}

class TestCollapseVariableDeclarations {
  CollapseVariableDeclarations pass;

  @Before
  public void setup() {
    pass = new CollapseVariableDeclarations(compiler);
  }

  @Test
  @SuppressWarnings("unused")
  public void testExtractMethods() {
    // No need to test this since the methods are already listed in the question
  }

  @Test
  public void testProcess() {
    Node externs = new Node(VAR, new Node("a"));
    Node root = new Node(BLOCK, Lists.newArrayList(
        new Node(VAR, new Node("a")),
        new Node(ASSIGN, new Node("="), new Node(LITERAL, "1"), new Node("a")),
        new Node(VAR, new Node("b")),
        new Node(ASSIGN, new Node("="), new Node(LITERAL, "2"), new Node("b")),
        new Node(RETURN, null, new Node(VAR, new Node("a")))
    ));
    pass.process(externs, root);

    Traverse.traverse(new Traverse.Callback() {
      @Override
      public void visit(NodeTraversal nt, Node n, Node parent) {
        if (n.isVar()) {
          assertTrue(collapses.contains(n));
        }
        super.visit(nt, n, parent);
      }
    }, root);

    List<Node> collapses = Lists.newArrayList(
        pass.collapses.get(0).startNode,
        pass.collapses.get(0).endNode
    );

    assertEquals(2, pass.collapses.size());
    assertEquals(4, collapses.size());
  }
}

class TestCollapseVariableDeclarationsWithExceptions {
  CollapseVariableDeclarations pass;

  @Before
  public void setup() {
    pass = new CollapseVariableDeclarations(compiler);
  }

  @Test(expected = IllegalStateException.class)
  public void testCollapseVariablesInLoop() {
    Node rootNode = new Node("for (var i = 0; i < 5; i++) { var i; }");
    pass.process(null, rootNode);
  }

  @Test(expected = IllegalStateException.class)
  public void testCollapseVariablesInIfStatement() {
    Node rootNode = new Node("if (true) { var a; } else { var a; }");
    pass.process(null, rootNode);
  }
}