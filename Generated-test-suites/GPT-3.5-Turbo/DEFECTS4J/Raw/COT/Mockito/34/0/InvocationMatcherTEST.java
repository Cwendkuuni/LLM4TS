import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.*;
import org.hamcrest.Matcher;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;

public class InvocationMatcherTest {

  @Test
  public void testConstructor_Invocation_List() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    assertNotNull(invocationMatcher);
  }

  @Test
  public void testConstructor_Invocation() {
    Invocation invocation = new Invocation();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    assertNotNull(invocationMatcher);
  }

  @Test
  public void testGetMethod() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    Method method = invocation.getMethod();
    assertNotNull(method);
  }

  @Test
  public void testGetInvocation() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    Invocation result = invocationMatcher.getInvocation();
    assertEquals(invocation, result);
  }

  @Test
  public void testGetMatchers() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    List<Matcher> result = invocationMatcher.getMatchers();
    assertEquals(matchers, result);
  }

  @Test
  public void testToString() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    String result = invocationMatcher.toString();
    assertNotNull(result);
  }

  @Test
  public void testMatches() {
    Invocation invocation = new Invocation();
    Invocation actual = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    boolean result = invocationMatcher.matches(actual);
    assertTrue(result);
  }

  @Test
  public void testSafelyArgumentsMatch() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    Object[] actualArgs = new Object[]{};
    boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
    assertFalse(result);
  }

  @Test
  public void testHasSimilarMethod() {
    Invocation invocation = new Invocation();
    Invocation candidate = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    assertTrue(result);
  }

  @Test
  public void testHasSameMethod() {
    Invocation invocation = new Invocation();
    Invocation candidate = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    boolean result = invocationMatcher.hasSameMethod(candidate);
    assertFalse(result);
  }

  @Test
  public void testGetLocation() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    Location result = invocationMatcher.getLocation();
    assertNotNull(result);
  }

  @Test
  public void testToString_PrintSettings() {
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    PrintSettings printSettings = new PrintSettings();
    String result = invocationMatcher.toString(printSettings);
    assertNotNull(result);
  }

  @Test
  public void testCaptureArgumentsFrom() {
    Invocation i = new Invocation();
    Invocation invocation = new Invocation();
    List<Matcher> matchers = new ArrayList<>();
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    invocationMatcher.captureArgumentsFrom(i);
  }
}