
package org.mockito.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        reporter = new Reporter();
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
    }
    
    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        
        T actual = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        assertNotNull(actual);
        assertTrue(mockUtil.isMock(actual));
    }
    
    @Test
    public void testMock_withMockSettings() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        T actual = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(actual);
        assertTrue(mockUtil.isMock(actual));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing expected = mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
        
        IOngoingStubbing actual = mockitoCore.stub();
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = NotAMockException.class)
    public void testStub_whenOngoingStubbingIsNull() {
        mockingProgress.reset();
        reporter.missingMethodInvocation();
        
        mockitoCore.stub();
    }
    
    @Test
    public void testStub_Deprecated() {
        DeprecatedOngoingStubbing<T> expected = mock(DeprecatedOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
        
        DeprecatedOngoingStubbing<T> actual = mockitoCore.stub(mock(T.class));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWhen() {
        OngoingStubbing<T> expected = mock(OngoingStubbing.class);
        mockingProgress.stubbingStarted();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(expected);
        
        OngoingStubbing<T> actual = mockitoCore.when(mock(T.class));
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = Mockito.times(1);
        
        T actual = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, actual);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_whenMockIsNull() {
        reporter.nullPassedToVerify();
        
        mockitoCore.verify(null, Mockito.times(1));
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerify_whenMockIsNotMock() {
        reporter.notAMockPassedToVerify();
        Object notAMock = new Object();
        
        mockitoCore.verify(notAMock, Mockito.times(1));
    }
    
    @Test
    public void testReset() {
        T[] mocks = {mock(T.class)};
        
        mockitoCore.reset(mocks);
        
        verify(mockUtil).resetMock(mocks[0]);
    }
    
    @Test
    public void testReset_whenMocksIsEmpty() {
        Object[] mocks = {};
        
        mockitoCore.reset(mocks);
        
        verifyZeroInteractions(mockUtil);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        T mock1 = mock(T.class);
        T mock2 = mock(T.class);
        
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
        
        verify(mockUtil).getMockHandler(mock1);
        verify(mockUtil).getMockHandler(mock2);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_whenMockIsNull() {
        reporter.nullPassedToVerifyNoMoreInteractions();
        
        mockitoCore.verifyNoMoreInteractions(null);
    }
    
    @Test
    public void testInOrder() {
        T mock1 = mock(T.class);
        T mock2 = mock(T.class);
        Object[] mocks = {mock1, mock2};
        
        InOrder expected = mock(InOrder.class);
        whenNew(InOrderImpl.class).withArguments(mocks).thenReturn(expected);
        
        InOrder actual = mockitoCore.inOrder(mocks);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_whenMocksIsEmpty() {
        reporter.mocksHaveToBePassedWhenCreatingInOrder();
        
        mockitoCore.inOrder();
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_whenMockIsNull() {
        reporter.nullPassedWhenCreatingInOrder();
        
        mockitoCore.inOrder(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testInOrder_whenMockIsNotMock() {
        reporter.notAMockPassedWhenCreatingInOrder();
        Object notAMock = new Object();
        
        mockitoCore.inOrder(notAMock);
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber expected = mock(Stubber.class);
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        when(mockitoCore.doAnswer(answer)).thenReturn(expected);
        
        Stubber actual = mockitoCore.doAnswer(answer);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStubVoid() {
        T mock = mock(T.class);
        MockHandlerInterface<T> handler = mock(MockHandlerInterface.class);
        mockingProgress.stubbingStarted();
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        
        VoidMethodStubbable<T> actual = mockitoCore.stubVoid(mock);
        
        assertEquals(handler.voidMethodStubbable(mock), actual);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockingProgress.validateState();
        
        mockitoCore.validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation expected = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(expected);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        mockingProgress.pullOngoingStubbing();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        
        Invocation actual = mockitoCore.getLastInvocation();
        
        assertEquals(expected, actual);
    }
}
