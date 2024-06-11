import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import static org.assertj.core.api.Assertions.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.junit.jupiter.MockitoExtension.extended;

public class DelegatingMethodTest {

 @Test
 public void testGetExceptionTypes() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, Integer.class }, new Class[] { Exception1.class, Exception2.class }));
 Assert.assertArrayEquals(new Class[] { Exception1.class, Exception2.class }, delegatingMethod.getExceptionTypes());
 }

 @Test
 public void testGetExceptionTypesWithNoExceptions() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] {}));
 Assert.assertArrayEquals(new Class[] {}, delegatingMethod.getExceptionTypes());
 }

 @Test
 public void testGetJavaMethod() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, Integer.class }));
 Method javaMethod = delegatingMethod.getJavaMethod();
 Assert.assertEquals("exampleMethod", javaMethod.getName());
 }

 @Test(expected = AssertionError.class)
 public void testGetJavaMethodWithNullMethod() {
 new DelegatingMethod(null);
 }

 @Test
 public void testGetName() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, Integer.class }));
 Assert.assertEquals("exampleMethod", delegatingMethod.getName());
 }

 @Test(expected = AssertionError.class)
 public void testGetNameWithEmptyMethodName() {
 new DelegatingMethod(new Method("", new Class[] {}));
 }

 @Test
 public void testIsVarArgs() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class }, new Class[] {}));
 Assert.assertFalse(delegatingMethod.isVarArgs());
 }

 @Test
 public void testIsVarArgsWithVarArgsMethod() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, String[].class }));
 Assert.assertTrue(delegatingMethod.isVarArgs());
 }

 @Test
 public void testIsAbstract() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] {}));
 Assert.assertFalse(delegatingMethod.isAbstract());
 }

 @Test
 public void testIsAbstractWithAbstractMethod() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] {}, new Class[] {}));
 Assert.assertTrue(delegatingMethod.isAbstract());
 }

 @Test
 public void testEquals() {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method("exampleMethod1", new Class[] { String.class, Integer.class }));
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method("exampleMethod2", new Class[] { String.class, Integer.class }));
 Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
 }

 @Test
 public void testEqualsWithSameObject() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, Integer.class }));
 Assert.assertTrue(delegatingMethod.equals(delegatingMethod));
 }

 @Test
 public void testHashCode() {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method("exampleMethod1", new Class[] { String.class, Integer.class }));
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method("exampleMethod2", new Class[] { String.class, Integer.class }));
 Assert.assertThat(delegatingMethod1.hashCode()).isNotEqualTo(delegatingMethod2.hashCode());
 }

 @Test
 public void testHashCodeWithSameObject() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("exampleMethod", new Class[] { String.class, Integer.class }));
 int initialHashCode = getInternalState(delegatingMethod, "hashCode");
 delegatingMethod.hashCode();
 Assert.assertThat(getInternalState(delegatingMethod, "hashCode")).isEqualTo(initialHashCode);
 }

}