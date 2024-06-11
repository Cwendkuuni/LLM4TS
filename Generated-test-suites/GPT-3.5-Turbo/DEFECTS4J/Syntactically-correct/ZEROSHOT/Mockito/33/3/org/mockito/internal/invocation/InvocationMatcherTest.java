import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invMatcher;
    private ArgumentCaptor[] captors;

    @Before
    public void setUp() throws Exception {
        invocation = new Invocation();
        matchers = new LinkedList<Matcher>();
        invMatcher = new InvocationMatcher(invocation, matchers);
        captors = null;
    }

    @Test
    public void testConstructor1() {
        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(matchers, invMatcher.getMatchers());
    }

    @Test
    public void testConstructor2() {
        invMatcher = new InvocationMatcher(invocation);
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(expectedMatchers, invMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = new Method();
        invocation.setMethod(expectedMethod);
        assertEquals(expectedMethod, invMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expectedString = invocation.toString(matchers, new PrintSettings());
        assertEquals(expectedString, invMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation();
        assertFalse(invMatcher.matches(actual));

        invocation.setMock(actual.getMock());
        assertFalse(invMatcher.matches(actual));

        Method method = new Method();
        invocation.setMethod(method);
        assertFalse(invMatcher.matches(actual));

        actual.setMock(invocation.getMock());
        assertFalse(invMatcher.matches(actual));

        actual.setMethod(invocation.getMethod());
        assertFalse(invMatcher.matches(actual));

        captors = new ArgumentCaptor[2];
        for (int i = 0; i < 2; i++) {
            captors[i] = ArgumentCaptor.forClass(String.class);
        }
        invMatcher.captureArgumentsFrom(actual);
        assertFalse(invMatcher.matches(actual));

        captors[0].setValue("test");
        invMatcher.captureArgumentsFrom(actual);
        assertFalse(invMatcher.matches(actual));

        captors[1].setValue("test");
        invMatcher.captureArgumentsFrom(actual);
        assertTrue(invMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        assertFalse(invMatcher.hasSimilarMethod(candidate));

        invocation.setMethod(new Method());
        assertFalse(invMatcher.hasSimilarMethod(candidate));

        candidate.setMethod(invocation.getMethod());
        assertFalse(invMatcher.hasSimilarMethod(candidate));

        invocation.setMock(candidate.getMock());
        assertFalse(invMatcher.hasSimilarMethod(candidate));

        candidate.setMock(invocation.getMock());
        assertFalse(invMatcher.hasSimilarMethod(candidate));

        invMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invMatcher.hasSimilarMethod(candidate));

        Method method = new Method();
        candidate.setMethod(method);
        assertTrue(invMatcher.hasSimilarMethod(candidate));

        matchers.add(Matchers.anyString());
        assertFalse(invMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation();
        assertFalse(invMatcher.hasSameMethod(candidate));

        invocation.setMethod(new Method());
        assertFalse(invMatcher.hasSameMethod(candidate));

        candidate.setMethod(invocation.getMethod());
        assertTrue(invMatcher.hasSameMethod(candidate));

        invocation.setMock(candidate.getMock());
        assertFalse(invMatcher.hasSameMethod(candidate));

        candidate.setMock(invocation.getMock());
        assertFalse(invMatcher.hasSameMethod(candidate));

        invMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invMatcher.hasSameMethod(candidate));

        Method method = new Method();
        candidate.setMethod(method);
        assertFalse(invMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = new Location();
        invocation.setLocation(expectedLocation);
        assertEquals(expectedLocation, invMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedString = invocation.toString(matchers, printSettings);
        assertEquals(expectedString, invMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        assertNull(captors);
        invMatcher.captureArgumentsFrom(i);
        assertNull(captors);

        MatchArg<String> captor0 = new MatchArg<String>();
        MatchArg<String> captor1 = new MatchArg<String>();
        matchers.add(captor0);
        i.setArguments(new String[] {});
        captors = new ArgumentCaptor[2];
        captors[0] = captor0;
        captors[1] = captor1;
        invMatcher.captureArgumentsFrom(i);
        assertNull(captor0.getValue());
        assertNull(captor1.getValue());

        matchers.add(captor1);
        String arg0 = "test0";
        i.setArguments(new String[] { arg0 });
        invMatcher.captureArgumentsFrom(i);
        assertEquals(arg0, captor0.getValue());
        assertNull(captor1.getValue());

        String arg1 = "test1";
        i.setArguments(new String[] { arg0, arg1 });
        invMatcher.captureArgumentsFrom(i);
        assertEquals(arg0, captor0.getValue());
        assertEquals(arg1, captor1.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        Invocation i1 = new Invocation();
        Invocation i2 = new Invocation();
        invocations.add(i1);
        invocations.add(i2);

        List<InvocationMatcher> expectedMatchers = new LinkedList<>();
        expectedMatchers.add(new InvocationMatcher(i1));
        expectedMatchers.add(new InvocationMatcher(i2));

        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedMatchers.size(), actualMatchers.size());
        for (int i = 0; i < expectedMatchers.size(); i++) {
            InvocationMatcher expectedMatcher = expectedMatchers.get(i);
            InvocationMatcher actualMatcher = actualMatchers.get(i);
            assertEquals(expectedMatcher.getInvocation(), actualMatcher.getInvocation());
            assertEquals(expectedMatcher.getMatchers(), actualMatcher.getMatchers());
        }
    }
}