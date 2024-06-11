import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testRestrictToArray() {
    JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);

    when(typeRegistry.createUnionType(any(), any())).thenReturn(new ObjectType(null));

    JSType result = interpreter.restrictToArrayVisitor.visit(objectType);
    assertSame(arrayType, result);

    result = interpreter.restrictToArrayVisitor.visit(arrayType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArray() {
    JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);

    when(typeRegistry.createUnionType(any(), any())).thenReturn(new ObjectType(null));

    JSType result = interpreter.restrictToNotArrayVisitor.visit(objectType);
    assertNull(result);

    result = interpreter.restrictToNotArrayVisitor.visit(arrayType);
    assertSame(objectType, result);
  }

  @Test
  public void testRestrictToObject() {
    JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);
    FunctionType functionType = new FunctionType(null, null, null);

    when(typeRegistry.createUnionType(any(), any())).thenReturn(new ObjectType(null));

    JSType result = interpreter.restrictToObjectVisitor.visit(arrayType);
    assertNull(result);

    result = interpreter.restrictToObjectVisitor.visit(objectType);
    assertSame(objectType, result);

    result = interpreter.restrictToObjectVisitor.visit(functionType);
    assertSame(functionType, result);
  }

  @Test
  public void testRestrictToNotObject() {
    JSType allType = getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN);
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);
    FunctionType functionType = new FunctionType(null, null, null);

    when(typeRegistry.createUnionType(any(), any())).thenReturn(allType);

    JSType result = interpreter.restrictToNotObjectVisitor.visit(allType);
    assertNull(result);

    result = interpreter.restrictToNotObjectVisitor.visit(objectType);
    assertNull(result);

    result = interpreter.restrictToNotObjectVisitor.visit(functionType);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    JSType stringType = getNativeType(JSTypeNative.STRING);
    JSType booleanType = getNativeType(JSTypeNative.BOOLEAN);
    JSType numberType = getNativeType(JSTypeNative.NUMBER);
    JSType functionType = getNativeType(JSTypeNative.FUNCTION);
    JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);
    JSType nullType = getNativeType(JSTypeNative.NULL_TYPE);
    JSType voidType = getNativeType(JSTypeNative.VOID_TYPE);

    Node condition = new Node();
    condition.setType(new ObjectType(null));

    Node left = new Node();
    Node right = new Node();
    left.setType(new ObjectType(null));
    right.setType(new StringType("goog"));
    condition.setFirstChild(left);
    condition.setLastChild(right);

    Function<TypeRestriction, JSType> isDefRestricter = interpreter.restricters.get("isDef");

    FlowScope blindScope = new FlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(objectType, result.getFlowScopeVariable("goog").getType());

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getFlowScopeVariable("goog"));

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(voidType, result.getFlowScopeVariable("goog").getType());

    left.setType(new NullType());
    right.setType(new StringType("isDef"));
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNull(result.getFlowScopeVariable("goog"));

    left.setType(new ObjectType(null));
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getFlowScopeVariable("goog"));

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(nullType, result.getFlowScopeVariable("goog").getType());

    left.setType(new ArrayType(new ObjectType(null)));
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(arrayType, result.getFlowScopeVariable("goog").getType());

    left.setType(new ObjectType(null));
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getFlowScopeVariable("goog"));

    left.setType(arrayType);
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(arrayType, result.getFlowScopeVariable("goog").getType());

    left.setType(objectType);
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getFlowScopeVariable("goog"));

    left.setType(objectType);
    condition.setLastChild(new Node());

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(objectType, result.getFlowScopeVariable("goog").getType());

    left.setType(functionType);
    condition.setLastChild(right);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertSame(functionType, result.getFlowScopeVariable("goog").getType());
  }

  @Test(expected = NullPointerException.class)
  public void testGetPreciserScopeKnowingConditionOutcomeNullCondition() {
    interpreter.getPreciserScopeKnowingConditionOutcome(null, null, true);
  }

  @Test(expected = NullPointerException.class)
  public void testGetPreciserScopeKnowingConditionOutcomeNullBlindScope() {
    Node condition = new Node();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
  }

  private JSType getNativeType(int typeCode) {
    // This is a stub for JSTypeNative.getNativeType(typeCode)
    return new ObjectType(null);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    // This is a stub for interpreter.restrictToNotUndefined(type)
    return type;
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    // This is a stub for interpreter.restrictToNotNull(type)
    return type;
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedType, boolean outcome) {
    // This is a stub for interpreter.restrictToType(type, expectedType, outcome)
    return new ObjectType(null);
  }

  private FunctionType getTypeIfRefinable(Node node, FlowScope flowScope) {
    // This is a stub for interpreter.getTypeIfRefinable(node, flowScope)
    return new FunctionType(null, null, null);
  }

  private void declareNameInScope(FlowScope scope, Node name, JSType type) {
    // This is a stub for interpreter.declareNameInScope(scope, name, type)
  }
}