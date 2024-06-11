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
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class mockClass = MockSettings.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = false;

        mockitoCore.mock(mockClass, mockSettings, shouldResetOngoingStubbing);

        verify(mockitoCore.getMockUtil()).createMock(mockClass, (MockSettingsImpl) mockSettings);
        verify(mockitoCore.getMockingProgress().validateState());
        verify(mockitoCore.getMockingProgress().resetOngoingStubbing());
    }

    @Test
    public void testMockArg() {
        Class mockClass = MockSettings.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);

        mockitoCore.mock(mockClass, mockSettings);

        verify(mockitoCore.getMockingProgress().validateState());
        verify(mockitoCore.getMockingProgress().resetOngoingStubbing());
        verify(mockitoCore.getMockUtil()).createMock(mockClass, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        Reporter reporter = Mockito.mock(Reporter.class);

        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.getMockUtil().isMock(Mockito.anyObject())).thenReturn(true);

        mockitoCore.stub();

        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testStubDeprecated() {
        Object methodCall = Mockito.mock(Object.class);
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        Reporter reporter = Mockito.mock(Reporter.class);

        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.getMockUtil().isMock(Mockito.anyObject())).thenReturn(true);

        mockitoCore.stub(methodCall);

        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testWhen() {
        Object methodCall = Mockito.mock(Object.class);
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        Reporter reporter = Mockito.mock(Reporter.class);

        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.getMockUtil().isMock(Mockito.anyObject())).thenReturn(true);

        mockitoCore.when(methodCall);

        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(reporter).missingMethodInvocation();
    }

    @Test
    public void testVerify() {
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        Reporter reporter = Mockito.mock(Reporter.class);

        when(mockitoCore.getMockUtil().isMock(mock)).thenReturn(true);

        mockitoCore.verify(mock, mode);

        verify(mockitoCore.getMockUtil()).isMock(mock);
        verify(mockitoCore.getMockingProgress()).verificationStarted(mode);
    }

    @Test
    public void testReset() {
        Object[] mocks = new Object[1];
        mocks[0] = Mockito.mock(Object.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);

        when(mockitoCore.getMockUtil()).thenReturn(mockUtil);
        when(mockitoCore.getMockingProgress()).thenReturn(mockingProgress);

        mockitoCore.reset(mocks);

        verify(mockitoCore.getMockingProgress()).validateState();
        verify(mockitoCore.getMockingProgress()).reset();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
        verify(mockitoCore.getMockUtil()).resetMock(mocks[0]);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[1];
        mocks[0] = Mockito.mock(Object.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);

        when(mockitoCore.getMockUtil()).thenReturn(mockUtil);
        when(mockitoCore.getMockingProgress()).thenReturn(mockingProgress);
        when(mockitoCore.getMockUtil().getMockHandler(mocks[0])).thenReturn(Mockito.mock(MockHandlerInterface.class));

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(mockitoCore.getMockingProgress()).validateState();
        verify(mockitoCore.getMockUtil()).getMockHandler(mocks[0]);
        verify(mockitoCore.getMockUtil().getMockHandler(mocks[0])).verifyNoMoreInteractions();
    }

    @Test
    public void testAssertMocksNotEmpty() {
        Object[] mocks = null;
        Reporter reporter = Mockito.mock(Reporter.class);

        mockitoCore.assertMocksNotEmpty(mocks);

        verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[1];
        mocks[0] = Mockito.mock(Object.class);
        Reporter reporter = Mockito.mock(Reporter.class);

        when(mockitoCore.getMockUtil()).thenReturn(mockitoCore.getMockUtil());

        mockitoCore.inOrder(mocks);

        verify(mockitoCore.getMockUtil()).isMock(mocks[0]);
        verify(reporter).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.getMockUtil().getMockHandler(mocks[0])).verifyNoMoreInteractions();
    }

    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);

        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(stubbing);
        when(mockitoCore.getMockingProgress().resetOngoingStubbing()).thenReturn(ongoingStubbing);

        mockitoCore.doAnswer(answer);

        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
        verify(ongoingStubbing).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface handler = Mockito.mock(MockHandlerInterface.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);

        when(mockitoCore.getMockUtil()).thenReturn(mockitoCore.getMockUtil());
        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(mockitoCore.getMockUtil().getMockHandler(mock)).thenReturn(handler);

        mockitoCore.stubVoid(mock);

        verify(mockitoCore.getMockingProgress()).stubbingStarted();
        verify(handler).voidMethodStubbable(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);

        when(mockitoCore.getMockingProgress()).thenReturn(mockingProgress);

        mockitoCore.validateMockitoUsage();

        verify(mockitoCore.getMockingProgress()).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        IOngoingStubbing ongoingStubbing = Mockito.mock(IOngoingStubbing.class);
        OngoingStubbingImpl ongoingStubbingImpl = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        when(mockitoCore.getMockingProgress().pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(invocation));

        mockitoCore.getLastInvocation();

        verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
        verify(ongoingStubbing).getRegisteredInvocations();
    }
}