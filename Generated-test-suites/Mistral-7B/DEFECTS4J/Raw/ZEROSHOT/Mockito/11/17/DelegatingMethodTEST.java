import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DelegatingMethodTest {

 private DelegatingMethod delegatingMethod;
 private Method testMethod;

 @Before
 public void setUp() throws Exception {
 Class<?> clazz = Reflections.getClassFromName("org.mockito.internal.creation.TestClass");
 testMethod = clazz.getMethod("testMethod", String.class, int.class);
 delegatingMethod = new DelegatingMethod(testMethod);
 }

 @Test
 public void testGetExceptionTypes() throws Exception {
 Class<?>[] expectedExceptionTypes = new Class[0];
 Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
 assertEquals(expectedExceptionTypes, actualExceptionTypes);
 }

 @Test
 public void testGetJavaMethod() {
 Method expectedMethod = testMethod;
 Method actualMethod = delegatingMethod.getJavaMethod();
 assertEquals(expectedMethod, actualMethod);
 }

 @Test
 public void testGetName() {
 String expectedName = "testMethod";
 String actualName = delegatingMethod.getName();
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testGetParameterTypes() {
 Class<?>[] expectedParameterTypes = {String.class, int.class};
 Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
 assertArrayEquals(expectedParameterTypes, actualParameterTypes);
 }

 @Test
 public void testGetReturnType() {
 Class<?> expectedReturnType = void.class;
 Class<?> actualReturnType = delegatingMethod.getReturnType();
 assertEquals(expectedReturnType, actualReturnType);
 }

 @Test
 public void testIsVarArgs() {
 boolean expectedResult = false;
 boolean actualResult = delegatingMethod.isVarArgs();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsAbstract() {
 Class<?> testClass = Reflections.getClassFromName("org.mockito.internal.creation.TestClass");
 Method abstractMethod = testClass.getMethod("abstractMethod");
 DelegatingMethod abstractDelegatingMethod = new DelegatingMethod(abstractMethod);
 boolean expectedResult = true;
 boolean actualResult = abstractDelegatingMethod.isAbstract();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testEquals() {
 DelegatingMethod equalDelegatingMethod = new DelegatingMethod(testMethod);
 assertTrue(delegatingMethod.equals(equalDelegatingMethod));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = 1;
 int actualHashCode = delegatingMethod.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testEqualsDifferentMethods() {
 Method differentMethod = Reflections.getClassFromName("org.mockito.internal.creation.TestClass")
 .getMethod("differentMethod", String.class);
 assertFalse(delegatingMethod.equals(new DelegatingMethod(differentMethod)));
 }
}