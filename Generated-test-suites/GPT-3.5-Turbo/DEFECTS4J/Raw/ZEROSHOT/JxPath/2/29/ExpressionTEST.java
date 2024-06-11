import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }

      @Override
      public Object computeValue(EvalContext context) {
        return null;
      }

      @Override
      public Object compute(EvalContext context) {
        return null;
      }
    };

    boolean result = expression.isContextDependent();

    assertFalse(result);
  }

  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }

      @Override
      public Object computeValue(EvalContext context) {
        return "value";
      }

      @Override
      public Object compute(EvalContext context) {
        return null;
      }
    };

    Object result = expression.computeValue(null);

    assertEquals("value", result);
  }

  @Test
  public void testCompute() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }

      @Override
      public Object computeValue(EvalContext context) {
        return null;
      }

      @Override
      public Object compute(EvalContext context) {
        return "result";
      }
    };

    Object result = expression.compute(null);

    assertEquals("result", result);
  }
  
  @Test
  public void testIterate() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }

      @Override
      public Object computeValue(EvalContext context) {
        return null;
      }

      @Override
      public Object compute(EvalContext context) {
        return new Object[] {1, 2, 3};
      }
    };

    Iterator result = expression.iterate(null);

    assertTrue(result.hasNext());
    assertEquals(1, result.next());
    assertTrue(result.hasNext());
    assertEquals(2, result.next());
    assertTrue(result.hasNext());
    assertEquals(3, result.next());
    assertFalse(result.hasNext());
  }
  
  @Test
  public void testIteratePointers() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return false;
      }

      @Override
      public Object computeValue(EvalContext context) {
        return null;
      }

      @Override
      public Object compute(EvalContext context) {
        return new Object[] {1, 2, 3};
      }
    };

    Iterator result = expression.iteratePointers(null);

    assertTrue(result.hasNext());
    assertEquals(1, result.next());
    assertTrue(result.hasNext());
    assertEquals(2, result.next());
    assertTrue(result.hasNext());
    assertEquals(3, result.next());
    assertFalse(result.hasNext());
  }

  @Test
  public void testPointerIterator() {
    Expression.PointerIterator pointerIterator = new Expression.PointerIterator(null, null, null);

    assertFalse(pointerIterator.hasNext());
  }

  @Test
  public void testValueIterator() {
    Expression.ValueIterator valueIterator = new Expression.ValueIterator(null);

    assertFalse(valueIterator.hasNext());
  }
}