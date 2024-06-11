import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Class classToMock = ClassToMock.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = false;

        ClassToMock mockedObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        assertNotNull(mockedObject);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("mock"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();

        assertNotNull(stubbing);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("stub"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testStub_T() {
        String methodCall = "test";

        DeprecatedOngoingStubbing stubbing = mockitoCore.stub(methodCall);

        assertNotNull(stubbing);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("stub"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testWhen() {
        String methodCall = "test";

        OngoingStubbing stubbing = mockitoCore.when(methodCall);

        assertNotNull(stubbing);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("stub"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testVerify() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        ClassToMock verifiedMock = mockitoCore.verify(mock, mode);

        assertNotNull(verifiedMock);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("verify"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testReset() {
        ClassToMock[] mocks = {Mockito.mock(ClassToMock.class)};

        mockitoCore.reset(mocks);

        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("reset"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("verifyNoMoreInteractions"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testInOrder() {
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        assertNotNull(inOrder);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("inOrder"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);

        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("doAnswer"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testStubVoid() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);

        assertNotNull(stubbable);
        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("stubVoid"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();

        assertTrue(mockitoCore.getLastInvocation().getMethod().getName().equals("validateMockitoUsage"));
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
    }

    private static class ClassToMock {
        // Class to use for Mockito mocking
    }

}