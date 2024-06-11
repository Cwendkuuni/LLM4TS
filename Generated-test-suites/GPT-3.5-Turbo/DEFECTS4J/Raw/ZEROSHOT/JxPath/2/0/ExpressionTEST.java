import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
      @Override
      public boolean computeContextDependent() {
        return true;
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

    assertTrue(expression.isContextDependent());
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

    assertEquals("value", expression.computeValue(null));
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

    assertEquals("result", expression.compute(null));
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
        return new Integer[] {1, 2, 3};
      }
    };

    assertNotNull(expression.iterate(null));
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
        return new Integer[] {1, 2, 3};
      }
    };

    assertNotNull(expression.iteratePointers(null));
  }

  @Test
  public void testPointerIterator() {
    Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
    assertFalse(iterator.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPointerIteratorRemove() {
    Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);
    iterator.remove();
  }

  @Test
  public void testValueIterator() {
    Expression.ValueIterator iterator = new Expression.ValueIterator(null);
    assertFalse(iterator.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testValueIteratorRemove() {
    Expression.ValueIterator iterator = new Expression.ValueIterator(null);
    iterator.remove();
  }
}