import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue_Equals() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
      protected boolean evaluateCompare(int compare) {
        return (compare == 0);
      }
    };

    EvalContext context = new EvalContext();

    Object result = expression.computeValue(context);

    assertFalse((Boolean) result);
  }

  @Test
  public void testComputeValue_LessThan() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
      protected boolean evaluateCompare(int compare) {
        return (compare < 0);
      }
    };

    EvalContext context = new EvalContext();

    Object result = expression.computeValue(context);

    assertFalse((Boolean) result);
  }

  @Test
  public void testComputeValue_GreaterThan() {
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
      protected boolean evaluateCompare(int compare) {
        return (compare > 0);
      }
    };

    EvalContext context = new EvalContext();

    Object result = expression.computeValue(context);

    assertFalse((Boolean) result);
  }
}