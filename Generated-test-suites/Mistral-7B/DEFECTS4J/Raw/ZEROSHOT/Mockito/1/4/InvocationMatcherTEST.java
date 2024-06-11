import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MockitoInvocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.mockitoSession;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private Invocation invocation;
 private MockitoInvocation mockitoInvocation;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(String.class);
 invocation = new MockitoInvocation(mock, "testMethod", new Object[]{1, "test"}, new Location("TestClass.java", 10));
 mockitoInvocation = new MockitoInvocation(mock, "testMethod", new Object[]{1, "test"}, new Location("TestClass.java", 10));
 invocationMatcher = new InvocationMatcher(invocation);
 }

 @Test
 public void testMatches() {
 InvocationMatcher matchedInvocation = new InvocationMatcher(mockitoInvocation);
 assertTrue(invocationMatcher.matches(matchedInvocation));
 }

 @Test
 public void testHasSimilarMethod() {
 InvocationMatcher differentMockMatcher = new InvocationMatcher(mockitoInvocation, Collections.singletonList(is(mockitoInvocation.getInvocation().getMock())));
 InvocationMatcher differentMethodMatcher = new InvocationMatcher(new MockitoInvocation(mock, "differentMethod", new Object[]{1, "test"}, new Location("TestClass.java", 10)));
 assertFalse(invocationMatcher.hasSimilarMethod(differentMockMatcher));
 assertFalse(invocationMatcher.hasSimilarMethod(differentMethodMatcher));

 InvocationMatcher sameMethodMatcher = new InvocationMatcher(new MockitoInvocation(mock, "testMethod", new Object[]{1, "test"}, new Location("TestClass.java", 10)));
 assertTrue(invocationMatcher.hasSimilarMethod(sameMethodMatcher));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 Object[] actualArgs = new Object[]{1, "test", "test"};
 assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCaptureArgumentsFromInvocationWithVarargs() {
 invocationMatcher.captureArgumentsFrom(mockitoInvocation);
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 Object arg1 = 1;
 Object arg2 = "test";
 ArgumentMatcher argMatcher1 = is(arg1);
 ArgumentMatcher argMatcher2 = is("test");
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(argMatcher1);
 matchers.add(argMatcher2);
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 invocationMatcherWithMatchers.captureArgumentsFrom(invocation);

 ArgumentCaptor<Object> argumentCaptor1 = ArgumentCaptor.forClass(Object.class);
 ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
 verify(mock).testMethod(argumentCaptor1.capture(), argumentCaptor2.capture());

 assertEquals(arg1, argumentCaptor1.getValue());
 assertEquals("test", argumentCaptor2.getValue());
 }

 @Test
 public void testVarargMatcher() {
 Object[] varargs = new Object[]{"one", "two", "three"};
 VarargMatcher varargMatcher = Arguments.arrayContaining(is("one"), is("two"));
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(varargMatcher);
 InvocationMatcher invocationMatcherWithVarargMatcher = new InvocationMatcher(mockitoInvocation, matchers);
 invocationMatcherWithVarargMatcher.captureArgumentsFrom(mockitoInvocation);

 ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);
 verify(mock).testMethod(argumentCaptor.capture());
 assertArrayEquals(varargs, argumentCaptor.getValue());
 }
}