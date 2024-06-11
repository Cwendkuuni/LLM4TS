
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
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

    private MockitoCore mockitoCore = new MockitoCore();

    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        T mock = mockitoCore.mock(classToMock, mockSettings);
        assertNotNull(mock);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNull(stubbing);
        verify(mockitoCore.mockUtil, times(1)).isMock(any());
    }

    @Test
    public void testStubWithMethodCall() {
        T methodCall = any();
        DeprecatedOngoingStubbing<T> stubbing = mockitoCore.stub(methodCall);
        assertNull(stubbing);
    }

    @Test
    public void testWhen() {
        T methodCall = any();
        OngoingStubbing<T> stubbing = mockitoCore.when(methodCall);
        assertNull(stubbing);
    }

    @Test
    public void testVerify() {
        T mock = any();
        VerificationMode mode = times(1);
        T result = mockitoCore.verify(mock, mode);
        assertEquals(mock, result);
        verify(mockitoCore.mockUtil, times(1)).isMock(any());
    }

    @Test
    public void testReset() {
        T[] mocks = { any() };
        mockitoCore.reset(mocks);
        verify(mockitoCore.mockUtil, times(1)).resetMock(any());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = { any() };
        mockitoCore.verifyNoMoreInteractions(mocks);
        verify(mockitoCore.mockUtil, times(1)).getMockHandler(any());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Arrays.asList(any());
        InOrderContext inOrderContext = mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockitoCore.mockUtil, times(1)).getMockHandler(any());
    }

    @Test
    public void testInOrder() {
        Object[] mocks = { any() };
        InOrder inOrder = mockitoCore.inOrder(mocks);
        assertNotNull(inOrder);
        verify(mockitoCore.mockUtil, times(2)).isMock(any());
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
        verify(mockitoCore.mockingProgress, times(2)).stubbingStarted();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        T mock = any();
        VoidMethodStubbable<T> stubbable = mockitoCore.stubVoid(mock);
        assertNotNull(stubbable);
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        verify(mockitoCore.mockingProgress, times(1)).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mockitoCore.getLastInvocation();
        assertNull(invocation);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockitoCore.mockingProgress, times(1)).reset();
    }

}
