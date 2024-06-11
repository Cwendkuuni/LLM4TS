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
import static org.junit.Assert.assertEquals;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object mocked = mockitoCore.mock(String.class, mockSettings);
        assertEquals( String.class, mocked.getClass());
    }

    @Test
    public void testStub() {
        IOngoingStubbing ongoingStubbing = mockitoCore.stub();
        assertEquals( null, ongoingStubbing);
    }

    @Test
    public void testWhen() {
        String testString = "test";
        OngoingStubbing<String> ongoingStubbing = mockitoCore.when(testString);
        assertEquals( null, ongoingStubbing);
    }

    @Test
    public void testVerify() {
        String mock = null;
        VerificationMode mode = VerificationModeFactory.atLeastOnce();
        String verified = mockitoCore.verify(mock, mode);
        assertEquals( null, verified);
    }

    @Test
    public void testReset() {
        String mock = "mock";
        mockitoCore.reset(mock);
        assertEquals(mock, mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = new Object[]{};
        mockitoCore.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testInOrder() {
        Object[] mocks = new Object[]{};
        InOrder inOrder = mockitoCore.inOrder(mocks);
        assertEquals( 0, inOrder.getMockCount());
    }

    @Test
    public void testDoAnswer() {
        Answer<String> answer = new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Hello";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        String mock = "mock";
        VoidMethodStubbable<String> result = mockitoCore.stubVoid(mock);
        assertEquals( mock, result.getMock());
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Invocation invocation = mockitoCore.getLastInvocation();
        assertEquals( null, invocation);
    }
}