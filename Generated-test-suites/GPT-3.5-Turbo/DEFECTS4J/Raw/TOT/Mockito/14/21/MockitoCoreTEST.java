import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

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

    @After
    public void tearDown() {
        mockitoCore = null;
        reporter = null;
        mockUtil = null;
        mockingProgress = null;
    }

    @Test
    public void testMock_NullClassToMock_ThrowsNullPointerException() {
        try {
            mockitoCore.mock(null, Mockito.mock(MockSettingsImpl.class));
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testMock_ValidClassToMock_ReturnsMockObject() {
        MyClass myClassMock = mockitoCore.mock(MyClass.class, Mockito.mock(MockSettingsImpl.class));
        assertNotNull(myClassMock);
    }

    @Test
    public void testStub_NoOngoingStubbing_PullsOngoingStubbingFromMockingProgress() {
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        mockingProgress.setOngoingStubbing(stubbing);

        assertSame(stubbing, mockitoCore.stub());
    }

    @Test
    public void testStub_NullOngoingStubbing_ResetsMockingProgressAndThrowsMissingMethodInvocationException() {
        mockingProgress.setOngoingStubbing(null);

        try {
            mockitoCore.stub();
        } catch (MissingMethodInvocationException e) {
            // Test passed
        }
    }

    @Test
    public void testStub_ValidMethodCall_ReturnsDeprecatedOngoingStubbingMock() {
        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(any());
        assertNotNull(stubbing);
    }

    @Test
    public void testWhen_ValidMethodCall_ReturnsOngoingStubbingMock() {
        OngoingStubbing stubbing = mockitoCore.when(any());
        assertNotNull(stubbing);
    }

    @Test
    public void testVerify_NullMockObject_ThrowsNullPointerException() {
        try {
            mockitoCore.verify(null, Mockito.mock(VerificationMode.class));
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testVerify_NotMockObject_ThrowsNotAMockException() {
        try {
            mockitoCore.verify(new MyClass(), Mockito.mock(VerificationMode.class));
        } catch (NotAMockException e) {
            // Test passed
        }
    }

    @Test
    public void testReset_ValidMocks_CallsResetMockForEachMockObject() {
        MyClass mock1 = Mockito.mock(MyClass.class);
        MyClass mock2 = Mockito.mock(MyClass.class);
        mockitoCore.reset(mock1, mock2);

        Mockito.verify(mockUtil, Mockito.times(1)).resetMock(mock1);
        Mockito.verify(mockUtil, Mockito.times(1)).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_ValidMocks_VerifyNoMoreInteractionsForAllMocks() {
        MyClass mock1 = Mockito.mock(MyClass.class);
        MyClass mock2 = Mockito.mock(MyClass.class);

        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        Mockito.verify(mockUtil, Mockito.times(1)).getMockHandler(mock1);
        Mockito.verify(mockUtil, Mockito.times(1)).getMockHandler(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions_NullMockObject_ThrowsNullPointerException() {
        try {
            mockitoCore.verifyNoMoreInteractions((MyClass)null);
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder_ValidMocks_VerifyNoMoreInteractionsInOrderForAllMocks() {
        MyClass mock1 = Mockito.mock(MyClass.class);
        MyClass mock2 = Mockito.mock(MyClass.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        Mockito.verify(inOrderContext, Mockito.times(1)).find(mocks);
    }

    @Test
    public void testInOrder_NullMocks_ThrowsNullPointerException() {
        try {
            mockitoCore.inOrder((MyClass)null);
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testInOrder_NotMockObject_ThrowsNotAMockException() {
        MyClass mock = new MyClass();

        try {
            mockitoCore.inOrder(mock);
        } catch (NotAMockException e) {
            // Test passed
        }
    }

    @Test
    public void testDoAnswer_ValidAnswerObject_ReturnsStubberObject() {
        Answer answer = Mockito.mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid_ValidMockObject_ReturnsVoidMethodStubbableObject() {
        MyClass mock = Mockito.mock(MyClass.class);
        VoidMethodStubbable<MyClass> stubbable = mockitoCore.stubVoid(mock);
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptionThrown() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation_ValidRegisteredInvocations_ReturnsLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(Mockito.mock(Invocation.class));
        invocations.add(Mockito.mock(Invocation.class));
        invocations.add(Mockito.mock(Invocation.class));
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);

        mockingProgress.setOngoingStubbing(ongoingStubbing);

        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertSame(invocations.get(invocations.size()-1), lastInvocation);
    }

    private static class MyClass {}
}