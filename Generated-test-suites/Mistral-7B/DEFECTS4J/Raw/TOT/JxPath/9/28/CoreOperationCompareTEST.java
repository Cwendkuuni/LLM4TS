import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.PathNotFoundException;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(Integer.valueOf(5)),
 new ConstantExpression(Integer.valueOf(5)));
 ExpressionContext context = new ExpressionContext();
 Object result = compare.compile(context).evaluate(new JXPathContext(new InitialContext(), context));
 assertTrue(result);
 }

 @Test
 public void testNotEquals() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(Integer.valueOf(5)),
 new ConstantExpression(Integer.valueOf(6)));
 ExpressionContext context = new ExpressionContext();
 Object result = compare.compile(context).evaluate(new JXPathContext(new InitialContext(), context));
 assertFalse(result);
 }

 @Test
 public void testEqualsWithNulls() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(Integer.valueOf(5)),
 new ConstantExpression(null));
 ExpressionContext context = new ExpressionContext();
 Object result = compare.compile(context).evaluate(new JXPathContext(new InitialContext(), context));
 assertFalse(result);
 }

 @Test
 public void testEqualsWithCollections() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new HashSet() {{ add(Integer.valueOf(5)); add(Integer.valueOf(6)); }}),
 new ConstantExpression(new HashSet() {{ add(Integer.valueOf(5)); add(Integer.valueOf(5)); }}));
 ExpressionContext context = new ExpressionContext();
 Object result = compare.compile(context).evaluate(new JXPathContext(new InitialContext(), context));
 assertTrue(result);
 }

 @Test(expected = PathNotFoundException.class)
 public void testMismatchedTypes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(Integer.valueOf(5)),
 new ConstantExpression("five"));
 ExpressionContext context = new ExpressionContext();
 compare.compile(context);
 }
}