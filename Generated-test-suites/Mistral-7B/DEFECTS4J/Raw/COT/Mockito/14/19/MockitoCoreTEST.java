package org.mockito.internal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        MockSettings settings = MockSettings.none();
        Object mock = new MockitoCore().mock(String.class, settings);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testStubbing() {
        Object mock = mock(String.class);
        when(mock).thenReturn("Mockito");
        String result = (String) mock.toString();
        verify(mock).toString();
        verify(mock).toString(); // should not throw an exception
        reset(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testStubbingWithAnswer() {
        Object mock = mock(String.class);
        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Answer";
            }
        };
        when(mock).thenAnswer(answer);
        String result = (String) mock.toString();
        verify(mock).toString();
        verify(mock).toString(); // should return "Answer"
        reset(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerification() {
        Object mock = mock(String.class);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullMock() {
        new MockitoCore().verify(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        new MockitoCore().verify(notAMock);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        new MockitoCore().verifyNoMoreInteractions(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        new MockitoCore().verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        InOrder inOrder = inOrder(mock1, mock2);
        when(mock1).thenReturn("Mock1");
        when(mock2).thenReturn("Mock2");
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
        verifyOrder(inOrder);
    }

    @Test(expected = org.mockito.exceptions.misusing.InOrderCannotBeUsedOnMockException.class)
    public void testInOrderOnMock() {
        Object mock = mock(String.class);
        InOrder inOrder = inOrder(mock);
        verifyOrder(inOrder);
    }

    @Test
    public void testGetLastInvocation() {
        Object mock = mock(String.class);
        when(mock).thenReturn("Mockito");
        Invocation lastInvocation = new MockitoCore().getLastInvocation();
        verify(mock).toString();
        verify(mock).toString();
        verify(mock).toString();
        reset(mock);
        verifyNoMoreInteractions(mock);
        assertEquals("toString()", lastInvocation.getMethod().getName());
    }

    @Test
    public void testStubbingMultipleMocks() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        when(mock1).thenReturn("Mock1");
        when(mock2).thenReturn("Mock2");
        reset(mock1);
        reset(mock2);
        Object[] mocks = {mock1, mock2};
        new MockitoCore().verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerificationWithThreadLocalMocks() {
        Object mock = mock(String.class);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int i = random.nextInt(10);
        for (int j = 0; j < i; j++) {
            when(mock).thenReturn(String.valueOf(j));
        }
        String result = "";
        for (int j = 0; j < i; j++) {
            result += (String) mock.toString();
        }
        reset(mock);
        new MockitoCore().verifyNoMoreInteractions(mock);
        assertEquals(Integer.toString(i), result);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithArgumentMatcher() {
        Object mock = mock(String.class);
        when(mock).thenReturn("Mockito");
        new MockitoCore().verifyNoMoreInteractions(ArgumentMatchers.eq("Mockito"));
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrderWithArgumentMatcher() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        inOrder(mock1, mock2).verify(mock1).toString();
        inOrder(mock1, mock2).verify(mock2).toString();
        new MockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), ArgumentMatchers.eq(mock1));
    }
}