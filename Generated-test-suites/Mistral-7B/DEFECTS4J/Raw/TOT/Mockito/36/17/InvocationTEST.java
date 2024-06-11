import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class InvocationTest {

 @Test
 public void testInvocationCreation() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock");
 Invocation invocation = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[0], 1, null);

 assertThat(invocation.getMock(), isA(InvocationOnMock.class));
 assertThat(invocation.getMethod(), is(mockitoMethod));
 assertThat(invocation.getArguments(), is(empty()));
 }

 @Test
 public void testInvocationWithArguments() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock", Object.class);
 Invocation invocation = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[]{"test"}, 1, null);

 assertThat(invocation.getArguments(), hasItem("test"));
 }

 @Test
 public void testInvocationWithVarArgs() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock", Object[].class);
 Invocation invocation = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[]{new Object[2]}, 1, null);

 Object[] rawArguments = invocation.getRawArguments();
 Object[] arguments = invocation.getArguments();

 assertThat(rawArguments, hasItem(instanceOf(Object[].class)));
 assertThat(arguments, hasSize(2));
 }

 @Test
 public void testInvocationWithLocation() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock");
 Invocation invocation = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[0], 1, null);

 Location location = invocation.getLocation();

 assertNotNull(location);
 }

 @Test
 public void testInvocationToString() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock");
 Invocation invocation = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[]{"test"}, 1, null);

 String toString = invocation.toString();

 assertNotNull(toString);
 }

 @Test(expected = RuntimeException.class)
 public void testInvocationHashCode() {
 MockitoMethod mockitoMethod = Mockito.method(Invocation.class, "getMock");
 Invocation invocation1 = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[]{"test1"}, 1, null);
 Invocation invocation2 = new Invocation(mock(InvocationOnMock.class), mockitoMethod, new Object[]{"test2"}, 2, null);

 invocation1.hashCode();
 invocation2.hashCode();
 }

 @Test
 public void testGetMock() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 InvocationOnMock actualMock = invocation.getMock();

 assertEquals(mock, actualMock);
 }

 @Test
 public void testGetMethod() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 MockitoMethod actualMethod = invocation.getMethod();

 assertEquals(Mockito.method(Invocation.class, "getMock"), actualMethod);
 }

 @Test
 public void testGetArguments() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", int.class), new Object[]{1}, 1, null);

 Object[] actualArguments = invocation.getArguments();

 assertEquals(new Object[]{1}, actualArguments);
 }

 @Test
 public void testIsVerified() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 assertFalse(invocation.isVerified());
 }

 @Test
 public void testMarkVerified() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 invocation.markVerified();

 assertTrue(invocation.isVerified());
 }

 @Test
 public void testIsVerifiedInOrder() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation1 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);
 Invocation invocation2 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 2, null);

 invocation1.markVerifiedInOrder();
 invocation2.markVerifiedInOrder();

 assertTrue(invocation1.isVerifiedInOrder());
 assertTrue(invocation2.isVerifiedInOrder());
 }

 @Test
 public void testToStringWithMatchers() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[]{"test"}, 1, null);

 String toString = invocation.toString(new PrintSettings(), Arrays.asList(new ArgumentMatcher<Integer>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals(1);
 }
 }, new ArgumentMatcher<String>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals("test");
 }
 }));

 assertNotNull(toString);
 }

 @Test
 public void testToStringWithNoMatchers() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 String toString = invocation.toString(new PrintSettings());

 assertNotNull(toString);
 }

 @Test
 public void testEquals() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation1 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);
 Invocation invocation2 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 assertTrue(invocation1.equals(invocation2));
 }

 @Test
 public void testNotEqualsDifferentClass() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation1 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);
 Object otherObject = new Object();

 assertFalse(invocation1.equals(otherObject));
 }

 @Test
 public void testNotEqualsDifferentInvocations() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation1 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);
 Invocation invocation2 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 2, null);

 assertFalse(invocation1.equals(invocation2));
 }

 @Test
 public void testNotEqualsDifferentArguments() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation1 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[]{1}, 1, null);
 Invocation invocation2 = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[]{2}, 1, null);

 assertFalse(invocation1.equals(invocation2));
 }

 @Test
 public void testIsToString() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "toString"), new Object[0], 1, null);

 assertTrue(Invocation.isToString(invocation));
 }

 @Test
 public void testIsNotToString() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 assertFalse(Invocation.isToString(invocation));
 }

 @Test
 public void testIsValidException() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 InvocationException exception = new InvocationException("test");

 assertTrue(invocation.isValidException(exception));
 }

 @Test
 public void testIsNotValidException() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 RuntimeException runtimeException = new RuntimeException("test");

 assertFalse(invocation.isValidException(runtimeException));
 }

 @Test
 public void testIsValidReturnType() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", int.class), new Object[0], 1, null);

 assertTrue(invocation.isValidReturnType(int.class));
 }

 @Test
 public void testIsNotValidReturnType() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", int.class), new Object[0], 1, null);

 assertTrue(invocation.isValidReturnType(String.class));
 }

 @Test
 public void testIsVoid() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", void.class), new Object[0], 1, null);

 assertTrue(invocation.isVoid());
 }

 @Test
 public void testIsNotVoid() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", String.class), new Object[0], 1, null);

 assertFalse(invocation.isVoid());
 }

 @Test
 public void testPrintMethodReturnType() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", String.class), new Object[0], 1, null);

 assertEquals("String", invocation.printMethodReturnType());
 }

 @Test
 public void testGetMethodName() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", String.class), new Object[0], 1, null);

 assertEquals("getMock", invocation.getMethodName());
 }

 @Test
 public void testReturnsPrimitive() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", boolean.class), new Object[0], 1, null);

 assertTrue(invocation.returnsPrimitive());
 }

 @Test
 public void testDoesNotReturnPrimitive() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", String.class), new Object[0], 1, null);

 assertFalse(invocation.returnsPrimitive());
 }

 @Test
 public void testGetLocation() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock"), new Object[0], 1, null);

 assertNotNull(invocation.getLocation());
 }

 @Test
 public void testGetArgumentsCount() {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", int.class, int.class), new Object[]{1, 2}, 1, null);

 assertEquals(2, invocation.getArgumentsCount());
 }

 @Test
 public void testCallRealMethod() throws Exception {
 InvocationOnMock mock = mock(InvocationOnMock.class);
 Invocation invocation = new Invocation(mock, Mockito.method(Invocation.class, "getMock", int.class), new Object[]{1}, 1, null);

 int result = invocation.callRealMethod();

 assertEquals(1, result);
 }
}