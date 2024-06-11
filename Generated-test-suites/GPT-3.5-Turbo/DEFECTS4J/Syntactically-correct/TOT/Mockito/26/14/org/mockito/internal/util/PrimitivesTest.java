import org.junit.Assert;
import org.junit.Test;

public class PrimitivesTest {

   @Test
   public void testPrimitiveTypeOf() {
      Class<Integer> clazz = Integer.class;
      Class<Integer> expected = Integer.TYPE;
      Class<Integer> result = Primitives.primitiveTypeOf(clazz);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveTypeOfWithPrimitive() {
      Class<Integer> clazz = int.class;
      Class<Integer> expected = int.class;
      Class<Integer> result = Primitives.primitiveTypeOf(clazz);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveTypeOfWithNonPrimitive() {
      Class<String> clazz = String.class;
      Class<String> expected = null;
      Class<String> result = Primitives.primitiveTypeOf(clazz);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testIsPrimitiveWrapper() {
      Class<Boolean> type = Boolean.class;
      boolean expected = true;
      boolean result = Primitives.isPrimitiveWrapper(type);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testIsPrimitiveWrapperWithPrimitive() {
      Class<Integer> type = int.class;
      boolean expected = false;
      boolean result = Primitives.isPrimitiveWrapper(type);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveWrapperOf() {
      Class<Integer> type = Integer.class;
      Integer expected = 0;
      Integer result = Primitives.primitiveWrapperOf(type);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveWrapperOfWithNonWrapper() {
      Class<String> type = String.class;
      String expected = null;
      String result = Primitives.primitiveWrapperOf(type);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveValueOrNullFor() {
      Class<Integer> primitiveType = int.class;
      int expected = 0;
      int result = Primitives.primitiveValueOrNullFor(primitiveType);
      Assert.assertEquals(expected, result);
   }

   @Test
   public void testPrimitiveValueOrNullForWithNonPrimitive() {
      Class<String> primitiveType = String.class;
      String expected = null;
      String result = Primitives.primitiveValueOrNullFor(primitiveType);
      Assert.assertEquals(expected, result);
   }
}