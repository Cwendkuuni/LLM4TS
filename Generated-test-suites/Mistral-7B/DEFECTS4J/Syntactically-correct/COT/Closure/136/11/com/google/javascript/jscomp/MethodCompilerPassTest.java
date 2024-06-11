import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass_EmptyExterns() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    Node root = null;
    Node externs = null;
    methodCompilerPass.process(externs, root);
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testMethodCompilerPass_SimpleFunctionDeclaration() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    String functionName = "testFunction";
    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(1);
    functionNode.setStartPosition(1);
    functionNode.setEndPosition(10);
    Node functionBody = new Node(Token.BLOCK);
    functionNode.setChildren(functionBody);
    root.setChildren(functionNode);
    methodCompilerPass.process(null, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals(functionName, methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals(functionName, methodCompilerPass.methodDefinitions.keySet().iterator().next());
    Node methodDefinition = methodCompilerPass.methodDefinitions.get(functionName);
    assertNotNull(methodDefinition);
  }

  @Test(expected = IllegalStateException.class)
  public void testMethodCompilerPass_UndefinedFunction() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    String functionName = "testFunction";
    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.NAME);
    functionNode.setLineNumber(1);
    functionNode.setStartPosition(1);
    functionNode.setEndPosition(5);
    functionNode.setString(functionName);
    root.setChildren(functionNode);
    methodCompilerPass.process(null, root);
  }

  @Test
  public void testMethodCompilerPass_FunctionDeclarationWithExtern() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    String functionName = "testFunction";
    Node root = new Node(Token.PROGRAM);
    Node externFunctionNode = new Node(Token.FUNCTION);
    externFunctionNode.setLineNumber(1);
    externFunctionNode.setStartPosition(1);
    externFunctionNode.setEndPosition(10);
    externFunctionNode.setChildren(new Node(Token.BLOCK));
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(2);
    functionNode.setStartPosition(2);
    functionNode.setEndPosition(12);
    Node functionBody = new Node(Token.BLOCK);
    functionNode.setChildren(functionBody);
    root.setChildren(externFunctionNode, functionNode);
    methodCompilerPass.process(null, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals(functionName, methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals(functionName, methodCompilerPass.methodDefinitions.keySet().iterator().next());
    Node methodDefinition = methodCompilerPass.methodDefinitions.get(functionName);
    assertNotNull(methodDefinition);
  }

  @Test
  public void testMethodCompilerPass_FunctionCall() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    String functionName = "testFunction";
    Node root = new Node(Token.PROGRAM);
    Node functionNode = createFunctionNode(functionName);
    Node callNode = new Node(Token.CALL);
    callNode.setLineNumber(1);
    callNode.setStartPosition(1);
    callNode.setEndPosition(5);
    callNode.setChildren(new Node(Token.NAME), functionNode);
    root.setChildren(callNode);
    methodCompilerPass.process(null, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals(functionName, methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals(functionName, methodCompilerPass.methodDefinitions.keySet().iterator().next());
    Node methodDefinition = methodCompilerPass.methodDefinitions.get(functionName);
    assertNotNull(methodDefinition);
  }

  @Test
  public void testMethodCompilerPass_FunctionCallWithExtern() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    String functionName = "testFunction";
    Node root = new Node(Token.PROGRAM);
    Node externFunctionNode = createFunctionNode(functionName);
    Node functionNode = createFunctionNode(functionName);
    Node callNode = new Node(Token.CALL);
    callNode.setLineNumber(1);
    callNode.setStartPosition(1);
    callNode.setEndPosition(5);
    callNode.setChildren(new Node(Token.NAME), functionNode);
    root.setChildren(externFunctionNode, callNode);
    methodCompilerPass.process(null, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals(functionName, methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals(functionName, methodCompilerPass.methodDefinitions.keySet().iterator().next());
    Node methodDefinition = methodCompilerPass.methodDefinitions.get(functionName);
    assertNotNull(methodDefinition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMethodCompilerPass_NullExterns() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    methodCompilerPass.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMethodCompilerPass_NullRoot() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
    methodCompilerPass.process(null, null);
  }

  private Node createFunctionNode(String functionName) {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setLineNumber(1);
    functionNode.setStartPosition(1);
    functionNode.setEndPosition(10);
    functionNode.setChildren(new Node(Token.BLOCK));
    return functionNode;
  }
}