import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
  
  @Test
  public void testComputeValue() {
    CoreOperationGreaterThan greaterthan = new CoreOperationGreaterThan(new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 10;
      }
    }, new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 5;
      }
    });
    assertEquals(Boolean.TRUE, greaterthan.computeValue(null));
    
    CoreOperationGreaterThan greaterthan2 = new CoreOperationGreaterThan(new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 5;
      }
    }, new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 10;
      }
    });
    assertEquals(Boolean.FALSE, greaterthan2.computeValue(null));
    
    CoreOperationGreaterThan greaterthan3 = new CoreOperationGreaterThan(new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 10;
      }
    }, new Expression() {
      @Override
      public Object computeValue(EvalContext context) {
        return 10;
      }
    });
    assertEquals(Boolean.FALSE, greaterthan3.computeValue(null));
  }
}