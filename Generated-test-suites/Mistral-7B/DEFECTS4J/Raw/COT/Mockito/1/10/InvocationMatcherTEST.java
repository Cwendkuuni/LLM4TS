import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 // Test constructor with empty list of matchers
 @Test
 public void testInvocationMatcherEmptyMatcherList() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "testEmptyMethod", new Class[0])));
 assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
 }

 // Test constructor with non-empty list of matchers
 @Test
 public void testInvocationMatcherNonEmptyMatcherList() {
 Matcher argMatcher1 = is("arg1");
 Matcher argMatcher2 = not(nullValue());
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "testNonEmptyMethod", new Class[]{String.class, Integer.TYPE})));
 invocationMatcher.addMatcherAt(0, argMatcher1);
 invocationMatcher.addMatcherAt(1, argMatcher2);
 List<Matcher> matchers = invocationMatcher.getMatchers();
 assertThat(matchers.get(0), is(argMatcher1));
 assertThat(matchers.get(1), is(argMatcher2));
 }

 // Test getMethod() method
 @Test
 public void testGetMethod() {
 Method method = new Method(TestClass.class, "testGetMethod", new Class[0]);
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), method));
 assertThat(invocationMatcher.getMethod(), is(method));
 }

 // Test getInvocation() method
 @Test
 public void testGetInvocation() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 assertThat(invocationMatcher.getInvocation(), is(invocation));
 }

 // Test getMatchers() method
 @Test
 public void testGetMatchers() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "testGetMatchers", new Class[0])));
 List<Matcher> matchers = invocationMatcher.getMatchers();
 assertThat(matchers, is(Collections.emptyList()));
 }

 // Test matches() method with same invocation
 @Test
 public void testMatchesWithSameInvocation() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 InvocationMatcher sameInvocationMatcher = new InvocationMatcher(invocation);
 assertThat(invocationMatcher.matches(sameInvocationMatcher), is(true));
 }

 // Test matches() method with different invocation
 @Test
 public void testMatchesWithDifferentInvocation() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertThat(invocationMatcher1.matches(invocationMatcher2), is(false));
 }

 // Test hasSimilarMethod() method with same method
 @Test
 public void testHasSimilarMethodWithSameMethod() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Method method = new Method(TestClass.class, "testHasSimilarMethodWithSameMethod", new Class[0]);
 invocation1.setMethod(method);
 invocation2.setMethod(method);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(true));
 }

 // Test hasSimilarMethod() method with different method
 @Test
 public void testHasSimilarMethodWithDifferentMethod() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Method method1 = new Method(TestClass.class, "testHasSimilarMethodWithSameMethod", new Class[0]);
 Method method2 = new Method(TestClass.class, "testHasSimilarMethodWithDifferentMethod", new Class[0]);
 invocation1.setMethod(method1);
 invocation2.setMethod(method2);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(false));
 }

 // Test hasSameMethod() method with same method
 @Test
 public void testHasSameMethodWithSameMethod() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Method method = new Method(TestClass.class, "testHasSameMethodWithSameMethod", new Class[0]);
 invocation1.setMethod(method);
 invocation2.setMethod(method);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertThat(invocationMatcher1.hasSameMethod(invocationMatcher2), is(true));
 }

 // Test hasSameMethod() method with different method
 @Test
 public void testHasSameMethodWithDifferentMethod() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Method method1 = new Method(TestClass.class, "testHasSameMethodWithSameMethod", new Class[0]);
 Method method2 = new Method(TestClass.class, "testHasSameMethodWithDifferentMethod", new Class[0]);
 invocation1.setMethod(method1);
 invocation2.setMethod(method2);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertThat(invocationMatcher1.hasSameMethod(invocationMatcher2), is(false));
 }

 // Test toString() method
 @Test
 public void testToString() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Matcher argMatcher = is("arg");
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argMatcher));
 String expected = "InvocationMatcher{invocation=InvocationOnMock{this=<[Mock()] org.junit.TestClass$TestClass$$EnhancerByMockitoCGLIB$$4f5b7b22>}, matchers=[Matcher{invoked=0, description=arg}]}";
 String actual = invocationMatcher.toString();
 assertThat(actual, is(expected));
 }

 // Test matches() method with arguments
 @Test(expected = UnsupportedOperationException.class)
 public void testMatchesWithArguments() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(TestClass.class, "testMatchesWithArguments", new Class[0])));
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocationMatcher.matches(new InvocationOnMockImpl(new Object(), invocation.getMethod()));
 }

 // Test captureArgumentsFrom() method with non-varargs arguments
 @Test
 public void testCaptureArgumentsFromNonVarargs() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 invocationMatcher.captureArgumentsFrom(invocation);
 Matcher argMatcher = ArgumentsMatcher.instance("arg");
 invocationMatcher.addMatcherAt(0, argMatcher);
 Object arg = new Object();
 invocation.setArgumentAt(0, arg);
 Matcher capturedMatcher = argMatcher.getCapture();
 ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
 Mockito.verify(invocation).getArgumentAt(0, argumentCaptor.capture());
 assertThat(capturedMatcher.getValue(), is(argumentCaptor.getValue()));
 }

 // Test captureArgumentsFrom() method with varargs arguments
 @Test(expected = UnsupportedOperationException.class)
 public void testCaptureArgumentsFromVarargs() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 invocationMatcher.captureArgumentsFrom(invocation);
 Matcher argMatcher = ArgumentsMatcher.instance("arg");
 invocationMatcher.addMatcherAt(0, argMatcher);
 Object[] args = new Object[]{new Object(), new Object()};
 invocation.setRawArguments(args);
 }

}

class TestClass {
 // Test methods
 public void testEmptyMethod() {}
 public void testNonEmptyMethod(String arg1, Integer arg2) {}
 public void testGetMethod() {}
 public void testMatches(Invocation invocation) {}
 public void testHasSimilarMethodWithSameMethod(Invocation invocation) {}
 public void testHasSimilarMethodWithDifferentMethod(Invocation invocation) {}
 public void testGetMatchers(Invocation invocation) {}
}