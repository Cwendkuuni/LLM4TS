import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanDoubleValues() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(5.5), new ConstantExpression(3.0));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 throw new RuntimeException(e);
 }

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanDoubleValues() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(3.0), new ConstantExpression(5.5));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 throw new RuntimeException(e);
 }

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGreaterThanFloatValues() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(5.5f), new ConstantExpression(3.0f));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 throw new RuntimeException(e);
 }

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanFloatValues() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(3.0f), new ConstantExpression(5.5f));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 throw new RuntimeException(e);
 }

 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testGreaterThanNullAndNumber() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(3.0), new ConstantExpression(null));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 throw new RuntimeException(e);
 }

 assertFalse(result instanceof Boolean);
 }

 @Test(expected = EvaluationException.class)
 public void testDivisionByZero() {
 InfoSet is = new InfoSet();
 Path path = new Path("/");
 JXPathContext ctx = new JXPathContext(is, path);

 CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new ConstantExpression(5.5), new ConstantExpression(0.0));
 Object result;
 try {
 result = expr.computeValue(ctx);
 } catch (EvaluationException e) {
 // Expected
 assertEquals("Error evaluating expression: division by zero", e.getMessage());
 }
 }
}