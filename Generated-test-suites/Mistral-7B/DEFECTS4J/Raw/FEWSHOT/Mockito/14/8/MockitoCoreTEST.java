package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.MockSettings;
import org.mockito.MockSettingType;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.util.MockUtil;

public class MockitoCoreTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockSettings spyMockSettings = spy(MockSettingsImpl.class);

        // Test mock with default settings
        Object mock = mockitoCoreClass.mock(String.class, mockSettings);
        assertNull(mock);

        // Test mock with custom settings
        mock = mockitoCoreClass.mock(String.class, mockSettings);
        assertSame(mockitoCoreClass, mock.getClass().getEnclosingClass());
        assertTrue(mockitoCoreClass.isMock(mock));

        // Test mock with spy settings
        mock = mockitoCoreClass.mock(String.class, spyMockSettings);
        assertSame(spyMockSettings, mockitoCoreClass.mockSettings(mock));
        assertFalse(mockitoCoreClass.isMock(mock));
    }

    @Test
    public void testStubbing() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        IOngoingStubbing stubbing = mockitoCoreClass.stub();
        stubbing.thenReturn("Mockito");

        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());
        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());

        stubbing.thenThrow(new RuntimeException("Test Exception"));

        try {
            mockitoCoreClass.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testStubWithDeprecatedMethod() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        DeprecatedOngoingStubbing<String> stubbing = mockitoCoreClass.stub((String) mockitoCoreClass.getLastInvocation().getArgument(0));
        stubbing.thenReturn("Mockito");

        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());
        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());

        stubbing.thenThrow(new RuntimeException("Test Exception"));

        try {
            mockitoCoreClass.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testOngoingStubbing() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        OngoingStubbing<String> stubbing = mockitoCoreClass.when((String) mockitoCoreClass.getLastInvocation().getArgument(0));
        stubbing.thenReturn("Mockito");

        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());
        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());

        stubbing.thenThrow(new RuntimeException("Test Exception"));

        try {
            mockitoCoreClass.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testVerification() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        verify(mock).toString();
        verify(mock).toString();

        String spyMock = spy(String.class);
        verify(spyMock).toString();

        try {
            verify(null).toString();
            fail();
        } catch (AssertionError e) {
            // Expected
        }

        try {
            verify(mockitoCoreClass.mock(Object.class, new MockSettingsImpl())).toString();
            fail();
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testReset() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());
        OngoingStubbing<String> stubbing = mockitoCoreClass.when(mock);
        stubbing.thenReturn("Mockito");

        String spyMock = mockitoCoreClass.spy(String.class);
        stubbing = mockitoCoreClass.when(spyMock);
        stubbing.thenReturn("SpyMockito");

        mockitoCoreClass.reset(mock, spyMock);

        assertEquals(null, mockitoCoreClass.verify(mock).toString());
        assertEquals("SpyMockito", mockitoCoreClass.verify(spyMock).toString());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        Object[] mocks = new Object[3];
        for (int i = 0; i < mocks.length; ++i) {
            mocks[i] = mockitoCoreClass.mock(String.class, new MockSettingsImpl());
        }

        mockitoCoreClass.verifyNoMoreInteractions(mocks);
        mockitoCoreClass.verifyNoMoreInteractions(mocks);

        for (Object mock : mocks) {
            ArgumentCaptor<List<Invocation>> invocationsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockitoCoreClass).verifyNoMoreInteractionsInOrder(invocationsCaptor.capture(), eq(inOrder(mocks)));
            List<Invocation> invocations = invocationsCaptor.getValue();
            assertEquals(0, invocations.size());
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        Object[] mocks = new Object[3];
        for (int i = 0; i < mocks.length; ++i) {
            mocks[i] = mockitoCoreClass.mock(String.class, new MockSettingsImpl());
        }

        String arg = "Mockito";
        Invocation invocation1 = mockitoCoreClass.verify(mocks[0]).toString();
        Invocation invocation2 = mockitoCoreClass.verify(mocks[1]).toString();
        Invocation invocation3 = mockitoCoreClass.verify(mocks[2]).toString();

        mockitoCoreClass.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), inOrder(mocks).verify(mocks[0]).thenReturn(arg).thenReturn(arg));
        mockitoCoreClass.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), inOrder(mocks).verify(mocks[0]).thenReturn(arg).thenThrow(new RuntimeException("Test Exception")));

        ArgumentCaptor<List<Invocation>> invocationsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<InOrderContext> inOrderCaptor = ArgumentCaptor.forClass(InOrderContext.class);
        verify(mockitoCoreClass).verifyNoMoreInteractionsInOrder(invocationsCaptor.capture(), inOrderCaptor.capture());

        List<Invocation> invocations = invocationsCaptor.getValue();
        InOrderContext inOrder = inOrderCaptor.getValue();

        assertEquals(2, invocations.size());
        verify(mocks[0]).toString();
        verify(mocks[0]).toString();

        verify(mocks[0]).toString();
        verify(mocks[0]).toString();

        inOrder.inOrder(mocks[0], mocks[1], mocks[2]);
        inOrder.verify(mocks[0]).toString();
        inOrder.verify(mocks[0]).toString();

        try {
            inOrder.verify(null).toString();
            fail();
        } catch (AssertionError e) {
            // Expected
        }

        try {
            inOrder.verify(mocks[3]).toString();
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testInOrder() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        Object[] mocks = new Object[3];
        for (int i = 0; i < mocks.length; ++i) {
            mocks[i] = mockitoCoreClass.mock(String.class, new MockSettingsImpl());
        }

        InOrder inOrder = mockitoCoreClass.inOrder(mocks);
        inOrder.verify(mocks[1]).toString();
        inOrder.verify(mocks[0]).toString();

        try {
            inOrder.verify(null).toString();
            fail();
        } catch (AssertionError e) {
            // Expected
        }

        try {
            inOrder.verify(mocks[3]).toString();
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testDoAnswer() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Mockito";
            }
        };

        Stubber stubber = mockitoCoreClass.doAnswer(answer);
        stubber.doAnswer(answer).when(mock).toString();

        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());
        assertEquals("Mockito", mockitoCoreClass.verify(mock).toString());

        stubber.doAnswer(answer).thenThrow(new RuntimeException("Test Exception"));

        try {
            mockitoCoreClass.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testStubVoid() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        VoidMethodStubbable<String> stubbable = mockitoCoreClass.stubVoid(mock);
        stubbable.doNothing();

        mockitoCoreClass.verifyNoMoreInteractions(mock);
        mockitoCoreClass.verifyNoMoreInteractions(mock);

        stubbable.doThrow(new RuntimeException("Test Exception"));

        try {
            mockitoCoreClass.verify(mock).toString();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testValidateMockitoUsage() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        mockitoCoreClass.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        String mock = mockitoCoreClass.mock(String.class, new MockSettingsImpl());

        OngoingStubbingImpl ongoingStubbing = (OngoingStubbingImpl) mockitoCoreClass.mockingProgress.pullOngoingStubbing();
        List<Invocation> invocations = ongoingStubbing.getRegisteredInvocations();

        assertEquals(ongoingStubbing, mockitoCoreClass.getLastInvocation().getMockingProgress());
        assertEquals(invocations.get(invocations.size() - 1), mockitoCoreClass.getLastInvocation().getInvocation());
    }
}