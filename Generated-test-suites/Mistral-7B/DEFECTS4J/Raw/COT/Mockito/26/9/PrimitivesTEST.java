import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private Map<Class<?>, Object> testCases = new HashMap<Class<?>, Object>();

 @Test
 public void testPrimitiveTypeOf() {
 Class<Boolean> booleanClass = Boolean.class;
 Class<Boolean> expected = Boolean.TYPE;
 Class<Boolean> actual = Primitives.primitiveTypeOf(booleanClass);
 Assert.assertEquals(expected, actual);

 Class<String> stringClass = String.class;
 Class<String> actualString = Primitives.primitiveTypeOf(stringClass);
 Assert.assertNull(actualString);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Boolean> booleanWrapper = Boolean.class;
 boolean isWrapper = Primitives.isPrimitiveWrapper(booleanWrapper);
 Assert.assertTrue(isWrapper);

 Class<Integer> integerClass = Integer.class;
 isWrapper = Primitives.isPrimitiveWrapper(integerClass);
 Assert.assertFalse(isWrapper);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanWrapper = Boolean.class;
 Object actual = Primitives.primitiveWrapperOf(booleanWrapper);
 Assert.assertSame(false, actual);

 Class<Character> characterWrapper = Character.class;
 actual = Primitives.primitiveWrapperOf(characterWrapper);
 Assert.assertSame('\u0000', actual);
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Class<Boolean> booleanType = Boolean.TYPE;
 Object actual = Primitives.primitiveValueOrNullFor(booleanType);
 Assert.assertSame(false, actual);

 Class<Character> characterType = Character.TYPE;
 actual = Primitives.primitiveValueOrNullFor(characterType);
 Assert.assertNull(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOfNull() {
 Class<Object> nullClass = null;
 Primitives.primitiveTypeOf(nullClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsPrimitiveWrapperNull() {
 Class<Object> nullClass = null;
 Primitives.isPrimitiveWrapper(nullClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveWrapperOfNull() {
 Class<Object> nullClass = null;
 Primitives.primitiveWrapperOf(nullClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveValueOrNullForNull() {
 Class<Object> nullClass = null;
 Primitives.primitiveValueOrNullFor(nullClass);
 }
}