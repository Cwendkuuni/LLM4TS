import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

  @Test
  public void testConstructorWithMatchers() {
    Invocation invocation = mock(Invocation.class);
    when(invocation.argumentsToMatchers()).thenReturn(Collections.singletonList(mock(Matcher.class)));
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    assertNotNull(invocationMatcher.getMethod());
    assertEquals(invocation, invocationMatcher.getInvocation());
    assertEquals(Collections.singletonList(mock(Matcher.class)), invocationMatcher.getMatchers());
  }
  
  @Test
  public void testConstructorWithoutMatchers() {
    Invocation invocation = mock(Invocation.class);
    when(invocation.argumentsToMatchers()).thenReturn(Collections.emptyList());
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    assertNotNull(invocationMatcher.getMethod());
    assertEquals(invocation, invocationMatcher.getInvocation());
    assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
  }
  
  @Test
  public void testToString() {
    Invocation invocation = mock(Invocation.class);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    assertNotNull(invocationMatcher.toString());
  }
  
  @Test
  public void testMatches() {
    Invocation invocation1 = mock(Invocation.class);
    Invocation invocation2 = mock(Invocation.class);
    when(invocation1.getMock()).thenReturn(mock(Object.class));
    when(invocation2.getMock()).thenReturn(mock(Object.class));
    when(invocation1.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(invocation2.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(invocation1.isVerified()).thenReturn(false);
    when(invocation2.isVerified()).thenReturn(false);
    when(invocation1.getArguments()).thenReturn(new Object[]{});
    when(invocation2.getArguments()).thenReturn(new Object[]{});
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
    assertTrue(invocationMatcher.matches(invocation2));
  }
  
  @Test
  public void testHasSimilarMethod() {
    Invocation invocation1 = mock(Invocation.class);
    Invocation invocation2 = mock(Invocation.class);
    when(invocation1.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(invocation2.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(invocation1.isVerified()).thenReturn(false);
    when(invocation2.isVerified()).thenReturn(false);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
    assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
  }
  
  @Test
  public void testHasSameMethod() {
    Invocation invocation1 = mock(Invocation.class);
    Invocation invocation2 = mock(Invocation.class);
    when(invocation1.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(invocation2.getMethod()).thenReturn(Object.class.getMethod("toString"));
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
    assertTrue(invocationMatcher.hasSameMethod(invocation2));
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
  public void testToStringWithPrintSettings() {
    Invocation invocation = mock(Invocation.class);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    PrintSettings printSettings = mock(PrintSettings.class);
    assertNotNull(invocationMatcher.toString(printSettings));
  }
  
  @Test
  public void testCaptureArgumentsFrom() {
    Invocation invocation = mock(Invocation.class);
    Invocation i = mock(Invocation.class);
    Matcher matcher = mock(Matcher.class);
    CapturesArguments capturesArguments = mock(CapturesArguments.class);
    when(invocation.getArguments()).thenReturn(new Object[]{});
    when(i.getArguments()).thenReturn(new Object[]{});
    when(i.getArguments()[0]).thenReturn(new Object());
    when(matcher instanceof CapturesArguments).thenReturn(true);
    when(matcher).thenReturn(capturesArguments);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    invocationMatcher.captureArgumentsFrom(i);
  }
}