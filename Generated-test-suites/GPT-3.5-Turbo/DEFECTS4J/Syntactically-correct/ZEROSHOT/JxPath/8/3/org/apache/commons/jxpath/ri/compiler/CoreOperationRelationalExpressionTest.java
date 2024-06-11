import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
  
  @Test
  public void testComputeValue() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    EvalContext context = new EvalContext();
    context.setSingleNodePointer(new SelfContext(context));
    
    Object result = testObject.computeValue(context);
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testGetPrecedence() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    int result = test.getPrecedence();
    assertEquals(3, result);
  }
  
  @Test
  public void testIsSymmetric() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    boolean result = test.isSymmetric();
    assertFalse(result);
  }
  
  @Test
  public void testCompute() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    Object result = testObject.compute(null, null);
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test
  public void testReduce() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    Object result = testObject.reduce(null);
    assertNull(result);
  }
  
  @Test
  public void testContainsMatch() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    Iterator it = null;
    Object value = null;
    boolean result = testObject.containsMatch(it, value);
    assertFalse(result);
  }
  
  @Test
  public void testFindMatch() {
    Expression[] args = {new Expression(), new Expression()};
    CoreOperationRelationalExpression testObject = new CoreOperationRelationalExpression(args) {
      protected boolean evaluateCompare(int compare) { return compare > 0; }      
    };
    
    Iterator lit = null;
    Iterator rit = null;
    boolean result = testObject.findMatch(lit, rit);
    assertFalse(result);
  }
}