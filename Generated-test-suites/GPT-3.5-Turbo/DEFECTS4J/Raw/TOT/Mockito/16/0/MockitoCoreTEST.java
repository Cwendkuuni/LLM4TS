
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.inOrder.InOrder;
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

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;

        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        if (stubbing != null) {
            fail("Expected stubbing to be null");
        }
    }

    @Test
    public void testDeprecatedStub() {
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

        Object result = mockitoCore.verify(mock, mode);

        assertSame(mock, result);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);

        mockitoCore.reset(mock);

        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockingProgress).reset();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
        verify(mockitoCore.mockUtil).resetMock(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockitoCore).assertMocksNotEmpty(new Object[]{mock});
        verify(mockitoCore.mockingProgress).validateState();
        verify(mockitoCore.mockUtil.getMockHandler(mock)).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
        verify(mockitoCore.mockingProgress).stubbingStarted();
        verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);

        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        assertNotNull(stubbable);
        verify(mockitoCore.mockingProgress).stubbingStarted();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        verify(mockitoCore.mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = new ArrayList<>();
        allInvocations.add(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation result = mockitoCore.getLastInvocation();

        assertEquals(invocation, result);
    }
}
