import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMockMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mock = mockitoCore.mock(classToMock, mockSettings, false);
        assertNotNull(mock);
    }

    @Test
    public void testMockMethodWithNullSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> classToMock = MockitoCore.class;
        MockSettings mockSettings = null;
        try {
            mockitoCore.mock(classToMock, mockSettings, false);
            fail("Expected NotAMockException or NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testMockMethodWithInvalidClass() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<String> classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        try {
            mockitoCore.mock(classToMock, mockSettings, false);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testStubMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("expected value");
        assertEquals("expected value", mockitoCore.verify(mock).thenReturn("expected value").get());
    }

    @Test
    public void testStubMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        try {
            mockitoCore.stub();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testWhenMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("expected value");
        assertEquals("expected value", mockitoCore.verify(mock).thenReturn("expected value").get());
    }

    @Test
    public void testWhenMethodWithNullMockOrMethodCall() {
        MockitoCore mockitoCore = new MockitoCore();
        try {
            mockitoCore.when(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        Object mock = Mockito.mock(Object.class);
        try {
            mockitoCore.when(mock, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        try {
            mockitoCore.verify(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyMethodWithNotAMock() {
        Object notAMock = new Object() {};
        MockitoCore mockitoCore = new MockitoCore();
        try {
            mockitoCore.verify(notAMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock);
        InOrder inOrder = mockitoCore.inOrder(mock);
        inOrder.verify(mock).methodCall();
        try {
            mockitoCore.verifyNoMoreInteractions(mock);
            fail("Expected VerificationFailedException");
        } catch (VerifyException e) {
            // Expected
        }
    }

    @Test(expected = VerifyException.class)
    public void testVerifyNoMoreInteractionsMethodWithException() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock);
        inOrder.verify(mock).methodCall();
        inOrder.verify(mock).methodCallThrows(new RuntimeException("expected exception"));
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testResetMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.reset(mock);
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(mock));
    }

    @Test(expected = NullPointerException.class)
    public void testResetMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(null);
    }

    @Test
    public void testResetMethodWithMultipleMocks() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mockitoCore.reset(mock1, mock2);
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(mock1));
        assertFalse(mockUtil.isMock(mock2));
    }

    @Test
    public void testInOrderMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).methodCall1();
        inOrder.verify(mock1).methodCall2();
        inOrder.verify(mock2).methodCall1();
        inOrder.verify(mock2).methodCall2();
    }

    @Test(expected = NullPointerException.class)
    public void testInOrderMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {null, Mockito.mock(Object.class)};
        mockitoCore.inOrder(mocks);
    }

    @Test(expected = VerifyException.class)
    public void testInOrderMethodWithMismatchedVerificationOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock2).methodCall1();
        inOrder.verify(mock1).methodCall1();
    }

    @Test
    public void testAnswerMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        Answer<Integer> answer = invocation -> invocation.getArgument(0).hashCode();
        Stubber stubber = mockitoCore.doAnswer(answer);
        Integer result = stubber.when(mock).methodWithOneArgument(1).get();
        assertEquals(1, result.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Answer<Integer> answer = invocation -> invocation.getArgument(0).hashCode();
        try {
            mockitoCore.doAnswer(answer);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVoidMethodStubbableMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.doCall();
    }

    @Test(expected = NullPointerException.class)
    public void testVoidMethodStubbableMethodWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(null);
        try {
            voidMethodStubbable.doCall();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testValidateMockitoUsageMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocationMethod() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenReturn("expected value");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals("expected value", lastInvocation.getArgument(0));
    }

    @Test(expected = MockitoException.class)
    public void testGetLastInvocationMethodWithNoInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.getLastInvocation();
    }
}