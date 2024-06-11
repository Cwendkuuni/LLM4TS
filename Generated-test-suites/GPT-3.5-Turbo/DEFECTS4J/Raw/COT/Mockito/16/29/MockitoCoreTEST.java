
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.*;
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

public class MockitoCoreTest {

    MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock_ClassToMockMockSettings_ShouldReturnMockObject() {
        ClassToMock obj = mockitoCore.mock(ClassToMock.class, new MockSettingsImpl(), true);
        assertNotNull(obj);
        assertTrue(obj instanceof ClassToMock);
    }

    @Test
    public void testMock_ClassToMockMockSettings_ShouldResetOngoingStubbing_ShouldReturnMockObject() {
        ClassToMock obj = mockitoCore.mock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(obj);
        assertTrue(obj instanceof ClassToMock);
    }

    @Test
    public void testStub_ShouldReturnOngoingStubbingObject() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
        assertTrue(stubbing instanceof IOngoingStubbing);
    }

    @Test(expected = NullPointerException.class)
    public void testStub_TMethodCall_ShouldThrowNullPointerException() {
        mockitoCore.stub(null);
    }

    @Test
    public void testWhen_TMethodCall_ShouldReturnOngoingStubbingObject() {
        OngoingStubbing stubbing = mockitoCore.when(new ClassToMock());
        assertNotNull(stubbing);
        assertTrue(stubbing instanceof OngoingStubbing);
    }

    @Test(expected = NullPointerException.class)
    public void testWhen_TMethodCall_ShouldThrowNullPointerException() {
        mockitoCore.when(null);
    }

    @Test(expected = NullPointerException.class)
    public void testVerify_ObjectVerificationMode_ShouldThrowNullPointerException() {
        mockitoCore.verify(null, VerificationMode.times(1));
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMockPassedToVerify_ShouldThrowNotAMockException() {
        mockitoCore.verify(new Object(), VerificationMode.times(1));
    }

    @Test
    public void testVerify_ObjectVerificationMode_ShouldReturnObject() {
        ClassToMock obj = new ClassToMock();
        ClassToMock result = mockitoCore.verify(obj, VerificationMode.times(1));
        assertNotNull(result);
        assertSame(obj, result);
    }

    @Test
    public void testReset_Mocks_MockUtilResetMockShouldBeCalledForEachMock() {
        ClassToMock mock1 = new ClassToMock();
        ClassToMock mock2 = new ClassToMock();
        mockitoCore.reset(mock1, mock2);
        assertTrue(mock1.isReset());
        assertTrue(mock2.isReset());
    }

    @Test
    public void testVerifyNoMoreInteractions_Mocks_NullPassedToVerifyNoMoreInteractions_ShouldThrowReporterException() {
        ClassToMock mock = new ClassToMock();
        Object[] mocks = { null, mock };
        assertThrows(NullPointerException.class, () -> mockitoCore.verifyNoMoreInteractions(mocks));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NotAMockPassedToVerifyNoMoreInteractions_ShouldThrowNotAMockException() {
        mockitoCore.verifyNoMoreInteractions(new Object());
    }

    @Test
    public void testInOrder_Mocks_EmptyMocksPassedWhenCreatingInOrder_ShouldThrowReporterException() {
        Object[] mocks = {};
        assertThrows(ReporterException.class, () -> mockitoCore.inOrder(mocks));
    }

    @Test(expected = ReporterException.class)
    public void testInOrder_Mocks_NullPassedWhenCreatingInOrder_ShouldThrowReporterException() {
        Object[] mocks = { null };
        mockitoCore.inOrder(mocks);
    }

    @Test(expected = ReporterException.class)
    public void testInOrder_Mocks_NotAMockPassedWhenCreatingInOrder_ShouldThrowReporterException() {
        Object[] mocks = { new Object() };
        mockitoCore.inOrder(mocks);
    }

    @Test
    public void testInOrder_Mocks_ValidMocksPassedWhenCreatingInOrder_ShouldReturnInOrderObject() {
        ClassToMock mock1 = new ClassToMock();
        ClassToMock mock2 = new ClassToMock();
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        assertNotNull(inOrder);
        assertTrue(inOrder instanceof InOrder);
    }

    @Test
    public void testDoAnswer_Answer_ShouldReturnStubberObject() {
        Answer answer = invocation -> null;
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
        assertTrue(stubber instanceof Stubber);
    }

    @Test
    public void testStubVoid_Mock_ShouldReturnVoidMethodStubbableObject() {
        ClassToMock mock = new ClassToMock();
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbable);
    }

    @Test
    public void testValidateMockitoUsage_ValidateStateShouldBeCalled() {
        mockitoCore.validateMockitoUsage();
        // Validate that the mockingProgress.validateState() method is called inside validateMockitoUsage()
    }

    @Test
    public void testGetLastInvocation_OngoingStubbingPersists_ShouldReturnLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = new OngoingStubbingImpl();
        Invocation invocation = new Invocation();
        
        ongoingStubbing.registerInvocation(invocation);
        mockingProgress.pushOngoingStubbing(ongoingStubbing);
        
        Invocation result = mockitoCore.getLastInvocation();
        assertNotNull(result);
        assertSame(invocation, result);
    }

    class ClassToMock {
        public boolean isReset() {
            return true;
        }
    }

}
