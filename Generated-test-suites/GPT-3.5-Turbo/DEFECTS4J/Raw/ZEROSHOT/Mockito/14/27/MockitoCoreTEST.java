import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class<TestClass> classToMock = TestClass.class;
        MockSettings mockSettings = mock(MockSettings.class);

        TestClass mock = mockitoCore.mock(classToMock, mockSettings);

        assertNotNull(mock);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("mock"));
        assertTrue(mockitoCore.getLastInvocation().getArguments().length == 2);
        assertTrue(mockitoCore.getLastInvocation().getArguments()[0] == classToMock);
        assertTrue(mockitoCore.getLastInvocation().getArguments()[1] == mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        mockitoCore.stub();

        verify(mockitoCore).stub();

        when(mockitoCore.stub()).thenReturn(stubbing);

        IOngoingStubbing result = mockitoCore.stub();

        assertNotNull(result);
        assertEquals(stubbing, result);
    }

    @Test
    public void testStub_MethodCall() {
        T methodCall = mock(T.class);
        DeprecatedOngoingStubbing<T> stubbing = mock(DeprecatedOngoingStubbing.class);

        mockitoCore.stub(methodCall);

        verify(mockitoCore).stub(methodCall);

        when(mockitoCore.stub(methodCall)).thenReturn(stubbing);

        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }

    @Test
    public void testWhen() {
        T methodCall = mock(T.class);
        OngoingStubbing<T> stubbing = mock(OngoingStubbing.class);

        mockitoCore.when(methodCall);

        verify(mockitoCore).when(methodCall);

        when(mockitoCore.when(methodCall)).thenReturn(stubbing);

        OngoingStubbing<T> result = mockitoCore.when(methodCall);

        assertNotNull(result);
        assertEquals(stubbing, result);
    }

    @Test
    public void testVerify() {
        T mock = mock(T.class);
        VerificationMode mode = mock(VerificationMode.class);

        mockitoCore.verify(mock, mode);

        verify(mockitoCore).verify(mock, mode);
        verify(mockitoCore).mockUtil.isMock(mock);

        when(mockitoCore.verify(mock, mode)).thenReturn(mock);

        T result = mockitoCore.verify(mock, mode);

        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test
    public void testReset() {
        T[] mocks = (T[]) new Object[]{mock(T.class), mock(T.class)};
        MockUtil mockUtil = mock(MockUtil.class);

        mockitoCore.reset(mocks);

        verify(mockitoCore).reset(mocks);
        verify(mockitoCore).mockUtil.resetMock(any());

        when(mockitoCore.reset(mocks)).thenReturn(mockitoCore);

        mockitoCore.reset(mocks);

        assertNotNull(mockitoCore);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[]{mock(T.class), mock(T.class)};
        MockUtil mockUtil = mock(MockUtil.class);

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(mockitoCore).verifyNoMoreInteractions(mocks);
        verify(mockitoCore, times(mocks.length)).mockUtil.getMockHandler(any());

        when(mockitoCore.verifyNoMoreInteractions(mocks)).thenReturn(mocks);

        mockitoCore.verifyNoMoreInteractions(mocks);

        assertNotNull(mockitoCore);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(mock(T.class), mock(T.class));
        InOrderContext inOrderContext = mock(InOrderContext.class);
        AllInvocationsFinder finder = mock(AllInvocationsFinder.class);
        VerificationDataInOrder data = mock(VerificationDataInOrder.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockitoCore).mockUtil.getMockHandler(any());

        when(mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext)).thenReturn(mocks);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        assertNotNull(mockitoCore);
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[]{mock(T.class), mock(T.class)};
        MockUtil mockUtil = mock(MockUtil.class);

        InOrder result = mockitoCore.inOrder(mocks);

        assertNotNull(result);
        assertEquals(InOrderImpl.class, result.getClass());
        assertEquals(mocks.length, result.size());
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);

        mockitoCore.doAnswer(answer);

        verify(mockitoCore).doAnswer(answer);
        verify(mockitoCore).mockingProgress.stubbingStarted();
        verify(mockitoCore).mockingProgress.resetOngoingStubbing();

        when(mockitoCore.doAnswer(answer)).thenReturn(stubber);

        Stubber result = mockitoCore.doAnswer(answer);

        assertNotNull(result);
        assertEquals(stubber, result);
    }

    @Test
    public void testStubVoid() {
        T mock = mock(T.class);
        MockHandlerInterface<T> handler = mock(MockHandlerInterface.class);
        VoidMethodStubbable stubbable = mock(VoidMethodStubbable.class);

        mockitoCore.stubVoid(mock);

        verify(mockitoCore).stubVoid(mock);
        verify(mockitoCore).mockUtil.getMockHandler(mock);
        verify(mockitoCore).mockingProgress.stubbingStarted();

        when(mockitoCore.stubVoid(mock)).thenReturn(stubbable);

        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);

        assertNotNull(result);
        assertEquals(stubbable, result);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockitoCore).validateMockitoUsage();
        verify(mockitoCore).mockingProgress.validateState();
    }

    @Test
    public void testGetLastInvocation() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));

        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        mockingProgress.pullOngoingStubbing();
        when(mockitoCore.getLastInvocation()).thenReturn(ongoingStubbing.getLastInvocation());

        Invocation result = mockitoCore.getLastInvocation();

        assertNotNull(result);
        assertEquals(invocations.get(invocations.size() - 1), result);
    }

    private static class TestClass {
    }
}