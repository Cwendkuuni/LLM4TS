package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MockitoMethod;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

 private MockitoMethod delegatingMethod;
 private Method testMethod;

 @Before
 public void setUp() throws NoSuchMethodException {
 testMethod = new MethodTestClass().getClass().getMethod("testMethod", int.class, String.class);
 delegatingMethod = new DelegatingMethod(testMethod);
 }

 @Test
 public void testGetExceptionTypes() {
 Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
 Class<?>[] expectedExceptionTypes = testMethod.getExceptionTypes();
 assertArrayEquals(expectedExceptionTypes, exceptionTypes);
 }

 @Test
 public void testGetJavaMethod() {
 assertEquals(testMethod, delegatingMethod.getJavaMethod());
 }

 @Test
 public void testGetName() {
 assertEquals("testMethod", delegatingMethod.getName());
 }

 @Test
 public void testGetParameterTypes() {
 Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
 assertArrayEquals(new Class<?>[]{int.class, String.class}, parameterTypes);
 }

 @Test
 public void testGetReturnType() {
 assertEquals(void.class, delegatingMethod.getReturnType());
 }

 @Test
 public void testIsVarArgs() {
 assertFalse(delegatingMethod.isVarArgs());
 }

 @Test
 public void testIsAbstract() {
 assertFalse(delegatingMethod.isAbstract());
 }

 @Test
 public void testEquals() {
 DelegatingMethod delegatingMethodWithSameMethod = new DelegatingMethod(testMethod);
 assertTrue(delegatingMethod.equals(delegatingMethodWithSameMethod));

 Method differentMethod = new MethodTestClass().getClass().getMethod("differentMethod", int.class);
 assertFalse(delegatingMethod.equals(new DelegatingMethod(differentMethod)));

 assertFalse(delegatingMethod.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 int hashCode = delegatingMethod.hashCode();
 assertEquals(hashCode, delegatingMethod.hashCode());
 }
}

class MethodTestClass {
 // A simple class with testMethod that DelegatingMethod is testing
 void testMethod(int i, String s) {
 // Empty method body
 }
}