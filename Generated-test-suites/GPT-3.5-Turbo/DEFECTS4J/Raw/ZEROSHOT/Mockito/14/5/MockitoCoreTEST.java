import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        Object mock = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(mock);
        assertTrue(mockitoCore.mockUtil.isMock(mock));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        
        assertNull(stubbing);
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.reporter).missingMethodInvocation();
    }
    
    @Test
    public void testStubWithMethodCall() {
        Object methodCall = new Object();
        
        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);
        
        assertNotNull(stubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }
    
    @Test
    public void testWhen() {
        Object methodCall = new Object();
        
        OngoingStubbing stubbing = mockitoCore.when(methodCall);
        
        assertNotNull(stubbing);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }
    
    @Test
    public void testVerify() {
        Object mock = new Object();
        VerificationMode mode = Mockito.times(1);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }
    
    @Test
    public void testVerifyWithNullMock() {
        Object mock = null;
        VerificationMode mode = Mockito.times(1);
        
        mockitoCore.verify(mock, mode);
        
        verify(mockitoCore.reporter).nullPassedToVerify();
        verifyNoMoreInteractions(mockitoCore.mockUtil);
    }
    
    @Test
    public void testVerifyWithNonMockObject() {
        Object mock = new Object();
        VerificationMode mode = Mockito.times(1);
        
        mockitoCore.verify(mock, mode);
        
        verify(mockitoCore.reporter).notAMockPassedToVerify();
        verifyNoMoreInteractions(mockitoCore.mockUtil);
    }
    
    @Test
    public void testReset() {
        Object[] mocks = {new Object(), new Object()};
        
        mockitoCore.reset(mocks);
        
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        
        for (Object mock : mocks) {
            verify(mockitoCore.mockUtil).resetMock(mock);
        }
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {new Object(), new Object()};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore.reporter, times(mocks.length)).nullPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, times(mocks.length)).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.mockUtil, times(mocks.length)).getMockHandler(any()).getInvocationContainer().getInvocations();
        verify(mockitoCore.mockingProgress, times(mocks.length)).validateState();
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(new Object(), new Object());
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.allInvocationsFinder).find(mocks);
        verify(mockitoCore.verificationDataInOrder).verifyInOrder(any(VerificationDataInOrderImpl.class));
    }
    
    @Test
    public void testAssertMocksNotEmpty() {
        Object[] mocks = {};
        
        mockitoCore.assertMocksNotEmpty(mocks);
        
        verify(mockitoCore.reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        Object[] mocks = {new Object(), new Object()};
        
        InOrder inOrder = mockitoCore.inOrder(mocks);
        
        assertNotNull(inOrder);
        verify(mockitoCore.reporter, times(mocks.length)).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(mocks.length)).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(mocks.length)).notAMockPassedWhenCreatingInOrder();
        verify(mockitoCore.mockUtil, times(mocks.length)).isMock(any());
        verify(mockitoCore.mockUtil).isMock(any());
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        
        Stubber stubber = mockitoCore.doAnswer(answer);
        
        assertNotNull(stubber);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        verify(stubber).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = new Object();
        
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        
        assertNotNull(stubbable);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore.mockingProgress).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, result);
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }   
}