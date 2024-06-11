
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {
   // Test cases for CoreOperationCompare

   @Test
   public void testEqualMethod() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationCompare coreOpCompare = new CoreOperationCompare(arg1, arg2);

      // Test with two equal values
      EvalContext context = new EvalContext();
      arg1.setValue(5);
      arg2.setValue(5);
      assertTrue(coreOpCompare.equal(context, arg1, arg2));

      // Test with two unequal values
      arg1.setValue(5);
      arg2.setValue(10);
      assertFalse(coreOpCompare.equal(context, arg1, arg2));
   }

   @Test
   public void testContainsMethod() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationCompare coreOpCompare = new CoreOperationCompare(arg1, arg2);

      // Test contains method with an iterator and a value
      Iterator it = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
      Object value = 2;
      assertTrue(coreOpCompare.contains(it, value));

      // Test contains method with an iterator and a value not present in the iterator
      it = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
      value = 4;
      assertFalse(coreOpCompare.contains(it, value));
   }

   @Test
   public void testFindMatchMethod() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationCompare coreOpCompare = new CoreOperationCompare(arg1, arg2);

      // Test findMatch method with two iterators containing matching values
      Iterator lit = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
      Iterator rit = new ArrayList<Integer>(Arrays.asList(3, 4, 5)).iterator();
      assertTrue(coreOpCompare.findMatch(lit, rit));

      // Test findMatch method with two iterators not containing matching values
      lit = new ArrayList<Integer>(Arrays.asList(1, 2, 3)).iterator();
      rit = new ArrayList<Integer>(Arrays.asList(4, 5, 6)).iterator();
      assertFalse(coreOpCompare.findMatch(lit, rit));
   }

   @Test
   public void testEqualMethodWithPointers() {
      Expression arg1 = new Expression();
      Expression arg2 = new Expression();
      CoreOperationCompare coreOpCompare = new CoreOperationCompare(arg1, arg2);

      // Test equal method with two pointers pointing to equal values
      Object l = new Pointer("test", "value");
      Object r = new Pointer("test", "value");
      assertTrue(coreOpCompare.equal(l, r));

      // Test equal method with two pointers pointing to unequal values
      r = new Pointer("test", "value2");
      assertFalse(coreOpCompare.equal(l, r));
   }
}

