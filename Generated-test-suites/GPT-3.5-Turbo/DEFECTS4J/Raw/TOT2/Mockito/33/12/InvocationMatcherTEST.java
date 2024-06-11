import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    // Test case for the first constructor of InvocationMatcher
    @Test
    public void testInvocationMatcherFirstConstructor() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);

        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Test case for the second constructor of InvocationMatcher
    @Test
    public void testInvocationMatcherSecondConstructor() {
        Invocation invocationMock = mock(Invocation.class);
        Matcher mockMatcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Collections.singletonList(mockMatcher));

        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(1, invocationMatcher.getMatchers().size());
        assertEquals(mockMatcher, invocationMatcher.getMatchers().get(0));
    }

    // Edge case test for hasSameMethod method
    @Test
    public void testHasSameMethod() {
        Method method1 = InvocationMatcher.class.getDeclaredMethod("hasSameMethod", Invocation.class);
        Method method2 = InvocationMatcher.class.getDeclaredMethod("hasSameMethod", Invocation.class);

        Invocation invocation1 = new Invocation(method1, null, null);
        Invocation invocation2 = new Invocation(method2, null, null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    // Exception handling test for safelyArgumentsMatch method
     @Test
     public void testSafelyArgumentsMatchExceptionHandling() {
         Method method = InvocationMatcher.class.getDeclaredMethod("safelyArgumentsMatch", Object[].class);
 
         Invocation invocation = new Invocation(method, null, null);
         InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 
         assertFalse(invocationMatcher.safelyArgumentsMatch(null));
     }

    // Test case for createFrom method
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        List<Invocation> invocationsList = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchersList = InvocationMatcher.createFrom(invocationsList);

        assertEquals(2, invocationMatchersList.size());
        assertEquals(invocation1, invocationMatchersList.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchersList.get(1).getInvocation());
    }
}