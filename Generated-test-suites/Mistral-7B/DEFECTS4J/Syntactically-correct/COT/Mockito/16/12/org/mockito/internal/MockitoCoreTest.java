import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = new MockitoCore();
        MockitoCore mock = mockitoCore.mock(classToMock, mockSettings);
        assertNotNull(mock);
    }

    @Test
    public void testMockWithNullClass() {
        MockitoCore mockitoCore = new MockitoCore();
        try {
            MockitoCore mock = mockitoCore.mock(null, new MockSettingsImpl());
            fail("Expected NotAMockException");
        } catch (IllegalArgumentException e) {
            assertEquals("mock() requires non-null class", e.getMessage());
        }
    }

    @Test
    public void testMockWithNullMockSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        try {
            MockitoCore mock = mockitoCore.mock(MockitoCore.class, null);
            fail("Expected NotAMockException");
        } catch (IllegalArgumentException e) {
            assertEquals("mockSettings is required", e.getMessage());
        }
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
    }

    @Test(expected = IllegalStateException.class)
    public void testStubWithNullStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.stub(null);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        assertNotNull(ongoingStubbing);
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = null;
        mockitoCore.when(mock);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.verify(mock, VerificationMode.VERIFIED);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null, VerificationMode.VERIFIED);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNonMock() {
        Object nonMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(nonMock, VerificationMode.VERIFIED);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.reset(mock);
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testResetWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNonMock() {
        Object nonMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(nonMock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).method1();
        inOrder.verify(mock2).method2();
    }

    @Test(expected = NullPointerException.class)
    public void testInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = null;
        InOrder inOrder = mockitoCore.inOrder(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderWithNonMock() {
        Object nonMock = new Object();
        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = mockitoCore.inOrder(nonMock);
    }

    @Test
    public void testDoAnswer() {
        Answer<Integer> answer = invocation -> 42;
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.doReturn(42).when(mock).method();
        assertEquals(42, mockitoCore.verify(mock).method().intValue());
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenVoidCallRealMethod();
        try {
            mockitoCore.verify(mock).voidMethod();
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // expected
        }
    }

    @Test
    public void testVerifyMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastInvocationWithNoStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.getLastInvocation();
    }
}