import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    Expression arg1 = new MockExpression();
    Expression arg2 = new MockExpression();
    args[0] = arg1;
    args[1] = arg2;
    CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);

    // Test case 1: arg1.computeValue() < arg2.computeValue()
    arg1.setComputeValueResult(2);
    arg2.setComputeValueResult(5);
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));

    // Test case 2: arg1.computeValue() > arg2.computeValue()
    arg1.setComputeValueResult(7);
    arg2.setComputeValueResult(3);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Test case 3: arg1.computeValue() == arg2.computeValue()
    arg1.setComputeValueResult(4);
    arg2.setComputeValueResult(4);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Edge case: arg1 and arg2 are instances of InitialContext
    arg1 = new InitialContextMockExpression();
    arg2 = new InitialContextMockExpression();
    args[0] = arg1;
    args[1] = arg2;
    expression = new MockCoreOperationRelationalExpression(args);

    // Test case 4: Iterator left == Iterator right (empty iterators)
    arg1.setComputeValueResult(new InitialContextMockIterator());
    arg2.setComputeValueResult(new InitialContextMockIterator());
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Test case 5: Iterator left != Iterator right
    arg1.setComputeValueResult(new InitialContextMockIterator());
    InitialContextMockIterator iteratorRight = new InitialContextMockIterator();
    iteratorRight.add("value");
    arg2.setComputeValueResult(iteratorRight);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Edge case: arg1 and arg2 are instances of Iterator
    IteratorMockExpression iteratorMockExpression = new IteratorMockExpression();
    arg1 = iteratorMockExpression;
    arg2 = iteratorMockExpression;
    args[0] = arg1;
    args[1] = arg2;
    expression = new MockCoreOperationRelationalExpression(args);

    // Test case 6: Iterator left == Iterator right (empty iterators)
    arg1.setComputeValueResult(new IteratorMockIterator());
    arg2.setComputeValueResult(new IteratorMockIterator());
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Test case 7: Iterator left != Iterator right
    arg1.setComputeValueResult(new IteratorMockIterator());
    IteratorMockIterator iteratorRight2 = new IteratorMockIterator();
    iteratorRight2.add("value1");
    iteratorRight2.add("value2");
    arg2.setComputeValueResult(iteratorRight2);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Test case 8: Iterator left contains match with Object right
    IteratorMockIterator iteratorLeft = new IteratorMockIterator();
    iteratorLeft.add("value1");
    iteratorLeft.add("value2");
    iteratorLeft.add("value4");
    arg1.setComputeValueResult(iteratorLeft);
    arg2.setComputeValueResult("value2");
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));

    // Test case 9: Iterator right contains match with Object left
    IteratorMockIterator iteratorRight3 = new IteratorMockIterator();
    iteratorRight3.add("value3");
    iteratorRight3.add("value2");
    iteratorRight3.add("value5");
    arg1.setComputeValueResult("value2");
    arg2.setComputeValueResult(iteratorRight3);
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));

    // Error scenario: arg1 and arg2 are instances of Double
    arg1 = new DoubleMockExpression();
    arg2 = new DoubleMockExpression();
    args[0] = arg1;
    args[1] = arg2;
    expression = new MockCoreOperationRelationalExpression(args);

    // Test case 10: Double left == Double right
    arg1.setComputeValueResult(3.14);
    arg2.setComputeValueResult(3.14);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));

    // Test case 11: Double left < Double right
    arg1.setComputeValueResult(2.5);
    arg2.setComputeValueResult(3.5);
    Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));

    // Test case 12: Double left > Double right
    arg1.setComputeValueResult(5.5);
    arg2.setComputeValueResult(4.5);
    Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));
  }

  private static class MockExpression extends Expression {
    private Object computeValueResult;

    public void setComputeValueResult(Object value) {
      computeValueResult = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
      return computeValueResult;
    }
  }

  private static class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

    public MockCoreOperationRelationalExpression(Expression[] args) {
      super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
      return false;
    }
  }

  private static class InitialContextMockExpression extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
      return new InitialContextMockIterator();
    }
  }

  private static class InitialContextMockIterator implements Iterator<Object> {
    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      return null;
    }
  }

  private static class IteratorMockExpression extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
      return new IteratorMockIterator();
    }
  }

  private static class IteratorMockIterator implements Iterator<Object> {
    private final Collection<Object> elements = new HashSet<>();
    
    public void add(Object element) {
      elements.add(element);
    }

    @Override
    public boolean hasNext() {
      return !elements.isEmpty();
    }

    @Override
    public Object next() {
      return elements.iterator().next();
    }
  }

  private static class DoubleMockExpression extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
      return 0;
    }
  }
}