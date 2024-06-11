import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

   CoreOperationCompare opCompare;

   @Before
   public void setUp() {
      opCompare = new CoreOperationCompare(null, null);
   }

   @Test
   public void testEqual_BothNull() {
      EvalContext context = new EvalContext();
      Expression left = null;
      Expression right = null;

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_LeftNull() {
      EvalContext context = new EvalContext();
      Expression left = null;
      Expression right = new Expression();

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_RightNull() {
      EvalContext context = new EvalContext();
      Expression left = new Expression();
      Expression right = null;

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_SameType() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(10);

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_DifferentType() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(10.0);

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_BooleanType() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(true);
      Expression right = new Expression(true);

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_BooleanType_NotEqual() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(true);
      Expression right = new Expression(false);

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_NumberType() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(10);

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_NumberType_NotEqual() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(20);

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_NumberType_Float() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(10.0);

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_StringType() {
      EvalContext context = new EvalContext();
      Expression left = new Expression("abc");
      Expression right = new Expression("abc");

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_StringType_NotEqual() {
      EvalContext context = new EvalContext();
      Expression left = new Expression("abc");
      Expression right = new Expression("def");

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_StringType_WithNull() {
      EvalContext context = new EvalContext();
      Expression left = new Expression("abc");
      Expression right = new Expression(null);

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_BothPointers() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(new Pointer());
      Expression right = new Expression(new Pointer());

      boolean result = opCompare.equal(context, left, right);

      assertTrue(result);
   }

   @Test
   public void testEqual_LeftPointer_RightNotPointer() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(new Pointer());
      Expression right = new Expression(10);

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testEqual_RightPointer_LeftNotPointer() {
      EvalContext context = new EvalContext();
      Expression left = new Expression(10);
      Expression right = new Expression(new Pointer());

      boolean result = opCompare.equal(context, left, right);

      assertFalse(result);
   }

   @Test
   public void testContains_EmptyIterator() {
      Iterator it = new Iterator();
      Object value = 10;

      boolean result = opCompare.contains(it, value);

      assertFalse(result);
   }

   @Test
   public void testContains_ElementNotPresent() {
      Collection collection = new Collection();
      collection.add(5);
      collection.add(8);
      collection.add(15);
      Iterator it = collection.iterator();
      Object value = 10;

      boolean result = opCompare.contains(it, value);

      assertFalse(result);
   }

   @Test
   public void testContains_ElementPresent() {
      Collection collection = new Collection();
      collection.add(5);
      collection.add(10);
      collection.add(15);
      Iterator it = collection.iterator();
      Object value = 10;

      boolean result = opCompare.contains(it, value);

      assertTrue(result);
   }

   @Test
   public void testFindMatch_EmptyIterators() {
      Iterator lit = new Iterator();
      Iterator rit = new Iterator();

      boolean result = opCompare.findMatch(lit, rit);

      assertFalse(result);
   }

   @Test
   public void testFindMatch_LeftEmptyIterator_RightHasElements() {
      Collection collection = new Collection();
      collection.add(5);
      collection.add(8);
      collection.add(15);
      Iterator lit = new Iterator();
      Iterator rit = collection.iterator();

      boolean result = opCompare.findMatch(lit, rit);

      assertFalse(result);
   }

   @Test
   public void testFindMatch_LeftHasElements_RightEmptyIterator() {
      Collection collection = new Collection();
      collection.add(5);
      collection.add(8);
      collection.add(15);
      Iterator lit = collection.iterator();
      Iterator rit = new Iterator();

      boolean result = opCompare.findMatch(lit, rit);

      assertFalse(result);
   }

   @Test
   public void testFindMatch_MatchFound() {
      Collection leftCollection = new Collection();
      leftCollection.add(5);
      leftCollection.add(10);
      leftCollection.add(15);
      Collection rightCollection = new Collection();
      rightCollection.add(20);
      rightCollection.add(10);
      rightCollection.add(25);
      Iterator lit = leftCollection.iterator();
      Iterator rit = rightCollection.iterator();

      boolean result = opCompare.findMatch(lit, rit);

      assertTrue(result);
   }

   @Test
   public void testFindMatch_NoMatchFound() {
      Collection leftCollection = new Collection();
      leftCollection.add(5);
      leftCollection.add(10);
      leftCollection.add(15);
      Collection rightCollection = new Collection();
      rightCollection.add(20);
      rightCollection.add(25);
      Iterator lit = leftCollection.iterator();
      Iterator rit = rightCollection.iterator();

      boolean result = opCompare.findMatch(lit, rit);

      assertFalse(result);
   }

   @Test
   public void testEqual_PointerComparision() {
      Pointer pointer1 = new Pointer();
      Pointer pointer2 = new Pointer();

      boolean result = opCompare.equal(pointer1, pointer2);

      assertFalse(result);
   }

   @Test
   public void testEqual_BothNumbers() {
      boolean result = opCompare.equal(10, 10);

      assertTrue(result);
   }

   @Test
   public void testEqual_BothNumbers_NotEqual() {
      boolean result = opCompare.equal(10, 20);

      assertFalse(result);
   }

   @Test
   public void testEqual_BothNumbers_Float() {
      boolean result = opCompare.equal(10, 10.0);

      assertTrue(result);
   }

   @Test
   public void testEqual_BothStrings() {
      boolean result = opCompare.equal("abc", "abc");

      assertTrue(result);
   }

   @Test
   public void testEqual_BothStrings_NotEqual() {
      boolean result = opCompare.equal("abc", "def");

      assertFalse(result);
   }

   @Test
   public void testEqual_StringAndPointer() {
      Pointer pointer = new Pointer();

      boolean result = opCompare.equal("abc", pointer);

      assertFalse(result);
   }

   @Test
   public void testEqual_WithNull() {
      boolean result = opCompare.equal(null, null);

      assertTrue(result);
   }

   @Test
   public void testEqual_LeftNull() {
      boolean result = opCompare.equal(null, "abc");

      assertFalse(result);
   }

   @Test
   public void testEqual_RightNull() {
      boolean result = opCompare.equal("abc", null);

      assertFalse(result);
   }

}