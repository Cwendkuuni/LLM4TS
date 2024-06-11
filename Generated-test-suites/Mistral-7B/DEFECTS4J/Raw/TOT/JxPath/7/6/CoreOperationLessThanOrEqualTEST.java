import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri. noddediting.NumberValue;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 private static Expression createExpression(String expression) {
 return new JXPathParser().parseExpression(expression).expressions[0];
 }

 @Test
 public void testLessThanOrEqualValid() {
 InfoSet infoSet = new InfoSet();
 infoSet.insertNode(new NumberValue(3.5, null));
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 createExpression("2.5"), createExpression("3.5")
 );

 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualEqual() {
 InfoSet infoSet = new InfoSet();
 infoSet.insertNode(new NumberValue(3.5, null));
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 createExpression("3.5"), createExpression("3.5")
 );

 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test
 public void testLessThanOrEqualFalse() {
 InfoSet infoSet = new InfoSet();
 infoSet.insertNode(new NumberValue(3.5, null));
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 createExpression("3.5"), createExpression("4.5")
 );

 assertFalse(op.computeValue(context).booleanValue());
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanOrEqualNullArg() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 null, createExpression("4.5")
 );

 op.computeValue(context); // expected RuntimeException
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanOrEqualNullArg2() {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 createExpression("2.5"), null
 );

 op.computeValue(context); // expected RuntimeException
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanOrEqualInvalidType() {
 InfoSet infoSet = new InfoSet();
 infoSet.insertNode(new NumberValue(3.5, null));
 EvalContext context = new EvalContext(infoSet);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 createExpression("2.5"), createExpression("true")
 );

 op.computeValue(context); // expected ClassCastException
 }
}