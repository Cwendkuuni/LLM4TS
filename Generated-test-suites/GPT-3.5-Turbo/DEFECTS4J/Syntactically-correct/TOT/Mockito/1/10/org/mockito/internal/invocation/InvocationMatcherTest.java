
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        invocation = createMockInvocation();
        matchers = createMatchers();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_withInvocationsAndMatchers_shouldSetInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withInvocationOnly_shouldSetInvocationAndDefaultMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod_shouldReturnMethodFromInvocation() {
        Method expectedMethod = invocation.getMethod();
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testToString_shouldReturnPrintedSettings() {
        String expectedString = "printSettings";
        PrintSettings spyPrintSettings = Mockito.spy(new PrintSettings());

        Mockito.when(spyPrintSettings.print(Mockito.anyList(), Mockito.any())).thenReturn(expectedString);
        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
        Mockito.verify(spyPrintSettings).print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        Invocation matchingInvocation = createMockInvocation();
        boolean result = invocationMatcher.matches(matchingInvocation);
        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingInvocation_shouldReturnFalse() {
        Invocation nonMatchingInvocation = createMockInvocation();
        nonMatchingInvocation.setMethod("nonMatchingMethod");
        boolean result = invocationMatcher.matches(nonMatchingInvocation);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSimilarMethodAndUnverifiedInvocation_shouldReturnTrue() {
        Invocation similarInvocation = createMockInvocation();
        boolean result = invocationMatcher.hasSimilarMethod(similarInvocation);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        Invocation differentNameInvocation = createMockInvocation();
        differentNameInvocation.setMethod("differentMethodName");
        boolean result = invocationMatcher.hasSimilarMethod(differentNameInvocation);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedInvocation_shouldReturnFalse() {
        Invocation verifiedInvocation = createMockInvocation();
        verifiedInvocation.setVerified(true);
        boolean result = invocationMatcher.hasSimilarMethod(verifiedInvocation);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        Invocation differentMockInvocation = createMockInvocation();
        differentMockInvocation.setMock("differentMock");
        boolean result = invocationMatcher.hasSimilarMethod(differentMockInvocation);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodAndSameArgs_shouldReturnFalse() {
        Invocation sameMethodAndArgsInvocation = createMockInvocation();
        sameMethodAndArgsInvocation.setArguments(invocation.getArguments());
        boolean result = invocationMatcher.hasSimilarMethod(sameMethodAndArgsInvocation);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withSameMethodAndOverloadedArgs_shouldReturnTrue() {
        Invocation sameMethodOverloadedArgsInvocation = createMockInvocation();
        sameMethodOverloadedArgsInvocation.setArguments(createArgsOverloaded());
        boolean result = invocationMatcher.hasSimilarMethod(sameMethodOverloadedArgsInvocation);
        assertTrue(result);
    }

    @Test
    public void testGetLocation_shouldReturnLocationFromInvocation() {
        Location expectedLocation = invocation.getLocation();
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_withVarArgsInvocation_shouldThrowUnsupportedOperationException() {
        Invocation varArgsInvocation = createMockInvocation();
        varArgsInvocation.getMethod().setVarArgs(true);

        try {
            invocationMatcher.captureArgumentsFrom(varArgsInvocation);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Expected UnsupportedOperationException to be thrown, got " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testCaptureArgumentsFrom_withNoMatchers_shouldNotCaptureArguments() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testCaptureArgumentsFrom_withMatchers_shouldCaptureArguments() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher mockMatcher = createMockMatcher();
        invocationMatcher.getMatchers().add(mockMatcher);

        invocationMatcher.captureArgumentsFrom(invocation);
        
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(mockMatcher).captureFrom(captor.capture());

        assertEquals(invocation.getArgumentAt(0, Object.class), captor.getValue());
    }

    private Invocation createMockInvocation() {
        Invocation invocation = new Invocation();
        invocation.setMethod("testMethod");
        invocation.setArguments(createArgs());
        invocation.setMock("testMock");
        invocation.setLocation(createLocation());
        return invocation;
    }

    private List<Object> createArgs() {
        List<Object> args = new ArrayList<>();
        args.add("arg1");
        args.add(2);
        args.add(true);
        return args;
    }

    private List<Object> createArgsOverloaded() {
        List<Object> args = new ArrayList<>();
        args.add("arg1");
        args.add(2);
        args.add(true);
        args.add('a');
        return args;
    }

    private List<Matcher> createMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMockMatcher());
        return matchers;
    }

    private Matcher createMockMatcher() {
        return Mockito.mock(Matcher.class);
    }

    private Location createLocation() {
        Location location = new Location();
        location.setFileName("testFile");
        location.setLine(1);
        location.setColumn(2);
        return location;
    }
}
