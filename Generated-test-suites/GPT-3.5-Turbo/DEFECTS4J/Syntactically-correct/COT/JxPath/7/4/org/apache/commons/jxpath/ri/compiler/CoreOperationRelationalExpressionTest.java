import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

   @Test
   public void testConstructorWithArgs() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
   }
   
   @Test
   public void testGetPrecedence() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      int precedence = coreOperationRelationalExpression.getPrecedence();
   }
   
   @Test
   public void testIsSymmetric() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      boolean isSymmetric = coreOperationRelationalExpression.isSymmetric();
   }
}