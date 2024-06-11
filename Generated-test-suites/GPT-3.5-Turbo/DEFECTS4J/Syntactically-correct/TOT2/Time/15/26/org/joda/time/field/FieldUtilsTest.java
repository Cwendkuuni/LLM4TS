import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {
   
   @Test
   public void testSafeNegate() {
       assertEquals(-5, FieldUtils.safeNegate(5));
       assertEquals(0, FieldUtils.safeNegate(0));
       try {
           FieldUtils.safeNegate(Integer.MIN_VALUE);
           fail("Expected ArithmeticException");
       } catch (ArithmeticException e) {
           // Expected
       }
   }

   @Test
   public void testSafeAdd() {
       assertEquals(5, FieldUtils.safeAdd(2, 3));
       assertEquals(-5, FieldUtils.safeAdd(-2, -3));
       try {
           FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
           fail("Expected ArithmeticException");
       } catch (ArithmeticException e) {
           // Expected
       }
   }

   @Test
   public void testSafeMultiply() {
       assertEquals(15, FieldUtils.safeMultiply(3, 5));
       assertEquals(-15, FieldUtils.safeMultiply(-3, 5));
       try {
           FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
           fail("Expected ArithmeticException");
       } catch (ArithmeticException e) {
           // Expected
       }
   }

   @Test
   public void testSafeSubtract() {
       assertEquals(4, FieldUtils.safeSubtract(7, 3));
       assertEquals(-4, FieldUtils.safeSubtract(3, 7));
       try {
           FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
           fail("Expected ArithmeticException");
       } catch (ArithmeticException e) {
           // Expected
       }
   }

   @Test
   public void testSafeToInt() {
       assertEquals(5, FieldUtils.safeToInt(5));
       assertEquals(0, FieldUtils.safeToInt(0));
       try {
           FieldUtils.safeToInt(Long.MAX_VALUE);
           fail("Expected ArithmeticException");
       } catch (ArithmeticException e) {
           // Expected
       }
   }

   @Test
   public void testGetWrappedValue() {
       assertEquals(2, FieldUtils.getWrappedValue(7, 5, 1, 10));
       assertEquals(5, FieldUtils.getWrappedValue(12, 5, 1, 10));
   }

   @Test
   public void testEquals() {
       assertTrue(FieldUtils.equals("test", "test"));
       assertFalse(FieldUtils.equals("test", "Test"));
       assertFalse(FieldUtils.equals(null, "test"));
       assertFalse(FieldUtils.equals("test", null));
       assertTrue(FieldUtils.equals(null, null));
   }

}