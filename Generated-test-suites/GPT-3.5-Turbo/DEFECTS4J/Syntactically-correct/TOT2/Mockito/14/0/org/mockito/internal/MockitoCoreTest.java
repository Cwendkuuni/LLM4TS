import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Reporter reporter = mock(Reporter.class);

        when(reporter).mock(classToMock).thenReturn(mockitoCore);
        assertNotNull(mockitoCore.mock(reporter.getClass(), mockitoCore));
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();

        OngoingStubbingImpl stubbing = mock(OngoingStubbingImpl.class);
        mockitoCore.stub();
        verify(mockitoCore).stub();
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();

        OngoingStubbingImpl stubbing = mock(OngoingStubbingImpl.class);
        mockitoCore.when(mockitoCore);
        verify(mockitoCore).when(mockitoCore);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        MockingProgress mockingProgress = mock(MockingProgress.class);
        NotAMockException notAMockException = mock(NotAMockException.class);

        when(mockingProgress).verify(any(), any()).thenThrow(notAMockException);
        assertNotNull(mockitoCore.verify(mockingProgress, any()));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {mock(MockingProgress.class)};

        mockitoCore.reset(mocks);
        verify(mockitoCore).reset(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);

        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);

        mockitoCore.inOrder(mock);
        verify(mockitoCore).inOrder(mock);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.doAnswer(any());
        verify(mockitoCore).doAnswer(any());
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);

        mockitoCore.stubVoid(mock);
        verify(mockitoCore).stubVoid(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.validateMockitoUsage();
        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();

        OngoingStubbingImpl stubbing = mock(OngoingStubbingImpl.class);
        mockitoCore.getLastInvocation();
        verify(mockitoCore).getLastInvocation();
    }

}