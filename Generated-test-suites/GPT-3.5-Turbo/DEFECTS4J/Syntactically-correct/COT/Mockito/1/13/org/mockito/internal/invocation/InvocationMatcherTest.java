import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.mock.MockCreationSettings;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(equalTo("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Collections.singletonList(equalTo("test"));
        Invocation invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        MockCreationSettings mockSettings = mock(MockCreationSettings.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getLocation()).thenReturn(location);
        when(invocation.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.isSerializable()).thenReturn(true);
        invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn("mock");
        when(invocation2.getMock()).thenReturn("mock");
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3});

        invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn("mock");
        when(invocation2.getMock()).thenReturn("mock");
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3});

        invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");

        invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Object[] args = {"arg1", "arg2"};
        when(invocation.getRawArguments()).thenReturn(args);
        invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = List.of(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertFalse(invocationMatchers.isEmpty());
    }
}