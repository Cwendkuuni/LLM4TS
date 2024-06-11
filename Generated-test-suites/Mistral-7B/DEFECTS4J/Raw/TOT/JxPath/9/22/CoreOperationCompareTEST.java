import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.iterator.SingleNodeIterator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 private CoreOperationCompare compare;
 private EvalContext context;
 private Expression arg1;
 private Expression arg2;
 private Pointer left;
 private Pointer right;

 @Before
 public void setUp() {
 // Create test expressions
 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 compare = new EqualExpression(arg1, arg2);
 context = new EvalContext();
 // Create test pointers
 left = new Pointer("5");
 right = new Pointer("5");
 }

 @Test
 public void testEquals() throws EvaluationException {
 // Test equals with simple values
 assertTrue(compare.evaluate(context).booleanValue());
 // Test equals with pointers to simple values
 left.setValue(5);
 right.setValue(5);
 context.setCurrentNode(left);
 assertTrue(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNotEquals() throws EvaluationException {
 // Test not equals with simple values
 Expression notEqual = new NotExpression(compare);
 assertFalse(notEqual.evaluate(context).booleanValue());
 // Test not equals with pointers to simple values
 assertFalse(compare.evaluate(context).booleanValue());
 left.setValue(6);
 right.setValue(5);
 context.setCurrentNode(left);
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test(expected = EvaluationException.class)
 public void testMismatchedTypes() throws EvaluationException {
 // Test mismatched types
 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression(true);
 compare = new EqualExpression(arg1, arg2);
 compare.evaluate(context);
 }

 @Test
 public void testBooleanValues() throws EvaluationException {
 // Test boolean values
 arg1 = new LiteralExpression(true);
 arg2 = new LiteralExpression(true);
 compare = new EqualExpression(arg1, arg2);
 assertTrue(compare.evaluate(context).booleanValue());
 arg2 = new LiteralExpression(false);
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testStringValues() throws EvaluationException {
 // Test string values
 arg1 = new LiteralExpression("Hello");
 arg2 = new LiteralExpression("Hello");
 compare = new EqualExpression(arg1, arg2);
 assertTrue(compare.evaluate(context).booleanValue());
 arg2 = new LiteralExpression("World");
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testNumberValues() throws EvaluationException {
 // Test number values
 arg1 = new LiteralExpression(5);
 arg2 = new LiteralExpression(5);
 compare = new EqualExpression(arg1, arg2);
 assertTrue(compare.evaluate(context).booleanValue());
 arg2 = new LiteralExpression(6);
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testCollectionValues() throws EvaluationException {
 // Test collection values
 Collection leftValues = new HashSet();
 leftValues.add(new LiteralExpression("5"));
 leftValues.add(new LiteralExpression("6"));
 arg1 = new VariableExpression("leftValues");
 context.setVariable("leftValues", new SingleNodeIterator(leftValues.iterator()));
 arg2 = new LiteralExpression("5");
 compare = new EqualExpression(arg1, arg2);
 assertTrue(compare.evaluate(context).booleanValue());
 arg2 = new LiteralExpression("6");
 assertFalse(compare.evaluate(context).booleanValue());
 }

 @Test
 public void testIterators() throws EvaluationException {
 // Test iterators
 Iterator leftIterator = new Iterator() {
 @Override public boolean hasNext() { return true; }
 @Override public Object next() { return new LiteralExpression("5"); }
 };
 arg1 = new VariableExpression("leftIterator");
 context.setVariable("leftIterator", leftIterator);
 arg2 = new LiteralExpression("5");
 compare = new EqualExpression(arg1, arg2);
 assertTrue(compare.evaluate(context).booleanValue());
 arg2 = new LiteralExpression("6");
 assertFalse(compare.evaluate(context).booleanValue());
 }
}