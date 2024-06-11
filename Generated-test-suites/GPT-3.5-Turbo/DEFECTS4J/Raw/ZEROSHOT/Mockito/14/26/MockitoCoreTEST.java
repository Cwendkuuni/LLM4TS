import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    @Mock
    private MockUtil mockUtil;

    @Mock
    private Reporter reporter;

    @Mock
    private ThreadSafeMockingProgress mockingProgress;

    private MockitoCore mockitoCore;

    @Mock
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockitoCore = new MockitoCore();
        mockitoCore.setMockUtil(mockUtil);
        mockitoCore.setReporter(reporter);
        mockitoCore.setMockingProgress(mockingProgress);
    }

    @Test
    public void testMock() {
        Class<?> classToMock = Object.class;
        when(mockUtil.createMock(classToMock, mockSettings)).thenReturn("mockObject");
        assertEquals("mockObject", mockitoCore.mock(classToMock, mockSettings));
        verify(mockUtil).createMock(classToMock, mockSettings);
        verify(mockingProgress).mockingStarted("mockObject", classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        assertEquals(stubbing, mockitoCore.stub());
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testStub_withMethodCall() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        assertEquals(stubbing, mockitoCore.stub("methodCall"));
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        assertEquals(stubbing, mockitoCore.when("methodCall"));
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).stubbingStarted();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testVerify_withNullMock() {
        VerificationMode mode = mock(VerificationMode.class);
        mockitoCore.verify(null, mode);
        verify(reporter).nullPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testVerify_withNotAMock() {
        VerificationMode mode = mock(VerificationMode.class);
        Object mock = new Object();
        mockitoCore.verify(mock, mode);
        verify(reporter).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        Object[] mocks = new Object[] { mock(Object.class) };
        for (Object mock : mocks) {
            mockitoCore.reset(mock);
            verify(mockingProgress).validateState();
            verify(mockingProgress).reset();
            verify(mockingProgress).resetOngoingStubbing();
            verify(mockUtil).resetMock(mock);
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_withNullMock() {
        Object[] mocks = new Object[] { null };
        mockitoCore.verifyNoMoreInteractions(mocks);
        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class));
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer().getInvocations()).thenReturn(invocations);
        Object[] mocks = new Object[] { mock(Object.class) };
        mockitoCore.verifyNoMoreInteractions(mocks);
        verify(mockingProgress).validateState();
        for (Object mock : mocks) {
            verify(mockUtil).getMockHandler(mock);
        }
    }

    @Test
    public void testVerifyNoMoreInteractions_withNotAMock() {
        Object[] mocks = new Object[] { new Object() };
        mockitoCore.verifyNoMoreInteractions(mocks);
        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(Object.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        AllInvocationsFinder finder = mock(AllInvocationsFinder.class);
        when(finder.find(mocks)).thenReturn(Arrays.asList(mock(Invocation.class)));
        VerificationDataInOrder data = mock(VerificationDataInOrder.class);
        whenNew(VerificationDataInOrderImpl.class).withArguments(inOrderContext, finder.find(mocks), null).thenReturn(data);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockingProgress).validateState();
        verify(finder).find(mocks);
        verify(data).verifyInOrder();
    }

    @Test
    public void testAssertMocksNotEmpty_withEmptyMocks() {
        Object[] mocks = new Object[0];
        mockitoCore.assertMocksNotEmpty(mocks);
        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testAssertMocksNotEmpty_withNonEmptyMocks() {
        Object[] mocks = new Object[] { mock(Object.class) };
        mockitoCore.assertMocksNotEmpty(mocks);
        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder_withNullMocks() {
        Object[] mocks = null;
        mockitoCore.inOrder(mocks);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withEmptyMocks() {
        Object[] mocks = new Object[0];
        mockitoCore.inOrder(mocks);
        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder_withNonMock() {
        Object[] mocks = new Object[] { new Object() };
        mockitoCore.inOrder(mocks);
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[] { mock(Object.class) };
        InOrderImpl inOrder = mock(InOrderImpl.class);
        whenNew(InOrderImpl.class).withArguments(Arrays.asList(mocks)).thenReturn(inOrder);
        assertEquals(inOrder, mockitoCore.inOrder(mocks));
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        MockitoCore mockitoCoreSpy = spy(mockitoCore);
        doReturn(mockingProgress).when(mockitoCoreSpy).getMockingProgress();
        StubberImpl stubber = mock(StubberImpl.class);
        whenNew(StubberImpl.class).withNoArguments().thenReturn(stubber);
        assertEquals(stubber, mockitoCoreSpy.doAnswer(answer));
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(stubber).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);
        when(handler.voidMethodStubbable(mock)).thenReturn(stubbable);
        assertEquals(stubbable, mockitoCore.stubVoid(mock));
        verify(mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        OngoingStubbing ongoingStubbingSuper = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        List<Invocation> registeredInvocations = Arrays.asList(mock(Invocation.class));
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        assertEquals(registeredInvocations.get(0), mockitoCore.getLastInvocation());
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }

    private class InOrderImpl implements InOrder {

        private List<Object> mocks;

        public InOrderImpl(List<Object> mocks) {
            this.mocks = mocks;
        }

        @Override
        public <T> T verify(T mock) {
            return null;
        }

        @Override
        public void verifyNoMoreInteractions() {

        }

        @Override
        public void verifyZeroInteractions() {

        }

        @Override
        public void verifyNoMoreInteractions(Object... mocks) {

        }

        @Override
        public void verifyZeroInteractions(Object... mocks) {

        }
    }

    private interface MockHandlerInterface<T> {
        VoidMethodStubbable voidMethodStubbable(T mock);
    }

    public void setMockUtil(MockUtil mockUtil) {
        this.mockUtil = mockUtil;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public void setMockingProgress(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

}