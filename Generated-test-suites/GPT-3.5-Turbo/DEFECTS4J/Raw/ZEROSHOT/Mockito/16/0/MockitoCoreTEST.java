import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Reporter reporterMock;
    private MockUtil mockUtilMock;
    private MockingProgress mockingProgressMock;

    @Before
    public void setUp() {
        reporterMock = mock(Reporter.class);
        mockUtilMock = mock(MockUtil.class);
        mockingProgressMock = mock(MockingProgress.class);
        mockitoCore = new MockitoCore(reporterMock, mockUtilMock, mockingProgressMock);
    }

    @Test
    public void testMock() {
        Class clazz = MyClass.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        MyClass result = mockitoCore.mock(clazz, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        verify(mockingProgressMock).validateState();
        verify(mockingProgressMock).resetOngoingStubbing();
        verify(mockUtilMock).createMock(clazz, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testMockWithDefaultMockSettings() {
        Class clazz = MyClass.class;
        MockSettings mockSettings = mock(MockSettings.class);

        MyClass result = mockitoCore.mock(clazz, mockSettings);

        assertNotNull(result);
        verify(mockingProgressMock).validateState();
        verify(mockingProgressMock).resetOngoingStubbing();
        verify(mockUtilMock).createMock(clazz, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        IOngoingStubbing result = mockitoCore.stub();

        assertNotNull(result);
        verify(mockingProgressMock).pullOngoingStubbing();
        verify(mockingProgressMock).reset();
        verify(reporterMock, never()).missingMethodInvocation();
    }

    @Test
    public void testStubWithMissingMethodInvocation() {
        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(null);

        IOngoingStubbing result = mockitoCore.stub();

        assertNull(result);
        verify(mockingProgressMock).pullOngoingStubbing();
        verify(mockingProgressMock).reset();
        verify(reporterMock).missingMethodInvocation();
    }

    @Test
    public void testStubWithDeprecatedMethod() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        DeprecatedOngoingStubbing result = mockitoCore.stub(new MyClass());

        assertNotNull(result);
        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).pullOngoingStubbing();
        verify(mockingProgressMock).reset();
        verify(reporterMock, never()).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        IOngoingStubbing ongoingStubbing = mock(IOngoingStubbing.class);
        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        OngoingStubbing result = mockitoCore.when(new MyClass());

        assertNotNull(result);
        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).pullOngoingStubbing();
        verify(mockingProgressMock).reset();
        verify(reporterMock, never()).missingMethodInvocation();
    }

    @Test
    public void testVerify() {
        MyClass mock = mock(MyClass.class);
        VerificationMode mode = mock(VerificationMode.class);

        MyClass result = mockitoCore.verify(mock, mode);

        assertSame(mock, result);
        verify(reporterMock, never()).nullPassedToVerify();
        verify(reporterMock, never()).notAMockPassedToVerify();
        verify(mockingProgressMock).verificationStarted(mode);
    }

    @Test
    public void testVerifyWithNullMock() {
        VerificationMode mode = mock(VerificationMode.class);

        MyClass result = mockitoCore.verify(null, mode);

        assertNull(result);
        verify(reporterMock).nullPassedToVerify();
        verify(reporterMock, never()).notAMockPassedToVerify();
        verify(mockingProgressMock, never()).verificationStarted(mode);
    }

    @Test
    public void testVerifyWithNotAMock() {
        VerificationMode mode = mock(VerificationMode.class);

        MyClass result = mockitoCore.verify(new MyClass(), mode);

        assertNull(result);
        verify(reporterMock, never()).nullPassedToVerify();
        verify(reporterMock).notAMockPassedToVerify();
        verify(mockingProgressMock, never()).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        MyClass mock = mock(MyClass.class);

        mockitoCore.reset(mock);

        verify(mockingProgressMock).validateState();
        verify(mockingProgressMock).reset();
        verify(mockingProgressMock).resetOngoingStubbing();
        verify(mockUtilMock).resetMock(mock);
    }

    @Test
    public void testResetWithMultipleMocks() {
        MyClass mock1 = mock(MyClass.class);
        MyClass mock2 = mock(MyClass.class);

        mockitoCore.reset(mock1, mock2);

        verify(mockingProgressMock).validateState();
        verify(mockingProgressMock).reset();
        verify(mockingProgressMock).resetOngoingStubbing();
        verify(mockUtilMock).resetMock(mock1);
        verify(mockUtilMock).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MyClass mock = mock(MyClass.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(reporterMock, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporterMock, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgressMock).validateState();
        verify(mockUtilMock).getMockHandler(mock);
        verify(mockUtilMock.getMockHandler(mock)).verifyNoMoreInteractions();
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        Object mock = null;

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(reporterMock).nullPassedToVerifyNoMoreInteractions();
        verify(reporterMock, never()).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgressMock, never()).validateState();
        verify(mockUtilMock, never()).getMockHandler(mock);
        verify(mockUtilMock, never()).getMockHandler(null);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        MyClass mock = new MyClass();

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(reporterMock, never()).nullPassedToVerifyNoMoreInteractions();
        verify(reporterMock).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockingProgressMock, never()).validateState();
        verify(mockUtilMock, never()).getMockHandler(mock);
    }

    @Test
    public void testInOrder() {
        MyClass mock1 = mock(MyClass.class);
        MyClass mock2 = mock(MyClass.class);

        InOrder result = mockitoCore.inOrder(mock1, mock2);

        assertNotNull(result);
        verify(reporterMock, never()).mocksHaveToBePassedWhenCreatingInOrder();
        verify(reporterMock, never()).nullPassedWhenCreatingInOrder();
        verify(reporterMock, never()).notAMockPassedWhenCreatingInOrder();
    }

    @Test
    public void testInOrderWithNoMocks() {
        Object[] mocks = new Object[]{};

        try {
            InOrder result = mockitoCore.inOrder(mocks);
            fail();
        } catch (Exception e) {
            verify(reporterMock).mocksHaveToBePassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testInOrderWithNullMock() {
        MyClass mock = null;

        try {
            InOrder result = mockitoCore.inOrder(mock);
            fail();
        } catch (Exception e) {
            verify(reporterMock).nullPassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testInOrderWithNotAMock() {
        MyClass mock = new MyClass();

        try {
            InOrder result = mockitoCore.inOrder(mock);
            fail();
        } catch (Exception e) {
            verify(reporterMock).notAMockPassedWhenCreatingInOrder();
        }
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber result = mockitoCore.doAnswer(answer);

        assertNotNull(result);
        verify(mockingProgressMock).stubbingStarted();
        verify(mockingProgressMock).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> handler = mock(MockHandlerInterface.class);
        when(mockUtilMock.getMockHandler(mock)).thenReturn(handler);

        VoidMethodStubbable result = mockitoCore.stubVoid(mock);

        assertNotNull(result);
        verify(mockingProgressMock).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockingProgressMock).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        List<Invocation> invocations = Arrays.asList(new Invocation(), new Invocation());
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();

        assertNotNull(result);
        assertEquals(invocations.get(invocations.size()-1), result);
    }
}