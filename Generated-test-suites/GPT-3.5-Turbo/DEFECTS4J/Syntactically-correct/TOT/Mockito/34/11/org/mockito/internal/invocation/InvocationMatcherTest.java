import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);
        Object[] arguments = new Object[]{};

        invocation = new Invocation(mock, method, arguments);

        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expected = Dummy.class.getMethod("dummyMethod");
        Method actual = invocationMatcher.getMethod();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation actual = invocationMatcher.getInvocation();
        
        assertEquals(invocation, actual);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expected = new ArrayList<>();
        List<Matcher> actual = invocationMatcher.getMatchers();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = invocation.toString();
        String actual = invocationMatcher.toString();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_WithMatchingInvocation_ReturnsTrue() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);
        Object[] arguments = new Object[]{};

        Invocation actualInvocation = new Invocation(mock, method, arguments);
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_WithNonMatchingMock_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock1 = mock(Dummy.class);
        Object mock2 = mock(Dummy.class);
        Object[] arguments = new Object[]{};

        Invocation actualInvocation = new Invocation(mock2, method, arguments);
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_WithNonMatchingMethod_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method1 = clazz.getMethod("dummyMethod");
        Method method2 = clazz.getMethod("anotherMethod");
        Object mock = mock(Dummy.class);
        Object[] arguments = new Object[]{};

        Invocation actualInvocation = new Invocation(mock, method2, arguments);
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_WithMatchingArguments_ReturnsTrue() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);
        Object[] arguments = new Object[]{1, 2, 3};

        Invocation actualInvocation = new Invocation(mock, method, arguments);
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_WithNonMatchingArguments_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Object[] arguments2 = new Object[]{4, 5, 6};

        Invocation actualInvocation = new Invocation(mock, method, arguments2);
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_WithMatchingMethodAndUnverified_ReturnsTrue() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);

        Invocation candidate = new Invocation(mock, method, new Object[]{});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithMatchingMethodAndVerified_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);

        Invocation candidate = new Invocation(mock, method, new Object[]{});
        candidate.markVerified();

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithMatchingMethodAndSameMockAndOverloaded_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method1 = clazz.getMethod("dummyMethod", int.class);
        Method method2 = clazz.getMethod("dummyMethod", String.class);
        Object mock = mock(Dummy.class);

        Invocation candidate1 = new Invocation(mock, method1, new Object[]{1});
        Invocation candidate2 = new Invocation(mock, method2, new Object[]{"test"});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate1));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate2));
    }

    @Test
    public void testHasSimilarMethod_WithMatchingMethodAndSameMockAndNotOverloaded_ReturnsTrue() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");
        Object mock = mock(Dummy.class);

        Invocation candidate = new Invocation(mock, method, new Object[]{});

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithMatchingMethod_ReturnsTrue() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method = clazz.getMethod("dummyMethod");

        Invocation candidate = new Invocation(mock(Dummy.class), method, new Object[]{});

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithNonMatchingMethod_ReturnsFalse() throws NoSuchMethodException {
        Class<?> clazz = Dummy.class;
        Method method1 = clazz.getMethod("dummyMethod");
        Method method2 = clazz.getMethod("anotherMethod");

        Invocation candidate1 = new Invocation(mock(Dummy.class), method1, new Object[]{});
        Invocation candidate2 = new Invocation(mock(Dummy.class), method2, new Object[]{});

        assertFalse(invocationMatcher.hasSameMethod(candidate1));
        assertFalse(invocationMatcher.hasSameMethod(candidate2));
    }
    
    private interface Dummy {
        void dummyMethod();
        void anotherMethod();
    }

}