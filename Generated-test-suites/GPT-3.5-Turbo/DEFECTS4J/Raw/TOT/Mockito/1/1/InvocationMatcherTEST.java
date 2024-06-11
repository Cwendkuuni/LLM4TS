import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        invocation = new Invocation(exampleClass, method, null, null);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testConstructor_withInvocationAndMatchersConstructor_shouldSetInvocationAndMatchers() {
        List<Matcher> matchers = Arrays.asList(new Matcher("matcher1"), new Matcher("matcher2"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor_withInvocationConstructorAndEmptyMatchers_shouldSetInvocationAndMatchersConvertedFromArguments() {
        invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getArguments(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod_shouldReturnMethodFromInvocation() {
        Method expectedMethod = invocation.getMethod();
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation_shouldReturnInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers_shouldReturnMatchersFromInvocationMatcher() {
        List<Matcher> expectedMatchers = Arrays.asList(new Matcher("matcher1"), new Matcher("matcher2"));
        invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString_shouldReturnPrintedStringOfMatchersAndInvocation() {
        String expectedString = "Matchers: matcher1 matcher2\nInvocation: " + invocation.toString();
        
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches_withEqualInvocationsAndMatchedArguments_shouldReturnTrue() {
        Invocation actual = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments(), null);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatches_withDifferentMocks_shouldReturnFalse() {
        Invocation actual = new Invocation(new Mock(), invocation.getMethod(), invocation.getArguments(), null);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatches_withDifferentMethods_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method differentMethod = exampleClass.getMethod("differentMethod");
        Invocation actual = new Invocation(invocation.getMock(), differentMethod, invocation.getArguments(), null);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatches_withDifferentArguments_shouldReturnFalse() {
        Object[] differentArguments = new Object[]{1, 2, 3};
        Invocation actual = new Invocation(invocation.getMock(), invocation.getMethod(), differentArguments, null);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMethodNames_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method differentMethod = exampleClass.getMethod("differentMethod");
        Invocation differentInvocation = new Invocation(exampleClass, differentMethod, null, null);
        
        assertFalse(invocationMatcher.hasSimilarMethod(differentInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodNames_butDifferentMocks_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        Invocation differentInvocation = new Invocation(new Mock(), method, null, null);
        
        assertFalse(invocationMatcher.hasSimilarMethod(differentInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodNames_butVerifiedInvocation_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        Invocation verifiedInvocation = new Invocation(exampleClass, method, null, null);
        verifiedInvocation.markAsVerified();
        
        assertFalse(invocationMatcher.hasSimilarMethod(verifiedInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodNames_sameMock_andUnverifiedInvocation_shouldReturnTrue() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        Invocation unverifiedInvocation = new Invocation(exampleClass, method, null, null);
        
        assertTrue(invocationMatcher.hasSimilarMethod(unverifiedInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodNames_sameMock_andDifferentArguments_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        Object[] differentArguments = new Object[]{1, 2, 3};
        Invocation differentArgumentsInvocation = new Invocation(exampleClass, method, differentArguments, null);
        
        assertFalse(invocationMatcher.hasSimilarMethod(differentArgumentsInvocation));
    }
    
    @Test
    public void testHasSameMethod_withInvocationsWithSameMethodNameAndSameParameterTypes_shouldReturnTrue() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod");
        Invocation sameMethodInvocation = new Invocation(exampleClass, method, null, null);
        
        assertTrue(invocationMatcher.hasSameMethod(sameMethodInvocation));
    }
    
    @Test
    public void testHasSameMethod_withInvocationsWithDifferentMethodName_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method differentMethod = exampleClass.getMethod("differentMethod");
        Invocation differentMethodInvocation = new Invocation(exampleClass, differentMethod, null, null);
        
        assertFalse(invocationMatcher.hasSameMethod(differentMethodInvocation));
    }
    
    @Test
    public void testHasSameMethod_withInvocationsWithDifferentParameterTypes_shouldReturnFalse() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method differentParameterTypes = exampleClass.getMethod("exampleMethod", String.class);
        Invocation differentParameterTypesInvocation = new Invocation(exampleClass, differentParameterTypes, null, null);
        
        assertFalse(invocationMatcher.hasSameMethod(differentParameterTypesInvocation));
    }
    
    @Test
    public void testCaptureArgumentsFrom_withVarArgsMethod_shouldThrowUnsupportedOperationException() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method varArgsMethod = exampleClass.getMethod("varArgsMethod", String.class, String[].class);

        Invocation varArgsInvocation = new Invocation(exampleClass, varArgsMethod, new Object[]{"test", new String[]{"arg1", "arg2"}}, null);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            invocationMatcher.captureArgumentsFrom(varArgsInvocation);
        });
    }
    
    @Test
    public void testCaptureArgumentsFrom_withMatchersImplementingCapturesArguments_shouldCaptureArguments() throws NoSuchMethodException {
        Class<ExampleClass> exampleClass = ExampleClass.class;
        Method method = exampleClass.getMethod("exampleMethod", String.class, int.class);

        Invocation invocationWithMatchers = new Invocation(exampleClass, method, new Object[]{new Matcher(), new CapturesArguments()}, null);
        
        invocationMatcher.captureArgumentsFrom(invocationWithMatchers);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Matcher firstMatcher = matchers.get(0);
        Matcher secondMatcher = matchers.get(1);
        
        assertTrue(firstMatcher instanceof Matcher);
        assertTrue(secondMatcher instanceof CapturesArguments);
        
        assertEquals("matcher1", ((Matcher) firstMatcher).getValue());
        assertEquals("capturedArgument", ((CapturesArguments) secondMatcher).getCapturedArgument());
    }
    
    @Test
    public void testCreateFrom_withInvocationList_shouldReturnListOfInvocationMatchers() {
        List<Invocation> invocations = Arrays.asList(
                new Invocation(new Mock(), new Method("method1"), null, null),
                new Invocation(new Mock(), new Method("method2"), null, null)
        );
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocations.size(); i++) {
            Invocation invocation = invocations.get(i);
            InvocationMatcher invocationMatcher = invocationMatchers.get(i);
            assertEquals(invocation, invocationMatcher.getInvocation());
        }
    }

    private static class ExampleClass {

        public void exampleMethod() {
            // implementation not needed for test
        }

        public void differentMethod() {
            // implementation not needed for test
        }
        
        public void varArgsMethod(String arg1, String... args) {
            // implementation not needed for test
        }
    }
}