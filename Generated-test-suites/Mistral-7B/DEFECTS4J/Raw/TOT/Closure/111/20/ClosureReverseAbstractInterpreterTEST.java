import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeCoercion.*;
import static com.google.javascript.jscomp.type.JSTypeRegistry.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Tests for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // true cases
    JSType intType = createPrimitiveType(typeRegistry, "int");
    JSType booleanType = createPrimitiveType(typeRegistry, "boolean");
    JSType stringType = createPrimitiveType(typeRegistry, "string");
    JSType functionType = createFunctionType(typeRegistry, ImmutableList.of(),
        ImmutableList.of(intType));

    Node isDefNode1 = makeCallNode("goog.isDef", ImmutableList.of(intType));
    Node isDefNode2 = makeCallNode("goog.isDef", ImmutableList.of(booleanType));
    Node isDefNode3 = makeCallNode("goog.isDef", ImmutableList.of(stringType));
    Node isDefNode4 = makeCallNode("goog.isDef", ImmutableList.of(functionType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode1, blindScope, true);
    assertEquals(intType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode2, informedScope1, true);
    assertEquals(booleanType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode3, informedScope1, true);
    assertEquals(stringType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode4, informedScope1, true);
    assertEquals(functionType, informedScope1.getVariableScope().get("isDef"));

    // false cases
    JSType nullType = createPrimitiveType(typeRegistry, "null");
    JSType voidType = createPrimitiveType(typeRegistry, "void");
    JSType objectType = createObjectType(typeRegistry, "Object");
    JSType arrayType = createArrayType(typeRegistry, intType);

    Node isDefNode5 = makeCallNode("goog.isDef", ImmutableList.of(nullType));
    Node isDefNode6 = makeCallNode("goog.isDef", ImmutableList.of(voidType));
    Node isDefNode7 = makeCallNode("goog.isDef", ImmutableList.of(objectType));
    Node isDefNode8 = makeCallNode("goog.isDef", ImmutableList.of(arrayType));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode5, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode6, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode7, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefNode8, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isDef"));
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType intType = createPrimitiveType(typeRegistry, "int");
    JSType booleanType = createPrimitiveType(typeRegistry, "boolean");
    JSType stringType = createPrimitiveType(typeRegistry, "string");
    JSType functionType = createFunctionType(typeRegistry, ImmutableList.of(),
        ImmutableList.of(intType));
    JSType nullType = createPrimitiveType(typeRegistry, "null");

    Node isDefAndNotNullNode1 = makeCallNode(
        "goog.isDefAndNotNull", ImmutableList.of(intType));
    Node isDefAndNotNullNode2 = makeCallNode(
        "goog.isDefAndNotNull", ImmutableList.of(booleanType));
    Node isDefAndNotNullNode3 = makeCallNode(
        "goog.isDefAndNotNull", ImmutableList.of(stringType));
    Node isDefAndNotNullNode4 = makeCallNode(
        "goog.isDefAndNotNull", ImmutableList.of(functionType));
    Node isDefAndNotNullNode5 = makeCallNode(
        "goog.isDefAndNotNull", ImmutableList.of(nullType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefAndNotNullNode1, blindScope, true);
    assertEquals(intType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefAndNotNullNode2, informedScope1, true);
    assertEquals(booleanType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefAndNotNullNode3, informedScope1, true);
    assertEquals(stringType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefAndNotNullNode4, informedScope1, true);
    assertEquals(functionType, informedScope1.getVariableScope().get("isDef"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isDefAndNotNullNode5, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isDef"));
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType stringType = createPrimitiveType(typeRegistry, "string");
    JSType numberType = createPrimitiveType(typeRegistry, "number");

    Node isStringNode1 = makeCallNode("goog.isString", ImmutableList.of(numberType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isStringNode1, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isString"));

    JSType stringLiteralType = createPrimitiveType(typeRegistry, "string", "abc");
    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isStringNode1, informedScope1, true);
    assertEquals(stringType, informedScope1.getVariableScope().get("isString"));
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType booleanType = createPrimitiveType(typeRegistry, "boolean");
    JSType numberType = createPrimitiveType(typeRegistry, "number");

    Node isBooleanNode1 = makeCallNode("goog.isBoolean", ImmutableList.of(numberType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isBooleanNode1, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isBoolean"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isBooleanNode1, informedScope1, true);
    assertEquals(booleanType, informedScope1.getVariableScope().get("isBoolean"));
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType numberType = createPrimitiveType(typeRegistry, "number");
    JSType stringType = createPrimitiveType(typeRegistry, "string");

    Node isNumberNode1 = makeCallNode("goog.isNumber", ImmutableList.of(numberType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isNumberNode1, blindScope, true);
    assertEquals(numberType, informedScope1.getVariableScope().get("isNumber"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isNumberNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isNumber"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isNumberNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isNumber"));

    Node isNumberNode2 = makeCallNode("goog.isNumber", ImmutableList.of(stringType));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isNumberNode2, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isNumber"));
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType functionType = createFunctionType(typeRegistry, ImmutableList.of(),
        ImmutableList.of());
    JSType intType = createPrimitiveType(typeRegistry, "int");

    Node isFunctionNode1 = makeCallNode("goog.isFunction", ImmutableList.of(functionType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isFunctionNode1, blindScope, true);
    assertEquals(functionType, informedScope1.getVariableScope().get("isFunction"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isFunctionNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isFunction"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isFunctionNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isFunction"));

    Node isFunctionNode2 = makeCallNode("goog.isFunction", ImmutableList.of(intType));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isFunctionNode2, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isFunction"));
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType intType = createPrimitiveType(typeRegistry, "int");
    JSType arrayType = createArrayType(typeRegistry, intType);

    Node isArrayNode1 = makeCallNode("goog.isArray", ImmutableList.of(intType));

    FlowScope blindScope = createRootFlowScope(typeRegistry);

    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isArrayNode1, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isArray"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isArrayNode1, informedScope1, true);
    assertEquals(arrayType, informedScope1.getVariableScope().get("isArray"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isArrayNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isArray"));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isArrayNode1, informedScope1, false);
    assertNull(informedScope1.getVariableScope().get("isArray"));

    Node isArrayNode2 = makeCallNode("goog.isArray", ImmutableList.of(null));

    informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        isArrayNode2, blindScope, false);
    assertNull(informedScope1.getVariableScope().get("isArray"));
  }

  private Node makeCallNode(String callee, ImmutableList<Node> args) {
    Node calleeNode = makeNameNode("goog", "goog");
    Node calleeCallNode = makeCallNode(calleeNode, args);
    return calleeCallNode;
  }

  private JSType createPrimitiveType(JSTypeRegistry typeRegistry, String typeName) {
    return createPrimitiveType(typeRegistry, typeName, null);
  }

  private JSType createPrimitiveType(JSTypeRegistry typeRegistry, String typeName,
      String value) {
    JSType primitiveType = typeRegistry.lookupPrimitive(typeName);
    if (value != null) {
      primitiveType = coerceToType(primitiveType, createPrimitiveType(
          typeRegistry, "string", value));
    }
    return primitiveType;
  }

  private JSType createFunctionType(JSTypeRegistry typeRegistry,
      ImmutableList<JSType> parameterTypes, ImmutableList<JSType> returnTypes) {
    return typeRegistry.createFunctionType(parameterTypes, returnTypes);
  }

  private JSType createArrayType(JSTypeRegistry typeRegistry, JSType elementType) {
    return typeRegistry.createArrayType(elementType);
  }

  private JSType createObjectType(JSTypeRegistry typeRegistry, String name) {
    return typeRegistry.createObjectType(name);
  }

  private FlowScope createRootFlowScope(JSTypeRegistry typeRegistry) {
    return new FlowScope(typeRegistry, null, null, ImmutableList.of());
  }
}