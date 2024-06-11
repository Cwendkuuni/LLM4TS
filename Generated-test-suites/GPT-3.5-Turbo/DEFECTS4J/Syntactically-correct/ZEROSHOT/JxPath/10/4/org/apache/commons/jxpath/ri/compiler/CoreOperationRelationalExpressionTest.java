import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
  @SuppressWarnings("rawtypes")
  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    EvalContext context = new EvalContext(null);
    context.setSingleNodePointer(null);
    
    Object result = coreOperationRelationalExpression.computeValue(context);
    Assert.assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    int result = coreOperationRelationalExpression.getPrecedence();
    Assert.assertEquals(3, result);
  }
  
  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    boolean result = coreOperationRelationalExpression.isSymmetric();
    Assert.assertEquals(false, result);
  }
  
  @Test
  public void testCompute() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    Object result = coreOperationRelationalExpression.compute(null, null);
    Assert.assertEquals(false, result);
  }
  
  @Test
  public void testReduce() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    Object result = coreOperationRelationalExpression.reduce(null);
    Assert.assertEquals(null, result);
  }
  
  @Test
  public void testContainsMatch() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    Object result = coreOperationRelationalExpression.containsMatch(null, null);
    Assert.assertEquals(false, result);
  }
  
  @Test
  public void testFindMatch() {
    Expression[] args = new Expression[2];
    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
      
      private static final long serialVersionUID = 1L;

      @Override
      protected boolean evaluateCompare(int compare) {
        return false;
      }
    };
    
    Object result = coreOperationRelationalExpression.findMatch(null, null);
    Assert.assertEquals(false, result);
  }
  
}