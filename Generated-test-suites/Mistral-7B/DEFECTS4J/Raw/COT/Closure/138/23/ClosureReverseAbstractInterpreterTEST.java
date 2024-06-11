import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 private JSTypeRegistry typeRegistry;
 private CodingConvention convention;
 private ClosureReverseAbstractInterpreter interpreter;

 @BeforeClass
 public static void setupClass() {
 // Initialize JSTypeRegistry and CodingConvention here if needed
 }

 @Before
 public void setup() {
 typeRegistry = new JSTypeRegistry();
 convention = new CodingConvention();
 interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
 Node condition = createCondition("goog.isDef(foo)");
 JSType expectedType = getNativeType(OBJECT_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertEquals(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNotDef() {
 Node condition = createCondition("goog.isDef(foo)");
 JSType expectedType = null;
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);
 assertNull(actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
 Node condition = createCondition("goog.isNull(foo)");
 JSType expectedType = getNativeType(NULL_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNotNull() {
 Node condition = createCondition("goog.isNull(foo)");
 JSType expectedType = null;
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);
 assertNull(actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
 Node condition = createCondition("goog.isDefAndNotNull(foo)");
 JSType expectedType = getNativeType(OBJECT_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertEquals(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test(expected = AssertionError.class)
 public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
 Node condition = createCondition("goog.invalidCondition(foo)");
 FlowScope scope = new FlowScope(null, null, null);
 interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
 Node condition = createCondition("goog.isArray(foo)");
 JSType expectedType = getNativeType(ARRAY_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNotArray() {
 Node condition = createCondition("goog.isArray(foo)");
 JSType expectedType = null;
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);
 assertNull(actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
 Node condition = createCondition("goog.isObject(foo)");
 JSType expectedType = getNativeType(OBJECT_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNotObject() {
 Node condition = createCondition("goog.isObject(foo)");
 JSType expectedType = null;
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);
 assertNull(actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isString() {
 Node condition = createCondition("goog.isString(foo)");
 JSType expectedType = getNativeType(STRING);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
 Node condition = createCondition("goog.isBoolean(foo)");
 JSType expectedType = getNativeType(BOOLEAN);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
 Node condition = createCondition("goog.isNumber(foo)");
 JSType expectedType = getNativeType(NUMBER);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
 Node condition = createCondition("goog.isFunction(foo)");
 JSType expectedType = getNativeType(FUNCTION_TYPE);
 FlowScope scope = new FlowScope(null, null, null);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);
 assertSame(expectedType, actualScope.getDeclaredType("foo"));
 }

 private Node createCondition(String conditionString) {
 Node condition = new Node_();
 condition.setType(CALL);
 condition.setFirstChild(new NodeIdent_("goog"));
 condition.setLastChild(new NodeString_(conditionString));
 return condition;
 }

}