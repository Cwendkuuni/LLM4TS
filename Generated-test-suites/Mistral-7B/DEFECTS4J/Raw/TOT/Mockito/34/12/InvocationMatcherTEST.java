import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_WithEmptyMatcherList() {
        // Given
        Invocation invocation = new InvocationOnMock(mock(Object.class), new Method(Object.class, String.class), "testMethod", new Object[]{"testString"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // When & Then
        Assertions.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assertions.assertTrue(matchers.isEmpty());
        Assertions.assertFalse(invocationMatcher.matches(new InvocationOnMock(mock(Object.class), new Method(Object.class, String.class), "testMethod", new Object[]{"anotherTestString"})));
    }

    @Test
    public void testInvocationMatcher_WithCustomMatcherList() {
        // Given
        Invocation invocation = new InvocationOnMock(mock(Object.class), new Method(Object.class, Integer.TYPE, String.class), "testMethod", new Object[]{1, "testString"});
        List<Matcher> matchers = Arrays.asList(
                isA(Integer.class),
                isInstanceOf(String.class)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        Assertions.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        Assertions.assertIterableEquals(matchers, invocationMatcher.getMatchers());
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        Object [] expectedArguments = new Object[] {1, "testString"};

        Mockito.when(mock(Object.class).testMethod(ArgumentCaptor.capture(integerCaptor), ArgumentCaptor.capture(stringCaptor))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                Assertions.assertArrayEquals(expectedArguments, arguments);
                return null;
            }
        });

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Object.class));

        invocationMatcher.matches(Mockito.invocation(mock(Object.class), 1, "testString"));

        verify(mock(Object.class)).testMethod(ArgumentCaptors.capture(integerCaptor), ArgumentCaptors.capture(stringCaptor));
        Object[] capturedArguments = (Object[]) integerCaptor.getValue();
        Assertions.assertArrayEquals(new Object[]{1}, capturedArguments);
        Assertions.assertEquals("testString", stringCaptor.getValue());
    }
}