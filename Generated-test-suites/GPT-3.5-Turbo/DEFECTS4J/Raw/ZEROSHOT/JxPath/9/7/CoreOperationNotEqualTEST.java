import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {
  
  @Test
  public void testComputeValue_equalArgs() {
    CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
    EvalContext context = new EvalContext();
    Object result = operation.computeValue(context);
    assert(result.equals(Boolean.FALSE));
  }
  
  @Test
  public void testComputeValue_notEqualArgs() {
    CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
    EvalContext context = new EvalContext();
    context.setNodeSet(new NodeSet());
    Object result = operation.computeValue(context);
    assert(result.equals(Boolean.TRUE));
  }
  
  @Test
  public void testGetSymbol() {
    CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
    String symbol = operation.getSymbol();
    assert(symbol.equals("!="));
  }
}