import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    // Testing 'mock' method
    @Test
    public void testMock() {
        ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, Mockito.mock(MockSettings.class));

        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);
    }

    // Testing 'stub' method when stubbing exists
    @Test
    public void testStub_existingStubbing() {
        OngoingStubbing ongoingStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.when(mockitoCore.stub()).thenReturn(ongoingStubbing);

        assertEquals(ongoingStubbing, mockitoCore.stub());
    }

    // Testing 'stub' method when no stubbing exists
    @Test(expected = RuntimeException.class)
    public void testStub_noStubbing() {
        Mockito.when(mockitoCore.stub()).thenReturn(null);

        mockitoCore.stub();
    }

    // Testing 'stub' method with methodCall
    @Test
    public void testStub_methodCall() {
        OngoingStubbing ongoingStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.when(mockitoCore.stub(any())).thenReturn(ongoingStubbing);

        assertEquals(ongoingStubbing, mockitoCore.stub(new Object()));
    }

    // Testing 'when' method with methodCall
    @Test
    public void testWhen_methodCall() {
        OngoingStubbing ongoingStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.when(mockitoCore.when(any())).thenReturn(ongoingStubbing);

        assertEquals(ongoingStubbing, mockitoCore.when(new Object()));
    }

    // Testing 'verify' method with null mock
    @Test(expected = RuntimeException.class)
    public void testVerify_nullMock() {
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);

        mockitoCore.verify(null, verificationMode);
    }

    // Testing 'verify' method with non-mock object
    @Test(expected = NotAMockException.class)
    public void testVerify_nonMock() {
        Object nonMockObject = new Object();
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);

        mockitoCore.verify(nonMockObject, verificationMode);
    }

    // Testing 'reset' method with mocks
    @Test
    public void testReset() {
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);

        mockitoCore.reset(mock1, mock2);

        verify(mock1).reset();
        verify(mock2).reset();
    }

    // Testing 'verifyNoMoreInteractions' method with mocks - no interactions
    @Test
    public void testVerifyNoMoreInteractions_noInteractions() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        mockitoCore.verifyNoMoreInteractions(mock);

        verify(mockitoCore.getMockHandler(mock)).getInvocationContainer().getInvocations();
        verifyZeroInteractions(mock);
    }

    // Testing 'verifyNoMoreInteractions' method with mocks - null mock
    @Test(expected = RuntimeException.class)
    public void testVerifyNoMoreInteractions_nullMock() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    // Testing 'verifyNoMoreInteractions' method with mocks - not a mock
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_notAMock() {
        Object nonMockObject = new Object();

        mockitoCore.verifyNoMoreInteractions(nonMockObject);
    }

    // Testing 'verifyNoMoreInteractionsInOrder' method with mocks
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        List<Object> mocks = Arrays.asList(Mockito.mock(ClassToMock.class));

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockitoCore.getMockHandler(mocks.get(0))).getInvocationContainer().getInvocations();
    }

    // Testing 'assertMocksNotEmpty' method with empty mocks array
    @Test(expected = RuntimeException.class)
    public void testAssertMocksNotEmpty_emptyMocksArray() {
        mockitoCore.assertMocksNotEmpty(new Object[]{});
    }

    // Testing 'assertMocksNotEmpty' method with null mocks array
    @Test(expected = RuntimeException.class)
    public void testAssertMocksNotEmpty_nullMocksArray() {
        mockitoCore.assertMocksNotEmpty(null);
    }

    // Testing 'inOrder' method with mocks
    @Test
    public void testInOrder() {
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        assertNotNull(inOrder);
        assertTrue(inOrder instanceof InOrder);
    }

    // Testing 'doAnswer' method
    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);
        Stubber stubber = Mockito.mock(Stubber.class);
        Mockito.when(mockitoCore.doAnswer(answer)).thenReturn(stubber);

        assertEquals(stubber, mockitoCore.doAnswer(answer));
    }

    // Testing 'stubVoid' method
    @Test
    public void testStubVoid() {
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        VoidMethodStubbable stubbable = Mockito.mock(VoidMethodStubbable.class);
        Mockito.when(mockitoCore.stubVoid(mock)).thenReturn(stubbable);

        assertEquals(stubbable, mockitoCore.stubVoid(mock));
    }

    // Testing 'validateMockitoUsage' method
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        // Validate state is implicitly checked by other methods, no explicit assertion needed
    }

    // Testing 'getLastInvocation' method
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation);

        Mockito.when(mockitoCore.getLastInvocation()).thenCallRealMethod();
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
        Mockito.when(mockitoCore.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        assertEquals(invocation, mockitoCore.getLastInvocation());
    }

    @SuppressWarnings("unchecked")
    private static class ClassToMock {
        public void reset() {}
    }
}