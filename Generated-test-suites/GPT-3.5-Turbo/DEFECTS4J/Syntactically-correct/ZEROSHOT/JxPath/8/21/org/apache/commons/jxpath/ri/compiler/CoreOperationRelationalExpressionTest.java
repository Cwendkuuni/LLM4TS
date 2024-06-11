import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    EvalContext context = new EvalContext();
    Object result = expression.computeValue(context);
    assertEquals(Boolean.TRUE, result);
  }

  private class MockExpression extends Expression {
    public Object computeValue(EvalContext context) {
      return new Boolean(false);
    }

    public int hashCode() {
      return 0;
    }

    public boolean equals(Object obj) {
      return false;
    }
  }

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    int result = expression.getPrecedence();
    assertEquals(3, result);
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    boolean result = expression.isSymmetric();
    assertEquals(false, result);
  }

  @Test
  public void testReduce() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    Object result = expression.reduce(new Object());
    assertNotEquals(null, result);
  }

  @Test
  public void testContainsMatch() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    Iterator it = new Iterator() {
      public boolean hasNext() {
        return false;
      }

      public Object next() {
        return null;
      }

      public void remove() {
      }
    };
    boolean result = expression.containsMatch(it, new Object());
    assertEquals(false, result);
  }

  @Test
  public void testFindMatch() {
    Expression[] args = new Expression[2];
    args[0] = new MockExpression(true);
    args[1] = new MockExpression(false);
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return compare > 0;
      }
    };
    Iterator lit = new Iterator() {
      public boolean hasNext() {
        return false;
      }

      public Object next() {
        return null;
      }

      public void remove() {
      }
    };
    Iterator rit = new Iterator() {
      public boolean hasNext() {
        return false;
      }

      public Object next() {
        return null;
      }

      public void remove() {
      }
    };
    boolean result = expression.findMatch(lit, rit);
    assertEquals(false, result);
  }
}