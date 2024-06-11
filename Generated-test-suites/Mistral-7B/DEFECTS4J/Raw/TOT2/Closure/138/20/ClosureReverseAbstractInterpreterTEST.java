import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

 private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
 private static final CodingConvention CONVENTION = new CodingConvention();

 @org.junit.BeforeClass
 public static void setUp() {
 TYPE_REGISTRY.init(CONVENTION);
 }

 @org.junit.Test
 public void testGetPreciserScopeKnowingConditionOutcome() {
 // Test case for isDef
 Node conditionDef = createNode("goog.isDef(myVar)");
 FlowScope blindScope = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScope = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionDef, blindScope, true);
 assertEquals("Expected informedScope to have type Object", JSTypeNative.OBJECT_TYPE, informedScope.getRootType());

 // Test case for isDef with null value
 Node conditionDefNull = createNode("goog.isDef(null)");
 FlowScope blindScopeNull = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeNull = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionDefNull, blindScopeNull, false);
 assertNull("Expected informedScopeNull to be null");

 // Test case for isDef with undefined value
 Node conditionDefUndefined = createNode("goog.isDef(undefined)");
 FlowScope blindScopeUndefined = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeUndefined = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionDefUndefined, blindScopeUndefined, false);
 assertNull("Expected informedScopeUndefined to be null");

 // Test case for isNull
 Node conditionNull = createNode("goog.isNull(myVar)");
 FlowScope blindScopeNull2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeNull2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionNull, blindScopeNull2, true);
 assertEquals("Expected informedScopeNull2 to have type Null", JSTypeNative.NULL_TYPE, informedScopeNull2.getRootType());

 // Test case for isNull with non-null value
 Node conditionNull2 = createNode("goog.isNull(1)");
 FlowScope blindScopeNull3 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeNull3 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionNull2, blindScopeNull3, false);
 assertNull("Expected informedScopeNull3 to be null");

 // Test case for isDefAndNotNull
 Node conditionDefNotNull = createNode("goog.isDefAndNotNull(myVar)");
 FlowScope blindScopeDefNotNull = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeDefNotNull = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionDefNotNull, blindScopeDefNotNull, true);
 assertEquals("Expected informedScopeDefNotNull to have type Object", JSTypeNative.OBJECT_TYPE, informedScopeDefNotNull.getRootType());

 // Test case for isDefAndNotNull with null value
 Node conditionDefNotNull2 = createNode("goog.isDefAndNotNull(null)");
 FlowScope blindScopeDefNotNull2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeDefNotNull2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionDefNotNull2, blindScopeDefNotNull2, false);
 assertNull("Expected informedScopeDefNotNull2 to be null");

 // Test case for isString
 Node conditionString = createNode("goog.isString(myVar)");
 FlowScope blindScopeString = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeString = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionString, blindScopeString, true);
 assertEquals("Expected informedScopeString to have type String", JSTypeNative.STRING_TYPE, informedScopeString.getRootType());

 // Test case for isString with non-string value
 Node conditionString2 = createNode("goog.isString(1)");
 FlowScope blindScopeString2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeString2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionString2, blindScopeString2, false);
 assertNull("Expected informedScopeString2 to be null");

 // Test case for isBoolean
 Node conditionBoolean = createNode("goog.isBoolean(myVar)");
 FlowScope blindScopeBoolean = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeBoolean = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionBoolean, blindScopeBoolean, true);
 assertEquals("Expected informedScopeBoolean to have type Boolean", JSTypeNative.BOOLEAN_TYPE, informedScopeBoolean.getRootType());

 // Test case for isBoolean with non-boolean value
 Node conditionBoolean2 = createNode("goog.isBoolean(1)");
 FlowScope blindScopeBoolean2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeBoolean2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionBoolean2, blindScopeBoolean2, false);
 assertNull("Expected informedScopeBoolean2 to be null");

 // Test case for isNumber
 Node conditionNumber = createNode("goog.isNumber(myVar)");
 FlowScope blindScopeNumber = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeNumber = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionNumber, blindScopeNumber, true);
 assertEquals("Expected informedScopeNumber to have type Number", JSTypeNative.NUMBER_TYPE, informedScopeNumber.getRootType());

 // Test case for isNumber with non-number value
 Node conditionNumber2 = createNode("goog.isNumber(myVar)");
 FlowScope blindScopeNumber2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeNumber2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionNumber2, blindScopeNumber2, false);
 assertNull("Expected informedScopeNumber2 to be null");

 // Test case for isFunction
 Node conditionFunction = createNode("goog.isFunction(myVar)");
 FlowScope blindScopeFunction = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeFunction = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionFunction, blindScopeFunction, true);
 assertEquals("Expected informedScopeFunction to have type Function", JSTypeNative.FUNCTION_TYPE, informedScopeFunction.getRootType());

 // Test case for isFunction with non-function value
 Node conditionFunction2 = createNode("goog.isFunction(1)");
 FlowScope blindScopeFunction2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeFunction2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionFunction2, blindScopeFunction2, false);
 assertNull("Expected informedScopeFunction2 to be null");

 // Test case for isArray
 Node conditionArray = createNode("goog.isArray(myVar)");
 FlowScope blindScopeArray = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeArray = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionArray, blindScopeArray, true);
 assertEquals("Expected informedScopeArray to have type Array", JSTypeNative.ARRAY_TYPE, informedScopeArray.getRootType());

 // Test case for isArray with non-array value
 Node conditionArray2 = createNode("goog.isArray(myVar)");
 FlowScope blindScopeArray2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeArray2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionArray2, blindScopeArray2, false);
 assertNull("Expected informedScopeArray2 to be null");

 // Test case for isObject
 Node conditionObject = createNode("goog.isObject(myVar)");
 FlowScope blindScopeObject = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeObject = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionObject, blindScopeObject, true);
 assertEquals("Expected informedScopeObject to have type Object", JSTypeNative.OBJECT_TYPE, informedScopeObject.getRootType());

 // Test case for isObject with non-object value
 Node conditionObject2 = createNode("goog.isObject(1)");
 FlowScope blindScopeObject2 = new FlowScope(new JSTypeRegistry(), ImmutableMap.of());
 FlowScope informedScopeObject2 = getInstanceUnderTest().getPreciserScopeKnowingConditionOutcome(conditionObject2, blindScopeObject2, false);
 assertNull("Expected informedScopeObject2 to be null");
 }

 private Node createNode(String nodeString) {
 Node node = new Node();
 node.setType(STRING);
 node.setString(nodeString);
 return node;
 }

 private ClosureReverseAbstractInterpreter getInstanceUnderTest() {
 return new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);
 }
}