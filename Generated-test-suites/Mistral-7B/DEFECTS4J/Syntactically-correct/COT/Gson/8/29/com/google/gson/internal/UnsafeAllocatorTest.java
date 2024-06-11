package com.google.gson.internal.test;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(Enclosed.class)
public class UnsafeAllocatorTest {

 @interface TestMethods {
   UnsafeAllocator method();
 }

 static class NewInstanceTest {

   @Test
   @TestMethods
   public void testNewInstance_Object_ShouldReturnInstanceOfObject(UnsafeAllocator unsafeAllocator) throws Exception {
     Object instance = unsafeAllocator.newInstance(Object.class);
     assertNotNull(instance);
     assertInstanceOf(Object.class, instance);
   }

   @Test
   @TestMethods
   public void testNewInstance_ClassWithConstructor_ShouldReturnInstanceOfThatClass(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<StringBuilder> stringBuilderClass = StringBuilder.class;
     StringBuilder instance = (StringBuilder) unsafeAllocator.newInstance(stringBuilderClass);
     assertNotNull(instance);
     assertInstanceOf(StringBuilder.class, instance);
   }

   @Test(expected = UnsupportedOperationException.class)
   @TestMethods
   public void testNewInstance_Interface_ShouldThrowUnsupportedOperationException(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<Object> interfaceClass = Runnable.class;
     unsafeAllocator.newInstance(interfaceClass);
   }

   @Test(expected = UnsupportedOperationException.class)
   @TestMethods
   public void testNewInstance_AbstractClass_ShouldThrowUnsupportedOperationException(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<Object> abstractClass = AbstractClassTest.class;
     unsafeAllocator.newInstance(abstractClass);
   }

   @Test(expected = InstantiationException.class)
   @TestMethods
   public void testNewInstance_ClassWithNoPublicConstructor_ShouldThrowInstantiationException(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
     unsafeAllocator.newInstance(privateConstructorClass);
   }

   @Test(expected = IllegalAccessException.class)
   @TestMethods
   public void testNewInstance_ProtectedConstructor_ShouldThrowIllegalAccessException(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<ProtectedConstructorClass> protectedConstructorClass = ProtectedConstructorClass.class;
     unsafeAllocator.newInstance(protectedConstructorClass);
   }

   @Test(expected = InvocationTargetException.class)
   @TestMethods
   public void testNewInstance_ThrowingConstructor_ShouldThrowInvocationTargetException(UnsafeAllocator unsafeAllocator)
         throws Exception {
     Class<ThrowingConstructorClass> throwingConstructorClass = ThrowingConstructorClass.class;
     try {
       unsafeAllocator.newInstance(throwingConstructorClass);
     } catch (InvocationTargetException e) {
       throw e.getTargetException();
     } catch (UndeclaredThrowableException e) {
       throw e.getThrowable();
     }
   }
 }

 static class ReflectionTest {

   @Test
   @TestMethods
   public void testReflection_Unsafe_ShouldReturnInstanceOfUnsafeAllocator(UnsafeAllocator unsafeAllocator) {
     assertInstanceOf(UnsafeAllocator.class, unsafeAllocator);
   }

   @Test
   @TestMethods
   public void testReflection_Unsafe_GetDeclaredField_ShouldReturnFieldOfTypeUnsafe(UnsafeAllocator unsafeAllocator) {
     assertInstanceOf(Class.class, unsafeAllocator.getClass().getDeclaredField("unsafe").getType());
   }

   @Test
   @TestMethods
   public void testReflection_Unsafe_GetMethodAllocateInstance_ShouldReturnMethodOfTypeMethod(UnsafeAllocator unsafeAllocator) {
     assertInstanceOf(Method.class, unsafeAllocator.getClass().getDeclaredMethod("newInstance", Class.class));
   }
 }
}