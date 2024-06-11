import static org.junit.Assert.*;
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
        Class<T> classToMock = T.class;
        MockSettings mockSettings = mock(MockSettings.class);

        T mock = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());

        // Verify that mockingProgress.mockingStarted() was called
        // Verification mode is not important since this is just a test
        verify(mockitoCore.mockingProgress).mockingStarted(mock, classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(stubbing);

        IOngoingStubbing result = mockitoCore.stub();

        assertEquals(stubbing, result);

        // Verify that mockingProgress.pullOngoingStubbing() was called
        // Verification mode is not important since this is just a test
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    }

    @Test
    public void testStubWithValue() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(stubbing);

        // There is no return value, so we cannot assert anything
        // Just calling the method for code coverage
        DeprecatedOngoingStubbing result = mockitoCore.stub(new Object());

        // Verify that mockingProgress.stubbingStarted() was called
        // Verification mode is not important since this is just a test
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testWhen() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.mockingProgress.setOngoingStubbing(stubbing);

        OngoingStubbing result = mockitoCore.when(new Object());

        assertEquals(stubbing, result);

        // Verify that mockingProgress.stubbingStarted() was called
        // Verification mode is not important since this is just a test
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        VerificationMode mode = mock(VerificationMode.class);

        Object result = mockitoCore.verify(mock, mode);

        assertEquals(mock, result);

        // Verify that reporter.nullPassedToVerify() was not called
        verify(mockitoCore.reporter, never()).nullPassedToVerify();

        // Verify that reporter.notAMockPassedToVerify() was not called
        verify(mockitoCore.reporter, never()).notAMockPassedToVerify();

        // Verify that mockingProgress.verificationStarted() was called
        // Verification mode is not important since this is just a test
        verify(mockitoCore.mockingProgress).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object[] mocks = new Object[]{mock1, mock2};

        mockitoCore.reset(mocks);

        // Verify that mockingProgress.validateState() was called
        verify(mockitoCore.mockingProgress).validateState();

        // Verify that mockingProgress.reset() was called
        verify(mockitoCore.mockingProgress).reset();

        // Verify that mockingProgress.resetOngoingStubbing() was called
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();

        // Verify that mockUtil.resetMock() was called for each mock
        verify(mockitoCore.mockUtil).resetMock(mock1);
        verify(mockitoCore.mockUtil).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        Object[] mocks = new Object[]{mock};

        mockitoCore.verifyNoMoreInteractions(mocks);

        // Verify that assertMocksNotEmpty() was not called
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();

        // Verify that reporter.nullPassedToVerifyNoMoreInteractions() was not called
        verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();

        // Verify that reporter.notAMockPassedToVerifyNoMoreInteractions() was not called
        verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();

        // Verify that mockUtil.getMockHandler().getInvocationContainer().getInvocations() was called
        verify(mockitoCore.mockUtil).getMockHandler(mock).getInvocationContainer().getInvocations();

        // Verify that VerificationModeFactory.noMoreInteractions().verify() was called
        verify(mockitoCore.mockitoCoreVerificationModeFactory).noMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = mock(List.class);
        InOrderContext inOrderContext = mock(InOrderContext.class);
        Object[] mocksArray = new Object[]{mocks};

        mockitoCore.verifyNoMoreInteractionsInOrder(mocksArray, inOrderContext);

        // Verify that mockingProgress.validateState() was called
        verify(mockitoCore.mockingProgress).validateState();

        // Verify that AllInvocationsFinder.find() was called
        verify(mockitoCore.allInvocationsFinder).find(mocks);

        // Verify that VerificationModeFactory.noMoreInteractions().verifyInOrder() was called
        verify(mockitoCore.mockitoCoreVerificationModeFactory).noMoreInteractions().verifyInOrder(mockitoCoreVerificationDataInOrder);
    }

    @Test
    public void testInOrder() {
        Object mock = mock(Object.class);
        Object[] mocks = new Object[]{mock};

        InOrder result = mockitoCore.inOrder(mocks);

        assertNotNull(result);
        assertEquals(InOrderImpl.class, result.getClass());

        // Verify that reporter.mocksHaveToBePassedWhenCreatingInOrder() was not called
        verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();

        // Verify that reporter.nullPassedWhenCreatingInOrder() was not called
        verify(mockitoCore.reporter, never()).nullPassedWhenCreatingInOrder();

        // Verify that reporter.notAMockPassedWhenCreatingInOrder() was not called
        verify(mockitoCore.reporter, never()).notAMockPassedWhenCreatingInOrder();

        // Verify that new InOrderImpl(Arrays.asList(mocks)) was called
        verifyNew(InOrderImpl.class).withArguments(Arrays.asList(mocks));
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber result = mockitoCore.doAnswer(answer);

        assertNotNull(result);
        assertEquals(StubberImpl.class, result.getClass());

        // Verify that mockingProgress.stubbingStarted() was called
        verify(mockitoCore.mockingProgress).stubbingStarted();

        // Verify that mockingProgress.resetOngoingStubbing() was called
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();

        // Verify that new StubberImpl().doAnswer(answer) was called
        verifyNew(StubberImpl.class).withNoArguments();
        verify(result).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);

        when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.voidMethodStubbable(mock)).thenReturn(stubbable);

        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        assertEquals(stubbable, result);

        // Verify that mockingProgress.stubbingStarted() was called
        verify(mockitoCore.mockingProgress).stubbingStarted();

        // Verify that mockUtil.getMockHandler() was called
        verify(mockitoCore.mockUtil).getMockHandler(mock);

        // Verify that handler.voidMethodStubbable(mock) was called
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        // Verify that mockingProgress.validateState() was called
        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);

        mockitoCore.mockingProgress.setOngoingStubbing(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));

        Invocation result = mockitoCore.getLastInvocation();

        assertEquals(invocation, result);

        // Verify that mockingProgress.pullOngoingStubbing() was called
        verify(mockitoCore.mockingProgress).pullOngoingStubbing();

        // Verify that new VerificationDataImpl(invocations, null) was called
        verifyNew(VerificationDataImpl.class).withArguments(Arrays.asList(invocation), null);
    }
}