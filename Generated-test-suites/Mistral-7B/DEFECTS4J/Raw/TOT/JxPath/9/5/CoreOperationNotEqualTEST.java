package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

 private Expression arg1, arg2;
 private CoreOperationNotEqual notEqual;
 private EvaluationContext evaluationContext;

 @Before
 public void setup() {
 arg1 = new Variable("arg1");
 arg2 = new Variable("arg2");
 evaluationContext = new EvaluationContext();
 notEqual = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test
 public void testNotEqual_WhenBothArgsAreFalse_ReturnsTrue() {
 evaluationContext.set("arg1", Boolean.FALSE);
 evaluationContext.set("arg2", Boolean.FALSE);
 Object result = notEqual.computeValue(evaluationContext);
 assertTrue(result instanceof Boolean && Boolean.TRUE.equals(result));
 }

 @Test
 public void testNotEqual_WhenBothArgsAreTrue_ReturnsFalse() {
 evaluationContext.set("arg1", Boolean.TRUE);
 evaluationContext.set("arg2", Boolean.TRUE);
 Object result = notEqual.computeValue(evaluationContext);
 assertFalse(result instanceof Boolean || result instanceof Boolean && Boolean.TRUE.equals(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_WhenOneArgIsNull_ThrowsIllegalArgumentException() {
 evaluationContext.set("arg1", null);
 evaluationContext.set("arg2", Boolean.TRUE);
 notEqual.computeValue(evaluationContext);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEqual_WhenBothArgsAreOfDifferentTypes_ThrowsIllegalArgumentException() {
 evaluationContext.set("arg1", Boolean.TRUE);
 evaluationContext.set("arg2", new Integer(5));
 notEqual.computeValue(evaluationContext);
 }
}