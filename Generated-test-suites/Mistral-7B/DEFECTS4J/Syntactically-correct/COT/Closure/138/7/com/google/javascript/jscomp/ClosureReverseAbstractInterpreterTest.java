import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeType.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.*;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.com.google.javascript.rhino.JSType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 private JSTypeRegistry typeRegistry;
 private CodingConvention codingConvention;
 private ClosureReverseAbstractInterpreter interpreter;

 @Before
 public void setUp() {
 typeRegistry = new JSTypeRegistry();
 codingConvention = new CodingConvention();
 interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
 Node condition = createNode("goog.isDef(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.BOOLEAN_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
 Node condition = createNode("goog.isDef(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
 Node condition = createNode("goog.isNull(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.NULL_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
 Node condition = createNode("goog.isNull(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
 Node condition = createNode("goog.isDefAndNotNull(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.OBJECT_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
 Node condition = createNode("goog.isDefAndNotNull(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isString() {
 Node condition = createNode("goog.isString(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.STRING_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isString_false() {
 Node condition = createNode("goog.isString(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
 Node condition = createNode("goog.isBoolean(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.BOOLEAN_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_false() {
 Node condition = createNode("goog.isBoolean(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
 Node condition = createNode("goog.isNumber(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.NUMBER_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNumber_false() {
 Node condition = createNode("goog.isNumber(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
 Node condition = createNode("goog.isArray(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.ARRAY_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
 Node condition = createNode("goog.isArray(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
 Node condition = createNode("goog.isObject(x)", "x");
 JSType expectedType = getNativeType(JSTypeNative.OBJECT_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertEquals(expectedType, actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
 Node condition = createNode("goog.isObject(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_unknownCondition() {
 Node condition = createNode("unknownCondition(x)", "x");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
 assertNull(actualScope.getDeclaredType(createNode("x")));
 }

 private Node createNode(String expression, String... ids) {
 Node node = new Node();
 if (ids != null && ids.length > 0) {
 node.setQualifiedName(ids);
 }
 node.setType(STRING);
 node.setString(expression);
 return node;
 }

 private JSType getNativeType(int type) {
 return typeRegistry.getFactory().getNativeType(type);
 }

 private JSType getNativeType(JSTypeNative type) {
 return typeRegistry.getFactory().getNativeType(type);
 }

 private JSType getRestrictedByTypeOfResult(JSType type, String expectedType, boolean outcome) {
 ObjectType objectType = (ObjectType) type;
 FunctionType functionType = null;
 if (objectType != null && objectType.isFunctionType()) {
 functionType = (FunctionType) objectType;
 }
 if (functionType != null) {
 return interpreter.restricters.get("isFunction").apply(new TypeRestriction(functionType, outcome));
 } else {
 return interpreter.restricters.get(expectedType).apply(new TypeRestriction(type, outcome));
 }
 }

 private JSType getRestrictedWithoutUndefined(JSType type) {
 return interpreter.restricters.get("isDef").apply(new TypeRestriction(type, true));
 }

 private JSType getRestrictedWithoutNull(JSType type) {
 return interpreter.restricters.get("isNull").apply(new TypeRestriction(type, false));
 }

 private static class TypeRestriction {
 private final JSType type;
 private final boolean outcome;

 private TypeRestriction(JSType type, boolean outcome) {
 this.type = type;
 this.outcome = outcome;
 }
 }
}