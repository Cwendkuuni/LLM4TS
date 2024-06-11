import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testutils.TestCompiler;
import com.google.javascript.rhino.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  @org.junit.Test
  public void testEnterScope() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  function bar() {" +
        "    var x = 3;" +
        "  }" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");

    // Check that 'x' in the function scope is renamed
    assertEquals("x$0", fooFunction.getChild(0).getString());
    assertEquals("x$1", findNode(fooFunction, NodeType.VAR, "x").getName().getString());

    // Check that 'y' is not renamed
    assertEquals("y", findNode(fooFunction, NodeType.VAR, "y").getName().getString());

    // Check that 'x' in the nested function 'bar' is renamed
    assertEquals("x$2", findNode(barFunction, NodeType.VAR, "x").getName().getString());
  }

  @org.junit.Test
  public void testExitScope() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {" +
        "    var x = 2;" +
        "  }" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");

    // Push 'foo' scope
    t.pushScope(fooFunction);

    // Check that 'x' in the function scope is renamed
    assertEquals("x$0", fooFunction.getChild(0).getString());
    assertEquals("x$0", findNode(fooFunction, NodeType.VAR, "x").getName().getString());

    // Pop 'foo' scope
    t.popScope();

    // Check that 'x' in the outer function 'bar' is not renamed
    assertEquals("x", findNode(barFunction, NodeType.VAR, "x").getName().getString());
  }

  @org.junit.Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "  }" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    // Check that 'bar' function is traversed
    assertTrue(makeDeclaredNamesUnique.shouldTraverse(t, findNode(rootNode, NodeType.FUNCTION, "bar"), null));
  }

  @org.junit.Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {" +
        "    var x = 2;" +
        "  }" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");
    Node xVarInFoo = findNode(fooFunction, NodeType.VAR, "x");
    Node xVarInBar = findNode(barFunction, NodeType.VAR, "x");

    // Check that 'x' in the function scope is renamed in 'foo'
    assertEquals("x$0", xVarInFoo.getName().getString());

    // Check that 'x' in the nested function 'bar' is renamed
    assertEquals("x$1", xVarInBar.getName().getString());

    // Check that 'x' in 'foo' is not renamed in 'bar'
    assertEquals("x", findNode(fooFunction, NodeType.VAR, "x").getName().getString());
  }

  @org.junit.Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    // Test an empty name
    assertEquals(null, makeDeclaredNamesUnique.getReplacementName(""));

    // Test a name that doesn't need to be renamed
    assertEquals("x", makeDeclaredNamesUnique.getReplacementName("x"));

    // Test a renamed name
    MakeDeclaredNamesUnique makeDeclaredNamesUniqueInstance = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUniqueInstance.addDeclaredName("x");
    assertEquals("x$0", makeDeclaredNamesUniqueInstance.getReplacementName("x"));
  }

  @org.junit.Test
  public void testStripConstIfReplaced() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    // Check that a non-renamed name does not have Constness removed
    NodeTraversal t = new NodeTraversal();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "const x = 1;";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node xVar = findNode(rootNode, NodeType.VAR, "x");
    assertFalse(xVar.hasProp(Node.IS_CONSTANT_NAME));

    // Check that a renamed name has Constness removed
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.addDeclaredName("x");

    inputCode = "const x = 1;";
    rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    xVar = findNode(rootNode, NodeType.VAR, "x");
    assertTrue(xVar.removeProp(Node.IS_CONSTANT_NAME));
  }

  @org.junit.Test
  public void testFindDeclaredNames() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    String inputCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    makeDeclaredNamesUnique.findDeclaredNames(rootNode, null, makeDeclaredNamesUnique);

    List<Node> vars = Lists.newArrayList(findAllNodes(rootNode, NodeType.VAR));
    ImmutableSet<String> declaredNames = ImmutableSet.copyOf(Arrays.asList(
        "x",
        "y"
    ));

    // Check that all declared names are added to the renamer
    assertEquals(declaredNames, makeDeclaredNamesUnique.names());
  }

  @org.junit.Test
  public void testEnterScopeWithFunctionDeclaration() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  function bar() {}" +
        "  var x = 1;" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");
    Node xVar = findNode(fooFunction, NodeType.VAR, "x");

    // Check that 'x' in the function scope is renamed
    assertEquals("x$0", xVar.getName().getString());

    // Check that 'bar' function is not renamed
    assertEquals("bar", barFunction.getName().getString());
  }

  @org.junit.Test
  public void testEnterScopeWithFunctionDeclarationAndVarDeclaration() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  function bar() {}" +
        "  var x = 1;" +
        "  var y = 2;" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");
    Node xVar = findNode(fooFunction, NodeType.VAR, "x");
    Node yVar = findNode(fooFunction, NodeType.VAR, "y");

    // Check that 'x' in the function scope is renamed
    assertEquals("x$0", xVar.getName().getString());

    // Check that 'y' in the function scope is renamed
    assertEquals("y$1", yVar.getName().getString());

    // Check that 'bar' function is not renamed
    assertEquals("bar", barFunction.getName().getString());
  }

  @org.junit.Test
  public void testEnterScopeWithVariableDeclarationAndFunctionDeclaration() {
    NodeTraversal t = new NodeTraversal();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    t.setScopedCallback(makeDeclaredNamesUnique);

    String inputCode = "function foo() {" +
        "  var x = 1;" +
        "  function bar() {}" +
        "}";

    TestCompiler compiler = new TestCompiler();
    Node rootNode = compiler.parse(inputCode, null, null);

    t.traverse(rootNode);

    Node fooFunction = findNode(rootNode, NodeType.FUNCTION, "foo");
    Node barFunction = findNode(rootNode, NodeType.FUNCTION, "bar");
    Node xVar = findNode(fooFunction, NodeType.VAR, "x");

    // Check that 'x' in the function scope is renamed
    assertEquals("x$0", xVar.getName().getString());

    // Check that 'bar' function is not renamed
    assertEquals("bar", barFunction.getName().getString());
  }
}