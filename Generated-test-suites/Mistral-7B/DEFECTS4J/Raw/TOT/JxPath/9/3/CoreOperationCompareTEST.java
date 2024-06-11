import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.PointValue;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(1),
 new LiteralExpression(1)
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { 1, 1 });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 assertTrue(result.getValue().booleanValue());
 }

 @Test
 public void testNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(1),
 new LiteralExpression(2)
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { 1, 2 });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 assertFalse(result.getValue().booleanValue());
 }

 @Test
 public void testEqualStrings() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression("Hello"),
 new LiteralExpression("Hello")
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { "Hello", "Hello" });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 assertTrue(result.getValue().booleanValue());
 }

 @Test
 public void testNotEqualStrings() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression("Hello"),
 new LiteralExpression("World")
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { "Hello", "World" });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 assertFalse(result.getValue().booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotComparableTypes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new LiteralExpression(1),
 new LiteralExpression("Hello")
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { 1, "Hello" });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 try {
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMismatchedIteratorsSize() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new VariableExpression("myCollection"),
 new LiteralExpression(5)
 );
 JXPathContext context = new JXPathContext(new EvaluationContext(), new Object[] { new Object[] { new Integer[] { 1, 2, 3, 4, 5, 6 }, null } });
 ExpressionContext eContext = context.setExpressionContext(new ExpressionContext());
 try {
 PointValue result = (PointValue) compare.evaluate(context, eContext);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}