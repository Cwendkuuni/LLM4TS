import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    public NodeFactory getFactory() {
      return mock(NodeFactory.class);
    }

    @Override
    public CompilerContext getContext() {
      return mock(CompilerContext.class);
    }

    @Override
    public Node getExterns() {
      return null;
    }

    @Override
    public Node getRootNode() {
      return null;
    }

    @Override
    public Node getTopLevelCode() {
      return null;
    }

    @Override
    public Scope getTopLevelScope() {
      return mock(Scope.class);
    }

    @Override
    public void reportCheck(int checkType, Node node, int lineNumber, String message) {
      // Do nothing
    }

    @Override
    public void reportCodeChange() {
      // Do nothing
    }

    @Override
    public void reportTreeChange() {
      // Do nothing
    }

    @Override
    public void reportWarning(int warningCode, Node node, String message, int lineNumber) {
      // Do nothing
    }
  }

  @org.junit.Test
  public void testCollapseSingleVariableDeclaration() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("var a;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("var a;");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableDeclarations() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("var a; var b = 1; var c = 2;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("var a, b = 1, c = 2;");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithAssignment() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("var a = 1; var b = a;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("var a = 1, b = a;");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocInfo() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("/** @type {number} */ var a; /** @type {number} */ var b = 1;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("/** @type {number} */ var a, /** @type {number} */ b = 1;");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsInsideFunction() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("function foo() { var a; var b = 1; var c = 2; }");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("function foo() { var a, b = 1, c = 2; }");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsInsideIfStatement() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("if (true) { var a; var b = 1; }");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("if (true) { }");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithIdenticalNames() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("var a = 1; var a = 2;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("var a = 1;"); // Only the first declaration is kept

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithIdenticalNamesAndAssignment() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Node rootNode = Node.createTextNode("var a = 1; var a = a + 1;");

    collapseVariableDeclarations.process(null, rootNode);

    Node expectedResult = Node.createTextNode("var a = 1;"); // Only the first declaration is kept

    assertEquals(expectedResult.toString(), rootNode.toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithIdenticalNamesInDifferentScopes() {
    TestCompiler compiler = new TestCompiler();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    Scope topLevelScope = mock(Scope.class);
    when(topLevelScope.getVar("a")).thenReturn(null);

    Node rootNode = Node.createTextNode("function foo() { var a = 1; } var a = 2;");

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, null, null, topLevelScope);
    collapseVariableDeclarations.process(null, rootNode.getFirstChild());

    Node expectedResult = Node.createTextNode("function foo() { var a = 1; }");

    assertEquals(expectedResult.toString(), rootNode.toString());
  }
}