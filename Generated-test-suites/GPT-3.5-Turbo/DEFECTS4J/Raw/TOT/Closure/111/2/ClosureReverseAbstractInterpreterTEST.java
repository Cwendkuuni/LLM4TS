import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter();
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    // Typical use case: outcome is true, type is defined
    FlowScope blindScope = new FlowScope();
    Node condition = createIsDefNode("x");
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertEquals("x", result.getVariableType("x").getName());

    // Typical use case: outcome is false, type is defined
    blindScope = new FlowScope();
    condition = createIsDefNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertNull(result.getVariableType("x"));
    
    // Edge case: outcome is true, type is undefined
    blindScope = new FlowScope();
    condition = createIsDefNode("x");
    outcome = true;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());

    // Edge case: outcome is false, type is undefined
    blindScope = new FlowScope();
    condition = createIsDefNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    // Typical use case: outcome is true, type is null
    FlowScope blindScope = new FlowScope();
    Node condition = createIsNullNode("x");
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertNull(result.getVariableType("x"));

    // Typical use case: outcome is false, type is null
    blindScope = new FlowScope();
    condition = createIsNullNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertNotNull(result.getVariableType("x"));
    
    // Edge case: outcome is true, type is not null
    blindScope = new FlowScope();
    condition = createIsNullNode("x");
    outcome = true;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());

    // Edge case: outcome is false, type is not null
    blindScope = new FlowScope();
    condition = createIsNullNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    // Typical use case: outcome is true, type is defined and not null
    FlowScope blindScope = new FlowScope();
    Node condition = createIsDefAndNotNullNode("x");
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertNotNull(result.getVariableType("x"));

    // Typical use case: outcome is false, type is defined and not null
    blindScope = new FlowScope();
    condition = createIsDefAndNotNullNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(1, result.getVariableCount());
    assertTrue(result.hasVariable("x"));
    assertNull(result.getVariableType("x"));
    
    // Edge case: outcome is true, type is undefined and null
    blindScope = new FlowScope();
    condition = createIsDefAndNotNullNode("x");
    outcome = true;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());

    // Edge case: outcome is false, type is undefined and null
    blindScope = new FlowScope();
    condition = createIsDefAndNotNullNode("x");
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(0, result.getVariableCount());
  }

  private Node createIsDefNode(String variable) {
    Node callNode = new Node(Token.CALL);
    callNode.addChildToFront(createGoogDotIsDefNode());
    callNode.addChildToBack(createQualifiedNameNode(variable));
    return callNode;
  }

  private Node createIsNullNode(String variable) {
    Node callNode = new Node(Token.CALL);
    callNode.addChildToFront(createGoogDotIsNullNode());
    callNode.addChildToBack(createQualifiedNameNode(variable));
    return callNode;
  }
  
  private Node createIsDefAndNotNullNode(String variable) {
    Node callNode = new Node(Token.CALL);
    callNode.addChildToFront(createGoogDotIsDefAndNotNullNode());
    callNode.addChildToBack(createQualifiedNameNode(variable));
    return callNode;
  }
  
  private Node createGoogDotIsDefNode() {
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToFront(createGoogNode());
    getPropNode.addChildToBack(createStringNode("isDef"));
    return getPropNode;
  }

  private Node createGoogDotIsNullNode() {
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToFront(createGoogNode());
    getPropNode.addChildToBack(createStringNode("isNull"));
    return getPropNode;
  }
  
  private Node createGoogDotIsDefAndNotNullNode() {
    Node getPropNode = new Node(Token.GETPROP);
    getPropNode.addChildToFront(createGoogNode());
    getPropNode.addChildToBack(createStringNode("isDefAndNotNull"));
    return getPropNode;
  }

  private Node createGoogNode() {
    return createNameNode("goog");
  }

  private Node createQualifiedNameNode(String name) {
    return createNameNode(name);
  }

  private Node createStringNode(String value) {
    Node stringNode = new Node(Token.STRING);
    stringNode.setString(value);
    return stringNode;
  }

  private Node createNameNode(String name) {
    Node nameNode = new Node(Token.NAME);
    nameNode.setString(name);
    return nameNode;
  }

}