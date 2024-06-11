package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {
    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object expected = new Object();

        when(mockitoCore.mock(classToMock, mockSettings)).thenReturn(expected);

        Object actual = mockitoCore.mock(classToMock, mockSettings);

        verify(mockitoCore, times(2)).mock(classToMock, mockSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void testStub() {
        IOngoingStubbing expected = mock(IOngoingStubbing.class);

        when(mockitoCore.stub()).thenReturn(expected);

        IOngoingStubbing actual = mockitoCore.stub();

        verify(mockitoCore).stub();
        assertEquals(expected, actual);
    }

    @Test
    public void testStub_methodCall() {
        DeprecatedOngoingStubbing<Object> expected = mock(DeprecatedOngoingStubbing.class);
        Object methodCall = new Object();

        when(mockitoCore.stub(methodCall)).thenReturn(expected);

        DeprecatedOngoingStubbing<Object> actual = mockitoCore.stub(methodCall);

        verify(mockitoCore).stub(methodCall);
        assertEquals(expected, actual);
    }

    @Test
    public void testWhen() {
        OngoingStubbing<Object> expected = mock(OngoingStubbing.class);
        Object methodCall = new Object();

        when(mockitoCore.when(methodCall)).thenReturn(expected);

        OngoingStubbing<Object> actual = mockitoCore.when(methodCall);

        verify(mockitoCore).when(methodCall);
        assertEquals(expected, actual);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = VerificationModeFactory.times(1);
        Object expected = mock;

        when(mockitoCore.verify(mock, mode)).thenReturn(expected);

        Object actual = mockitoCore.verify(mock, mode);

        verify(mockitoCore, times(2)).verify(mock, mode);
        assertEquals(expected, actual);
    }

    @Test
    public void testReset() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);

        mockitoCore.reset(mocks);

        verify(mockitoCore).reset(mocks);
        verify(mockitoCore, atLeastOnce()).validateMockitoUsage();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(mockitoCore).verifyNoMoreInteractions(mocks);
        verify(mockitoCore, atLeastOnce()).validateMockitoUsage();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockitoCore, atLeastOnce()).validateMockitoUsage();
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);

        InOrder expected = mock(InOrderImpl.class);

        when(mockitoCore.inOrder(mocks)).thenReturn(expected);

        InOrder actual = mockitoCore.inOrder(mocks);

        verify(mockitoCore).inOrder(mocks);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoAnswer() {
        Answer<Object> answer = mock(Answer.class);
        Stubber expected = mock(StubberImpl.class);

        when(mockitoCore.doAnswer(answer)).thenReturn(expected);

        Stubber actual = mockitoCore.doAnswer(answer);

        verify(mockitoCore).doAnswer(answer);
        assertEquals(expected, actual);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        VoidMethodStubbable<Object> expected = mock(VoidMethodStubbable.class);

        when(mockitoCore.stubVoid(mock)).thenReturn(expected);

        VoidMethodStubbable<Object> actual = mockitoCore.stubVoid(mock);

        verify(mockitoCore).stubVoid(mock);
        assertEquals(expected, actual);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation expected = mock(Invocation.class);

        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocations = new ArrayList<>();
        registeredInvocations.add(mock(Invocation.class));
        registeredInvocations.add(mock(Invocation.class));
        registeredInvocations.add(expected);

        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        when(mockitoCore.getLastInvocation()).thenCallRealMethod();
        when(mockitoCore.stub()).thenReturn(ongoingStubbing);

        Invocation actual = mockitoCore.getLastInvocation();

        verify(mockitoCore).getLastInvocation();
        assertEquals(expected, actual);
    }
}