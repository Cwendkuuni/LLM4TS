import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {
  
  private CoreOperationLessThan coreOperationLessThan;
  private EvalContext context;
  
  @Before
  public void setUp() {
    coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    context = new EvalContext();
  }
  
  @Test
  public void testComputeValue() {
    double l = InfoSetUtil.doubleValue(arg1.computeValue(context));
    double r = InfoSetUtil.doubleValue(arg2.computeValue(context));
    Object actual = coreOperationLessThan.computeValue(context);
    Object expected = l < r ? Boolean.TRUE : Boolean.FALSE;
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSymbol() {
    String actual = coreOperationLessThan.getSymbol();
    String expected = "<";
    assertEquals(expected, actual);
  }
}