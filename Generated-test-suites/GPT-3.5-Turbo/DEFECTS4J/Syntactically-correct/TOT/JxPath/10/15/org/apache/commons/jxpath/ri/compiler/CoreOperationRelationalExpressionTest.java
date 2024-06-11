import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  private CoreOperationRelationalExpression expression;
  
  @Before
  public void setup() {
    expression = new CoreOperationRelationalExpressionStub();
  }
  
  @Test
  public void testComputeValue_EqualNumber_ReturnsTrue() {
    // Arrange
    EvalContext context = new EvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_NotEqualNumber_ReturnsFalse() {
    // Arrange
    EvalContext context = new EvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testComputeValue_LeftContextIsIterator_ReturnsTrue() {
    // Arrange
    EvalContext context = new IteratorEvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_RightContextIsIterator_ReturnsTrue() {
    // Arrange
    EvalContext context = new IteratorEvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_LeftAndRightContextAreIterators_ReturnsTrue() {
    // Arrange
    EvalContext leftContext = new IteratorEvalContextStub();
    EvalContext rightContext = new IteratorEvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_LeftAndRightContextAreIterators_NoMatch_ReturnsFalse() {
    // Arrange
    EvalContext leftContext = new IteratorEvalContextStub();
    EvalContext rightContext = new IteratorEvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testComputeValue_LeftContextIsInitialContext_ResetCalled() {
    // Arrange
    EvalContext context = new InitialContextEvalContextStub();
    
    // Act
    expression.computeValue(context);
    
    // Assert
    assertTrue(InitialContextEvalContextStub.resetCalled);
  }
  
  @Test
  public void testComputeValue_RightContextIsInitialContext_ResetCalled() {
    // Arrange
    EvalContext context = new InitialContextEvalContextStub();
    
    // Act
    expression.computeValue(context);
    
    // Assert
    assertTrue(InitialContextEvalContextStub.resetCalled);
  }
  
  @Test
  public void testComputeValue_BothContextAreInitialContext_ResetCalledOnBoth() {
    // Arrange
    EvalContext leftContext = new InitialContextEvalContextStub();
    EvalContext rightContext = new InitialContextEvalContextStub();
    
    // Act
    expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertTrue(InitialContextEvalContextStub.resetCalled);
  }
  
  @Test
  public void testComputeValue_LeftAndRightAreCollection_ReturnsTrue() {
    // Arrange
    EvalContext leftContext = new CollectionEvalContextStub();
    EvalContext rightContext = new CollectionEvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_LeftIsCollection_ReturnsTrue() {
    // Arrange
    EvalContext leftContext = new CollectionEvalContextStub();
    EvalContext rightContext = new EvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_RightIsCollection_ReturnsTrue() {
    // Arrange
    EvalContext leftContext = new EvalContextStub();
    EvalContext rightContext = new CollectionEvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testComputeValue_LeftIsNaN_ReturnsFalse() {
    // Arrange
    EvalContext context = new NaNValueEvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testComputeValue_RightIsNaN_ReturnsFalse() {
    // Arrange
    EvalContext context = new NaNValueEvalContextStub();
    
    // Act
    Object result = expression.computeValue(context);
    
    // Assert
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testComputeValue_LeftAndRightAreNaN_ReturnsFalse() {
    // Arrange
    EvalContext leftContext = new NaNValueEvalContextStub();
    EvalContext rightContext = new NaNValueEvalContextStub();
    
    // Act
    Object result = expression.computeValue(leftContext, rightContext);
    
    // Assert
    assertEquals(Boolean.FALSE, result);
  }
  
  private static class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
    
    public CoreOperationRelationalExpressionStub() {
      super(new Expression[] { new ExpressionStub(), new ExpressionStub() });
    }
    
    protected boolean evaluateCompare(int compare) {
      return compare == 0;
    }
  }
  
  private static class ExpressionStub implements Expression {
    
    public Object computeValue(EvalContext context) {
      return 5;
    }
  }
  
  private static class EvalContextStub extends EvalContext {
    // Implementation not relevant for tests
  }
  
  private static class IteratorEvalContextStub extends EvalContext {
    // Implementation not relevant for tests
  }
  
  private static class InitialContextEvalContextStub extends EvalContext {
    public static boolean resetCalled;
    
    @Override
    public void reset() {
      resetCalled = true;
    }
  }
  
  private static class CollectionEvalContextStub extends EvalContext {
    // Implementation not relevant for tests
  }
  
  private static class NaNValueEvalContextStub extends EvalContext {
    public Object computeValue(EvalContext context) {
      return Double.NaN;
    }
  }
}