import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Test isDef
    Node condition = new Node("goog.isDef(null)");
    FlowScope blindScope = new FlowScope(typeRegistry);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    condition = new Node("goog.isDef(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(VoidType.INSTANCE, result.lookupVariable("goog.isDef").getType());

    // Test isNull
    condition = new Node("goog.isNull(null)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(NullType.INSTANCE, result.lookupVariable("goog.isNull").getType());

    condition = new Node("goog.isNull(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isDefAndNotNull
    condition = new Node("goog.isDefAndNotNull(null)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    condition = new Node("goog.isDefAndNotNull(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(NullVoidType.INSTANCE, result.lookupVariable(
        "goog.isDefAndNotNull").getType());

    // Test isString
    condition = new Node("goog.isString('')");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(StringType.INSTANCE, result.lookupVariable("goog.isString").getType());

    condition = new Node("goog.isString(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isBoolean
    condition = new Node("goog.isBoolean(true)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(BooleanType.INSTANCE, result.lookupVariable("goog.isBoolean")
        .getType());

    condition = new Node("goog.isBoolean(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isNumber
    condition = new Node("goog.isNumber(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(NumberType.INSTANCE, result.lookupVariable("goog.isNumber")
        .getType());

    condition = new Node("goog.isNumber('')");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isFunction
    condition = new Node("goog.isFunction(function() {})");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(FunctionType.INSTANCE, result.lookupVariable("goog.isFunction")
        .getType());

    condition = new Node("goog.isFunction(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isArray
    condition = new Node("goog.isArray(null)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(ArrayType.INSTANCE, result.lookupVariable("goog.isArray")
        .getType());

    condition = new Node("goog.isArray([1, 2])");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(ArrayType.INSTANCE, result.lookupVariable("goog.isArray")
        .getType());

    condition = new Node("goog.isArray(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    // Test isObject
    condition = new Node("goog.isObject(null)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(NoObjectType.INSTANCE, result.lookupVariable("goog.isObject")
        .getType());

    condition = new Node("goog.isObject({})");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(ObjectType.INSTANCE, result.lookupVariable("goog.isObject")
        .getType());

    condition = new Node("goog.isObject(1)");
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);
  }

  @Test
  public void testRestrictParameter() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node parameter = new Node("p");
    JSType type = NumberType.INSTANCE;
    FlowScope blindScope = new FlowScope(typeRegistry);

    JSType restrictedType = interpreter.restrictParameter(parameter, type,
        blindScope, null, false).lookupVariable("p").getType();
    assertNull(restrictedType);

    type = StringType.INSTANCE;
    restrictedType = interpreter.restrictParameter(parameter, type,
        blindScope, new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return p.type;
          }
        }, true).lookupVariable("p").getType();
    assertSame(type, restrictedType);

    type = BooleanType.INSTANCE;
    restrictedType = interpreter.restrictParameter(parameter, type,
        blindScope, new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return p.outcome ? p.type : NullType.INSTANCE;
          }
        }, false).lookupVariable("p").getType();
    assertSame(NullType.INSTANCE, restrictedType);

    type = ArrayType.INSTANCE;
    restrictedType = interpreter.restrictParameter(parameter, type,
        blindScope, new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return p.outcome ? p.type : VoidType.INSTANCE;
          }
        }, true).lookupVariable("p").getType();
    assertSame(type, restrictedType);

    type = ObjectType.INSTANCE;
    restrictedType = interpreter.restrictParameter(parameter, type,
        blindScope, new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return p.outcome ? p.type : NoObjectType.INSTANCE;
          }
        }, false).lookupVariable("p").getType();
    assertSame(NoObjectType.INSTANCE, restrictedType);
  }
}