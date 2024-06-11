import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 private Expression createExpression(NumberNode left, NumberNode right) {
 return new CoreOperationLessThanOrEqual(left, right);
 }

 @Test
 public void testLessThanOrEqualTrue() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(3.0);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualFalse() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(5.0);
 NumberNode right = new NumberNode(3.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualEqual() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(3.0);
 NumberNode right = new NumberNode(3.0);
 Expression op = createExpression(left, right);
 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNaN() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(Double.NaN);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNegativeNumber() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(-3.0);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualZero() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(0.0);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualZeroToZero() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(0.0);
 NumberNode right = new NumberNode(0.0);
 Expression op = createExpression(left, right);
 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualInf() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(Double.POSITIVE_INFINITY);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNegInf() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(Double.NEGATIVE_INFINITY);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNull() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode((Double) null);
 NumberNode right = new NumberNode(5.0);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualNullRight() {
 EvalContext context = new EvalContext();
 NumberNode left = new NumberNode(3.0);
 NumberNode right = new NumberNode((Double) null);
 Expression op = createExpression(left, right);
 assertFalse(op.computeValue(context).booleanValue());
 }
}