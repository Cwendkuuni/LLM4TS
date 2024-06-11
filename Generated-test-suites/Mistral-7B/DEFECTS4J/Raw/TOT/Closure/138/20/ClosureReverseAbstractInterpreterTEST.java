import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDef"),
        Node.newUndefined(), Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    ObjectType expectedType = typeRegistry.getObjectType("boolean");
    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefFalse() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDef"),
        Node.newUndefined(), Node.newBoolean(false));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, false);

    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(null, actualType);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isFunction"),
        Node.newFunction(null, new ObjectType(FunctionType.VOID_FUNCTION_TYPE)),
        Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    ObjectType expectedType = typeRegistry.getObjectType("function");
    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunctionFalse() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isFunction"),
        Node.newUndefined(), Node.newBoolean(false));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, false);

    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(null, actualType);
  }

  @Test(expected = AssertionError.class)
  public void testIsDefAndNotNullNullInput() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDefAndNotNull"),
        Node.newNull(), Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    // This test should fail since null is not an object and cannot be
    // restricted to an object type.
  }

  @Test
  public void testIsDefAndNotNullBooleanTrue() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDefAndNotNull"),
        Node.newBoolean(true), Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    ObjectType expectedType = typeRegistry.getObjectType("boolean");
    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNullBooleanFalse() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDefAndNotNull"),
        Node.newBoolean(false), Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, false);

    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(null, actualType);
  }

  @Test
  public void testIsDefAndNotNullString() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDefAndNotNull"),
        new Node[] { Node.newString("test") }, Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    ObjectType expectedType = typeRegistry.getObjectType("string");
    JSType actualType = informedScope.getVariableByName("result").getType();
    assertEquals(expectedType, actualType);
  }

  @Test(expected = AssertionError.class)
  public void testIsDefAndNotNullStringNullInput() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = Node.newCall(null, Node.newQualifiedName("goog", "isDefAndNotNull"),
        Node.newUndefined(), Node.newBoolean(true));

    FlowScope blindScope = Node.newRootScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, blindScope, true);

    // This test should fail since undefined is not a string and cannot be
    // restricted to a string type.
  }
}