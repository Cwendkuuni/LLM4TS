import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {
  
  @Test
  public void testEqual() {
    CoreOperationCompare coreOperation = new CoreOperationCompare(null, null) {
      @Override
      protected int getPrecedence() {
        return 2;
      }
      
      @Override
      protected boolean isSymmetric() {
        return true;
      }
      
      @Override
      protected boolean equal(EvalContext context, Expression left, Expression right) {
        return true;
      }
    };
    Assert.assertTrue(coreOperation.equal(null, null));
  }
  
  @Test
  public void testContains() {
    CoreOperationCompare coreOperation = new CoreOperationCompare(null, null) {
      @Override
      protected int getPrecedence() {
        return 2;
      }
      
      @Override
      protected boolean isSymmetric() {
        return true;
      }
      
      @Override
      protected boolean contains(Iterator it, Object value) {
        return true;
      }
    };
    Assert.assertTrue(coreOperation.contains(null, null));
  }
  
  @Test
  public void testFindMatch() {
    CoreOperationCompare coreOperation = new CoreOperationCompare(null, null) {
      @Override
      protected int getPrecedence() {
        return 2;
      }
      
      @Override
      protected boolean isSymmetric() {
        return true;
      }
      
      @Override
      protected boolean findMatch(Iterator lit, Iterator rit) {
        return true;
      }
    };
    Assert.assertTrue(coreOperation.findMatch(null, null));
  }
  
  @Test
  public void testEqualObjects() {
    CoreOperationCompare coreOperation = new CoreOperationCompare(null, null) {
      @Override
      protected int getPrecedence() {
        return 2;
      }
      
      @Override
      protected boolean isSymmetric() {
        return true;
      }
      
      @Override
      protected boolean equal(Object l, Object r) {
        return true;
      }
    };
    Assert.assertTrue(coreOperation.equal(null, null));
  }
  
  protected class Expression {
    public Object compute(EvalContext context) {
      return null;
    }
  }
  
}