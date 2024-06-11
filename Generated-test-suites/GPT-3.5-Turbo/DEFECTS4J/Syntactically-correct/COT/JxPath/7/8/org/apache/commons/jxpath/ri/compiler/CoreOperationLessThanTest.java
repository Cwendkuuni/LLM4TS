import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

   @Test
   public void testComputeValue() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
      EvalContext context = new EvalContext();
      context.setVariable("arg1", 10);
      context.setVariable("arg2", 5);
      assertEquals(Boolean.FALSE, operation.computeValue(context));
   }

   @Test
   public void testGetSymbol() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
      assertEquals("<", operation.getSymbol());
   }

}