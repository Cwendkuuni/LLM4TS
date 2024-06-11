import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();

  @org.junit.BeforeClass
  public static void setUp() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CodingConvention.GOOGLE, typeRegistry);
  }

  // Test for isDef method
  public void testIsDef() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newThis(), Node.newString("someVariable"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(ObjectType.TYPE);
    JSType actualType = informedScope.getVariable("someVariable").getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsDefFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newThis(), Node.newName("undefined"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("undefined").getType();

    TestCase.assertNull(actualType);
  }

  // Test for isNull method
  public void testIsNull() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isNull"),
        Node.newThis(), Node.newName("null"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(NULL_TYPE);
    JSType actualType = informedScope.getVariable("null").getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsNotNull() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isNull"),
        Node.newThis(), Node.newName("someVariable"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = typeRegistry.getNativeType(NO_OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("someVariable").getType();

    TestCase.assertNotNull(actualType);
    TestCase.assertFalse(expectedType.isSubtype(actualType));
  }

  // Test for isDefAndNotNull method
  public void testIsDefAndNotNull() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newThis(), Node.newName("someVariable"));

    Node notNullCondition = Node.newCall(Node.newGetProp(Node.newName("goog"),
        "isNotNull"), Node.newThis(), Node.newGetProp(Node.newThis(), "someVariable"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newBinaryOp(Node.AND, condition, notNullCondition),
        blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(ObjectType.TYPE);
    JSType actualType = informedScope.getVariable("someVariable").getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  // Test for isString method
  public void testIsString() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isString"),
        Node.newThis(), Node.newString("someString"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(String.class);
    JSType actualType = informedScope.getVariable("someString").getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsStringFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isString"),
        Node.newThis(), Node.newNumber(1));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable(1).getType();

    TestCase.assertNull(actualType);
  }

  // Test for isBoolean method
  public void testIsBoolean() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isBoolean"),
        Node.newThis(), Node.newBoolean(true));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(Boolean.class);
    JSType actualType = informedScope.getVariable(true).getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsBooleanFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isBoolean"),
        Node.newThis(), Node.newNumber(1));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable(1).getType();

    TestCase.assertNull(actualType);
  }

  // Test for isNumber method
  public void testIsNumber() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isNumber"),
        Node.newThis(), Node.newNumber(1));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(Number.class);
    JSType actualType = informedScope.getVariable(1).getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsNumberFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isNumber"),
        Node.newThis(), Node.newString("someString"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someString").getType();

    TestCase.assertNull(actualType);
  }

  // Test for isFunction method
  public void testIsFunction() {
    Node condition =
        Node.newCall(Node.newGetProp(Node.newName("goog"), "isFunction"), Node.newThis(),
            Node.newFunction(null, null));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(FunctionType.TYPE);
    JSType actualType = informedScope.getVariable(0).getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsFunctionFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isFunction"),
        Node.newThis(), Node.newString("someString"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someString").getType();

    TestCase.assertNull(actualType);
  }

  // Test for isArray method
  public void testIsArrayTrue() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isArray"),
        Node.newThis(), Node.newArray(new Object[]{}));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(ARRAY_TYPE);
    JSType actualType = informedScope.getVariable(0).getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsArrayFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isArray"),
        Node.newThis(), Node.newNumber(1));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable(1).getType();

    TestCase.assertNull(actualType);
  }

  // Test for isObject method
  public void testIsObjectTrue() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isObject"),
        Node.newThis(), Node.newObject());

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = typeRegistry.getNativeType(ObjectType.TYPE);
    JSType actualType = informedScope.getVariable(0).getType();

    TestCase.assertEquals(expectedType, actualType);
  }

  public void testIsObjectFalse() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"), "isObject"),
        Node.newThis(), Node.newString("someString"));

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariable("someString").getType();

    TestCase.assertNull(actualType);
  }
}