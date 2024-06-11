import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherFactory.arrayContaining;

@RunWith(org.mockito.runners.JUnit4)
public class InvocationTest {

 @Test
 public void testNewInvocation() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[0]);
 Object[] arguments = new Object[]{new String("hello")};
 RealMethod realMethod = Mockito.mock(RealMethod.class);
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

 // then
 assertThat(invocation.getMock(), is(mock));
 assertThat(invocation.getMethod(), is(mockitoMethod));
 assertThat(invocation.getArguments(), is(arguments));
 assertThat(invocation.getSequenceNumber(), is(1));
 }

 @Test
 public void testExpandVarArgs() {
 // given
 Object[] args = new Object[]{1, new Object[]{1, 2, 3}};

 // when
 Object[] result = Invocation.expandVarArgs(true, args);

 // then
 assertThat(result, is(arrayContaining(new Object[]{1, 1, 2, 3})));
 }

 @Test(expected = RuntimeException.class)
 public void testHashCode() {
 // given
 Invocation invocation = Mockito.spy(new Invocation(null, null, null, 1, null));

 // when, then
 invocation.hashCode();
 }

 @Test
 public void testToString() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[0]);
 Object[] arguments = new Object[]{new String("hello")};
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));

 // when
 String result = invocation.toString();

 // then
 assertThat(result, is("mock.<String> length(arguments)"));
 }

 @Test
 public void testToStringWithMatchers() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[0]);
 Object[] arguments = new Object[]{new String("hello")};
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));

 // when
 String result = invocation.toString(Invocation.argumentsToMatchers(arguments), new PrintSettings());

 // then
 assertThat(result, is("mock.<String> length(arguments:\n [\n  \"hello\"\n ]\n)"));
 }

 @Test
 public void testIsToString() {
 // given
 Invocation invocation = Mockito.spy(new Invocation(new Object() {
 @Override
 public String toString() {
 return "mock";
 }
 }, Mockito.method(Object.class, "toString", new Class[0], new Class[0]), null, 1, Mockito.mock(RealMethod.class)));

 // when
 boolean result = Invocation.isToString(invocation);

 // then
 assertThat(result, is(true));
 }

 @Test
 public void testIsValidException() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[]{RuntimeException.class}), null, 1, Mockito.mock(RealMethod.class));

 // when
 boolean result = invocation.isValidException(new RuntimeException("test"));

 // then
 assertThat(result, is(true));
 }

 @Test
 public void testIsValidReturnType() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 boolean result = invocation.isValidReturnType(int.class);

 // then
 assertThat(result, is(true));
 }

 @Test
 public void testIsVoid() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "toString", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 boolean result = invocation.isVoid();

 // then
 assertThat(result, is(false));
 }

 @Test
 public void testPrintMethodReturnType() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 String result = invocation.printMethodReturnType();

 // then
 assertThat(result, is("int"));
 }

 @Test
 public void testGetMethodName() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 String result = invocation.getMethodName();

 // then
 assertThat(result, is("length"));
 }

 @Test
 public void testReturnsPrimitive() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(boolean.class), Mockito.method(boolean.class, "booleanValue", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 boolean result = invocation.returnsPrimitive();

 // then
 assertThat(result, is(true));
 }

 @Test
 public void testGetLocation() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 Location result = invocation.getLocation();

 // then
 assertThat(result.getLineNumber(), is((int) 42)); // replace with real line number
 assertThat(result.getFileName(), is("InvocationTest.java"));
 }

 @Test
 public void testGetArgumentsCount() {
 // given
 Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length", new Class[]{}, new Class[0]), null, 1, Mockito.mock(RealMethod.class));

 // when
 int result = invocation.getArgumentsCount();

 // then
 assertThat(result, is(0));
 }

 @Test
 public void testCallRealMethod() throws Exception {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[0]);
 RealMethod realMethod = Mockito.mock(RealMethod.class);
 Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, realMethod);
 String expectedResult = "test";
 Mockito.when(realMethod.invoke(mock, new Object[0])).thenReturn(expectedResult);

 // when
 String result = (String) invocation.callRealMethod();

 // then
 assertThat(result, is(expectedResult));
 }
}