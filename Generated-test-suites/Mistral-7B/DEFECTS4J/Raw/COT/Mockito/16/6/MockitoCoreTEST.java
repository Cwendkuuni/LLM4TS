import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoCore;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.List;
import java.util.Arrays;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockHandlerInterface<MockitoCore> mockHandler = MockUtil.getMockHandler(mockitoCore);
        MockitoCore mockitoCoreMock = mockitoCore.mock(mockitoCoreClass, mockSettings);
        assertNotNull(mockitoCoreMock);
        assertEquals(mockitoCore, mockitoCoreMock);
    }

    @Test
    public void testMockWithSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCoreMock = mockitoCore.mock(mockitoCoreClass, mockSettings);
        assertNotNull(mockitoCoreMock);
        assertEquals(mockitoCore, mockitoCoreMock);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
        Stubber stubber = (Stubber) stubbing;
        stubber.thenReturn(ArgumentMatchers.anyString());
        String result = stubber.mock.toString();
        assertEquals("ProxyMock$MockProxy@5a484710", result);
    }

    @Test(expected = AssertionError.class)
    public void testStubDeprecatedMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
        stubbing.thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        });
        mockitoCore.verify(mock);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn(ArgumentMatchers.anyString());
        String result = (String) ongoingStubbing.mock.toString();
        assertEquals("ProxyMock$MockProxy@5a484710", result);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        MockitoCore.verify(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        MockitoCore.verify(nullMock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object notAMock = new Object();
        MockitoCore.verify(notAMock);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        MockitoCore.reset(mock);
        MockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        MockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        MockitoCore.verifyNoMoreInteractions(nullMock);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object notAMock = new Object();
        MockitoCore.verifyNoMoreInteractions(notAMock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        InOrder inOrder = mockitoCore.inOrder(nullMock);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderNotAMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object notAMock = new Object();
        InOrder inOrder = mockitoCore.inOrder(notAMock);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.mock.toString();
        assertEquals("ProxyMock$MockProxy@5a484710", stubber.mock.toString());
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface<Object> handler = MockUtil.getMockHandler(mock);
        StubberImpl stubber = (StubberImpl) mockitoCore.stub();
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        });
        handler.verifyVoidMethodStubbable(mock).answer();
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("answer");
        Invocation invocation = mockitoCore.getLastInvocation();
        assertEquals("thenReturn", invocation.getMethod().getName());
    }
}