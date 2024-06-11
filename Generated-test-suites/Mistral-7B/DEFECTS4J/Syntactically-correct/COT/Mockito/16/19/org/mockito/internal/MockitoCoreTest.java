import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.exceptions.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.api.VerificationOrder;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.mock(mockSettings);
        assertNotNull(mockitoCore);
    }

    @Test(expected = NotAMockException.class)
    public void testMockNullClass() {
        MockitoCore mockitoCore = MockitoCore.mock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testMockNotInstance() {
        MockitoCore mockitoCore = MockitoCore.mock(String.class, new MockSettingsImpl());
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("test");
        assertEquals("test", mockitoCore.verify(mockitoCore).methodCall(any()));
    }

    @Test(expected = NotAMockException.class)
    public void testStubNullObject() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        mockitoCore.stubbingStarted();
        mockitoCore.stub(null);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("test");
        ArgumentMatcher<String> argumentMatcher = ArgumentMatchers.eq("test");
        mockitoCore.when(mockitoCore).methodCall(argumentMatcher).thenReturn("expected");
        assertEquals("expected", mockitoCore.verify(mockitoCore).methodCall(ArgumentMatchers.eq("test")));
    }

    @Test(expected = NotAMockException.class)
    public void testWhenNullObject() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        mockitoCore.when(null).methodCall(any());
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        Mockito.verify(mockitoCore).methodCall(any());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullObject() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        Mockito.verify(null).methodCall(any());
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("test");
        mockitoCore.reset();
        Mockito.verify(mockitoCore, never()).methodCall(any());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        mockitoCore.verifyNoMoreInteractions(mockitoCore);
        Mockito.verify(mockitoCore, never()).methodCall(any());
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsWithMocks() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        MockitoCore mockitoCore2 = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        mockitoCore.verifyNoMoreInteractions(mockitoCore, mockitoCore2);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        Mockito.<Object>inOrder(mockitoCore).verify(mockitoCore).methodCall(any());
        mockitoCore.methodCall(any());
        mockitoCore.methodCall(any());
    }

    @Test(expected = AssertionError.class)
    public void testInOrderWithNulls() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        InOrder inOrder = Mockito.inOrder(mockitoCore, null);
        inOrder.verify(mockitoCore).methodCall(any());
    }

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 42;
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertEquals(42, mockitoCore.verify(mockitoCore).methodCall(any()));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockitoCore mockitoCore = MockitoCore.mock(MockitoCore.class, new MockSettingsImpl());
        MockHandlerInterface<MockitoCore> handler = MockitoCore.class.cast(mockitoCore).mockUtil.getMockHandler(mockitoCore);
        VoidMethodStubbable<MockitoCore> stubbable = handler.voidMethodStubbable(mockitoCore);
        stubbable.doNothing();
        mockitoCore.verifyNoMoreInteractions(mockitoCore);
    }
}