import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

 private final JSTypeRegistry typeRegistry = new JSTypeRegistry();
 private final CodingConvention convention = new CodingConvention();
 private final ClosureReverseAbstractInterpreter interpreter =
         new ClosureReverseAbstractInterpreter(convention, typeRegistry);

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
   JSType inputType = VOID_TYPE.getGreatestSubtype(STRING_TYPE);
   JSType expectedOutputType = STRING_TYPE;

   Node condition = createNode("goog.isDef(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
   JSType inputType = STRING_TYPE;
   JSType expectedOutputType = VOID_TYPE;

   Node condition = createNode("goog.isNull(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
   JSType inputType = STRING_TYPE;
   JSType expectedOutputType = STRING_TYPE;

   Node condition = createNode("goog.isDefAndNotNull(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isString() {
   JSType inputType = NUMBER_TYPE;
   JSType expectedOutputType = STRING_TYPE;

   Node condition = createNode("goog.isString(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
   JSType inputType = NUMBER_TYPE;
   JSType expectedOutputType = BOOLEAN_TYPE;

   Node condition = createNode("goog.isBoolean(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
   JSType inputType = BOOLEAN_TYPE;
   JSType expectedOutputType = NUMBER_TYPE;

   Node condition = createNode("goog.isNumber(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
   JSType inputType = BOOLEAN_TYPE;
   JSType expectedOutputType = FUNCTION_TYPE;

   Node condition = createNode("goog.isFunction(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
   JSType inputType = NUMBER_TYPE;
   JSType expectedOutputType = ARRAY_TYPE;

   Node condition = createNode("goog.isArray(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
   JSType inputType = NUMBER_TYPE;
   JSType expectedOutputType = OBJECT_TYPE;

   Node condition = createNode("goog.isObject(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isArrayFalse() {
   JSType inputType = OBJECT_TYPE;
   JSType expectedOutputType = VOID_TYPE;

   Node condition = createNode("goog.isArray(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isObjectFalse() {
   JSType inputType = OBJECT_TYPE;
   JSType expectedOutputType = VOID_TYPE;

   Node condition = createNode("goog.isObject(x)");
   Node parameter = createNode("x");

   FlowScope blindScope = new BasicFlowScope(typeRegistry, null);

   FlowScope actualOutput = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
   JSType actualOutputType = actualOutput.getDeclaredType(parameter.getName());

   assertEquals(expectedOutputType, actualOutputType);
 }

 private Node createNode(String nodeType) {
   Node node = new Node_rt();
   node.setType(new NodeType(nodeType));
   return node;
 }

}