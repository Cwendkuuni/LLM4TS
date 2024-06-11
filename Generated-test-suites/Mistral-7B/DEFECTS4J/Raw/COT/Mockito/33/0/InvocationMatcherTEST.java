import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.PrintableInvocation;
import org.mockito.internal.invocation.PrintingFriendlyInvocation;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();

        // Basic functionality test
        assert invocationMatcher.getMethod().equals(method);
        assert invocationMatcher.getInvocation().equals(invocation);
        assert invocationMatcher.getMatchers().isEmpty();
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(CapturesArguments.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();

        // Basic functionality test
        assert invocationMatcher.getMethod().equals(method);
        assert invocationMatcher.getInvocation().equals(invocation);
        assert invocationMatcher.getMatchers().equals(matchers);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assert invocationMatcher.getLocation().equals(location);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(CapturesArguments.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString(printSettings);

        // Basic functionality test
        assert result != null;
        verify(printSettings, never()).printError(anyString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));

        // Basic functionality test
        assert !invocationMatcher1.matches(invocationMatcher2);

        // Edge case: same mock, same method, same arguments
        Object[] arguments = new Object[0];
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation2.getArguments()).thenReturn(arguments);

        // Test
        assert invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test
        assert invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName2");

        // Basic functionality test
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same method name, same mock, unverified
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName1");
        assert invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method name, different method
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName2");
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, different arguments
        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[1];
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, same arguments but different order
        Object[] arguments3 = new Object[1];
        Object[] arguments4 = new Object[1];
        System.arraycopy(arguments1, 0, arguments3, 0, arguments1.length);
        System.arraycopy(arguments2, arguments2.length - 1, arguments4, 0, arguments2.length);
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments4);
        assert invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, same arguments but different classes
        Object arg1 = mock(String.class);
        Object arg2 = mock(Integer.class);
        Object[] arguments5 = new Object[]{arg1, arg2};
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments5);
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, same arguments but different types
        when(invocation1.getArguments()[0].getClass()).thenReturn(String.class);
        when(invocation2.getArguments()[0].getClass()).thenReturn(Integer.class);
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, same arguments but different primitives
        int intArg1 = 1;
        int intArg2 = 2;
        when(invocation1.getArguments()[0]).thenReturn(intArg1);
        when(invocation2.getArguments()[0]).thenReturn(intArg2);
        assert !invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Edge case: same mock, same method, same object arguments but different order
        Object objectArg1 = mock(Object.class);
        Object objectArg2 = mock(Object.class);
        when(invocation1.getArguments()[0]).thenReturn(objectArg1);
        when(invocation2.getArguments()[1]).thenReturn(objectArg1);
        assert invocationMatcher1.hasSimilarMethod(invocationMatcher2);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // Basic functionality test
        assert invocationMatcher1.hasSameMethod(invocationMatcher2);

        // Edge case: different methods
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName2");
        assert false;

        // Edge case: same method, different classes
        when(method1.getDeclaringClass()).thenReturn(String.class);
        when(method2.getDeclaringClass()).thenReturn(Integer.class);
        assert false;

        // Edge case: same method, different return types
        when(method1.getReturnType()).thenReturn(int.class);
        when(method2.getReturnType()).thenReturn(String.class);
        assert false;

        // Edge case: same method, different exception types
        when(method1.getExceptionTypes()).thenReturn(new Class[]{NullPointerException.class});
        when(method2.getExceptionTypes()).thenReturn(new Class[]{IllegalArgumentException.class});
        assert false;
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assert invocationMatchers.size() == 2;
        assert invocationMatchers.get(0) instanceof InvocationMatcher;
        assert invocationMatchers.get(1) instanceof InvocationMatcher;
    }
}