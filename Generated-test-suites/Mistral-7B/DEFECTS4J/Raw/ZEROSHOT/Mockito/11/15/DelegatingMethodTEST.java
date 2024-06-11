import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

 private DelegatingMethod delegatingMethod1;
 private DelegatingMethod delegatingMethod2;
 private Method method;

 @Test
 public void testGetExceptionTypes() {
 method = new Method(getClass(), "testGetExceptionTypes");
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
 assertEquals(0, exceptionTypes.length);
 }

 @Test
 public void testGetJavaMethod() {
 method = new Method(getClass(), "testGetJavaMethod");
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 Method javaMethod = delegatingMethod.getJavaMethod();
 assertEquals(method, javaMethod);
 }

 @Test
 public void testGetName() {
 method = new Method(getClass(), "testGetName");
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 String name = delegatingMethod.getName();
 assertEquals("testGetName", name);
 }

 @Test
 public void testGetParameterTypes() {
 method = new Method(getClass(), "testGetParameterTypes", new Class[0]);
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
 assertEquals(0, parameterTypes.length);
 }

 @Test
 public void testGetReturnType() {
 method = new Method(getClass(), "testGetReturnType", new Class[0]);
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 Class<?> returnType = delegatingMethod.getReturnType();
 assertEquals(void.class, returnType);
 }

 @Test
 public void testIsVarArgs() {
 method = new Method(getClass(), "testIsVarArgs", new Class[] {int[].class});
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 assertTrue(delegatingMethod.isVarArgs());

 method = new Method(getClass(), "testIsVarArgs", new Class[0]);
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
 assertFalse(delegatingMethod2.isVarArgs());
 }

 @Test
 public void testIsAbstract() {
 method = new Method(Object.class, "toString", new Class[0]);
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 assertFalse(delegatingMethod.isAbstract());

 method = new Method(getClass(), "testIsAbstract", new Class[0]);
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
 assertFalse(delegatingMethod2.isAbstract());

 method = new Method(AbstractClass.class, "abstractMethod", new Class[0]);
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(method);
 assertTrue(delegatingMethod3.isAbstract());
 }

 @Test
 public void testEquals() {
 method = new Method(getClass(), "testEquals");
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
 assertTrue(delegatingMethod1.equals(delegatingMethod2));

 method = new Method(getClass(), "testGetExceptionTypes");
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(method);
 assertFalse(delegatingMethod1.equals(delegatingMethod3));
 }

 @Test
 public void testHashCode() {
 method = new Method(getClass(), "testHashCode");
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
 assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
 }

 @Test
 public void testConstructors() {
 method = new Method(getClass(), "testConstructors", new Class[0]);
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
 assertEquals(delegatingMethod1, delegatingMethod2);
 }

 @Test
 public void testSameObject() {
 method = new Method(getClass(), "testSameObject");
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
 DelegatingMethod delegatingMethod2 = delegatingMethod1;
 assertEquals(delegatingMethod1, delegatingMethod2);
 }

 @Test
 public void testDelegatingMethodWithNullMethod() {
 assertFalse(new DelegatingMethod(null).isAbstract());
 }

 @Test
 public void testDelegatingMethodWithNullName() {
 String name = null;
 try {
 new Method(getClass(), name, new Class[0]);
 } catch (NoSuchMethodException e) {
 // expected
 }
 assertFalse(new DelegatingMethod(null).isAbstract());
 }

 @Test
 public void testDelegatingMethodWithNullParameterTypes() {
 Class<?>[] parameterTypes = null;
 Method method = new Method(getClass(), "testDelegatingMethodWithNullParameterTypes", parameterTypes);
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 assertEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
 }

 @Test
 public void testDelegatingMethodWithNullReturnType() {
 Method method = new Method(getClass(), "testDelegatingMethodWithNullReturnType", new Class[0]);
 DelegatingMethod delegatingMethod = new DelegatingMethod(method);
 assertNull(delegatingMethod.getReturnType());
 }

 @Test(expected = NullPointerException.class)
 public void testDelegatingMethodWithNullMethod2() {
 new DelegatingMethod(null);
 }

 private static class AbstractClass {
 abstract void abstractMethod();
 }
}