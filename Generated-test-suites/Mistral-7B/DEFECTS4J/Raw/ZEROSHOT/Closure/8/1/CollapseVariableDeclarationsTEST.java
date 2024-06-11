import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Shim;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations.
 */
public class CollapseVariableDeclarationsTest {

  @Shim(clazz = CollapseVariableDeclarations.class)
  static class TestCollapseVariableDeclarations implements CollapseVariableDeclarations, CompilerPass {
    private AbstractCompiler compiler;

    TestCollapseVariableDeclarations(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    public List<CompilerPass> getDependencies() {
      return Lists.newArrayList();
    }
  }

  /**
   * Tests collapsing single variable declarations.
   */
  @org.junit.Test
  public void testCollapsingSingleVariableDeclarations() {
    String jsCode = "function foo() {"
        + "  var a; // should be collapsed into var a;"
        + "  var b = 1; // should be collapsed into var b = 1;"
        + "  var c = 2; // should be collapsed into var c = 2;"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    expectedVariableDeclarations.addChildToBack(new Node("a"));
    expectedVariableDeclarations.addChildToBack(new Node("b").setInt(1));
    expectedVariableDeclarations.addChildToBack(new Node("c").setInt(2));
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }

  /**
   * Tests collapsing multiple variable declarations into a single one.
   */
  @org.junit.Test
  public void testCollapsingMultipleVariableDeclarations() {
    String jsCode = "function foo() {"
        + "  var a; // should be collapsed into var a;"
        + "  var b = 1; // should be collapsed into var b = 1;"
        + "  var c = 2; // should be collapsed into var c = 2;"
        + "  var a, b = 1, c = 2; // should be collapsed into var a, b = 1, c = 2;"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    expectedVariableDeclarations.addChildToBack(new Node("a"));
    expectedVariableDeclarations.addChildToBack(new Node("b").setInt(1));
    expectedVariableDeclarations.addChildToBack(new Node("c").setInt(2));
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    Node variableDeclaration1 = rootNode.getFirstChild();
    assertEquals("var a, b = 1, c = 2", variableDeclaration1.toStringTree());

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }

  /**
   * Tests collapsing variable assignments.
   */
  @org.junit.Test
  public void testCollapsingVariableAssignments() {
    String jsCode = "function foo() {"
        + "  var a = true;"
        + "  var b = true;"
        + "  var c = true;"
        + "  a = false; // should not be collapsed as it's an assignment"
        + "  b = false; // should be collapsed into a = b = false"
        + "  c = false; // should be collapsed into a = c = false"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    Node expectedCollapsedAssignments = new Node(Token.ASSIGN);
    expectedAssignments.setLeft(new Node("a"));
    Node expectedRight = new Node(Token.ASSIGN);
    expectedRight.setRight(new Node("false"));
    expectedCollapsedAssignments.setRight(expectedRight);
    expectedVariableDeclarations.addChildToBack(new Node("a"));
    expectedVariableDeclarations.addChildToBack(expectedCollapsedAssignments);
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    Node variableDeclaration1 = rootNode.getFirstChild();
    Node variableDeclaration2 = variableDeclaration1.getNext();
    Node assignments = variableDeclaration2.getFirstChild();
    Node collapsedAssignments = assignments.getFirstChild();

    assertEquals("var a, b = a = false, c = false", variableDeclaration1.toStringTree());
    assertEquals("a = false, c = false", collapsedAssignments.toStringTree());

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }

  /**
   * Tests collapsing multiple assignments into one.
   */
  @org.junit.Test
  public void testCollapsingMultipleAssignments() {
    String jsCode = "function foo() {"
        + "  var a = true;"
        + "  var b = true;"
        + "  var c = true;"
        + "  a = false; // should not be collapsed as it's an assignment"
        + "  b = false; // should be collapsed into a = b = false"
        + "  c = false; // should be collapsed into a = c = false"
        + "  a = 1; // should not be collapsed as it's an assignment"
        + "  b = 1; // should be collapsed into a = b = 1"
        + "  c = 1; // should be collapsed into a = c = 1"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    Node expectedCollapsedAssignments = new Node(Token.ASSIGN);
    expectedAssignments.setLeft(new Node("a"));
    Node expectedRight1 = new Node(Token.ASSIGN);
    expectedRight1.setRight(new Node("false"));
    Node expectedRight2 = new Node(Token.ASSIGN);
    expectedRight2.setRight(new Node(Token.INT, 1));
    expectedCollapsedAssignments.setRight(expectedRight2);
    expectedVariableDeclarations.addChildToBack(new Node("a"));
    expectedVariableDeclarations.addChildToBack(expectedCollapsedAssignments);
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    Node variableDeclaration1 = rootNode.getFirstChild();
    Node assignments = variableDeclaration1.getNext().getFirstChild();
    Node collapsedAssignments = assignments.getFirstChild();

    assertEquals("var a, b = a = 1, c = 1", variableDeclaration1.toStringTree());
    assertEquals("a = 1, c = 1", collapsedAssignments.toStringTree());

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }

  /**
   * Tests collapsing variables in IF statements.
   */
  @org.junit.Test
  public void testCollapsingVariablesInIfStatements() {
    String jsCode = "function foo() {"
        + "  var a = 1; // should not be collapsed as it's not in an IF statement"
        + "  if (true) {"
        + "    var b = 1; // should be collapsed into var b = 1;"
        + "    var c = 1; // should be collapsed into var c = 1;"
        + "  }"
        + "  var d = 1; // should not be collapsed as it's not in an IF statement"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    Node expectedCollapsedVariables1 = new Node(Token.VAR);
    expectedCollapsedVariables1.addChildToBack(new Node("b"));
    expectedCollapsedVariables1.addChildToBack(new Node("c"));
    expectedVariableDeclarations.addChildToBack(expectedCollapsedVariables1);
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    Node variableDeclaration1 = rootNode.getFirstChild();
    Node ifStatement = variableDeclaration1.getNext();
    Node collapsedVariables = ifStatement.getFirstChild();

    assertEquals("var a, b = 1, c = 1", collapsedVariables.toStringTree());

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }

  /**
   * Tests collapsing variables in functions.
   */
  @org.junit.Test
  public void testCollapsingVariablesInFunctions() {
    String jsCode = "function foo() {"
        + "  function bar() {"
        + "    var a = 1;"
        + "    var b = 1;"
        + "    var c = 1;"
        + "  }"
        + "  var a = 1; // should not be collapsed as it's not in a function"
        + "  var d = 1; // should not be collapsed as it's not in a function"
        + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new TestCollapseVariableDeclarations(compiler));

    Node rootNode = AbstractCompiler.parseTree(jsCode, null);
    passManager.process(rootNode, null);

    Node expectedRoot = new Node(Token.BLOCK);
    Node expectedVariableDeclarations = new Node(Token.VAR);
    Node expectedCollapsedVariables = new Node(Token.VAR);
    expectedCollapsedVariables.addChildToBack(new Node("a"));
    expectedCollapsedVariables.addChildToBack(new Node("b"));
    expectedCollapsedVariables.addChildToBack(new Node("c"));
    expectedVariableDeclarations.addChildToBack(expectedCollapsedVariables);
    expectedRoot.addChildToBack(expectedVariableDeclarations);

    Node functionDeclaration = rootNode.getFirstChild();
    Node functionBody = functionDeclaration.getFirstChild();
    Node collapsedVariables = functionBody.getFirstChild();

    assertEquals("var a, b = 1, c = 1", collapsedVariables.toStringTree());

    assertEquals(expectedRoot.toStringTree(), rootNode.toStringTree());
  }
}