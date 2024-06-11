import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.INFOSET_TYPE;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.core.Context;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan_Numbers_Different_Values() {
 Expression arg1 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 5.0);
 Expression arg2 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 10.0);
 CoreOperationLessThan opLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = opLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThan_Numbers_Same_Value() {
 Expression arg1 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 5.0);
 Expression arg2 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 5.0);
 CoreOperationLessThan opLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = opLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThan_Numbers_First_Smaller() {
 Expression arg1 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 2.5);
 Expression arg2 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 5.0);
 CoreOperationLessThan opLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext();
 Object result = opLessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThan_Invalid_Arguments() {
 Expression arg1 = new CoreOperationArithmeticExpression(new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 5.0), Operator.DIVIDE, new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), "invalid"));
 Expression arg2 = new LiteralExpression(new Context(INFOSET_TYPE.DOCUMENT, new Path(""), new InfoSet()), 10.0);
 CoreOperationLessThan opLessThan = new CoreOperationLessThan(arg1, arg2);
 EvalContext context = new EvalContext();
 opLessThan.computeValue(context); // This should throw an exception
 }
}