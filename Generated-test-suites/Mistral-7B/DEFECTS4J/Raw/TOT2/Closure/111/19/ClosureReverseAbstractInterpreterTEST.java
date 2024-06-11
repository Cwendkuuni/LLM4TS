import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), null);
    JSType type = null;
    JSType expected = JSType.NULL_TYPE;
    Node condition = new Node("goog.isDef(null)");

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    JSType actual = informedScope.getVariableValue(condition.getFirstChild().getString());

    assertEquals(expected, actual);

    type = new JSType(new FunctionType(JSType.VOID_TYPE, new JSType[]{}));
    expected = interpreter.getNativeType(JSType.BOOLEAN_TYPE);
    condition = new Node("goog.isDef(function() {})");

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    actual = informedScope.getVariableValue(condition.getFirstChild().getString());

    assertEquals(expected, actual);
  }

  // Add other test cases for methods isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, and isObject.
}