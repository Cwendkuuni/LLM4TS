
import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

   @Test
   public void testGetMethod() {
      InvocationMatcher invocationMatcher = new InvocationMatcher(null);
      Method expected = null;
      
      Method actual = invocationMatcher.getMethod();
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testGetInvocation() {
      Invocation expected = null;
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      Invocation actual = invocationMatcher.getInvocation();
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testGetMatchers() {
      List<Matcher> expected = Collections.emptyList();
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      List<Matcher> actual = invocationMatcher.getMatchers();
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testToString() {
      String expected = "printSettings";
      InvocationMatcher invocationMatcher = new InvocationMatcher(null);
      
      String actual = invocationMatcher.toString();
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testMatches() {
      Invocation invocation = mock(Invocation.class);
      when(invocation.getMock()).thenReturn("mock");
      when(invocation.getMethod()).thenReturn(null);
      when(invocation.getArguments()).thenReturn(null);
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      boolean actual = invocationMatcher.matches(invocation);
      
      assertTrue(actual);
   }
   
   @Test
   public void testHasSimilarMethod() {
      Invocation invocation = mock(Invocation.class);
      when(invocation.isVerified()).thenReturn(false);
      when(invocation.getMethod()).thenReturn(null);
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      boolean actual = invocationMatcher.hasSimilarMethod(invocation);
      
      assertTrue(actual);
   }
   
   @Test
   public void testHasSameMethod() {
      Invocation invocation = mock(Invocation.class);
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      boolean actual = invocationMatcher.hasSameMethod(invocation);
      
      assertTrue(actual);
   }
   
   @Test
   public void testGetLocation() {
      Location expected = null;
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      Location actual = invocationMatcher.getLocation();
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testCaptureArgumentsFrom() {
      Invocation invocation = mock(Invocation.class);
      when(invocation.getMethod()).thenReturn(null);
      InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
      
      invocationMatcher.captureArgumentsFrom(invocation);
      
      // Verify expected behavior
      
   }
   
   @Test
   public void testCreateFrom() {
      List<Invocation> invocations = Collections.emptyList();
      
      List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
      
      assertNotNull(actual);
   }
}
