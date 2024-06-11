import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    private Invocation invocation;

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches_SameMockAndMethod_SameArguments() {
        Invocation actualInvocation = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_DifferentMock() {
        Invocation actualInvocation = new Invocation(new Object(), invocation.getMethod(), invocation.getArguments());
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_DifferentMethod() throws NoSuchMethodException {
        Method differentMethod = invocation.getMethod().getDeclaringClass().getMethod("differentMethod");
        Invocation actualInvocation = new Invocation(invocation.getMock(), differentMethod, invocation.getArguments());
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_SameMockAndMethod_DifferentArguments() {
        Object[] differentArguments = new Object[]{1, 2, 3};
        Invocation actualInvocation = new Invocation(invocation.getMock(), invocation.getMethod(), differentArguments);
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_SameMethodName_SameMock_Unverified() {
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_DifferentMethodName() throws NoSuchMethodException {
        Method differentMethod = invocation.getMethod().getDeclaringClass().getMethod("differentMethod");
        Invocation candidate = new Invocation(invocation.getMock(), differentMethod, invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_Verified() {
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
        candidate.markVerified();
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_DifferentMock() {
        Invocation candidate = new Invocation(new Object(), invocation.getMethod(), invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_OverloadedButSameArguments() {
        Object[] sameArguments = new Object[]{1, 2, 3};
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), sameArguments);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_DifferentArguments() {
        Object[] differentArguments = new Object[]{4, 5, 6};
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), differentArguments);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_NotSameName() throws NoSuchMethodException {
        Method differentMethod = invocation.getMethod().getDeclaringClass().getMethod("differentMethod");
        Invocation candidate = new Invocation(invocation.getMock(), differentMethod, invocation.getArguments());
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_NotSameParameterTypes() throws NoSuchMethodException {
        Method differentMethod = invocation.getMethod().getDeclaringClass().getMethod("differentMethod", int.class);
        Invocation candidate = new Invocation(invocation.getMock(), differentMethod, invocation.getArguments());
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_SameMethod() {
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_VarArgsMethod() throws NoSuchMethodException {
        Object[] arguments = new Object[]{1, 2, 3};
        Method varArgsMethod = invocation.getMethod().getDeclaringClass().getMethod("varArgsMethod", Object[].class);
        Invocation invocation = new Invocation(this.invocation.getMock(), varArgsMethod, arguments);
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify
        // Matcher 1 should capture the first argument (1)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(0)).hasCaptured());
        assertEquals(1, ((CapturesArguments) invocationMatcher.getMatchers().get(0)).getCapturedArgument());

        // Matcher 2 should capture the second argument (2)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(1)).hasCaptured());
        assertEquals(2, ((CapturesArguments) invocationMatcher.getMatchers().get(1)).getCapturedArgument());

        // Matcher 3 should capture the third argument (3)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(2)).hasCaptured());
        assertEquals(3, ((CapturesArguments) invocationMatcher.getMatchers().get(2)).getCapturedArgument());
    }

    @Test
    public void testCaptureArgumentsFrom_NotVarArgsMethod() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new Invocation(this.invocation.getMock(), this.invocation.getMethod(), arguments);
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify
        // Matcher 1 should capture the first argument (1)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(0)).hasCaptured());
        assertEquals(1, ((CapturesArguments) invocationMatcher.getMatchers().get(0)).getCapturedArgument());

        // Matcher 2 should capture the second argument (2)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(1)).hasCaptured());
        assertEquals(2, ((CapturesArguments) invocationMatcher.getMatchers().get(1)).getCapturedArgument());

        // Matcher 3 should capture the third argument (3)
        assertTrue(((CapturesArguments) invocationMatcher.getMatchers().get(2)).hasCaptured());
        assertEquals(3, ((CapturesArguments) invocationMatcher.getMatchers().get(2)).getCapturedArgument());
    }

    @Test
    public void testCreateFrom() {
        LinkedList<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);
        List<InvocationMatcher> expectedInvocationMatchers = new LinkedList<>();
        expectedInvocationMatchers.add(invocationMatcher);

        assertEquals(expectedInvocationMatchers, InvocationMatcher.createFrom(invocations));
    }
}