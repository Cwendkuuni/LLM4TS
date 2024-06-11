import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationException;
import org.apache.commons.jxpath.ri.AbstractTestSupport;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest extends AbstractTestSupport {

 @Test
 public void testCoreOperationCompareEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(1), new LiteralExpression(1)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testCoreOperationCompareNotEqual() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(1), new LiteralExpression(2)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test
 public void testCoreOperationCompareEqualCollections() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CollectionExpression(new Object[]{1, 2, 3}),
 new CollectionExpression(new Object[]{1, 2, 3})
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 assertTrue(op.evaluate(context).booleanValue());
 }

 @Test
 public void testCoreOperationCompareNotEqualCollections() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CollectionExpression(new Object[]{1, 2, 3}),
 new CollectionExpression(new Object[]{1, 2, 4})
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 assertFalse(op.evaluate(context).booleanValue());
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareBooleanAndBoolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(true), new LiteralExpression(true)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareBooleanAndPointer() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(true), new LiteralExpression(new Integer(1))
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareBooleanAndNull() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(true), new LiteralExpression(null)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareStringAndStringNull() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression("hello"), new LiteralExpression(null)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareStringAndNumber() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression("hello"), new LiteralExpression(1)
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareNumberAndString() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(1), new LiteralExpression("hello")
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareNullAndPointer() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(null), new LiteralExpression(new Integer(1))
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }

 @Test(expected = EvaluationException.class)
 public void testCoreOperationCompareNullAndCollection() {
 CoreOperationCompare op = new CoreOperationCompare(
 new LiteralExpression(null), new CollectionExpression(new Object[]{1})
 );
 EvaluationContext context = new EvaluationContext(getDoc());
 op.evaluate(context); // should not reach here
 fail("Expected EvaluationException, but it did not occur.");
 }
}