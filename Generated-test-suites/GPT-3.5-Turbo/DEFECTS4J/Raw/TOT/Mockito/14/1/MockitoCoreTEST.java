import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Mock
    private MockUtil mockUtil;

    @Mock
    private Reporter reporter;

    @Mock
    private IOngoingStubbing ongoingStubbing;

    @Mock
    private Stubber stubber;

    @Mock
    private VerificationMode verificationMode;

    @Mock
    private VerificationDataInOrder verificationDataInOrder;

    @Mock
    private InOrderContext inOrderContext;

    @Mock
    private List<Object> mocks;

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        Class classToMock = Mockito.mock(Class.class);
        Object result = Mockito.mock(Object.class);

        when(mockUtil.createMock(classToMock, (MockitoCore.MockSettingsImpl) mockSettings)).thenReturn(result);

        Object mock = mockitoCore.mock(classToMock, mockSettings);

        assertEquals(result, mock);
        verify(mockUtil).createMock(classToMock, (MockitoCore.MockSettingsImpl) mockSettings);
        verify(mockingProgress).mockingStarted(mock, classToMock, mockSettings);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());
        VerificationDataImpl data = new VerificationDataImpl(invocations, null);

        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(mockUtil.getMockHandler(mock)).thenReturn(new MockHandlerInterface<>());

        IOngoingStubbing result = mockitoCore.stub();

        assertEquals(ongoingStubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testStubWithMock() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        DeprecatedOngoingStubbing result = mockitoCore.stub(mock);

        assertEquals(ongoingStubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        OngoingStubbing result = mockitoCore.when(mock);

        assertEquals(ongoingStubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter, never()).missingMethodInvocation();
    }

    @Test
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();

        Object mock = null;
        mockitoCore.verify(mock, verificationMode);

        verify(reporter).nullPassedToVerify();
        verify(mockingProgress, never()).verificationStarted(verificationMode);
    }

    @Test
    public void testVerifyWithNonMock() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(false);
        mockitoCore.verify(mock, verificationMode);

        verify(reporter).notAMockPassedToVerify();
        verify(mockingProgress, never()).verificationStarted(verificationMode);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(true);
        Object result = mockitoCore.verify(mock, verificationMode);

        assertEquals(mock, result);
        verify(reporter, never()).nullPassedToVerify();
        verify(reporter, never()).notAMockPassedToVerify();
        verify(mockingProgress).verificationStarted(verificationMode);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();

        Object[] mocksArray = new Object[]{mock};
        mockitoCore.reset(mocksArray);

        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil).resetMock(mock);
    }

    @Test
    public void testResetWithEmptyMocks() {
        MockitoCore mockitoCore = new MockitoCore();

        Object[] mocksArray = new Object[]{};
        mockitoCore.reset(mocksArray);

        verify(mockingProgress).validateState();
        verify(mockingProgress).reset();
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockUtil, never()).resetMock(any());
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocksArray = new Object[]{null};

        mockitoCore.verifyNoMoreInteractions(mocksArray);

        verify(reporter).nullPassedToVerifyNoMoreInteractions();
        verify(mockingProgress, never()).validateState();
        verify(mockUtil, never()).getMockHandler(any());
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNonMock() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(false);
        mockitoCore.verifyNoMoreInteractions(mock);

        verify(reporter).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgress, never()).validateState();
        verify(mockUtil, never()).getMockHandler(any());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(true);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());
        when(mockUtil.getMockHandler(mock)).thenReturn(new MockHandlerInterface<>(invocations));

        Object[] mocksArray = new Object[]{mock};
        mockitoCore.verifyNoMoreInteractions(mocksArray);

        verify(reporter, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgress).validateState();
        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();

        List<Object> mockList = new ArrayList<>();
        mockList.add(mock);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new Invocation());

        AllInvocationsFinder finder = new AllInvocationsFinder();
        when(finder.find(mockList)).thenReturn(invocations);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(verificationDataInOrder);

        mockitoCore.verifyNoMoreInteractionsInOrder(mockList, inOrderContext);

        verify(mockingProgress).validateState();
        verify(finder).find(mockList);
        verify(verificationDataInOrder).verifyInOrder(verificationMode);
    }

    @Test
    public void testAssertMocksNotEmptyWithEmptyMocks() {
        MockitoCore mockitoCore = new MockitoCore();

        Object[] mocksArray = new Object[]{};
        mockitoCore.assertMocksNotEmpty(mocksArray);

        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testAssertMocksNotEmptyWithNonEmptyMocks() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocksArray = new Object[]{mock};

        mockitoCore.assertMocksNotEmpty(mocksArray);

        verify(reporter, never()).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrderWithNullMocks() {
        MockitoCore mockitoCore = new MockitoCore();

        Object[] mocksArray = new Object[]{null};
        mockitoCore.inOrder(mocksArray);

        verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockUtil, never()).isMock(any());
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrderWithNonMock() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(false);
        mockitoCore.inOrder(mock);

        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mock);
        verify(reporter).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrderWithMocks() {
        MockitoCore mockitoCore = new MockitoCore();

        when(mockUtil.isMock(mock)).thenReturn(true);
        List<Object> objects = Arrays.asList(mock);
        InOrder result = mockitoCore.inOrder(mock);

        assertEquals(objects, ((InOrderImpl) result).getMockList());
        verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockUtil).isMock(mock);
        verify(reporter, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();

        StubberImpl stubberImpl = new StubberImpl();
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubberImpl);

        Answer answer = Mockito.mock(Answer.class);
        Stubber result = mockitoCore.doAnswer(answer);

        assertEquals(stubberImpl, result);
        verify(mockingProgress).stubbingStarted();
        verify(mockingProgress).resetOngoingStubbing();
        verify(stubberImpl).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();

        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);

        VoidMethodStubbable voidMethodStubbable = new VoidMethodStubbable(mock);
        when(handler.voidMethodStubbable(mock)).thenReturn(voidMethodStubbable);

        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        assertEquals(voidMethodStubbable, result);
        verify(mockingProgress).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.validateMockitoUsage();

        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();

        OngoingStubbingImpl ongoingStubbingImpl = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation = new Invocation();
        invocations.add(invocation);

        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbingImpl);
        when(ongoingStubbingImpl.getRegisteredInvocations()).thenReturn(invocations);

        Invocation result = mockitoCore.getLastInvocation();

        assertEquals(invocation, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(ongoingStubbingImpl).getRegisteredInvocations();
    }
}