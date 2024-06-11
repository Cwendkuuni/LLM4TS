package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateResult;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.axes.AxisIterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.EmptyIterateResult;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterateResult;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(6));
 EvalContext context = new EvalContext();
 context.setAxis(Axis.CHILD);

 // Test with empty node sets
 IterateResult left = new EmptyIterateResult();
 IterateResult right = new EmptyIterateResult();
 assertFalse("Expected false for empty node sets", expression.computeValue(context, left, right).equals(Boolean.TRUE));

 // Test with single node sets
 Node leftNode = new Node("left", 5);
 Node rightNode = new Node("right", 6);
 AxisIterator leftIter = Iterators.iterate(new SelfContext(leftNode));
 AxisIterator rightIter = Iterators.iterate(new SelfContext(rightNode));
 IterateResult leftResult = new SingleNodeIterateResult(leftIter);
 IterateResult rightResult = new SingleNodeIterateResult(rightIter);
 assertTrue("Expected true for nodes with values 5 and 6", expression.computeValue(context, leftResult, rightResult).equals(Boolean.TRUE));

 // Test with iterators having the same elements in different orders
 Iterator leftIt = Iterators.iterate(new SelfContext(5));
 Iterator rightIt = Iterators.iterate(new SelfContext(6));
 IterateResult leftResult2 = new IterateResult(leftIt, Iterators.emptyIterateResult(), null);
 IterateResult rightResult2 = new IterateResult(rightIt, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements in different orders", expression.computeValue(context, leftResult2, rightResult2).equals(Boolean.TRUE));

 // Test with iterators having the same elements in the same order
 Iterator leftIt2 = Iterators.iterate(new SelfContext(6));
 Iterator rightIt2 = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult3 = new IterateResult(leftIt2, Iterators.emptyIterateResult(), null);
 IterateResult rightResult3 = new IterateResult(rightIt2, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with the same elements in reverse order", expression.computeValue(context, leftResult3, rightResult3).equals(Boolean.TRUE));

 // Test with iterators having different elements
 Iterator leftIt3 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt3 = Iterators.iterate(new SelfContext(7));
 IterateResult leftResult4 = new IterateResult(leftIt3, Iterators.emptyIterateResult(), null);
 IterateResult rightResult4 = new IterateResult(rightIt3, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with different elements", expression.computeValue(context, leftResult4, rightResult4).equals(Boolean.TRUE));
}

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(6));
 EvalContext context = new EvalContext();
 context.setAxis(Axis.CHILD);

 // Test with empty node sets
 IterateResult left = new EmptyIterateResult();
 IterateResult right = new EmptyIterateResult();
 assertTrue("Expected true for empty node sets", expression.computeValue(context, left, right).equals(Boolean.TRUE));

 // Test with single node sets
 Node leftNode = new Node("left", 5);
 Node rightNode = new Node("right", 6);
 AxisIterator leftIter = Iterators.iterate(new SelfContext(leftNode));
 AxisIterator rightIter = Iterators.iterate(new SelfContext(rightNode));
 IterateResult leftResult = new SingleNodeIterateResult(leftIter);
 IterateResult rightResult = new SingleNodeIterateResult(rightIter);
 assertTrue("Expected true for nodes with values 5 and 6", expression.computeValue(context, leftResult, rightResult).equals(Boolean.TRUE));

 // Test with iterators having the same elements in different orders
 Iterator leftIt = Iterators.iterate(new SelfContext(6));
 Iterator rightIt = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult2 = new IterateResult(leftIt, Iterators.emptyIterateResult(), null);
 IterateResult rightResult2 = new IterateResult(rightIt, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements in different orders", expression.computeValue(context, leftResult2, rightResult2).equals(Boolean.TRUE));

 // Test with iterators having the same elements in the same order
 Iterator leftIt2 = Iterators.iterate(new SelfContext(6));
 Iterator rightIt2 = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult3 = new IterateResult(leftIt2, Iterators.emptyIterateResult(), null);
 IterateResult rightResult3 = new IterateResult(rightIt2, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements in the same order", expression.computeValue(context, leftResult3, rightResult3).equals(Boolean.TRUE));

 // Test with iterators having the same elements but one having a larger value
 Iterator leftIt4 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt4 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt5 = Iterators.iterate(new SelfContext(6));
 IterateResult leftResult4 = new IterateResult(leftIt4, Iterators.iterate(new SelfContext(5)), null);
 IterateResult rightResult4 = new IterateResult(rightIt4, IterateResult.iterate(rightIt5), null);
 assertTrue("Expected true for iterators with the same elements but one having a larger value", expression.computeValue(context, leftResult4, rightResult4).equals(Boolean.TRUE));

 // Test with iterators having different elements
 Iterator leftIt5 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt5 = Iterators.iterate(new SelfContext(7));
 IterateResult leftResult5 = new IterateResult(leftIt5, Iterators.emptyIterateResult(), null);
 IterateResult rightResult5 = new IterateResult(rightIt5, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with different elements", expression.computeValue(context, leftResult5, rightResult5).equals(Boolean.TRUE));
}

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(5), new ConstantExpression(6));
 EvalContext context = new EvalContext();
 context.setAxis(Axis.CHILD);

 // Test with empty node sets
 IterateResult left = new EmptyIterateResult();
 IterateResult right = new EmptyIterateResult();
 assertFalse("Expected false for empty node sets", expression.computeValue(context, left, right).equals(Boolean.TRUE));

 // Test with single node sets
 Node leftNode = new Node("left", 5);
 Node rightNode = new Node("right", 6);
 AxisIterator leftIter = Iterators.iterate(new SelfContext(leftNode));
 AxisIterator rightIter = Iterators.iterate(new SelfContext(rightNode));
 IterateResult leftResult = new SingleNodeIterateResult(leftIter);
 IterateResult rightResult = new SingleNodeIterateResult(rightIter);
 assertFalse("Expected false for nodes with values 5 and 6", expression.computeValue(context, leftResult, rightResult).equals(Boolean.TRUE));

 // Test with iterators having the same elements in different orders
 Iterator leftIt = Iterators.iterate(new SelfContext(6));
 Iterator rightIt = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult2 = new IterateResult(leftIt, Iterators.emptyIterateResult(), null);
 IterateResult rightResult2 = new IterateResult(rightIt, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with the same elements in different orders", expression.computeValue(context, leftResult2, rightResult2).equals(Boolean.TRUE));

 // Test with iterators having the same elements in the same order
 Iterator leftIt2 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt2 = Iterators.iterate(new SelfContext(6));
 IterateResult leftResult3 = new IterateResult(leftIt2, Iterators.emptyIterateResult(), null);
 IterateResult rightResult3 = new IterateResult(rightIt2, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with the same elements in the same order", expression.computeValue(context, leftResult3, rightResult3).equals(Boolean.TRUE));

 // Test with iterators having different elements
 Iterator leftIt3 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt3 = Iterators.iterate(new SelfContext(7));
 IterateResult leftResult4 = new IterateResult(leftIt3, Iterators.emptyIterateResult(), null);
 IterateResult rightResult4 = new IterateResult(rightIt3, Iterators.emptyIterateResult(), null);
 assertFalse("Expected false for iterators with different elements", expression.computeValue(context, leftResult4, rightResult4).equals(Boolean.TRUE));
}

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(6));
 EvalContext context = new EvalContext();
 context.setAxis(Axis.CHILD);

 // Test with empty node sets
 IterateResult left = new EmptyIterateResult();
 IterateResult right = new EmptyIterateResult();
 assertTrue("Expected true for empty node sets", expression.computeValue(context, left, right).equals(Boolean.TRUE));

 // Test with single node sets
 Node leftNode = new Node("left", 5);
 Node rightNode = new Node("right", 6);
 AxisIterator leftIter = Iterators.iterate(new SelfContext(leftNode));
 AxisIterator rightIter = Iterators.iterate(new SelfContext(rightNode));
 IterateResult leftResult = new SingleNodeIterateResult(leftIter);
 IterateResult rightResult = new SingleNodeIterateResult(rightIter);
 assertFalse("Expected false for nodes with values 5 and 6", expression.computeValue(context, leftResult, rightResult).equals(Boolean.TRUE));

 // Test with iterators having the same elements in different orders
 Iterator leftIt = Iterators.iterate(new SelfContext(6));
 Iterator rightIt = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult2 = new IterateResult(leftIt, Iterators.emptyIterateResult(), null);
 IterateResult rightResult2 = new IterateResult(rightIt, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements in different orders", expression.computeValue(context, leftResult2, rightResult2).equals(Boolean.TRUE));

 // Test with iterators having the same elements in the same order
 Iterator leftIt2 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt2 = Iterators.iterate(new SelfContext(6));
 IterateResult leftResult3 = new IterateResult(leftIt2, Iterators.emptyIterateResult(), null);
 IterateResult rightResult3 = new IterateResult(rightIt2, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements in the same order", expression.computeValue(context, leftResult3, rightResult3).equals(Boolean.TRUE));

 // Test with iterators having the same elements but one having a smaller value
 Iterator leftIt4 = Iterators.iterate(new SelfContext(6));
 Iterator rightIt4 = Iterators.iterate(new SelfContext(5));
 IterateResult leftResult4 = new IterateResult(leftIt4, Iterators.emptyIterateResult(), null);
 IterateResult rightResult4 = new IterateResult(rightIt4, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with the same elements but one having a smaller value", expression.computeValue(context, leftResult4, rightResult4).equals(Boolean.TRUE));

 // Test with iterators having different elements
 Iterator leftIt5 = Iterators.iterate(new SelfContext(5));
 Iterator rightIt5 = Iterators.iterate(new SelfContext(7));
 IterateResult leftResult5 = new IterateResult(leftIt5, Iterators.emptyIterateResult(), null);
 IterateResult rightResult5 = new IterateResult(rightIt5, Iterators.emptyIterateResult(), null);
 assertTrue("Expected true for iterators with different elements", expression.computeValue(context, leftResult5, rightResult5).equals(Boolean.TRUE));
}

}