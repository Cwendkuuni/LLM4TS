import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 // Test case 1: Testing with valid double values
 @Test
 public void testGreaterThanOrEqualWithValidValues() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertTrue(op.computeValue(context).booleanValue());

 arg2 = new LiteralExpression(2.0);
 assertTrue(op.computeValue(context).booleanValue());
 }

 // Test case 2: Testing with negative double values
 @Test
 public void testGreaterThanOrEqualWithNegativeValues() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(-1.5);
 Expression arg2 = new LiteralExpression(-1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertTrue(op.computeValue(context).booleanValue());

 arg2 = new LiteralExpression(-2.0);
 assertTrue(op.computeValue(context).booleanValue());
 }

 // Test case 3: Testing with one argument as a number and the other as a string
 @Test(expected = ClassCastException.class)
 public void testGreaterThanOrEqualWithMismatchedTypes() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new StringExpression("5.5");
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 op.computeValue(context); // This line should throw a ClassCastException
 }

 // Test case 4: Testing with one argument as a number and the other as null
 @Test(expected = NullPointerException.class)
 public void testGreaterThanOrEqualWithNullValue() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new LiteralExpression(1.5);
 Expression arg2 = new NullExpression();
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 op.computeValue(context); // This line should throw a NullPointerException
 }

 // Test case 5: Testing with one argument as a null expression
 @Test(expected = NullPointerException.class)
 public void testGreaterThanOrEqualWithNullExpression() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new NullExpression();
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 op.computeValue(context); // This line should throw a NullPointerException
 }

 // Test case 6: Testing with one argument as an empty expression
 @Test
 public void testGreaterThanOrEqualWithEmptyExpression() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new EmptyExpression();
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertFalse(op.computeValue(context).booleanValue());
 }

 // Test case 7: Testing with one argument as a expression that returns null
 @Test
 public void testGreaterThanOrEqualWithNullReturningExpression() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new ConstantExpression(null);
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 assertFalse(op.computeValue(context).booleanValue());
 }

 // Test case 8: Testing with one argument as a expression that throws an exception
 @Test(expected = RuntimeException.class)
 public void testGreaterThanOrEqualWithThrowingExpression() {
 InfoSet infoSet = TestUtil.newInfoSet();
 EvalContext context = new EvalContext(infoSet);

 Expression arg1 = new ThrowingExpression();
 Expression arg2 = new LiteralExpression(1.5);
 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);

 try {
 op.computeValue(context);
 } catch (RuntimeException e) {
 // Test passes if the computeValue method throws a RuntimeException
 }
 }
}