package org.mockito.internal.creation.instance;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConstructorInstantiatorTest {

   @Test
   public void testNewInstance_withNullOuterClassInstance() {
      ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
      Class<Object> cls = Object.class;
      Object result = constructorInstantiator.newInstance(cls);
      assertNull(result);
   }

   @Test
   public void testNewInstance_withNonNullOuterClassInstance() {
      Object outerClassInstance = new Object();
      ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
      Class<Object> cls = Object.class;
      Object result = constructorInstantiator.newInstance(cls);
      assertNotNull(result);
      assertEquals(outerClassInstance.getClass(), result.getClass());
   }

   @Test
   public void testNewInstance_withOuterClassConstructorException() {
      Object outerClassInstance = new Object();
      ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
      Class<Object> cls = Object.class;
      try {
         constructorInstantiator.newInstance(cls);
         fail("Expected InstantationException to be thrown");
      } catch (InstantationException e) {
         assertEquals("Unable to create mock instance of '"
                 + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
      }
   }

   @Test
   public void testNewInstance_withNoArgConstructorException() {
      ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
      Class<Object> cls = Object.class;
      try {
         constructorInstantiator.newInstance(cls);
         fail("Expected InstantationException to be thrown");
      } catch (InstantationException e) {
         assertEquals("Unable to create mock instance of '"
                 + cls.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
      }
   }
}