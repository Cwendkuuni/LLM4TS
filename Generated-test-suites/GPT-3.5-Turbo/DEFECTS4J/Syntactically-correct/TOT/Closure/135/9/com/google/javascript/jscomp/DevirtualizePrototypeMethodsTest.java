import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class DevirtualizePrototypeMethodsTest {

  private DevirtualizePrototypeMethods dpm;

  @Before
  public void setUp() {
    dpm = new DevirtualizePrototypeMethods(null);
  }

  @Test
  public void isCall_ValidCallExpression_ReturnsTrue() {
    Node node = new Node(Token.CALL);
    node.addChildToFront(new Node(Token.NAME, "test"));
    node.addChildToFront(new Node(Token.EMPTY));
    boolean result = dpm.isCall(new UseSite(node));
    assertTrue(result);
  }

  @Test
  public void isCall_InvalidCallExpression_ReturnsFalse() {
    Node node = new Node(Token.NAME, "test");
    boolean result = dpm.isCall(new UseSite(node));
    assertFalse(result);
  }

  @Test
  public void isPrototypeMethodDefinition_ValidNode_ReturnsTrue() {
    Node node = new Node(Token.GETPROP);
    Node prototype = new Node(Token.GETPROP);
    prototype.addChildToFront(new Node(Token.THIS));
    prototype.addChildToFront(new Node(Token.STRING, "prototype"));
    node.addChildToFront(prototype);
    Node functionNode = new Node(Token.FUNCTION);
    node.addChildToBack(functionNode);
    boolean result = dpm.isPrototypeMethodDefinition(node);
    assertTrue(result);
  }

  @Test
  public void isPrototypeMethodDefinition_InvalidNode_ReturnsFalse() {
    Node node = new Node(Token.GETPROP);
    boolean result = dpm.isPrototypeMethodDefinition(node);
    assertFalse(result);
  }

  @Test
  public void getRewrittenMethodName_ValidMethodName_ReturnsNewMethodName() {
    String originalMethodName = "test";
    String expectedNewMethodName = "JSCompiler_StaticMethods_test";
    String result = dpm.getRewrittenMethodName(originalMethodName);
    assertEquals(expectedNewMethodName, result);
  }

  @Test
  public void rewriteDefinitionIfEligible_EligibleDefinition_NoChangeInCode() {
    Node node = new Node(Token.NAME, "test");
    node.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(null);
    Definition definition = new Definition(node, null, null);
    DefinitionSite defSite = new DefinitionSite(definition, true, true, null);
    defFinder.addDefinitionSite(defSite);
    defFinder.addDefinition(definition);
    dpm.rewriteDefinitionIfEligible(defSite, defFinder);
    assertEquals(node, defSite.node);
  }

  @Test
  public void rewriteDefinitionIfEligible_IneligibleDefinition_NoChangeInCode() {
    Node node = new Node(Token.NAME, "test");
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(null);
    Definition definition = new Definition(node, null, null);
    DefinitionSite defSite = new DefinitionSite(definition, false, false, null);
    defFinder.addDefinitionSite(defSite);
    defFinder.addDefinition(definition);
    dpm.rewriteDefinitionIfEligible(defSite, defFinder);
    assertEquals(node, defSite.node);
  }

  @Test
  public void rewriteCallSites_ValidDefinition_ChangesInCode() {
    Node node = new Node(Token.NAME, "test");
    Node parent = new Node(Token.CALL);
    parent.addChildToFront(node);
    parent.addChildToBack(new Node(Token.EMPTY));
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(null);
    Definition definition = new Definition(node, null, null);
    defFinder.addUseSite(new UseSite(parent, definition, null));
    dpm.rewriteCallSites(defFinder, definition, "test2");
    Node expectedNewNode = new Node(Token.GETPROP);
    expectedNewNode.addChildToFront(new Node(Token.NAME, "test2"));
    expectedNewNode.addChildToFront(node);
    assertEquals(expectedNewNode, parent.getFirstChild());
  }

  @Test
  public void fixFunctionType_ValidFunctionNode_ChangesType() {
    Node functionNode = new Node(Token.FUNCTION);
    FunctionType type = new FunctionType();
    type.setTypeOfThis(JSTypeNative.UNKNOWN_TYPE);
    functionNode.setJSType(type);
    dpm.fixFunctionType(functionNode);
    assertNotNull(functionNode.getJSType());
    assertNotEquals(type, functionNode.getJSType());
  }

  @Test
  public void replaceReferencesToThis_ValidNode_ReplacesReferences() {
    Node node = new Node(Token.THIS);
    String name = "self";
    Node expectedNewNode = new Node(Token.NAME, name);
    dpm.replaceReferencesToThis(node, name);
    assertEquals(expectedNewNode, node);
  }
}