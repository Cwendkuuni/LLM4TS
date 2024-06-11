import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new ConstantExpression(5.5), new ConstantExpression(3.0)
 );

 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanEqual() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new ConstantExpression(5.5), new ConstantExpression(5.0)
 );

 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithNaN() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new ConstantExpression(Double.NaN), new ConstantExpression(3.0)
 );

 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanWithInfiniteValues() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new ConstantExpression(Double.NEGATIVE_INFINITY), new ConstantExpression(3.0)
 );

 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanWithNegativeZero() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new ConstantExpression(-0.0), new ConstantExpression(-3.0)
 );

 Object result = lessThan.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithNonNumericValues() {
 InfoSet infoSet = new InfoSet(); // empty InfoSet
 EvalContext context = new EvalContext();
 context.setCurrentInfo(infoSet);

 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new StringConstant("hello"), new StringConstant("world")
 );
 lessThan.computeValue(context); // should throw ClassCastException
 }
}