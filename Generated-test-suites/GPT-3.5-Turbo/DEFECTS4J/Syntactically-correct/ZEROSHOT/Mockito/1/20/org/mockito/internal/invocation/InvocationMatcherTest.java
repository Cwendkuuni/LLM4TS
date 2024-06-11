import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.MethodInvocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

  @Test
  public void testConstructorWithMatchers() {
    Invocation invocation = mock(Invocation.class);
    List<Matcher> matchers = new ArrayList<Matcher>();
    matchers.add(anyInt());
    matchers.add(anyString());

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

    assertEquals(invocation, invocationMatcher.getInvocation());
    assertEquals(matchers, invocationMatcher.getMatchers());
  }

  @Test
  public void testConstructorWithoutMatchers() {
    Invocation invocation = mock(Invocation.class);

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertEquals(invocation, invocationMatcher.getInvocation());
    assertTrue(invocationMatcher.getMatchers().isEmpty());
  }

  @Test
  public void testGetMethod() {
    Invocation invocation = mock(Invocation.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertEquals(method, invocationMatcher.getMethod());
  }

  @Test
  public void testMatches() {
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMock()).thenReturn("test");
    when(invocation.getArguments()).thenReturn(new Object[] {1, "example"});

    Invocation actual = mock(Invocation.class);
    when(actual.getMock()).thenReturn("test");
    when(actual.getArguments()).thenReturn(new Object[] {1, "example"});

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertTrue(invocationMatcher.matches(actual));
  }

  @Test
  public void testHasSimilarMethod() {
    Invocation invocation = mock(Invocation.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.getName()).thenReturn("testMethod");

    Invocation candidate = mock(Invocation.class);
    Method candidateMethod = mock(Method.class);
    when(candidate.getMethod()).thenReturn(candidateMethod);
    when(candidateMethod.getName()).thenReturn("testMethod");

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertTrue(invocationMatcher.hasSimilarMethod(candidate));
  }

  @Test
  public void testHasSameMethod() {
    Invocation invocation = mock(Invocation.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.getName()).thenReturn("testMethod");

    Invocation candidate = mock(Invocation.class);
    Method candidateMethod = mock(Method.class);
    when(candidate.getMethod()).thenReturn(candidateMethod);
    when(candidateMethod.getName()).thenReturn("testMethod");

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertTrue(invocationMatcher.hasSameMethod(candidate));
  }

  @Test
  public void testGetLocation() {
    Invocation invocation = mock(Invocation.class);
    Location location = mock(Location.class);
    when(invocation.getLocation()).thenReturn(location);

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    assertEquals(location, invocationMatcher.getLocation());
  }

  @Test
  public void testCaptureArgumentsFrom() {
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(null);

    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

    invocationMatcher.captureArgumentsFrom(invocation);

    // Verify that captureArgumentsFrom() does not throw an exception
  }

  @Test
  public void testCreateFrom() {
    List<Invocation> invocations = new ArrayList<Invocation>();
    Invocation invocation1 = mock(Invocation.class);
    Invocation invocation2 = mock(Invocation.class);
    invocations.add(invocation1);
    invocations.add(invocation2);

    List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

    assertEquals(2, invocationMatchers.size());
    assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
    assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
  }
}