import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
  
    // Test case 1
    EvalContext context1 = new EvalContext();
    Object left1 = null;
    Object right1 = null;
    boolean expected1 = true;
    boolean result1 = operation.equal(context1, left1, right1);
    assertEquals(expected1, result1);
  
    // Test case 2
    EvalContext context2 = new EvalContext();
    Object left2 = "hello";
    Object right2 = "hello";
    boolean expected2 = true;
    boolean result2 = operation.equal(context2, left2, right2);
    assertEquals(expected2, result2);
  
    // Test case 3
    EvalContext context3 = new EvalContext();
    Object left3 = "hello";
    Object right3 = "world";
    boolean expected3 = false;
    boolean result3 = operation.equal(context3, left3, right3);
    assertEquals(expected3, result3);
  }

  @Test
  public void testContains() {
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
  
    // Test case 1
    Iterator it1 = null;
    Object value1 = null;
    boolean expected1 = false;
    boolean result1 = operation.contains(it1, value1);
    assertEquals(expected1, result1);
  
    // Test case 2
    Iterator it2 = new Iterator(){
      public boolean hasNext(){
        return true;
      }
      public Object next(){
        return null;
      }
    };
    Object value2 = "hello";
    boolean expected2 = false;
    boolean result2 = operation.contains(it2, value2);
    assertEquals(expected2, result2);
  
    // Test case 3
    Iterator it3 = new Iterator(){
      public boolean hasNext(){
        return true;
      }
      public Object next(){
        return "hello";
      }
    };
    Object value3 = "hello";
    boolean expected3 = true;
    boolean result3 = operation.contains(it3, value3);
    assertEquals(expected3, result3);
  }

  @Test
  public void testFindMatch() {
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
  
    // Test case 1
    Iterator lit1 = null;
    Iterator rit1 = null;
    boolean expected1 = false;
    boolean result1 = operation.findMatch(lit1, rit1);
    assertEquals(expected1, result1);
  
    // Test case 2
    Iterator lit2 = new Iterator(){
      public boolean hasNext(){
        return true;
      }
      public Object next(){
        return null;
      }
    };
    Iterator rit2 = new Iterator(){
      public boolean hasNext(){
        return true;
      }
      public Object next(){
        return null;
      }
    };
    boolean expected2 = false;
    boolean result2 = operation.findMatch(lit2, rit2);
    assertEquals(expected2, result2);
  
    // Test case 3
    Iterator lit3 = new Iterator(){
      public boolean hasNext(){
        return true;
      }
      public Object next(){
        return "hello";
      }
    };
    Iterator rit3 = new Iterator(){
      public boolean hasNext(){
        return false;
      }
      public Object next(){
        return null;
      }
    };
    boolean expected3 = false;
    boolean result3 = operation.findMatch(lit3, rit3);
    assertEquals(expected3, result3);
  }

  @Test
  public void testEqual() {
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
  
    // Test case 1
    Object l1 = null;
    Object r1 = null;
    boolean expected1 = true;
    boolean result1 = operation.equal(l1, r1);
    assertEquals(expected1, result1);
  
    // Test case 2
    Object l2 = "hello";
    Object r2 = "hello";
    boolean expected2 = true;
    boolean result2 = operation.equal(l2, r2);
    assertEquals(expected2, result2);
  
    // Test case 3
    Object l3 = "hello";
    Object r3 = "world";
    boolean expected3 = false;
    boolean result3 = operation.equal(l3, r3);
    assertEquals(expected3, result3);
  }

}