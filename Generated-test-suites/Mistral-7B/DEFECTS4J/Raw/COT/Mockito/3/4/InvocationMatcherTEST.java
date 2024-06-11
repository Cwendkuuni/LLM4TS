import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

 private Invocation invocation;
 private List<Matcher> matchers;
 private InvocationMatcher invocationMatcher;

 @Before
 public void setup() {
 invocation = mock(Invocation.class);
 matchers = Arrays.asList(eq("test"), isA(String.class));
 invocationMatcher = new InvocationMatcher(invocation, matchers);
 }

 @Test
 public void testConstructorWithInvocationAndEmptyMatchersList() {
 InvocationMatcher invocationMatcherEmpty = new InvocationMatcher(invocation);
 assertEquals(Collections.emptyList(), invocationMatcherEmpty.getMatchers());
 }

 @Test
 public void testConstructorWithInvocationAndMatchersList() {
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 assertEquals(matchers, invocationMatcherWithMatchers.getMatchers());
 }

 @Test
 public void testGetMethod() {
 Method method = invocation.getMethod();
 assertEquals(method, invocationMatcher.getMethod());
 }

 @Test
 public void testGetInvocation() {
 Invocation returnedInvocation = invocationMatcher.getInvocation();
 assertEquals(invocation, returnedInvocation);
 }

 @Test
 public void testGetMatchers() {
 assertEquals(matchers, invocationMatcher.getMatchers());
 }

 @Test
 public void testToString() {
 String expected = new PrintSettings().print(matchers, invocation);
 String actual = invocationMatcher.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMock() {
 Invocation otherInvocation = mock(Invocation.class);
 InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
 assertFalse(invocationMatcher.matches(otherMatcher));
 }

 @Test
 public void testMatchesWithSameMethod() {
 Invocation otherInvocationWithSameMethod = mock(Invocation.class);
 when(otherInvocation.getMethod()).thenReturn(invocation.getMethod());
 InvocationMatcher otherMatcherWithSameMethod = new InvocationMatcher(otherInvocationWithSameMethod);
 assertTrue(invocationMatcher.matches(otherMatcherWithSameMethod));
 }

 @Test
 public void testMatchesWithDifferentMethod() {
 Invocation otherInvocationWithDifferentMethod = mock(Invocation.class);
 Method otherMethod = mock(Method.class);
 when(otherInvocation.getMethod()).thenReturn(otherMethod);
 when(otherMethod.getName()).thenReturn("otherMethodName");
 InvocationMatcher otherMatcherWithDifferentMethod = new InvocationMatcher(otherInvocationWithDifferentMethod);
 assertFalse(invocationMatcher.matches(otherMatcherWithDifferentMethod));
 }

 @Test
 public void testMatchesWithUnverifiedInvocation() {
 Invocation otherUnverifiedInvocation = mock(Invocation.class);
 InvocationMatcher otherMatcherUnverified = new InvocationMatcher(otherUnverifiedInvocation);
 assertTrue(invocationMatcher.matches(otherMatcherUnverified));
 }

 @Test
 public void testMatchesWithVerifiedInvocation() {
 Invocation otherVerifiedInvocation = mock(Invocation.class);
 doAnswer(invocation -> {}).when(otherVerifiedInvocation).invoke();
 InvocationMatcher otherMatcherVerified = new InvocationMatcher(otherVerifiedInvocation);
 assertFalse(invocationMatcher.matches(otherMatcherVerified));
 }

 @Test
 public void testHasSimilarMethodWithSameMethod() {
 Invocation otherInvocationWithSameMethod = mock(Invocation.class);
 when(otherInvocation.getMethod()).thenReturn(invocation.getMethod());
 InvocationMatcher otherMatcherWithSameMethod = new InvocationMatcher(otherInvocationWithSameMethod);
 assertTrue(invocationMatcher.hasSimilarMethod(otherMatcherWithSameMethod));
 }

 @Test
 public void testHasSimilarMethodWithDifferentMethod() {
 Invocation otherInvocationWithDifferentMethod = mock(Invocation.class);
 when(otherInvocation.getMethod()).thenReturn(new Method("otherMethodName", new Class[0]));
 InvocationMatcher otherMatcherWithDifferentMethod = new InvocationMatcher(otherInvocationWithDifferentMethod);
 assertFalse(invocationMatcher.hasSimilarMethod(otherMatcherWithDifferentMethod));
 }

 @Test
 public void testHasSameMethodWithSameMethod() {
 Invocation otherInvocationWithSameMethod = mock(Invocation.class);
 when(otherInvocation.getMethod()).thenReturn(invocation.getMethod());
 InvocationMatcher otherMatcherWithSameMethod = new InvocationMatcher(otherInvocationWithSameMethod);
 assertTrue(invocationMatcher.hasSameMethod(otherMatcherWithSameMethod));
 }

 @Test
 public void testHasSameMethodWithDifferentMethod() {
 Invocation otherInvocationWithDifferentMethod = mock(Invocation.class);
 when(otherInvocation.getMethod()).thenReturn(new Method("otherMethodName", new Class[0]));
 InvocationMatcher otherMatcherWithDifferentMethod = new InvocationMatcher(otherInvocationWithDifferentMethod);
 assertFalse(invocationMatcher.hasSameMethod(otherMatcherWithDifferentMethod));
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 Capture<String> capture = argumentCaptor(String.class);
 Invocation invocationWithArguments = mock(Invocation.class);
 when(invocationWithArguments.getArguments()).thenReturn(new Object[]{"test1", "test2"});
 invocationMatcher.captureArgumentsFrom(invocationWithArguments);
 assertEquals("test1", capture.getValue());
 }

 @Test
 public void testCaptureArgumentsFromInvocationWithVarargs() {
 Capture<String[]> capture = argumentCaptor(String[].class);
 Invocation invocationWithVarargs = mock(Invocation.class);
 when(invocationWithVarargs.getArguments()).thenReturn(new Object[]{"test1", "test2", "test3"});
 invocationMatcher.captureArgumentsFrom(invocationWithVarargs);
 assertEquals(new String[]{"test2", "test3"}, capture.getValue());
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = Arrays.asList(invocation, invocation);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 assertEquals(2, invocationMatchers.size());
 }
}