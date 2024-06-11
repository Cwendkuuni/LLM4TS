import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    Node condition = Node.newBinaryOp(CALL, "goog", "isDef", Node.newQualifiedName("foo"));
    JSType expectedResult = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getFlowScopeVariable("foo").getType();
    assertEquals(getNativeType(ObjectType.OBJECT), expectedResult);
  }

  @Test
  public void testIsDefWithUndefinedValue() {
    Node condition = Node.newBinaryOp(CALL, "goog", "isDef", Node.newQualifiedName("foo"));
    JSType expectedResult = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getFlowScopeVariable("foo").getType();
    assertNull(expectedResult);
  }

  @Test
  public void testIsDefWithNullValue() {
    Node condition = Node.newBinaryOp(CALL, "goog", "isDef", Node.newQualifiedName("foo"));
    when(typeRegistry.getType(Node.newString("null"))).thenReturn(getNativeType(NULL_TYPE));
    JSType expectedResult = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getFlowScopeVariable("foo").getType();
    assertNull(expectedResult);
  }

  @Test
  public void testIsDefWithBooleanTrueValue() {
    Node condition = Node.newBinaryOp(CALL, "goog", "isDef", Node.newBoolean(true));
    JSType expectedResult = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getType();
    assertEquals(getNativeType(ObjectType.OBJECT), expectedResult);
  }

  @Test
  public void testIsDefWithBooleanFalseValue() {
    Node condition = Node.newBinaryOp(CALL, "goog", "isDef", Node.newBoolean(false));
    JSType expectedResult = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getType();
    assertNull(expectedResult);
  }

  // Add tests for other methods in the class: isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, isObject
}