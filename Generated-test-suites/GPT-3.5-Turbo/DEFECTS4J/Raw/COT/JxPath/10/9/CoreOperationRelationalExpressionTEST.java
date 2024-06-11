import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return true;
      }
    };
    
    EvalContext context = new EvalContext();
    Object result = expression.computeValue(context);
  
    assertEquals(Boolean.TRUE, result);
  }

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    int precedence = expression.getPrecedence();
  
    assertEquals(3, precedence);
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    boolean symmetric = expression.isSymmetric();
  
    assertFalse(symmetric);
  }
  
  @Test
  public void testCompute() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) {
        return true;
      }
    };
    
    boolean result = expression.compute("test1", "test2");
  
    assertTrue(result);
  }

  @Test
  public void testReduce() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Object reducedObject = expression.reduce("test");
  
    assertEquals("test", reducedObject);
  }

  @Test
  public void testContainsMatch() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Iterator it = new Iterator() {
      private int index = 0;
      
      public boolean hasNext() {
        return index < 3;
      }
      
      public Object next() {
        index++;
        if (index == 1) {
          return "test1";
        } else if (index == 2) {
          return "test2";
        } else {
          return "test3";
        }
      }
      
      public void remove() {}
    };
    
    boolean containsMatch = expression.containsMatch(it, "test2");
  
    assertTrue(containsMatch);
  }

  @Test
  public void testFindMatch() {
    Expression[] args = new Expression[2];
    args[0] = new Expression();
    args[1] = new Expression();
    
    CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
    
    Iterator lit = new Iterator() {
      private int index = 0;
      
      public boolean hasNext() {
        return index < 3;
      }
      
      public Object next() {
        index++;
        if (index == 1) {
          return "test1";
        } else if (index == 2) {
          return "test2";
        } else {
          return "test3";
        }
      }
      
      public void remove() {}
    };
    
    Iterator rit = new Iterator() {
      private int index = 0;
      
      public boolean hasNext() {
        return index < 3;
      }
      
      public Object next() {
        index++;
        if (index == 1) {
          return "test3";
        } else if (index == 2) {
          return "test4";
        } else {
          return "test5";
        }
      }
      
      public void remove() {}
    };
    
    boolean findMatch = expression.findMatch(lit, rit);
  
    assertTrue(findMatch);
  }

}