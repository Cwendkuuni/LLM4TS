import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.hamcrest.Matcher;

public class InvocationMatcherTest {

  private Invocation invocation;
  private InvocationMatcher invocationMatcher;

  @Before
  public void setUp() {
    invocation = mock(Invocation.class);
    invocationMatcher = new InvocationMatcher(invocation);
  }

  @Test
  public void testGetMethod() {
    Method method = invocation.getMethod();
    assertSame(method, invocationMatcher.getMethod());
  }

  @Test
  public void testGetInvocation() {
    assertSame(invocation, invocationMatcher.getInvocation());
  }

  @Test
  public void testGetMatchers() {
    List<Matcher> matchers = invocationMatcher.getMatchers();
    assertTrue(matchers.isEmpty());
  }

  @Test
  public void testToString() {
    String expected = invocationMatcher.toString();
    assertEquals(expected, invocationMatcher.toString());
  }

  @Test
  public void testMatches() {
    Invocation actual = mock(Invocation.class);
    when(invocation.getMock()).thenReturn(actual.getMock());
    when(invocation.getArguments()).thenReturn(new Object[] {});
    when(actual.getArguments()).thenReturn(new Object[] {});
    assertTrue(invocationMatcher.matches(actual));
  }

  @Test
  public void testHasSimilarMethod() {
    Invocation candidate = mock(Invocation.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(candidate.getMethod()).thenReturn(method);
    when(invocation.isVerified()).thenReturn(false);
    when(util.hasSameMethod(candidate)).thenReturn(true);
    assertTrue(invocationMatcher.hasSimilarMethod(candidate));
  }

  @Test
  public void testHasSameMethod() {
    Invocation candidate = mock(Invocation.class);
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(candidate.getMethod()).thenReturn(method);
    when(invocation.getMethod().getName()).thenReturn("methodName");
    when(candidate.getMethod().getName()).thenReturn("methodName");
    when(util.hasSameMethod(candidate)).thenReturn(true);
    assertTrue(invocationMatcher.hasSameMethod(candidate));
  }

  @Test
  public void testGetLocation() {
    Location location = mock(Location.class);
    when(invocation.getLocation()).thenReturn(location);
    assertSame(location, invocationMatcher.getLocation());
  }

  @Test
  public void testCaptureArgumentsFrom() {
    Invocation invocation = mock(Invocation.class);
    CapturesArguments capturesArguments = mock(CapturesArguments.class);
    when(invocation.getMethod().isVarArgs()).thenReturn(true);
    when(invocation.getMethod().getParameterTypes()).thenReturn(new Class[] {});
    when(invocation.getRawArguments()).thenReturn(new Object[] {});
    when(util.isVarargMatcher(capturesArguments)).thenReturn(true);
    when(util.isVariableArgument(invocation, 0)).thenReturn(false);
    List<Matcher> matchers = new LinkedList<>();
    matchers.add(capturesArguments);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    invocationMatcher.captureArgumentsFrom(invocation);
    verify(capturesArguments).captureFrom(any());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCaptureArgumentsFromWithVarargs() {
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod().isVarArgs()).thenReturn(true);
    when(invocation.getMethod().getParameterTypes()).thenReturn(new Class[] {});
    when(invocation.getRawArguments()).thenReturn(new Object[] {});
    invocationMatcher.captureArgumentsFrom(invocation);
  }

  @Test
  public void testCreateFrom() {
    List<Invocation> invocations = new LinkedList<>();
    invocations.add(mock(Invocation.class));
    List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
    assertEquals(invocations.size(), invocationMatchers.size());
  }
}