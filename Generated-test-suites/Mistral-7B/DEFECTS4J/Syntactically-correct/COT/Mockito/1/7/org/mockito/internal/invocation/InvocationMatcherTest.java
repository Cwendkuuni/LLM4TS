import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNull(invocationMatcher.getMatchers());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = createMockMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void testMatchesMethodWithSameInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation sameInvocation = createMockInvocation();
        assertTrue(invocationMatcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesMethodWithDifferentInvocationSameMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation differentInvocation = createMockInvocation(new Object(), new Object());
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesMethodWithDifferentInvocationDifferentMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation differentInvocation = createMockInvocation(new Object(), new Object(), new Object());
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargMethod() {
        Invocation invocation = createMockInvocation(new Object[2]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(new InvocationMatcher(createMockInvocation(new Object(), new Object()))));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(new InvocationMatcher(createMockInvocation(new Object(), new Object()))));
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = "InvocationMatcher:\n" +
                "invocation = Invocation [method=public java.lang.String java.lang.String.length()\n" +
                "location = InvocationLocation:\n" +
                " line = 0\n" +
                "file = <not available>\n" +
                "classFile = <not available>\n" +
                ", arguments = [null]]\n" +
                ", matchers = []";

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(), createMockInvocation());
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertNotNull(invocationMatcher.getMatchers());
            assertNotNull(invocationMatcher.getInvocation());
            assertNotNull(invocationMatcher.getMethod());
            assertNotNull(invocationMatcher.getLocation());
        }
    }

    private Invocation createMockInvocation() {
        return createMockInvocation((Object) null);
    }

    private Invocation createMockInvocation(Object... arguments) {
        return createMockInvocation(new Object(), arguments);
    }

    private Invocation createMockInvocation(Object receiver, Object... arguments) {
        Method method = new Method("length", new Class[0]);
        Object mock = mock(DescribedInvocation.class);
        when(mock.getMethod()).thenReturn(method);
        when(mock.getArguments()).thenReturn(arguments == null ? new Object[0] : arguments);
        when(mock.getLocation()).thenReturn(new Location());
        when(mock.getMock()).thenReturn(receiver);
        return (Invocation) mock.getMock();
    }

    private List<Matcher> createMockMatchers() {
        return Collections.singletonList(createMockMatcher());
    }

    private Matcher createMockMatcher() {
        return mock(Matcher.class);
    }
}