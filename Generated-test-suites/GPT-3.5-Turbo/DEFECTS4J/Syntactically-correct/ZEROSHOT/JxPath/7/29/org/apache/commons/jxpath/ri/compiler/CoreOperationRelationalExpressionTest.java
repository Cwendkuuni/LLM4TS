import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationRelationalExpressionTest {

  private CoreOperationRelationalExpression coreOpRelExp;

  @Before
  public void setUp() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    coreOpRelExp = new CoreOperationRelationalExpressionStub(args);
  }

  @Test
  public void testGetPrecedence() {
    int result = coreOpRelExp.getPrecedence();
    assertEquals(3, result);
  }

  @Test
  public void testIsSymmetric() {
    boolean result = coreOpRelExp.isSymmetric();
    assertEquals(false, result);
  }

  // Stub for CoreOperationRelationalExpression class
  private class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionStub(Expression[] args) {
      super(args);
    }
  }

}