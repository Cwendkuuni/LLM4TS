import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {
  
  @Test
  public void testGetMethod() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    Method method = invocationMatcher.getMethod();
    
    // Assertion
    //...
  }
  
  @Test
  public void testGetInvocation() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    Invocation returnedInvocation = invocationMatcher.getInvocation();
    
    // Assertion
    //...
  }
  
  @Test
  public void testGetMatchers() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    List<Matcher> matchers = invocationMatcher.getMatchers();
    
    // Assertion
    //...
  }
  
  @Test
  public void testToString() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    String result = invocationMatcher.toString();
    
    // Assertion
    //...
  }
  
  @Test
  public void testMatches() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    Invocation actual = new Invocation(null, null, null);
    
    boolean result = invocationMatcher.matches(actual);
    
    // Assertion
    //...
  }
  
  @Test
  public void testHasSimilarMethod() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    Invocation candidate = new Invocation(null, null, null);
    
    boolean result = invocationMatcher.hasSimilarMethod(candidate);
    
    // Assertion
    //...
  }
  
  @Test
  public void testHasSameMethod() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    Invocation candidate = new Invocation(null, null, null);
    
    boolean result = invocationMatcher.hasSameMethod(candidate);
    
    // Assertion
    //...
  }
  
  @Test
  public void testGetLocation() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    
    Location location = invocationMatcher.getLocation();
    
    // Assertion
    //...
  }
  
  @Test
  public void testToStringPrintSettings() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    PrintSettings printSettings = new PrintSettings();
    
    String result = invocationMatcher.toString(printSettings);
    
    // Assertion
    //...
  }
  
  @Test
  public void testCaptureArgumentsFrom() {
    Invocation invocation = new Invocation(null, null, null);
    InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
    Invocation i = new Invocation(null, null, null);
    
    invocationMatcher.captureArgumentsFrom(i);
    
    // Assertion
    //...
  }
  
  @Test
  public void testCreateFrom() {
    List<Invocation> invocations = new LinkedList<Invocation>();
    invocations.add(new Invocation(null, null, null));
    
    List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
    
    // Assertion
    //...
  }
}