import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
  
  private CoreOperationCompare operation;
  
  @Before
  public void setup() {
    Expression arg1 = new ExpressionMock();
    Expression arg2 = new ExpressionMock();
    operation = new CoreOperationCompare(arg1, arg2){};
  }
  
  @Test
  public void testEqual() {
    EvalContext context = new EvalContext();
    Expression left = new ExpressionMock();
    Expression right = new ExpressionMock();
    boolean result = operation.equal(context, left, right);
    assertTrue(result);
  }
  
  @Test
  public void testContains() {
    Iterator it = new IteratorMock();
    Object value = new Object();
    boolean result = operation.contains(it, value);
    assertFalse(result);
  }
  
  @Test
  public void testFindMatch() {
    Iterator lit = new IteratorMock();
    Iterator rit = new IteratorMock();
    boolean result = operation.findMatch(lit, rit);
    assertFalse(result);
  }
  
  @Test
  public void testEqualObjectObject() {
    Object l = "test";
    Object r = "test";
    boolean result = operation.equal(l, r);
    assertTrue(result);
  }
  
  private class ExpressionMock extends Expression {
    public ExpressionMock() {
      super(null);
    }
    
    public Object compute(EvalContext context) {
      return null;
    }
  }
  
  private class IteratorMock implements Iterator<Object> {
    public boolean hasNext() {
      return false;
    }
    
    public Object next() {
      return null;
    }
    
    public void remove() {
      
    }
  }
}