import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

 private JSTypeRegistry typeRegistry;
 private CodingConvention convention;
 private ClosureReverseAbstractInterpreter interpreter;

 @Before
 public void setUp() {
 typeRegistry = new JSTypeRegistry();
 convention = new CodingConvention(typeRegistry);
 interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeWithTrueIsDef() {
 Node condition = createNode("/isDef/true");
 JSType expectedType = getNativeType(OBJECT_TYPE);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, null, true);
 assertEquals(expectedType, actualScope.getVariable("result").getType());
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeWithFalseIsDef() {
 Node condition = createNode("/isDef/false");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, null, false);
 assertNull(actualScope.getVariable("result"));
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeWithTrueIsString() {
 Node condition = createNode("/isString/true");
 JSType expectedType = getNativeType(STRING);
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, null, true);
 assertEquals(expectedType, actualScope.getVariable("result").getType());
 }

 @Test
 public void testGetPreciserScopeKnowingConditionOutcomeWithFalseIsString() {
 Node condition = createNode("/isString/false");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, null, false);
 assertNull(actualScope.getVariable("result"));
 }

 @Test(expected = AssertionError.class)
 public void testGetPreciserScopeKnowingConditionOutcomeWithNullType() {
 Node condition = createNode("/null");
 FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(
 condition, null, true);
 fail("Expected null type to throw an AssertionError");
 }

 private Node createNode(String expression) {
 Node node = new Node();
 node.setType(CALL);
 Node callee = new Node();
 callee.setType(GETPROP);
 node.addChild(callee);
 Node left = new Node();
 left.setType(NAME);
 left.setString("goog");
 callee.addChild(left);
 Node right = new Node();
 right.setType(STRING);
 right.setString(expression);
 callee.addChild(right);
 node.addChild(right);
 return node;
 }

}