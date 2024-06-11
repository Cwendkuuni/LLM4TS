package org.mockito.internal.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWrapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        MockSettings mockSettings = new MockSettings();
        Object mock = MockitoCore.mock(String.class, mockSettings);
        Assert.assertNotNull(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockCreationWithNullMockSettings() {
        Object mock = MockitoCore.mock(String.class, null);
    }

    @Test
    public void testStubbing() {
        Object mock = mock(String.class);
        when(mock).thenReturn("test");
        String result = (String) mock.toString();
        Assert.assertEquals("test", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingWithNoOngoingStubbing() {
        Object mock = mock(String.class);
        when(mock).thenReturn("test");
        mockingProgress.reset(); // resetting progress should throw an exception
        mock.toString();
    }

    @Test
    public void testVerifyWithNullMock() {
        Object mock = mock(String.class);
        Assert.assertNotNull(MockitoCore.verify(mock, VerificationModeFactory.instance()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyWithNonMockObject() {
        Object mock = new Object();
        MockitoCore.verify(mock, VerificationModeFactory.instance());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(String.class);
        Object mock2 = mock(Integer.class);

        Mockito.verifyNoMoreInteractions(mock, mock2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        Object mock = mock(String.class);
        Mockito.verifyNoMoreInteractions((Object) null);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        InOrder inOrder = inOrder(mock1, mock2);

        when(mock1.toString()).thenReturn("test1");
        when(mock2.toString()).thenReturn("test2");
        mock1.toString();
        mock2.toString();

        verifyOrder(inOrder);
    }

    @Test
    public void testReset() {
        Object mock = mock(String.class);
        String result1 = (String) mock.toString();
        Assert.assertEquals("", result1);

        when(mock.toString()).thenReturn("test");
        String result2 = (String) mock.toString();
        Assert.assertEquals("test", result2);

        MockitoCore.reset(mock);

        String result3 = (String) mock.toString();
        Assert.assertEquals("", result3);
    }

    @Test
    public void testVerifyInteractions() {
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("test");
        mock.toString();

        Mockito.verify(mock, times(1)).toString();
    }

    @Test
    public void testVerifyInteractionsMultipleTimes() {
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("test");
        mock.toString();
        mock.toString();

        Mockito.verify(mock, times(2)).toString();
    }

    @Test
    public void testVerifyInteractionsNever() {
        Object mock = mock(String.class);
        mock.toString();

        Mockito.verify(mock, never()).toString();
    }

    @Test(expected = AssertionError.class)
    public void testVerifyInteractionsNeverWithOneInteraction() {
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("test");
        mock.toString();

        Mockito.verify(mock, never()).toString();
    }

    @Test
    public void testAnswer() {
        Object mock = mock(String.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };

        Object result = MockitoCore.doAnswer(answer).answer(mock, ArgumentMatchers.anyString());
        Assert.assertEquals("answer", result);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(String.class);
        MockitoCore.stubVoid(mock).toString();
        mock.toString();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore validateMockitoUsage = new MockitoCore();
        validateMockitoUsage.validateMockitoUsage();
    }
}