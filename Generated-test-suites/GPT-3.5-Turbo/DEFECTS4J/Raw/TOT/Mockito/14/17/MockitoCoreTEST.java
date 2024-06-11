import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    // Test mock method
    @Test
    public void testMock() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        assertNotNull(mockitoCore);
    }

    // Test stub method
    @Test
    public void testStub() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stub()).thenReturn(mock(OngoingStubbing.class));
        
        OngoingStubbing stubbing = mockitoCore.stub();
        
        assertNotNull(stubbing);
    }

    // Test deprecated stub method
    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stub(any())).thenReturn(mock(OngoingStubbing.class));

        OngoingStubbing stubbing = mockitoCore.stub(null);

        assertNotNull(stubbing);
    }

    // Test when method
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stub()).thenReturn(mock(OngoingStubbing.class));

        OngoingStubbing stubbing = mockitoCore.when(null);

        assertNotNull(stubbing);
    }

    // Test verify method with null mock
    @Test
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore.reporter = reporter;

        mockitoCore.verify(null, Mockito.times(1));

        verify(reporter, times(1)).nullPassedToVerify();
    }

    // Test verify method with non-mock object
    @Test
    public void testVerifyWithNonMockObject() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore.reporter = reporter;

        mockitoCore.verify(new Object(), Mockito.times(1));

        verify(reporter, times(1)).notAMockPassedToVerify();
    }

    // Test reset method
    @Test
    public void testReset() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        mockitoCore.mockUtil = mockUtil;
        mockitoCore.mockingProgress = mockingProgress;

        Object mock1 = new Object();
        Object mock2 = new Object();
        Object[] mocks = {mock1, mock2};

        mockitoCore.reset(mocks);

        verify(mockingProgress, times(1)).validateState();
        verify(mockingProgress, times(1)).reset();
        verify(mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockUtil, times(1)).resetMock(mock1);
        verify(mockUtil, times(1)).resetMock(mock2);
    }

    // Test verifyNoMoreInteractions method with null mock
    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stub()).thenReturn(mock(OngoingStubbing.class));

        mockitoCore.verifyNoMoreInteractions(null);

        Reporter reporter = mockitoCore.reporter;
        verify(reporter, times(1)).nullPassedToVerifyNoMoreInteractions();
    }

    // Test verifyNoMoreInteractionsInOrder method
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        InOrder inOrder = Mockito.mock(InOrder.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mockitoCore);
        mocks.add(inOrder);
        AllInvocationsFinder finder = Mockito.mock(AllInvocationsFinder.class);
        VerificationDataInOrderImpl verificationDataInOrder = new VerificationDataInOrderImpl(inOrder, finder.find(mocks), null);
        mockitoCore.mockingProgress = Mockito.mock(MockingProgress.class);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrder);

        VerificationModeFactory.noMoreInteractions().verifyInOrder(verificationDataInOrder);
    }

    // Test inOrder method
    @Test
    public void testInOrderWithNullMocks() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore.reporter = reporter;
        Object[] mocks = null;

        mockitoCore.inOrder(mocks);

        verify(reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
    }

    // Test inOrder method with null argument
    @Test
    public void testInOrderWithNullArgument() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore.reporter = reporter;
        Object[] mocks = {null};

        mockitoCore.inOrder(mocks);

        verify(reporter, times(1)).nullPassedWhenCreatingInOrder();
    }

    // Test inOrder method with non-mock object
    @Test
    public void testInOrderWithNonMockObject() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        mockitoCore.reporter = reporter;
        Object[] mocks = {new Object()};

        mockitoCore.inOrder(mocks);

        verify(reporter, times(1)).notAMockPassedWhenCreatingInOrder();
    }

    // Test doAnswer method
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stub()).thenReturn(mock(OngoingStubbing.class));

        Stubber stubber = mockitoCore.doAnswer(null);

        assertNotNull(stubber);
    }

    // Test stubVoid method
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        when(mockitoCore.stubVoid(any())).thenReturn(mock(VoidMethodStubbable.class));

        VoidMethodStubbable stubbable = mockitoCore.stubVoid(new Object());

        assertNotNull(stubbable);
    }

    // Test validateMockitoUsage method
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        mockitoCore.mockingProgress = mockingProgress;

        mockitoCore.validateMockitoUsage();

        verify(mockingProgress, times(1)).validateState();
    }

    // Test getLastInvocation method
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Mockito.when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation = Mockito.mock(Invocation.class);
        invocations.add(invocation);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);

        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertEquals(invocation, lastInvocation);
    }

}