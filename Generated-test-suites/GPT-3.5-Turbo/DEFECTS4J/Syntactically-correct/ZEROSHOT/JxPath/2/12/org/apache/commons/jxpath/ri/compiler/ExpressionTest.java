import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
   private Expression expression;

   @Before
   public void setUp() {
      expression = new Expression() {
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
   }

   @Test
   public void testIsContextDependent() {
      assertTrue(expression.isContextDependent());
   }

   @Test
   public void testComputeContextDependent() {
      assertTrue(expression.computeContextDependent());
   }

   @Test
   public void testComputeValue() {
      EvalContext context = new EvalContext();
      assertNull(expression.computeValue(context));
   }

   @Test
   public void testCompute() {
      EvalContext context = new EvalContext();
      assertNull(expression.compute(context));
   }

   @Test
   public void testIterate() {
      EvalContext context = new EvalContext();
      Iterator iterator = expression.iterate(context);
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testIteratePointers() {
      EvalContext context = new EvalContext();
      Iterator iterator = expression.iteratePointers(context);
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testPointerIterator() {
      Iterator it = Collections.EMPTY_LIST.iterator();
      QName qname = new QName(null, "value");
      Locale locale = Locale.getDefault();
      Expression.PointerIterator pointerIterator = new Expression.PointerIterator(it, qname, locale);
      assertFalse(pointerIterator.hasNext());
      assertNull(pointerIterator.next());
   }

   @Test
   public void testValueIterator() {
      Iterator it = Collections.EMPTY_LIST.iterator();
      Expression.ValueIterator valueIterator = new Expression.ValueIterator(it);
      assertFalse(valueIterator.hasNext());
      assertNull(valueIterator.next());
   }
}