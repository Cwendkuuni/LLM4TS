package org.apache.commons.cli;

import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 private CodingConvention codingConvention = new CodingConvention();
 private JSTypeRegistry typeRegistry = new JSTypeRegistry();
 private ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsDef() {
 Node condition = createNode("goog.isDef(x)");
 JSType expectedType = getNativeType(JSTypeNative.BOOLEAN);
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsDefNotNull() {
 Node condition = createNode("goog.isDefAndNotNull(x)");
 JSType expectedType = getNativeType(JSTypeNative.OBJECT);
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsDefNull() {
 Node condition = createNode("goog.isDefAndNotNull(x)");
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
 assertNull(getTypeFromFlowScope(informedScope, "x"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsString() {
 Node condition = createNode("goog.isString(x)");
 JSType expectedType = typeRegistry.createStringType();
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsBoolean() {
 Node condition = createNode("goog.isBoolean(x)");
 JSType expectedType = typeRegistry.createBooleanType();
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsNumber() {
 Node condition = createNode("goog.isNumber(x)");
 JSType expectedType = typeRegistry.createNumberType();
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsFunction() {
 Node condition = createNode("goog.isFunction(x)");
 JSType expectedType = typeRegistry.createFunctionType(getNativeType(JSTypeNative.VOID),
         ImmutableList.of());
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsArray() {
 Node condition = createNode("goog.isArray(x)");
 JSType expectedType = getNativeType(JSTypeNative.ARRAY_TYPE);
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsObject() {
 Node condition = createNode("goog.isObject(x)");
 JSType expectedType = getNativeType(JSTypeNative.OBJECT_TYPE);
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
 JSType actualType = getTypeFromFlowScope(informedScope, "x");
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsObjectNull() {
 Node condition = createNode("goog.isObject(x)");
 FlowScope blindScope = createFlowScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
 assertNull(getTypeFromFlowScope(informedScope, "x"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeIsNotArray() {
 Node condition = createNode("goog.isArray(x)");
 FlowScope blindScope = createFlowScope();
 Visitor<JSType> visitor = new RestrictByFalseTypeOfResultVisitor() {
 @Override public JSType caseObjectType(ObjectType type) {
 return null;
 }};
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
 assertNull(getTypeFromFlowScope(informedScope, "x"));
 }

 private Node createNode(String expression) {
 return RhinoFactory.createNodeFromString(expression, codingConvention, typeRegistry);
 }

 private FlowScope createFlowScope() {
 return new FlowScopeImpl(codingConvention, typeRegistry);
 }

 private JSType getTypeFromFlowScope(FlowScope flowScope, String variableName) {
 return flowScope.getVariable(variableName).getType();
 }

}