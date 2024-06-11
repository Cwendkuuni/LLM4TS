import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeFactory;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclaration() {
    CompilerInput input = new CompilerInput("TestInput.js");
    Node root = NodeFactory.createRootNode(input);
    VarCheck varCheck = new VarCheck(null);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(input.getAstRoot(new Compiler())), varCheck);

    Node varNode = findNodeWithName(root, "validVar");
    assertNotNull(varNode);
  }

  @Test
  public void testUndefinedVariable() {
    CompilerInput input = new CompilerInput("TestInput.js");
    Node root = NodeFactory.createRootNode(input);
    VarCheck varCheck = new VarCheck(null);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(input.getAstRoot(new Compiler())), varCheck);

    Node varNode = findNodeWithName(root, "undefinedVar");
    assertNull(varNode);

    // Verify the diagnostic message is correct.
    assertEquals("variable undefinedVar is undefined",
        varCheck.getLastDiagnostic().getMessage());
  }

  @Test
  public void testVariableAccessAcrossModuleBoundaries() {
    CompilerInput input1 = new CompilerInput("ModuleA.js");
    Node root1 = NodeFactory.createRootNode(input1);
    VarCheck varCheck1 = new VarCheck(null);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(input1.getAstRoot(new Compiler())), varCheck1);

    CompilerInput input2 = new CompilerInput("ModuleB.js");
    Node root2 = NodeFactory.createRootNode(input2);
    VarCheck varCheck2 = new VarCheck(null);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(input2.getAstRoot(new Compiler())), varCheck2);

    Node varNode1 = findNodeWithName(root1, "sharedVar");
    Node varNode2 = findNodeWithName(root2, "sharedVar");
    assertNotNull(varNode1);
    assertNotNull(varNode2);

    // Verify the variable can be accessed across module boundaries.
    assertEquals(varNode1.getFirstChild().toInt32(), varNode2.getFirstChild().toInt32());

    // Verify a warning is emitted for the variable reference in ModuleB.
    assertEquals("missing module dependency; ModuleB.js should depend " +
        "on ModuleA.js because it references sharedVar",
        varCheck2.getLastDiagnostic().getMessage());
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    CompilerInput input = new CompilerInput("TestInput.js");
    Node root = NodeFactory.createRootNode(input);
    VarCheck varCheck = new VarCheck(null);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(input.getAstRoot(new Compiler())), varCheck);

    Node functionNode = findNodeWithName(root, "invalidFunction");
    assertNotNull(functionNode);

    // Verify the diagnostic message is correct.
    assertEquals("Syntax error: function declaration must have a name",
        varCheck.getLastDiagnostic().getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerInput inputA = new CompilerInput("ModuleA.js");
    Node rootA = NodeFactory.createRootNode(inputA);
    VarCheck varCheckA = new VarCheck(null, true);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(inputA.getAstRoot(new Compiler())), varCheckA);

    CompilerInput inputB = new CompilerInput("ModuleB.js");
    Node rootB = NodeFactory.createRootNode(inputB);
    VarCheck varCheckB = new VarCheck(null, true);

    NodeTraversal.traverseRoots(
        new Compiler(), Lists.newArrayList(inputB.getAstRoot(new Compiler())), varCheckB);

    Node varNodeA = findNodeWithName(rootA, "sharedVar");
    Node varNodeB = findNodeWithName(rootB, "sharedVar");
    assertNull(varNodeA);
    assertNotNull(varNodeB);

    // Verify the variable declaration in ModuleA is suppressed.
    assertTrue(varNodeB.isSuppressed("duplicate"));
  }

  private Node findNodeWithName(Node root, String name) {
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.getType() == Token.NAME &&
          currentNode.getString().equals(name)) {
        return currentNode;
      }
      currentNode = currentNode.getNextSibling();
    }
    return null;
  }
}