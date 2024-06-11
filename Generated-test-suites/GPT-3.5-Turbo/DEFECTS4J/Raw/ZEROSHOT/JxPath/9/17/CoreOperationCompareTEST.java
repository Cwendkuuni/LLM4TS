import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
   
   @Test
   public void testEqual() {
      CoreOperationCompare core = new CoreOperationCompare(new Expression(), new Expression());
      EvalContext context = new EvalContext();
      Expression left = new Expression();
      Expression right = new Expression();
      boolean result = core.equal(context, left, right);
      
      //assertions
   }
   
   @Test
   public void testContains() {
      CoreOperationCompare core = new CoreOperationCompare(new Expression(), new Expression());
      Iterator it = new Iterator();
      Object value = new Object();
      boolean result = core.contains(it, value);
      
      //assertions
   }
   
   @Test
   public void testFindMatch() {
      CoreOperationCompare core = new CoreOperationCompare(new Expression(), new Expression());
      Iterator lit = new Iterator();
      Iterator rit = new Iterator();
      boolean result = core.findMatch(lit, rit);
      
      //assertions
   }
   
   @Test
   public void testEqualObjectObject() {
      CoreOperationCompare core = new CoreOperationCompare(new Expression(), new Expression());
      Object l = new Object();
      Object r = new Object();
      boolean result = core.equal(l, r);
      
      //assertions
   }
   
   @Test
   public void testEqualPointerPointer() {
      CoreOperationCompare core = new CoreOperationCompare(new Expression(), new Expression());
      Pointer l = new Pointer();
      Pointer r = new Pointer();
      boolean result = core.equal(l, r);
      
      //assertions
   }
}