
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
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
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class<Object> classToMock = Object.class;
        MockSettings mockSettings = mock(MockSettings.class);
        
        Object mock = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        verify(mockingProgress).mockingStarted(mock, classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockingProgress).pullOngoingStubbing();
        doNothing().when(mockingProgress).reset();
        doNothing().when(reporter).missingMethodInvocation();
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertEquals(stubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testStub_withMethodCall() {
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockingProgress).pullOngoingStubbing();
        doNothing().when(mockingProgress).reset();
        doNothing().when(reporter).missingMethodInvocation();
        
        DeprecatedOngoingStubbing<Object> result = mockitoCore.stub(methodCall);
        
        assertEquals(stubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        Object methodCall = new Object();
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doReturn(stubbing).when(mockingProgress).pullOngoingStubbing();
        doNothing().when(mockingProgress).reset();
        doNothing().when(reporter).missingMethodInvocation();
        
        OngoingStubbing<Object> result = mockitoCore.when(methodCall);
        
        assertEquals(stubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);
        doReturn(false).when(mockUtil).isMock(null);
        doNothing().when(reporter).nullPassedToVerify();
        doNothing().when(reporter).notAMockPassedToVerify();
        doNothing().when(mockingProgress).verificationStarted(mode);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(mockUtil).isMock(mock);
        verify(reporter, never()).nullPassedToVerify();
        verify(reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        doNothing().when(mockingProgress).validateState();
        doNothing().when(mockingProgress).reset();
        doNothing().when(mockingProgress).resetOngoingStubbing();
        doNothing().when(mockUtil).resetMock(any());
        
        mockitoCore.reset(mocks);
        
        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mocks[0]);
        verify(mockUtil).resetMock(mocks[1]);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        doNothing().when(mockingProgress).validateState();
        doNothing().when(reporter).nullPassedToVerifyNoMoreInteractions();
        doNothing().when(reporter).notAMockPassedToVerifyNoMoreInteractions();
        doReturn(false).when(mockUtil).isMock(null);
        doReturn(mock(Util.class)).when(mockUtil).getMockHandler(mocks[0]);
        doReturn(mock(Util.class)).when(mockUtil).getMockHandler(mocks[1]);
        doReturn(Arrays.asList(mock(Invocation.class))).when(mockUtil.getMockHandler(mocks[0])).getInvocationContainer().getInvocations();
        doNothing().when(VerificationModeFactory.noMoreInteractions()).verify(any());
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockingProgress).validateState();
        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockUtil, never()).isMock(mocks[0]);
        verify(mockUtil, never()).getMockHandler(mocks[0]);
        verify(mock(Util.class)).getMockHandler(mocks[1]);
        verify(mock(Util.class)).getInvocationContainer().getInvocations();
        verify(VerificationModeFactory.noMoreInteractions()).verify(any());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        doNothing().when(mockingProgress).validateState();
        doReturn(Arrays.asList(mock(Invocation.class))).when(new AllInvocationsFinder()).find(mocks);
        doReturn(mock(VerificationMode.class)).when(VerificationModeFactory.noMoreInteractions());
        doReturn(mock(VerificationDataInOrder.class)).when(new VerificationDataInOrderImpl()).verifyInOrder(any());
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockingProgress).validateState();
        verify(new AllInvocationsFinder()).find(mocks);
        verify(new VerificationDataInOrderImpl()).verifyInOrder(any());
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[2];
        mocks[0] = mock(Object.class);
        mocks[1] = mock(Object.class);
        doNothing().when(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        doNothing().when(reporter).nullPassedWhenCreatingInOrder();
        doNothing().when(reporter).notAMockPassedWhenCreatingInOrder();
        doReturn(false).when(mockUtil).isMock(null);
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        assertNotNull(result);
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).nullPassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mocks[0]);
        verify(mockUtil).isMock(mocks[1]);
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        doNothing().when(mockingProgress).stubbingStarted();
        doNothing().when(mockingProgress).resetOngoingStubbing();
        doReturn(stubbing).when(stubbingImpl).doAnswer(answer);
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(stubbingImpl).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        Util handler = mock(Util.class);
        doNothing().when(mockingProgress).stubbingStarted();
        doReturn(handler).when(mockUtil).getMockHandler(mock);
        
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        assertNotNull(result);
        verify(mockingProgress).stubbingStarted();
        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        doNothing().when(mockingProgress).validateState();
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        doReturn(ongoingStubbing).when(mockingProgress).pullOngoingStubbing();
        doReturn(allInvocations).when(ongoingStubbing).getRegisteredInvocations();
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertEquals(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}
