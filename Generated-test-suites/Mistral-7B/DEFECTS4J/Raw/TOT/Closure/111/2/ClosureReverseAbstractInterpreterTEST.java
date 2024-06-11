import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {

 private CodingConvention convention;
 private JSTypeRegistry typeRegistry;
 private ClosureReverseAbstractInterpreter interpreter;

 @Before
 public void setUp() {
 convention = new CodingConvention();
 typeRegistry = new JSTypeRegistry();
 interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
 }

 @Test
 public void testIsDef() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newUndefined());
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, true);

 JSType expectedType = typeRegistry.createTopType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithBooleanValue() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newBoolean(true));
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, true);

 JSType expectedType = typeRegistry.createBooleanType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithNumberValue() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newNumber(1));
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, true);

 JSType expectedType = typeRegistry.createNumberType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithStringValue() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newString("hello"));
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, true);

 JSType expectedType = typeRegistry.createStringType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithFunctionValue() {
 Node function = Node.newExpressionStatement(
 Node.newCall(Node.newName("goog"), Node.newName("isDef"), Node.newName("foo")));
 Node condition = Node.newCall(Node.newName("goog"), Node.newName("isDef"),
 Node.newCall(Node.newName("goog"), Node.newName("getProp"), Node.newName("foo"),
 Node.newString("bar")));
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, true);

 JSType expectedType = typeRegistry.createFunctionType(new FunctionType(
 Node.newName("foo"), VoidType.getInstance(), new Class<?>[0]));
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithNullValue() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newNull());
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, false);

 JSType expectedType = typeRegistry.createVoidType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithUndefinedValue() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newUndefined());
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, false);

 JSType expectedType = typeRegistry.createVoidType();
 assertEquals(expectedType, informedScope.getValueType());
 }

 @Test
 public void testIsDefWithNonExistentProperty() {
 Node condition = Node.newCall(
 Node.newName("goog"), Node.newName("isDef"), Node.newCall(
 Node.newName("goog"), Node.newName("getProp"), Node.newName("foo"),
 Node.newString("bar")));
 FlowScope blindScope = interpreter.getBlindScope();
 FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, blindScope, false);

 JSType expectedType = typeRegistry.createVoidType();
 assertEquals(expectedType, informedScope.getValueType());
 }
}