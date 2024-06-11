import org.junit.Test;
import org.junit.Assert.*;
import java.util.Map;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {
         Boolean.class, Character.class, Byte.class, Short.class,
         Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf_ValidPrimitiveClass() {
     for (Class<?> primitiveType : PRIMITIVE_WRAPPER_TYPES) {
         Class<?> result = Primitives.primitiveTypeOf(primitiveType);
         Assert.assertEquals(primitiveType, result);
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOf_NonPrimitiveClass() {
     Class<String> stringClass = String.class;
     try {
         Primitives.primitiveTypeOf(stringClass);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testIsPrimitiveWrapper_ValidWrapperClass() {
     for (Class<?> wrapperType : PRIMITIVE_WRAPPER_TYPES) {
         assertTrue(Primitives.isPrimitiveWrapper(wrapperType));
     }
 }

 @Test
 public void testIsPrimitiveWrapper_NonWrapperClass() {
     Class<String> stringClass = String.class;
     assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf_ValidWrapperClass() {
     for (Class<?> wrapperType : PRIMITIVE_WRAPPER_TYPES) {
         Object result = Primitives.primitiveWrapperOf(wrapperType);
         if (Boolean.class.equals(wrapperType)) {
             assertTrue(result instanceof Boolean);
             assertEquals(false, ((Boolean) result).booleanValue());
         } else if (Character.class.equals(wrapperType)) {
             assertTrue(result instanceof Character);
             assertEquals('\u0000', ((Character) result).charValue());
         } else {
             Class<?> primitiveType = Primitives.primitiveTypeOf(wrapperType);
             assertEquals(primitiveType.getName(), Primitives.primitiveTypeOf(result.getClass()).getName());
             assertNotNull(result);
             assertEquals(Primitives.primitiveValueOrNullFor(primitiveType), result);
         }
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor_ValidPrimitiveType() {
     for (Map.Entry<Class<?>, Object> primitiveValueEntry : Primitives.primitiveValues.entrySet()) {
         Object result = Primitives.primitiveValueOrNullFor(primitiveValueEntry.getKey());
         assertEquals(primitiveValueEntry.getValue(), result);
     }
 }

 @Test(expected = NullPointerException.class)
 public void testPrimitiveValueOrNullFor_NullPrimitiveType() {
     try {
         Primitives.primitiveValueOrNullFor(null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 public void testWrapperTypes_ValidPrimitiveTypesAndWrapperTypesMappedCorrectly() {
     for (Map.Entry<Class<?>, Class<?>> entry : Primitives.primitiveTypes.entrySet()) {
         Class<?> primitiveType = entry.getKey();
         Class<?> wrapperType = entry.getValue();
         assertEquals(wrapperType.getName(), Primitives.wrapperTypes.get(primitiveType).getName());
     }
 }

 @Test(expected = IllegalStateException.class)
 public void testWrapperTypes_UnmappedPrimitiveType() {
     try {
         Primitives.wrapperTypes.get(String.class);
         fail("Expected IllegalStateException");
     } catch (IllegalStateException e) {
         // Expected
     }
 }

}