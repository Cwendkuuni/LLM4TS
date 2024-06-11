import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClosureReverseAbstractInterpreterTest {

 private ClosureReverseAbstractInterpreter interpreter;
 private JSTypeRegistry typeRegistry;
 private CodingConvention convention;

 @Before
 public void setUp() {
 typeRegistry = new JSTypeRegistry();
 convention = new CodingConvention();
 interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
 }

 @Test
 public void testIsDef() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isDef"),
 Node.newArray(Node.newBoolean(true)));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsDefFalse() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isDef"),
 Node.newArray(Node.newBoolean(false)));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, false);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsDefNull() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isDef"),
 Node.newArray(Node.newNull()));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsNull() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isNull"),
 Node.newNull());
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.NULL_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsNotNull() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isNull"),
 Node.newNull());
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, false);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsDefAndNotNull() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isDefAndNotNull"),
 Node.newNull());
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsString() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isString"),
 Node.newString("test"));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.STRING_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsBoolean() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isBoolean"),
 Node.newBoolean(true));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsNumber() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isNumber"),
 Node.newNumber(1));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsFunction() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isFunction"),
 Node.newFunction(null, null, null));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsArray() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isArray"),
 Node.newArray(Node.newNumber(1)));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsArrayFalse() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isArray"),
 Node.newNull());
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, false);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsObject() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isObject"),
 Node.newObjectLiteral(null));
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, true);
 assertEquals(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE),
 flowScope.getVariable("result").getType());
 }

 @Test
 public void testIsObjectFalse() {
 Node node = Node.newCall(
 Node.newIdentifier("goog"),
 Node.newIdentifier("isObject"),
 Node.newNull());
 TypeScope scope = TypeScope.newRootScope();
 FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(node, scope, false);
 assertEquals(typeRegistry.getTypeFactory().createNullType(),
 flowScope.getVariable("result").getType());
 }

}