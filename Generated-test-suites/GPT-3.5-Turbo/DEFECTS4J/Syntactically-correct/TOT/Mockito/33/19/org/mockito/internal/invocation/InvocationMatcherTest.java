import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

  @Test
  public void testInvocationMatcher_withInvocationAndMatchers_shouldSetInvocationAndMatchers() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
    
    // Act
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    // Assert
    assertSame(invocation, invocationMatcher.getInvocation());
    assertSame(matchers, invocationMatcher.getMatchers());
  }
  
  @Test
  public void testInvocationMatcher_withInvocation_shouldSetInvocationAndEmptyMatchers() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    
    // Act
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Assert
    assertSame(invocation, invocationMatcher.getInvocation());
    assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
  }
  
  @Test
  public void testGetMethod_shouldReturnMethodFromInvocation() throws NoSuchMethodException {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    Method expectedMethod = InvocationMatcher.class.getMethod("getMethod");
    when(invocation.getMethod()).thenReturn(expectedMethod);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    Method actualMethod = invocationMatcher.getMethod();
    
    // Assert
    assertSame(expectedMethod, actualMethod);
  }
  
  @Test
  public void testMatches_withMatchingInvocation_shouldReturnTrue() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMock()).thenReturn(mock(Object.class));
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("matches", Invocation.class));
    when(invocation.argumentsToMatchers()).thenReturn(Collections.emptyList());
    when(invocation.isVerified()).thenReturn(false);
    
    Invocation actual = new Invocation(invocation.getMock(), InvocationMatcher.class.getMethod("matches", Invocation.class), new Object[0]);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.matches(actual);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testSafelyArgumentsMatch_withMatchingArguments_shouldReturnTrue() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.argumentsMatch(any(), any())).thenReturn(true);
    Object[] actualArgs = new Object[] { "arg1", 2, true };
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testHasSimilarMethod_withMatchingCandidate_shouldReturnTrue() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(invocation.getMock()).thenReturn(mock(Object.class));
    when(invocation.isVerified()).thenReturn(false);
    when(invocation.argumentsMatch(any(), any())).thenReturn(true);
    
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(candidate.getMock()).thenReturn(invocation.getMock());
    when(candidate.isVerified()).thenReturn(false);
    when(candidate.getArguments()).thenReturn(new Object[] { "arg1", 2, true });
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testHasSimilarMethod_withDifferentMethodNames_shouldReturnFalse() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod()).thenReturn(PrintingFriendlyInvocation.class.getMethod("getLocation"));
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testHasSimilarMethod_withAlreadyVerifiedCandidate_shouldReturnFalse() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(candidate.isVerified()).thenReturn(true);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testHasSimilarMethod_withDifferentMocks_shouldReturnFalse() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(candidate.getMock()).thenReturn(mock(Object.class));
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testHasSimilarMethod_withSameMethodAndArguments_shouldReturnFalse() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(invocation.getMock()).thenReturn(mock(Object.class));
    when(invocation.isVerified()).thenReturn(false);
    when(invocation.argumentsMatch(any(), any())).thenReturn(true);
    
    Invocation candidate = mock(Invocation.class);
    when(candidate.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
    when(candidate.getMock()).thenReturn(invocation.getMock());
    when(candidate.isVerified()).thenReturn(false);
    when(candidate.getArguments()).thenReturn(new Object[] { "arg1", 2, true });
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testHasSameMethod_withEqualMethods_shouldReturnTrue() throws NoSuchMethodException {
    // Arrange
    Method m1 = InvocationMatcher.class.getMethod("getMethod");
    Method m2 = InvocationMatcher.class.getMethod("getMethod");
    
    Invocation invocation = mock(Invocation.class);
    Invocation candidate = mock(Invocation.class);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    boolean result = invocationMatcher.hasSameMethod(candidate);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testGetLocation_shouldReturnLocationFromInvocation() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    Location expectedLocation = mock(Location.class);
    when(invocation.getLocation()).thenReturn(expectedLocation);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    // Act
    Location actualLocation = invocationMatcher.getLocation();
    
    // Assert
    assertSame(expectedLocation, actualLocation);
  }
  
  @Test
  public void testToString_withPrintSettings_shouldReturnStringFromInvocationWithMatchersAndPrintSettings() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
    PrintSettings printSettings = mock(PrintSettings.class);
    String expectedString = "test invocation";
    when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    // Act
    String actualString = invocationMatcher.toString(printSettings);
    
    // Assert
    assertEquals(expectedString, actualString);
  }
  
  @Test
  public void testCaptureArgumentsFrom_withCapturesArgumentsMatcher_shouldCaptureArgumentsFromInvocation() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    CapturesArguments capturesArgumentsMatcher1 = mock(CapturesArguments.class);
    CapturesArguments capturesArgumentsMatcher2 = mock(CapturesArguments.class);
    Matcher matcher1 = mock(Matcher.class);
    Matcher matcher2 = mock(Matcher.class);
    List<Matcher> matchers = Arrays.asList(capturesArgumentsMatcher1, capturesArgumentsMatcher2, matcher1, matcher2);
    
    Object arg1 = "arg1";
    Object arg2 = 2;
    Object arg3 = true;
    Object arg4 = "arg4";
    
    Object[] invocationArgs = new Object[] { arg1, arg2, arg3, arg4 };
    Object[] matcher1Args = new Object[] { arg1 };
    Object[] matcher2Args = new Object[] { arg2, arg3 };
    
    when(invocation.getArguments()).thenReturn(invocationArgs);
    when(capturesArgumentsMatcher1.captureFrom(arg1)).thenReturn(true);
    when(capturesArgumentsMatcher2.captureFrom(arg2)).thenReturn(true);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    Invocation i = mock(Invocation.class);
    when(i.getArguments()).thenReturn(matcher1Args);
    Invocation i2 = mock(Invocation.class);
    when(i2.getArguments()).thenReturn(matcher2Args);
    
    // Act
    invocationMatcher.captureArgumentsFrom(i);
    invocationMatcher.captureArgumentsFrom(i2);
    
    // Assert
    verify(capturesArgumentsMatcher1).captureFrom(arg1);
    verify(capturesArgumentsMatcher2).captureFrom(arg2);
    verify(matcher1, never()).captureFrom(any());
    verify(matcher2, never()).captureFrom(any());
  }
  
  @Test
  public void testCreateFrom_shouldCreateInvocationMatchersFromInvocations() {
    // Arrange
    Invocation i1 = mock(Invocation.class);
    Invocation i2 = mock(Invocation.class);
    List<Invocation> invocations = Arrays.asList(i1, i2);
    
    InvocationMatcher m1 = mock(InvocationMatcher.class);
    InvocationMatcher m2 = mock(InvocationMatcher.class);
    whenNew(InvocationMatcher.class).withArguments(i1).thenReturn(m1);
    whenNew(InvocationMatcher.class).withArguments(i2).thenReturn(m2);
    
    // Act
    List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
    
    // Assert
    assertEquals(Arrays.asList(m1, m2), invocationMatchers);
  }
  
  @Test
  public void testToString_shouldReturnStringFromInvocationWithMatchersAndDefaultPrintSettings() {
    // Arrange
    Invocation invocation = mock(Invocation.class);
    List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
    String expectedString = "test invocation";
    when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);
    
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    // Act
    String actualString = invocationMatcher.toString();
    
    // Assert
    assertEquals(expectedString, actualString);
  }
}