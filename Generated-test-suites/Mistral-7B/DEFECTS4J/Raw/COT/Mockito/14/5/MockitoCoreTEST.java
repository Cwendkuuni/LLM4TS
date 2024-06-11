import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = MockitoCore.mock(mockitoCoreClass, mockSettings);
        assertNotNull(mockitoCore);
    }

    @Test(expected = NullPointerException.class)
    public void testMockNullClass() {
        MockitoCore mockitoCore = MockitoCore.mock(null, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockInvalidSettings() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        mockSettings.setVerbose(true); // set verbose to true, which is not supported
        MockitoCore mockitoCore = MockitoCore.mock(mockitoCoreClass, mockSettings);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
    }

    @Test(expected = IllegalStateException.class)
    public void testStubAlreadyMocked() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("test");
        try {
            stubbing.thenReturn("test2");
        } finally {
            mockitoCore.reset(mock);
        }
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("test");
        assertEquals("test", mockitoCore.verify(mock, Mockito.eq(1)));
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullMock() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        mockitoCore.verify(null, Mockito.eq(1));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = MockitoCore.getInstance();
        mockitoCore.verify(notAMock, Mockito.eq(1));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("test");
        mockitoCore.reset(mock);
        assertNull(mockitoCore.verify(mock));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsNullMocks() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        mockitoCore.verifyNoMoreInteractions((Object[]) null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object notAMock = new Object();
        MockitoCore mockitoCore = MockitoCore.getInstance();
        mockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Answer<String> answer = invocation -> "test";
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object mock = Mockito.mock(Object.class);
        stubber.thenAnswer(answer).when(mock).foo();
        assertEquals("test", mockitoCore.verify(mock).foo());
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock = Mockito.mock(Object.class);
        VoidMethodStubbable<Object> stubbable = mockitoCore.stubVoid(mock);
        stubbable.thenCallRealMethod();
        mockitoCore.verify(mock).foo();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("test");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals("test", lastInvocation.getArgument(0));
    }
}