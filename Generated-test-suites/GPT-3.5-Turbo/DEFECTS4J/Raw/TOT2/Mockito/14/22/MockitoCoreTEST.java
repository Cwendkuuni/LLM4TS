import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Reporter reporter = new Reporter();
        Object mock = mockitoCore.mock(Object.class, Mockito.mockSettings());
        assertNotNull(mock);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        MockingProgress mockingProgress = mock(MockingProgress.class);
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        assertEquals(ongoingStubbing, mockitoCore.stub());
    }

    @Test
    public void testStubWithMethodCall() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.stub(new Object()));
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.when(new Object()));
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Object mock = mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        assertEquals(mock, mockitoCore.verify(mock, mode));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mockitoCore.reset(mock1, mock2);
        verify(mockitoCore.mockUtil).resetMock(mock1);
        verify(mockitoCore.mockUtil).resetMock(mock2);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        assertNotNull(mockitoCore.inOrder(mock1, mock2));
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = Mockito.mock(Answer.class);
        assertNotNull(mockitoCore.doAnswer(answer));
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);
        assertNotNull(mockitoCore.stubVoid(mock));
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Collections.singletonList(invocation));
        assertEquals(invocation, mockitoCore.getLastInvocation());
    }
}