package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.IteratorExpression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

@Test
public void testComputeValueBasic() {
CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(10));
Assert.assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext()));
}

@Test
public void testGetPrecedence() {
CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(null, null);
Assert.assertEquals(RELATIONAL_EXPR_PRECEDENCE, expr.getPrecedence());
}

@Test
public void testIsSymmetric() {
CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(null, null);
Assert.assertEquals(false, expr.isSymmetric());
}

@Test
public void testEvaluateCompare() {
// Replace with appropriate values or methods to test the evaluateCompare() logic
CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(null, null);
Assert.assertEquals(true, expr.evaluateCompare(1));
Assert.assertEquals(false, expr.evaluateCompare(0));
}

@Test
public void testComputeValueEdgeCases() {
CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(5));
Assert.assertEquals(Boolean.FALSE, expr1.computeValue(new EvalContext()));

CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(new ConstantExpression("5"), new ConstantExpression("5")); // String comparison
Assert.assertEquals(Boolean.FALSE, expr2.computeValue(new EvalContext()));

CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
Assert.assertEquals(Boolean.FALSE, expr3.computeValue(new EvalContext()));
}

@Test(expected = RuntimeException.class)
public void testComputeValueException() {
CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression("5"), new ConstantExpression(new Object())); // Invalid type exception
Assert.fail("This test should throw an exception");
}

@Test
public void testComputeValueIterators() {
CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new IteratorExpression(Arrays.asList(1, 2, 3).iterator()), new ConstantExpression(3));
Assert.assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext()));

CoreOperationGreaterThan expr2 = new CoreOperationGreaterThan(new ConstantExpression(Arrays.asList(1, 2, 3).iterator()), new IteratorExpression(Arrays.asList(2, 3, 4).iterator()));
Assert.assertEquals(Boolean.FALSE, expr2.computeValue(new EvalContext()));
}

@Test
public void testComputeValueInvalidInputTypes() {
// Test invalid input types
CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(new ConstantExpression("5"), new ConstantExpression(10));
Assert.assertThrows(RuntimeException.class, () -> expr1.computeValue(new EvalContext()));

CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[0]);
Assert.assertThrows(RuntimeException.class, () -> expr2.computeValue(new EvalContext()));
}

@Test
public void testComputeValueNaNValues() {
CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
Assert.assertEquals(Boolean.FALSE, expr3.computeValue(new EvalContext()));
}

@Test
public void testComputeValueIteratorsEmpty() {
CoreOperationGreaterThan expr4 = new CoreOperationGreaterThan(new IteratorExpression(Collections.emptyIterator().iterator()), new ConstantExpression(3));
Assert.assertEquals(Boolean.FALSE, expr4.computeValue(new EvalContext()));
}

@Test
public void testComputeValueIteratorsMismatchSize() {
CoreOperationGreaterThan expr5 = new CoreOperationGreaterThan(new IteratorExpression(Arrays.asList(1, 2, 3).iterator()), new IteratorExpression(Arrays.asList(1, 2).iterator()));
Assert.assertEquals(Boolean.FALSE, expr5.computeValue(new EvalContext()));
}

@Test
public void testComputeValueIteratorsNull() {
CoreOperationGreaterThan expr6 = new CoreOperationGreaterThan(new IteratorExpression(null), new ConstantExpression(3));
Assert.assertNull(expr6.computeValue(new EvalContext()));
}
}