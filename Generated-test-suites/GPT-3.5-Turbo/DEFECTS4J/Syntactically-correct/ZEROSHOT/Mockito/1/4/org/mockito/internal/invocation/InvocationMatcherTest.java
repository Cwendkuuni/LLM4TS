import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        // Create matchers list
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));

        // Create InvocationMatcher object using constructor
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Validate the invocation and matchers are correctly set
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorNoMatchers() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        // Create InvocationMatcher object using constructor without matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Validate that matchers list is created from invocation arguments
        List<Matcher> argumentsMatchers = Collections.emptyList();
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(argumentsMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Validate the correct method is returned
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Validate that toString is called and PrintSettings.print is used
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        when(printSettings.print(anyList(), any(Invocation.class))).thenReturn("test");
        assertEquals("test", invocationMatcher.toString());
    }

    // Additional test cases for other methods can be written similarly

}