import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
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

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private MockingProgress mockingProgress;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockitoCore = new MockitoCore();
        mockingProgress = new ThreadSafeMockingProgress();
        mockUtil = new MockUtil();
    }

    @Test
    public void testMock() {
        ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, mockitoCore.new MockSettingsImpl(), true);
        assertNotNull(classToMock);
        assertTrue(mockUtil.isMock(classToMock));
    }

    @Test
    public void testStub() {
        IOngoingStubbing ongoingStubbing = mockitoCore.stub();
        assertNull(ongoingStubbing);

        ongoingStubbing = mockitoCore.stub("methodCall");
        assertNotNull(ongoingStubbing);
    }

    @Test
    public void testStubDeprecated() {
        DeprecatedOngoingStubbing<String> ongoingStubbing = mockitoCore.stub("methodCall");
        assertNotNull(ongoingStubbing);
    }

    @Test
    public void testWhen() {
        OngoingStubbing<String> ongoingStubbing = mockitoCore.when("methodCall");
        assertNotNull(ongoingStubbing);
    }

    @Test
    public void testVerify() {
        ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, mockitoCore.new MockSettingsImpl(), true);
        VerificationMode mode = mockitoCore.atLeastOnce();
        ClassToMock result = mockitoCore.verify(classToMock, mode);
        assertEquals(classToMock, result);
    }

    @Test
    public void testReset() {
        ClassToMock classToMock1 = mockitoCore.mock(ClassToMock.class, mockitoCore.new MockSettingsImpl(), true);
        ClassToMock classToMock2 = mockitoCore.mock(ClassToMock.class, mockitoCore.new MockSettingsImpl(), true);
        
        mockitoCore.reset(classToMock1, classToMock2);
        verify(mockUtil, times(2)).resetMock(classToMock1);
        verify(mockUtil, times(2)).resetMock(classToMock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        ClassToMock classToMock = mockitoCore.mock(ClassToMock.class);
        
        mockitoCore.verifyNoMoreInteractions(classToMock);
        verify(mockitoCore).assertMocksNotEmpty(new Object[] { classToMock });
        verify(mockitoCore.getMockHandler(classToMock)).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        ClassToMock classToMock1 = mockitoCore.mock(ClassToMock.class);
        ClassToMock classToMock2 = mockitoCore.mock(ClassToMock.class);
        
        InOrder inOrder = mockitoCore.inOrder(classToMock1, classToMock2);
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mockitoCore.mock(Answer.class);
        
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        ClassToMock classToMock = mockitoCore.mock(ClassToMock.class);
        
        VoidMethodStubbable<Void> stubbable = mockitoCore.stubVoid(classToMock);
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        verify(mockingProgress).validateState();
    }

    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = mockitoCore.mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Arrays.asList(new Invocation());
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals(allInvocations.get(0), lastInvocation);
    }
}

class ClassToMock {

}