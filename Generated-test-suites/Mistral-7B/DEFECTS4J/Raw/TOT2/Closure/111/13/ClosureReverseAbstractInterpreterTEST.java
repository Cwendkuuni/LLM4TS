import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.JSType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.visitor.Visitor;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CodingConvention.Standard, new JSTypeRegistry());
    Node condition = new Node("goog.isDef(null)");
    JSType expectedType = JSType.NULL_TYPE;
    JSType actualType = interpreter.getTypeIfRefinable(condition, null);
    assertEquals(expectedType, actualType);

    condition = new Node("goog.isDef(1)");
    expectedType = JSType.VOID_TYPE;
    actualType = interpreter.getTypeIfRefinable(condition, null);
    assertEquals(expectedType, actualType);

    condition = new Node("goog.isDef({})");
    expectedType = interpreter.getNativeType(JSTypeNative.OBJECT_TYPE);
    actualType = interpreter.getTypeIfRefinable(condition, null);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefWithOutcomeTrue() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CodingConvention.Standard, new JSTypeRegistry());
    Node condition = new Node("goog.isDef(1)");

    // Restricting the type of the condition to boolean
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;
    JSType type = visitor.visit(interpreter.getNativeType(FUNCTION_TYPE));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restrictParameter(
        condition, interpreter.getNativeType(FUNCTION_TYPE), null,
        interpreter.restricters.get("isDef"), true);

    JSType expectedType = JSType.BOOLEAN;
    JSType actualType = interpreter.getTypeIfRefinable(condition, null);
    assertEquals(expectedType, actualType);
  }

  // Add other test cases for methods `isNull`, `isDefAndNotNull`, `isString`,
  // `isBoolean`, `isNumber`, `isFunction`, `isArray`, and `isObject`.

  @Test(expected = RuntimeException.class)
  public void testUnexpectedCallToGoogIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CodingConvention.Standard, new JSTypeRegistry());
    Node condition = new Node("goog.isUndefined(1)"); // Incorrect method name
    interpreter.getTypeIfRefinable(condition, null);
  }
}