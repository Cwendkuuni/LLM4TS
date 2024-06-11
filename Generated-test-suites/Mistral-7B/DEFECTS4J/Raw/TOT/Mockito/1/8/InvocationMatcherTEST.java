import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mockitoSession;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*; // Import all stubbing methods here

public class InvocationMatcherTest {

 private MockitoSession session;
 private InvocationMatcher invocationMatcher;

 @Before
 public void setup() {
 session = new MockitoSession();
 invocationMatcher = new InvocationMatcher(session.createMock(Object.class), Collections.emptyList());
 }

 @Test
 public void testMatchesSameInvocation() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(invocationMatcher.getMethod());
 when(invocation.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());

 assertTrue(invocationMatcher.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testDoesNotMatchDifferentMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method("differentMethod", Integer.TYPE));

 assertFalse(invocationMatcher.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testDoesNotMatchDifferentMock() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(new Object());

 assertFalse(invocationMatcher.matches(invocation));
 }

 @Test
 public void testMatchesSameMethodWithSameArguments() {
 Object[] sameArguments = new Object[]{1, "two"};
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(invocationMatcher.getMethod());
 when(invocation.getArguments()).thenReturn(sameArguments);

 assertTrue(invocationMatcher.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testDoesNotMatchSameMethodWithDifferentArguments() {
 Object[] differentArguments = new Object[]{1, "two", 3};
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(invocationMatcher.getMethod());
 when(invocation.getArguments()).thenReturn(differentArguments);

 assertFalse(invocationMatcher.matches(invocation));
 }

 @Test
 public void testMatchesInvocationWithMatchers() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("two"));
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 when(invocation.getMethod()).thenReturn(invocationMatcherWithMatchers.getMethod());
 when(invocation.getArguments()).thenReturn(new Object[]{1, "two"});

 assertTrue(invocationMatcherWithMatchers.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testDoesNotMatchInvocationWithDifferentArgumentsAndMatchers() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("two"));
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 when(invocation.getMethod()).thenReturn(invocationMatcherWithMatchers.getMethod());
 when(invocation.getArguments()).thenReturn(new Object[]{1, "two", 3});

 assertFalse(invocationMatcherWithMatchers.matches(invocation));
 }

 @Test
 public void testMatchesInvocationWithVarargsMatcher() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Object[] varargs = new Object[]{"one", "two"};
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new VarargMatcher(ArgumentMatchers.containsInAnyOrder(varargs))));
 InvocationMatcher invocationMatcherWithVarargsMatcher = new InvocationMatcher(invocation, matchers);
 when(invocation.getMethod()).thenReturn(invocationMatcherWithVarargsMatcher.getMethod());
 when(invocation.getArguments()).thenReturn(new Object[]{1, varargs});

 assertTrue(invocationMatcherWithVarargsMatcher.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testDoesNotMatchInvocationWithDifferentVarargs() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Object[] varargs = new Object[]{"one", "two"};
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new VarargMatcher(ArgumentMatchers.containsInAnyOrder(new Object[]{"one", "three"}))));
 InvocationMatcher invocationMatcherWithVarargsMatcher = new InvocationMatcher(invocation, matchers);
 when(invocation.getMethod()).thenReturn(invocationMatcherWithVarargsMatcher.getMethod());
 when(invocation.getArguments()).thenReturn(new Object[]{1, varargs});

 assertFalse(invocationMatcherWithVarargsMatcher.matches(invocation));
 }

 @Test
 public void testMatchesInvocationWithCapturedArguments() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Object capturedArg = "three";
 List<Matcher> matchers = Arrays.asList(new CapturesArguments("capturedArgument"){@Override public void captureFrom(Object arg) { this.capturedArg = arg;}});
 InvocationMatcher invocationMatcherWithCapturedArguments = new InvocationMatcher(invocation, matchers);
 when(invocation.getMethod()).thenReturn(invocationMatcherWithCapturedArguments.getMethod());
 when(invocation.getArguments()).thenReturn(new Object[]{1, "two", capturedArg});

 assertTrue(invocationMatcherWithCapturedArguments.matches(invocation));
 assertEquals(capturedArg, "three");
 }
}