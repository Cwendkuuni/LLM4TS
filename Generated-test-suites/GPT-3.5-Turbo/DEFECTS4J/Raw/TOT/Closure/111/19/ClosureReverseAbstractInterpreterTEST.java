import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isDef")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isNull")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isDefAndNotNull")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isString")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isBoolean")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isNumber")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isFunction")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isArray")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    Node condition = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isObject")),
        new Node(Token.NAME, "param"));

    ObjectType objectType = new ObjectType();
    FlowScope blindScope = new FlowScope(objectType);

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    ObjectType resultObjectType = resultScope.getScope();
    JSType paramType = resultObjectType.getProp("param").getType();

    assertEquals("Expected restricted param type", "RestrictedType", paramType.getName());
  }
}