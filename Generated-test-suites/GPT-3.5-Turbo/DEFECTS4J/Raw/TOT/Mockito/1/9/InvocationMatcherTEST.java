import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocationMock;

    @Before
    public void setup() {
        invocationMock = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocationMock);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocationMock, matchers);

        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        invocationMatcher = new InvocationMatcher(invocationMock);

        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);

        assertEquals(methodMock, invocationMatcher.getMethod());
        verify(invocationMock).getMethod();
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocationMock, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Matcher matcherMock = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcherMock);

        invocationMatcher = new InvocationMatcher(invocationMock, matchers);

        String expected = "PrintSettings().print(matchers, invocation)";
        when(matcherMock.toString()).thenReturn("matcher");
        when(invocationMock.toString()).thenReturn("invocation");

        assertEquals(expected, invocationMatcher.toString());
        verify(invocationMock).toString();
        verify(matcherMock).toString();
    }

    @Test
    public void testMatches_equalMockSameMethodSameArguments() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(invocationMock.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(actualInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(actualInvocation.getMethod()).thenReturn(methodMock);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_notEqualMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(invocationMock.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(methodMock);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_notSameMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(invocationMock.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(actualInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getArguments()).thenReturn(new Object[] { 1, 2, 3 });

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_argumentsMatch() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(invocationMock.getArguments()).thenReturn(new Object[] { 1, 2, 3 });
        when(actualInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(actualInvocation.getMethod()).thenReturn(methodMock);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { 1, 4, 3 });

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodNameSameMockNotVerifiedSameMethod_returnsTrue() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidateInvocation.getMock());
        when(candidateInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_notSameMethodName_returnsFalse() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidateInvocation.getMock());
        when(candidateInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.getMethod().getName()).thenReturn("method1");
        when(candidateInvocation.getMethod().getName()).thenReturn("method2");

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_isVerified_returnsFalse() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.isVerified()).thenReturn(true);
        when(invocationMock.getMock()).thenReturn(candidateInvocation.getMock());
        when(candidateInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_notSameMock_returnsFalse() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodNotSameArgs_returnsTrue() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidateInvocation.getMock());
        when(candidateInvocation.getMock()).thenReturn(invocationMock.getMock());
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocationMatcher.safelyArgumentsMatch(any())).thenReturn(true);

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod_sameMethod_sameName_sameMock_returnsTrue() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(invocationMock.getMethod());

        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }
    
    @Test
    public void testCaptureArgumentsFrom_varArgsMethod_throwsUnsupportedOperationException() {
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.isVarArgs()).thenReturn(true);

        assertThrows(UnsupportedOperationException.class, () -> invocationMatcher.captureArgumentsFrom(invocationMock));
    }

    // Additional tests can be added

}