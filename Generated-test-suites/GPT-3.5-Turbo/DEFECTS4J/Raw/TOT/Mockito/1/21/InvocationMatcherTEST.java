import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
  
  private Invocation invocation;
  private InvocationMatcher invocationMatcher;
  
  @Before
  public void setup() {
    invocation = mock(Invocation.class);
    MockCreationSettings<?> settings = mock(MockCreationSettings.class);
    when(invocation.getMock()).thenReturn(settings);
  }
  
  @Test
  public void testConstructorWithInvocationAndMatchers() {
    List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
    
    invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    assertMethodEquals(invocation.getMethod(), invocationMatcher.getMethod());
    assertMatchersEquals(matchers, invocationMatcher.getMatchers());
  }
  
  @Test
  public void testConstructorWithInvocationAndEmptyMatchers() {
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertMethodEquals(invocation.getMethod(), invocationMatcher.getMethod());
    assertMatchersEquals(Collections.emptyList(), invocationMatcher.getMatchers());
  }
  
  @Test
  public void testGetMethod() {
    Method method = invocation.getMethod();
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertMethodEquals(method, invocationMatcher.getMethod());
  }
  
  @Test
  public void testGetInvocation() {
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertInvocationEquals(invocation, invocationMatcher.getInvocation());
  }
  
  @Test
  public void testGetMatchers() {
    List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
    
    invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    assertMatchersEquals(matchers, invocationMatcher.getMatchers());
  }
  
  @Test
  public void testToString() {
    List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
    
    invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    String printSettings = new ArgumentsComparator().print(matchers, invocation);
    
    assertEquals(printSettings, invocationMatcher.toString());
  }
  
  @Test
  public void testMatches_WhenAllConditionsMet_ReturnsTrue() {
    Invocation actual = mock(Invocation.class);
    when(invocation.getMock().equals(actual.getMock())).thenReturn(true);
    when(invocation.getMethod()).thenReturn(mock(Method.class));
    when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertTrue(invocationMatcher.matches(actual));
  }
  
  @Test
  public void testMatches_WhenMockNotEquals_ReturnsFalse() {
    Invocation actual = mock(Invocation.class);
    when(invocation.getMock().equals(actual.getMock())).thenReturn(false);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.matches(actual));
  }
  
  @Test
  public void testMatches_WhenMethodNotEquals_ReturnsFalse() {
    Invocation actual = mock(Invocation.class);
    when(invocation.getMock().equals(actual.getMock())).thenReturn(true);
    when(invocation.getMethod()).thenReturn(mock(Method.class));
    when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.matches(actual));
  }
  
  @Test
  public void testMatches_WhenArgumentsNotMatch_ReturnsFalse() {
    Invocation actual = mock(Invocation.class);
    when(invocation.getMock().equals(actual.getMock())).thenReturn(true);
    when(invocation.getMethod()).thenReturn(mock(Method.class));
    when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(false);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.matches(actual));
  }
  
  @Test
  public void testHasSimilarMethod_WhenAllConditionsMet_ReturnsTrue() {
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod().getName()).thenReturn("methodName");
    when(invocation.getMethod().getName()).thenReturn("methodName");
    when(candidate.isVerified()).thenReturn(false);
    when(invocation.getMock() == candidate.getMock()).thenReturn(true);
    when(invocation.hasSameMethod(candidate)).thenReturn(true);
    when(new ArgumentsComparator().argumentsMatch(invocation, candidate)).thenReturn(false);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertTrue(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSimilarMethod_WhenMethodNameNotEquals_ReturnsFalse() {
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod().getName()).thenReturn("methodName1");
    when(invocation.getMethod().getName()).thenReturn("methodName2");
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSimilarMethod_WhenIsVerified_ReturnsFalse() {
    Invocation candidate = mock(Invocation.class);
    when(candidate.isVerified()).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSimilarMethod_WhenMockNotEquals_ReturnsFalse() {
    Invocation candidate = mock(Invocation.class);
    when(invocation.getMock() == candidate.getMock()).thenReturn(false);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSimilarMethod_WhenHasSameMethod_ReturnsFalse() {
    Invocation candidate = mock(Invocation.class);
    when(invocation.hasSameMethod(candidate)).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSimilarMethod_WhenArgumentsMatch_ReturnsFalse() {
    Invocation candidate = mock(Invocation.class);
    when(invocation.getArguments()).thenReturn(mock(Object[].class));
    when(candidate.getArguments()).thenReturn(mock(Object[].class));
    when(new ArgumentsComparator().argumentsMatch(invocation, candidate)).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSimilarMethod(candidate));
  }
  
  @Test
  public void testHasSameMethod_WhenMethodEquals_ReturnsTrue() throws NoSuchMethodException {
    Method method = invocation.getMethod();
  
    Method candidateMethod = method.getClass().getMethod("getName", null);
    when(method.getName() != null && method.getName().equals(candidateMethod.getName())).thenReturn(true);
    when(method.getParameterTypes()).thenReturn(new Class[] {});
    when(candidateMethod.getParameterTypes()).thenReturn(new Class[] {});
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertTrue(invocationMatcher.hasSameMethod(invocation));
  }
  
  @Test
  public void testHasSameMethod_WhenMethodNotEquals_ReturnsFalse() throws NoSuchMethodException {
    Method method = invocation.getMethod();
  
    Method candidateMethod = method.getClass().getMethod("getName", null);
    when(method.getName() != null && method.getName().equals(candidateMethod.getName())).thenReturn(false);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertFalse(invocationMatcher.hasSameMethod(invocation));
  }
  
  @Test
  public void testGetLocation() {
    Location location = mock(Location.class);
    when(invocation.getLocation()).thenReturn(location);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    assertLocationEquals(location, invocationMatcher.getLocation());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testCaptureArgumentsFrom_WhenMethodVarArgs_ThrowsUnsupportedOperationException() {
    Method method = mock(Method.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.isVarArgs()).thenReturn(true);
    
    invocationMatcher = new InvocationMatcher(invocation);
    
    invocationMatcher.captureArgumentsFrom(invocation);
  }
  
  @Test
  public void testCaptureArgumentsFrom_WhenMatcherCapturesArguments_CapturesArguments() {
    Matcher matcher1 = mock(Matcher.class);
    Matcher matcher2 = mock(Matcher.class);
    List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
    when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
    when(invocation.getArgumentAt(1, Object.class)).thenReturn("arg2");
    
    invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    invocationMatcher.captureArgumentsFrom(invocation);
    
    verify((CapturesArguments) matcher1).captureFrom("arg1");
    verify((CapturesArguments) matcher2).captureFrom("arg2");
  }
  
  @Test
  public void testCreateFrom() {
    List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
    
    List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
    
    assertEquals(invocations.size(), invocationMatchers.size());
    for (int i = 0; i < invocations.size(); i++) {
      Invocation invocation = invocations.get(i);
      InvocationMatcher invocationMatcher = invocationMatchers.get(i);
      assertInvocationEquals(invocation, invocationMatcher.getInvocation());
    }
  }
  
  private void assertMethodEquals(Method expected, Method actual) {
    assertEquals(expected, actual);
  }
  
  private void assertInvocationEquals(Invocation expected, Invocation actual) {
    assertEquals(expected, actual);
  }
  
  private void assertMatchersEquals(List<Matcher> expected, List<Matcher> actual) {
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      Matcher expectedMatcher = expected.get(i);
      Matcher actualMatcher = actual.get(i);
      assertEquals(expectedMatcher, actualMatcher);
    }
  }
  
  private void assertLocationEquals(Location expected, Location actual) {
    assertEquals(expected, actual);
  }

}