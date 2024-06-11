import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

   private InvocationMatcher invocationMatcher;

   @Before
   public void setup() {
      Invocation invocation = mock(Invocation.class);
      List<Matcher> matchers = new ArrayList<Matcher>();

      invocationMatcher = new InvocationMatcher(invocation, matchers);
   }

   @Test
   public void testGetMethod() {
      Method expectedMethod = mock(Method.class);
      when(invocationMatcher.getMethod()).thenReturn(expectedMethod);

      Method actualMethod = invocationMatcher.getMethod();

      assertEquals(expectedMethod, actualMethod);
   }

   @Test
   public void testGetInvocation() {
      Invocation expectedInvocation = mock(Invocation.class);
      when(invocationMatcher.getInvocation()).thenReturn(expectedInvocation);

      Invocation actualInvocation = invocationMatcher.getInvocation();

      assertEquals(expectedInvocation, actualInvocation);
   }

   @Test
   public void testGetMatchers() {
      List<Matcher> expectedMatchers = new ArrayList<Matcher>();
      when(invocationMatcher.getMatchers()).thenReturn(expectedMatchers);

      List<Matcher> actualMatchers = invocationMatcher.getMatchers();

      assertEquals(expectedMatchers, actualMatchers);
   }

   @Test
   public void testToString() {
      String expectedResult = "Mockito Matchers";
      List<Matcher> matchers = new ArrayList<Matcher>();
      matchers.add(mock(Matcher.class));
      Invocation invocation = mock(Invocation.class);
      when(invocationMatcher.getMatchers()).thenReturn(matchers);
      when(invocationMatcher.getInvocation()).thenReturn(invocation);
      when(invocationMatcher.toString()).thenReturn(expectedResult);

      String actualResult = invocationMatcher.toString();

      assertEquals(expectedResult, actualResult);
   }

   @Test
   public void testMatches() {
      Invocation actualInvocation = mock(Invocation.class);
      when(invocationMatcher.getInvocation()).thenReturn(actualInvocation);
      when(invocationMatcher.matches(actualInvocation)).thenReturn(true);

      boolean result = invocationMatcher.matches(actualInvocation);

      assertTrue(result);
   }

   @Test
   public void testHasSimilarMethod() {
      Invocation candidate = mock(Invocation.class);
      when(invocationMatcher.hasSimilarMethod(candidate)).thenReturn(true);

      boolean result = invocationMatcher.hasSimilarMethod(candidate);

      assertTrue(result);
   }

   @Test
   public void testHasSameMethod() {
      Invocation candidate = mock(Invocation.class);
      when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);

      boolean result = invocationMatcher.hasSameMethod(candidate);

      assertTrue(result);
   }

   @Test
   public void testGetLocation() {
      Location expectedLocation = mock(Location.class);
      when(invocationMatcher.getLocation()).thenReturn(expectedLocation);

      Location actualLocation = invocationMatcher.getLocation();

      assertEquals(expectedLocation, actualLocation);
   }

   @Test
   public void testCaptureArgumentsFrom() {
      Invocation invocation = mock(Invocation.class);
      invocationMatcher.captureArgumentsFrom(invocation);

      verify(invocationMatcher).captureArgumentsFrom(invocation);
   }

   @Test
   public void testCreateFrom() {
      List<Invocation> invocations = new ArrayList<Invocation>();
      invocations.add(mock(Invocation.class));
      when(invocationMatcher.createFrom(invocations)).thenReturn(Collections.emptyList());

      List<InvocationMatcher> result = invocationMatcher.createFrom(invocations);

      assertNotNull(result);
   }
}