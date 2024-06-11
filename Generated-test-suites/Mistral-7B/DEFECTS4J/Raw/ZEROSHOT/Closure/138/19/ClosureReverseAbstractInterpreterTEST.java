import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArray() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(interpreter, ARRAY_TYPE);
    JSType objectType = getNativeType(interpreter, OBJECT_TYPE);

    Node condition = createNode("goog.isArray(x)");
    TypeRestriction typeRestriction = new TypeRestriction(objectType, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(arrayType, restrictedType);
  }

  @Test
  public void testRestrictToNotArray() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(interpreter, ARRAY_TYPE);
    JSType objectType = getNativeType(interpreter, OBJECT_TYPE);

    Node condition = createNode("goog.isArray(x)");
    TypeRestriction typeRestriction = new TypeRestriction(objectType, false);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictToFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType functionType = getFunctionType(interpreter);

    Node condition = createNode("goog.isFunction(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(functionType, restrictedType);
  }

  @Test
  public void testRestrictToNotFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType functionType = getFunctionType(interpreter);

    Node condition = createNode("goog.isFunction(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, false);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictToObject() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType objectType = getNativeType(interpreter, OBJECT_TYPE);

    Node condition = createNode("goog.isObject(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(objectType, restrictedType);
  }

  @Test
  public void testRestrictToNotObject() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType objectType = getNativeType(interpreter, OBJECT_TYPE);

    Node condition = createNode("goog.isObject(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, false);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictToNull() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType nullType = getNativeType(interpreter, NULL_TYPE);

    Node condition = createNode("goog.isNull(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(nullType, restrictedType);
  }

  @Test
  public void testRestrictToNotNull() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType nullType = getNativeType(interpreter, NULL_TYPE);

    Node condition = createNode("goog.isNull(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, false);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictToBoolean() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType booleanType = getNativeType(interpreter, "boolean");

    Node condition = createNode("goog.isBoolean(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(booleanType, restrictedType);
  }

  @Test
  public void testRestrictToString() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType stringType = getNativeType(interpreter, "string");

    Node condition = createNode("goog.isString(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(stringType, restrictedType);
  }

  @Test
  public void testRestrictToUndefined() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType undefinedType = getNativeType(interpreter, "undefined");

    Node condition = createNode("goog.isDef(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(undefinedType, restrictedType);
  }

  @Test
  public void testRestrictToDefAndNotNull() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType undefinedType = getNativeType(interpreter, "undefined");
    JSType nullType = getNativeType(interpreter, NULL_TYPE);

    Node condition = createNode("goog.isDefAndNotNull(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, true);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertSame(
        getNativeType(interpreter, "undefined").getSuperclass(nullType),
        restrictedType);
  }

  @Test
  public void testRestrictToDefButNotNotNull() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType undefinedType = getNativeType(interpreter, "undefined");
    JSType nullType = getNativeType(interpreter, NULL_TYPE);

    Node condition = createNode("goog.isDefAndNotNull(x)");
    TypeRestriction typeRestriction = new TypeRestriction(null, false);

    FlowScope blindScope = createBlindFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType restrictedType = informedScope.getDeclaredVariable(0).getType();
    assertNull(restrictedType);
  }

  private Node createNode(String nodeString) {
    NodeFactory nodeFactory = NodeFactory.instance();
    return nodeFactory.makeCall(nodeFactory.makeName("goog"), null,
        nodeFactory.makeString(nodeString), null);
  }

  private FlowScope createBlindFlowScope() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    FlowScope flowScope = new FlowScope(typeRegistry);
    when(typeRegistry.lookupType(ArgumentMatchers.anyString()))
        .thenAnswer(invocation -> getNativeType(interpreter, invocation.getArgument(0)));
    return flowScope;
  }

  private JSType getNativeType(ClosureReverseAbstractInterpreter interpreter,
      String typeName) {
    JSTypeRegistry typeRegistry = interpreter.getTypeRegistry();
    return typeRegistry.lookupType(typeName);
  }

  private JSType getNativeType(ClosureReverseAbstractInterpreter interpreter,
      JSType.Type type) {
    return interpreter.getTypeRegistry().lookupType(type.getName());
  }

  private JSType getFunctionType(ClosureReverseAbstractInterpreter interpreter) {
    JSTypeRegistry typeRegistry = interpreter.getTypeRegistry();
    return new FunctionType(typeRegistry.lookupType("function"),
        new ObjectType(typeRegistry))
      .setDeclaredName("Function");
  }
}