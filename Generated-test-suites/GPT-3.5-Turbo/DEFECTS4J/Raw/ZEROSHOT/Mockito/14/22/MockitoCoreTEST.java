import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
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

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        reporter = new Reporter();
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
    }

    @Test
    public void testMock() {
        Class classToMock = MyClass.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);

        MyClass mock = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        assertNotNull(stubbing);
    }

    @Test
    public void testStubWithMethodCall() {
        Object methodCall = new Object();

        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);

        assertNotNull(stubbing);
    }

    @Test
    public void testWhen() {
        Object methodCall = new Object();

        OngoingStubbing stubbing = mockitoCore.when(methodCall);

        assertNotNull(stubbing);
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        Object verifiedMock = mockitoCore.verify(mock, mode);

        assertEquals(mock, verifiedMock);
    }

    @Test
    public void testReset() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mockitoCore.reset(mock1, mock2);

        verify(mockUtil, times(2)).resetMock(any());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = mock(List.class);
        InOrderContext inOrderContext = mock(InOrderContext.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockingProgress).validateState();
        verify(mocks).get(anyInt());
        verify(inOrderContext).getResolver();
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[]{mock(Object.class), mock(Object.class)};

        InOrder inOrder = mockitoCore.inOrder(mocks);

        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);

        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        assertNotNull(stubbable);
        verify(mockingProgress).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> registeredInvocations = mock(List.class);
        Invocation lastInvocation = mock(Invocation.class);

        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        when(registeredInvocations.size()).thenReturn(1);
        when(registeredInvocations.get(0)).thenReturn(lastInvocation);

        Invocation result = mockitoCore.getLastInvocation();

        assertEquals(lastInvocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).resetOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
        verify(registeredInvocations).get(0);
    }
}

class MyClass {}