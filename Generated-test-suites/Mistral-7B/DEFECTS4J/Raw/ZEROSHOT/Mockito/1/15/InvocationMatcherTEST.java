import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithEmptyListOfMatchers() {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, notNullValue());
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = mock(InvocationOnMock.class);
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();
        assertThat(matchersFromInvocationMatcher, is(matchers));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        verify(invocation).getMethod();
        assertThat(method, notNullValue());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertThat(invocationFromMatcher, sameInstance(invocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        List<Matcher> matchersFromArguments = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        assertThat(matchers, is(matchersFromArguments));
    }

    @Test
    public void testToString() throws Exception {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String toString = invocationMatcher.toString();
        verify(invocation).toString();
        assertThat(toString, notNullValue());
    }

    @Test
    public void testMatches() throws Exception {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation2.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        boolean matches = invocationMatcher1.matches(invocationMatcher2);
        verify(invocation1, invocation2).matches();
        assertThat(matches, is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() throws Exception {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3});
        verify(invocation).getArguments();
        assertThat(safelyArgumentsMatch, is(true));
    }

    @Test
    public void testHasSimilarMethod() throws Exception {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation2.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3, 4});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        verify(invocation1, invocation2).hasSimilarMethod();
        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethod() throws Exception {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation1.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation2.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2, 3});
        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
        verify(invocation1, invocation2).hasSameMethod();
        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testGetLocation() throws Exception {
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getLocation()).thenReturn(Mockito.mock(Location.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = invocationMatcher.getLocation();
        verify(invocation).getLocation();
        assertThat(location, notNullValue());
    }

    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        Invocation invocation = mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Stubber stubber = Mockito.mock(Stubber.class);
        Answer<Object> answer = Mockito.mock(Answer.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        when(stubber.thenAnswer(answer)).thenReturn(stubber);
        when(matcher.captureFrom(any(Object.class))).thenAnswer(answer);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(invocation).captureArgumentsFrom(invocation);
        int capturedArgsSize = answer.getCapturedArgs().size();
        assertThat(capturedArgsSize, is(arguments.length));
    }
}