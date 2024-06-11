import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStubber;

import java.util.List;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Object mock;
    private Answer answer;
    private InvocationOnMock invocationOnMock;
    private AnswerStubber answerStubber;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        MockitoAnnotations.initMocks(this);
        answer = Mockito.answer(inv -> inv.callRealMethod());
        answerStubber = Mockito.stub(AnswerStubber.class);
    }

    @Test
    public void testMockCreation() {
        mock = mockitoCore.mock(String.class);
        assertNotNull(mock);
    }

    @Test(expected = IllegalStateException.class)
    public void testMockCreationWithInvalidArgument() {
        mockitoCore.mock(Object.class, null);
    }

    @Test
    public void testStubbing() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.when(mock).toString().thenReturn("MockitoCore Test");
        assertEquals("MockitoCore Test", mockitoCore.verify(mock).toString().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingWithNullArgument() {
        mockitoCore.when(null).toString().thenReturn("MockitoCore Test");
    }

    @Test
    public void testVerification() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.verify(mock).toString();
    }

    @Test(expected = IllegalStateException.class)
    public void testVerificationWithNullArgument() {
        mockitoCore.verify(null);
    }

    @Test
    public void testReset() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.when(mock).toString().thenReturn("MockitoCore Test");
        mockitoCore.verify(mock).toString();
        mockitoCore.reset(mock);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsWithNullArgument() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testInOrder() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.verify(mock).toString();
        mockitoCore.verify(mock).toString();
        InOrder inOrder = mockitoCore.inOrder(mock);
        inOrder.verify(mock).toString();
        inOrder.verifyNoMoreInteractions();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInOrderWithNullArgument() {
        mockitoCore.inOrder(null);
    }

    @Test
    public void testDoAnswer() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.doAnswer(answer).when(mock).toString();
        mockitoCore.verify(mock).toString();
        assertEquals("MockitoCore Test", mockitoCore.verify(mock, ArgumentMatchers.eq("MockitoCore Test")).toString().get(0));
    }

    @Test
    public void testStubVoid() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.stubVoid(mock).toString();
        mockitoCore.verify(mock).toString();
    }

    @Test(expected = IllegalStateException.class)
    public void testStubVoidWithNullArgument() {
        mockitoCore.stubVoid(null);
    }

    @Test
    public void testAnswerStubber() {
        mock = mockitoCore.mock(String.class);
        answerStubber.answer(ArgumentMatchers.anyObject(), ArgumentMatchers.eq("AnswerStubber Test"));
        mockitoCore.doAnswer(answerStubber).when(mock).toString();
        mockitoCore.verify(mock).toString();
        assertEquals("AnswerStubber Test", mockitoCore.verify(mock, ArgumentMatchers.eq("AnswerStubber Test")).toString().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerStubberWithNullArgument() {
        mockitoCore.doAnswer(answerStubber).when(null).toString();
    }

    @Test
    public void testGetLastInvocation() {
        mock = mockitoCore.mock(String.class);
        mockitoCore.when(mock).toString().thenReturn("MockitoCore Test");
        mockitoCore.verify(mock).toString();
        Invocation invocation = mockitoCore.getLastInvocation();
        assertEquals("toString()", invocation.getMethod().getName());
        assertEquals("MockitoCore Test", invocation.getArguments()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastInvocationWithNoInvocations() {
        mockitoCore.getLastInvocation();
    }
}