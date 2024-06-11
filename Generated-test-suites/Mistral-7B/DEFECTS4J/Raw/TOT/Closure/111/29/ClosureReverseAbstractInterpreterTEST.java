import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ClosureReverseAbstractInterpreterTest {

 private static final CodingConvention CONVENTION = new CodingConvention();
 private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
 private static final ClosureReverseAbstractInterpreter interpreter =
     new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);

 @Test
 public void testIsDef() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Type expectedType = Node.newObjectType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getVariable("someVariable").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("nullVariable"));
 Type expectedType = Node.newNullType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getVariable("nullVariable").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefAndNotNull() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Node notNullCondition = Node.newLogicalOperator(
 Node.OP_NE, Node.newName("someVariable"), Node.newName("null"));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, Node.newFlowScope(), false).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 notNullCondition, Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 expectedType = Node.newObjectType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsString() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isString"),
 Node.newString("someString"));
 Type expectedType = Node.newStringType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsStringFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isString"),
 Node.newNumber(1));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsFunction() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isFunction"),
 Node.newName("someFunction"));
 Type expectedType = Node.newFunctionType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getVariable("someFunction").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsFunctionFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isFunction"),
 Node.newNumber(1));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsArray() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isArray"),
 Node.newName("someArray"));
 Type expectedType = Node.newArrayType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getVariable("someArray").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsArrayFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isArray"),
 Node.newNumber(1));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsObject() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isObject"),
 Node.newName("someObject"));
 Type expectedType = Node.newObjectType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getVariable("someObject").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsObjectFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isObject"),
 Node.newNumber(1));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsNull() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isNull"),
 Node.newName("nullVariable"));
 Type expectedType = Node.newNullType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, true).getScope().getVariable("nullVariable").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsNullFalse() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isNull"),
 Node.newName("someVariable"));
 Type expectedType = Node.newVoidType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
 null, false).getScope().getVariable("someVariable").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefAndNotNullAndIsString() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Node notNullCondition = Node.newLogicalOperator(
 Node.OP_NE, Node.newName("someVariable"), Node.newName("null"));
 Node isStringCondition = Node.newCall(Node.newName("goog"), Node.newName("isString"),
 Node.newName("someVariable"));
 Type expectedType = Node.newStringType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, Node.newFlowScope(), false).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(notNullCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(isStringCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefAndNotNullAndIsFunction() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Node notNullCondition = Node.newLogicalOperator(
 Node.OP_NE, Node.newName("someVariable"), Node.newName("null"));
 Node isFunctionCondition = Node.newCall(Node.newName("goog"), Node.newName("isFunction"),
 Node.newName("someVariable"));
 Type expectedType = Node.newFunctionType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, Node.newFlowScope(), false).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(notNullCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(isFunctionCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefAndNotNullAndIsArray() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Node notNullCondition = Node.newLogicalOperator(
 Node.OP_NE, Node.newName("someVariable"), Node.newName("null"));
 Node isArrayCondition = Node.newCall(Node.newName("goog"), Node.newName("isArray"),
 Node.newName("someVariable"));
 Type expectedType = Node.newArrayType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, Node.newFlowScope(), false).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(notNullCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(isArrayCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefAndNotNullAndIsObject() {
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newName("someVariable"));
 Node notNullCondition = Node.newLogicalOperator(
 Node.OP_NE, Node.newName("someVariable"), Node.newName("null"));
 Node isObjectCondition = Node.newCall(Node.newName("goog"), Node.newName("isObject"),
 Node.newName("someVariable"));
 Type expectedType = Node.newObjectType();
 Type actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, Node.newFlowScope(), false).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(notNullCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 actualType = interpreter.getPreciserScopeKnowingConditionOutcome(isObjectCondition,
 Node.newFlowScope(), true).getScope().getVariable("someVariable")
 .getType();
 assertEquals(expectedType, actualType);
 }
}