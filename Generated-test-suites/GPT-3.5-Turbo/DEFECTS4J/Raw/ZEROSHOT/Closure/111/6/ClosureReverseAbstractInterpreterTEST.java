import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    JSType topType = typeRegistry.createAllType();
    JSType result = interpreter.restrictToArrayVisitor.caseTopType(topType);
    assertEquals(topType, result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    JSType objectType = typeRegistry.createObjectType();
    JSType arrayType = typeRegistry.getNativeType(typeRegistry.ARRAY_TYPE);
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType_notSubtype() {
    JSType numberType = typeRegistry.getNativeType(typeRegistry.NUMBER_STRING_BOOLEAN);
    JSType objectType = typeRegistry.createObjectType();
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(numberType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    JSType objectType = typeRegistry.createObjectType();
    JSType arrayType = typeRegistry.getNativeType(typeRegistry.ARRAY_TYPE);
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType_notSubtype() {
    JSType numberType = typeRegistry.getNativeType(typeRegistry.NUMBER_STRING_BOOLEAN);
    JSType objectType = typeRegistry.createObjectType();
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(numberType);
    assertEquals(numberType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    JSType topType = typeRegistry.createAllType();
    JSType noObjectType = typeRegistry.getNativeType(typeRegistry.NO_OBJECT_TYPE);
    JSType result = interpreter.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(noObjectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    JSType objectType = typeRegistry.createObjectType();
    JSType result = interpreter.restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    JSType functionType = typeRegistry.createFunctionType();
    JSType result = interpreter.restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(functionType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseAllType() {
    JSType numberType = typeRegistry.getNativeType(typeRegistry.NUMBER_STRING_BOOLEAN);
    JSType nullVoidType = typeRegistry.getNativeType(typeRegistry.NULL_VOID);
    JSType result = interpreter.restrictToNotObjectVisitor.caseAllType();
    JSType expected = typeRegistry.createUnionType(numberType, nullVoidType);
    assertEquals(expected, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    JSType objectType = typeRegistry.createObjectType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    JSType functionType = typeRegistry.createFunctionType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_array() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP), new Node(Node.QUALIFIED_NAME));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    JSType arrayType = typeRegistry.getNativeType(typeRegistry.ARRAY_TYPE);
    assertNotNull(result);
    assertEquals(arrayType, result.getType(condition.getLastChild()));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_object() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP), new Node(Node.QUALIFIED_NAME));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    JSType objectType = typeRegistry.getNativeType(typeRegistry.OBJECT_TYPE);
    assertNotNull(result);
    assertEquals(objectType, result.getType(condition.getLastChild()));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_notArray() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP), new Node(Node.QUALIFIED_NAME));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    JSType objectType = typeRegistry.createObjectType();
    assertNotNull(result);
    assertNull(result.getType(condition.getLastChild()));
    assertEquals(objectType, result.getType(condition.getLastChild().getNext()));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_notObject() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP), new Node(Node.QUALIFIED_NAME));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    JSType nullVoidType = typeRegistry.getNativeType(typeRegistry.NULL_VOID);
    assertNotNull(result);
    assertNull(result.getType(condition.getLastChild()));
    assertEquals(nullVoidType, result.getType(condition.getLastChild().getNext()));
  }
}