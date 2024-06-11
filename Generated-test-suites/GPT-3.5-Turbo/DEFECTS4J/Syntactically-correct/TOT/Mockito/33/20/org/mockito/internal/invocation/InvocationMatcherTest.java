import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertEquals(Foo.class.getMethod("bar", int.class), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertNotNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = "org.mockito.internal.invocation.InvocationMatcherTest$InvocationMatcher@12345";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        Invocation invocation2 = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("baz", int.class), new Object[]{10});
        assertFalse(invocationMatcher.hasSimilarMethod(invocation1));
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        assertNull(invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        assertEquals("org.mockito.internal.invocation.InvocationMatcherTest$InvocationMatcher@12345", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("bar", int.class), new Object[]{5});
        Invocation invocation2 = new Invocation(MockCreator.createMock(Foo.class), Foo.class.getMethod("baz", int.class), new Object[]{10});
        LinkedList<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        assertEquals(invocations.size(), InvocationMatcher.createFrom(invocations).size());
    }
}