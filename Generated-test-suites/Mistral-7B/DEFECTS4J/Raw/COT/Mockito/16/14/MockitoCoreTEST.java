import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.NotAMockException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.Callable;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoMock = mockitoCore.mock(mockitoCoreClass, mockSettings, false);
        assertNotNull(mockitoMock);
    }

    @Test(expected = NullPointerException.class)
    public void testMockWithNullClass() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(null, new MockSettingsImpl(), false);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn(true);
        boolean result = mockitoMock.someMethod();
        assertTrue(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingAfterVerification() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        mockitoCore.verify(mockitoMock, Mockito.verifyNoMoreInteractions(mockitoMock));
        IOngoingStubbing stubbing = mockitoCore.stub();
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        IOngoingStubbing stubbing = mockitoCore.when(mockitoMock).someMethod();
        stubbing.thenReturn("result");
        String result = mockitoMock.someMethod();
        assertEquals("result", result);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        mockitoCore.verify(mockitoMock);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(notAMock);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        mockitoCore.reset(mockitoMock);
        boolean result = mockitoMock.someMethod();
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mockitoMock = mockitoCore.mock(MockitoCore.class, new MockSettingsImpl(), false);
        mockitoCore.verifyNoMoreInteractions(mockitoMock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();
    }

    @Test(expected = NullPointerException.class)
    public void testInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = mockitoCore.inOrder(null);
    }

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        Answer<Integer> answer = invocation -> 42;
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.when(mock).someMethod();
        int result = mockitoCore.getLastInvocation().getArgument(0);
        assertEquals(42, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnswerWithWrongArgumentType() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        Answer<String> answer = invocation -> "result";
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.when(mock).someMethod((Callable) null);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl(), false);
        StubberImpl.VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.when(mock).someMethod();
        voidMethodStubbable.thenCallRealMethod();
        mockitoCore.getLastInvocation().proceed();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }
}