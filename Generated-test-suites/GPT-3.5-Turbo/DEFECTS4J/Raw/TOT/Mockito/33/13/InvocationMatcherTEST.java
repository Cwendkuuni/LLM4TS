import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    // test cases for constructor with matchers list
    @Test
    public void testConstructorWithMatchersList() {
        Invocation invocation = createDummyInvocation();
        List<Matcher<?>> matchers = createDummyMatchers();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithEmptyMatchersList() {
        Invocation invocation = createDummyInvocation();
        List<Matcher<?>> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    // test cases for constructor without matchers list
    @Test
    public void testConstructorWithoutMatchersList() {
        Invocation invocation = createDummyInvocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    // test cases for getMethod method
    @Test
    public void testGetMethod() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocation.getMethod();
        Method methodFromMatcher = invocationMatcher.getMethod();
        
        assertEquals(method, methodFromMatcher);
    }
    
    // test cases for getInvocation method
    @Test
    public void testGetInvocation() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation returnedInvocation = invocationMatcher.getInvocation();
        
        assertEquals(invocation, returnedInvocation);
    }

    // test cases for getMatchers method
    @Test
    public void testGetMatchers() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher<?>> returnedMatchers = invocationMatcher.getMatchers();

        assertEquals(invocation.argumentsToMatchers(), returnedMatchers);
    }

    // test cases for toString method
    @Test
    public void testToString() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedString = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        String returnedString = invocationMatcher.toString();

        assertEquals(expectedString, returnedString);
    }

    // test cases for matches method
    @Test
    public void testMatchesWithEqualInvocations() {
        Invocation invocation = createDummyInvocation();
        Invocation actualInvocation = createDummyInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithDifferentMocks() {
        Invocation invocation = createDummyInvocation();
        Invocation actualInvocation = createDummyInvocationWithDifferentMock();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        Invocation invocation = createDummyInvocation();
        Invocation actualInvocation = createDummyInvocationWithDifferentMethod();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation invocation = createDummyInvocation();
        Invocation actualInvocation = createDummyInvocationWithDifferentArguments();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    // test cases for hasSimilarMethod method
    @Test
    public void testHasSimilarMethodWithEqualInvocations() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocationWithDifferentMethodName();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethodWithVerifiedInvocation() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocation();
        candidateInvocation.markVerified();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMocks() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocationWithDifferentMock();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodSignatureButOverloaded() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocationWithSameMethodSignatureButOverloaded();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    // test cases for hasSameMethod method
    @Test
    public void testHasSameMethodWithEqualInvocations() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodNames() {
        Invocation invocation = createDummyInvocation();
        Invocation candidateInvocation = createDummyInvocationWithDifferentMethodName();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    // test cases for getLocation method
    @Test
    public void testGetLocation() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location location = invocation.getLocation();
        Location locationFromMatcher = invocationMatcher.getLocation();

        assertEquals(location, locationFromMatcher);
    }

    // test cases for toString(PrintSettings printSettings) method
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        String expectedString = invocation.toString(invocation.argumentsToMatchers(), printSettings);
        String returnedString = invocationMatcher.toString(printSettings);

        assertEquals(expectedString, returnedString);
    }

    // test cases for captureArgumentsFrom(Invocation i) method
    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = createDummyInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation capturedInvocation = createDummyInvocation();

        invocationMatcher.captureArgumentsFrom(capturedInvocation);

        // Verify that each matcher has captured the correct argument
        verifyMatcherCaptureArguments(invocationMatcher.getMatchers(), capturedInvocation.getArguments());
    }

    // test cases for createFrom(List<Invocation> invocations) method
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = createDummyInvocationsList();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());

        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }

    /**
     * Helper method to create a dummy Invocation object
     */
    private Invocation createDummyInvocation() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {1, "test", true};

        return InvocationFactory.createInvocation(mock, method, arguments);
    }

    /**
     * Helper method to create a dummy Invocation object with a different mock
     */
    private Invocation createDummyInvocationWithDifferentMock() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {1, "test", true};

        return InvocationFactory.createInvocation(mock(Object.class), method, arguments);
    }

    /**
     * Helper method to create a dummy Invocation object with a different method
     */
    private Invocation createDummyInvocationWithDifferentMethod() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {1, "test", true};

        return InvocationFactory.createInvocation(mock, new Method("differentMethod", Object.class, null, null), arguments);
    }

    /**
     * Helper method to create a dummy Invocation object with different arguments
     */
    private Invocation createDummyInvocationWithDifferentArguments() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {2, "different", false};

        return InvocationFactory.createInvocation(mock, method, arguments);
    }

    /**
     * Helper method to create a dummy Invocation object with a different method name
     */
    private Invocation createDummyInvocationWithDifferentMethodName() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {1, "test", true};

        return InvocationFactory.createInvocation(mock, new Method("differentMethodName", Object.class, null, null), arguments);
    }

    /**
     * Helper method to create a dummy Invocation object with the same method signature but overloaded
     */
    private Invocation createDummyInvocationWithSameMethodSignatureButOverloaded() {
        Method method = new Method("dummyMethod", Object.class, null, null);
        Object mock = mock(Object.class);
        Object[] arguments = {1, "test", true};

        return InvocationFactory.createInvocation(mock, new Method("dummyMethod", Object.class, new Class[]{int.class, String.class, boolean.class}, null), arguments);
    }

    /**
     * Helper method to create a list of dummy Invocation objects
     */
    private List<Invocation> createDummyInvocationsList() {
        List<Invocation> invocations = new ArrayList<>();

        invocations.add(createDummyInvocation());
        invocations.add(createDummyInvocationWithDifferentMock());
        invocations.add(createDummyInvocationWithDifferentMethod());

        return invocations;
    }

    /**
     * Helper method to create a list of dummy Matchers objects
     */
    private List<Matcher<?>> createDummyMatchers() {
        List<Matcher<?>> matchers = new ArrayList<>();

        matchers.add(anyInt());
        matchers.add(eq("test"));
        matchers.add(anyBoolean());

        return matchers;
    }
    
    /**
     * Helper method to verify that each matcher has captured the correct argument
     */
    private void verifyMatcherCaptureArguments(List<Matcher<?>> matchers, Object[] arguments) {
        for (int i = 0; i < matchers.size(); i++) {
            if (matchers.get(i) instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matchers.get(i);
                verify(capturesArguments).captureFrom(arguments[i]);
            }
        }
    }

}