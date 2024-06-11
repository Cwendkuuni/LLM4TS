import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void testIsContextDependent() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return true;
      }
      public Object computeValue(EvalContext context) {
        return null;
      }
      public Object compute(EvalContext context) {
        return null;
      }
    };

    boolean isContextDependent = expression.isContextDependent();

    assertTrue(isContextDependent);
  }

  @Test
  public void testComputeValue() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }
      public Object computeValue(EvalContext context) {
        return "value";
      }
      public Object compute(EvalContext context) {
        return null;
      }
    };

    String value = (String) expression.computeValue(null);

    assertEquals("value", value);
  }

  @Test
  public void testCompute() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }
      public Object computeValue(EvalContext context) {
        return null;
      }
      public Object compute(EvalContext context) {
        return "result";
      }
    };

    String result = (String) expression.compute(null);

    assertEquals("result", result);
  }

  @Test
  public void testIterate() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }
      public Object computeValue(EvalContext context) {
        return null;
      }
      public Object compute(EvalContext context) {
        return new Object[] {"value1", "value2"};
      }
    };

    Iterator iterator = expression.iterate(null);

    assertTrue(iterator.hasNext());
    assertEquals("value1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value2", iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testIteratePointers() {
    Expression expression = new Expression() {
      public boolean computeContextDependent() {
        return false;
      }
      public Object computeValue(EvalContext context) {
        return null;
      }
      public Object compute(EvalContext context) {
        return new Object[] {"value1", "value2"};
      }
    };

    Iterator iterator = expression.iteratePointers(null);

    assertTrue(iterator.hasNext());
    assertEquals("value1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value2", iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testPointerIterator() {
    Expression.PointerIterator iterator = new Expression.PointerIterator(null, null, null);

    assertFalse(iterator.hasNext());
  }

  @Test
  public void testValueIterator() {
    Expression.ValueIterator iterator = new Expression.ValueIterator(null);

    assertFalse(iterator.hasNext());
  }
}