import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
   @Test
   public void testComputeValue() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      EvalContext context = new EvalContext();
      Object result = coreOperationRelationalExpression.computeValue(context);
      assertNotNull(result);
   }

   @Test
   public void testGetPrecedence() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      int result = coreOperationRelationalExpression.getPrecedence();
      assertTrue(result >= 0);
   }

   @Test
   public void testIsSymmetric() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      boolean result = coreOperationRelationalExpression.isSymmetric();
      assertFalse(result);
   }

   @Test
   public void testEvaluateCompare() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      int compare = 0;
      boolean result = coreOperationRelationalExpression.evaluateCompare(compare);
      assertNotNull(result);
   }

   @Test
   public void testCompute() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      Object left = new Object();
      Object right = new Object();
      boolean result = coreOperationRelationalExpression.compute(left, right);
      assertNotNull(result);
   }

   @Test
   public void testReduce() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      Object o = new Object();
      Object result = coreOperationRelationalExpression.reduce(o);
      assertNotNull(result);
   }

   @Test
   public void testContainsMatch() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      Iterator it = new Iterator();
      Object value = new Object();
      boolean result = coreOperationRelationalExpression.containsMatch(it, value);
      assertNotNull(result);
    }

   @Test
   public void testFindMatch() {
      Expression[] args = new Expression[2];
      CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
      Iterator lit = new Iterator();
      Iterator rit = new Iterator();
      boolean result = coreOperationRelationalExpression.findMatch(lit, rit);
      assertNotNull(result);
    }
}