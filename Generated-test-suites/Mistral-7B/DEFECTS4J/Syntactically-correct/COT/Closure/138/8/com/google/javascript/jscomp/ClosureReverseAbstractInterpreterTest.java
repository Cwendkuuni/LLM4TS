import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.FunctionType;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 private JSTypeRegistry typeRegistry;
 private CodingConvention convention;
 private ClosureReverseAbstractInterpreter interpreter;

 @Before
 public void setup() {
 typeRegistry = new JSTypeRegistry();
 convention = new CodingConvention();
 interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
 }

 @Test
 public void testIsDef() {
 Node condition = Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newBool(true));
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, true);
 JSType expectedType = interpreter.restricters.get("isDef").apply(expectedRestriction);
 FlowScope expectedFlowScope = interpreter.restrictParameter(
 Node.newIdent("x"), expectedType, new FlowScope(), expectedRestriction, true);

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true).getVariable("x").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsDefFalse() {
 Node condition = Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newNull());
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, false);
 JSType expectedType = interpreter.restricters.get("isDef").apply(expectedRestriction);
 FlowScope expectedFlowScope = new FlowScope();

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), false).getVariable("x").getType();
 assertNull(actualType);
 }

 @Test
 public void testIsDefAndNotNull() {
 Node condition = Node.newBinaryOp(AND, Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newBool(true)), Node.newBinaryOp(NEQ, Node.newIdent("x"), Node.newNull()));
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, true);
 JSType expectedType = interpreter.restricters.get("isDefAndNotNull").apply(expectedRestriction);
 FlowScope expectedFlowScope = interpreter.restrictParameter(
 Node.newIdent("x"), expectedType, new FlowScope(), expectedRestriction, true);

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true).getVariable("x").getType();
 assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testIsDefAndNotNullWithNullParameter() {
 Node condition = Node.newBinaryOp(AND, Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newBool(true)), Node.newIdent("x"));
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, true);
 JSType expectedType = interpreter.restricters.get("isDefAndNotNull").apply(expectedRestriction);

 interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true);
 }

 @Test
 public void testIsString() {
 Node condition = Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newString("test"));
 TypeRestriction expectedRestriction = new TypeRestriction(STRING, true);
 JSType expectedType = interpreter.restricters.get("isString").apply(expectedRestriction);
 FlowScope expectedFlowScope = new FlowScope();

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true).getVariable("x").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsStringFalse() {
 Node condition = Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newInt(5));
 TypeRestriction expectedRestriction = new TypeRestriction(STRING, false);

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), false).getVariable("x").getType();
 assertNull(actualType);
 }

 @Test
 public void testIsArray() {
 Node condition = Node.newBinaryOp(CALL, Node.newIdent("goog"), Node.newString("isArray"), Node.newArray(new Object[] { Node.newIdent("x") }));
 TypeRestriction expectedRestriction = new TypeRestriction(NULL_TYPE, true);
 JSType expectedType = interpreter.restricters.get("isArray").apply(expectedRestriction);
 FlowScope expectedFlowScope = interpreter.restrictParameter(
 Node.newIdent("x"), expectedType, new FlowScope(), expectedRestriction, true);
 expectedFlowScope.setVariable("x", Node.newArray(new Object[] {}));

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true).getVariable("x").getType();
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIsArrayFalse() {
 Node condition = Node.newBinaryOp(CALL, Node.newIdent("goog"), Node.newString("isArray"), Node.newIdent("x"));
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, false);

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), false).getVariable("x").getType();
 assertNull(actualType);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeWithNullCondition() {
 Node condition = null;
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, true);

 JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true).getVariable("x").getType();
 assertNull(actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetPreciserScopeKnowingConditionOutcomeWithNullType() {
 Node condition = Node.newBinaryOp(EQ, Node.newIdent("x"), Node.newBool(true));
 TypeRestriction expectedRestriction = new TypeRestriction(null, true);

 interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidCondition() {
 Node condition = Node.newInt(5);
 TypeRestriction expectedRestriction = new TypeRestriction(OBJECT_TYPE, true);

 interpreter.getPreciserScopeKnowingConditionOutcome(condition, new FlowScope(), true);
 }
}