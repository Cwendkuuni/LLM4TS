import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        Object mockObject = mockitoCore.mock(String.class, MockSettings.withSettings(), true);
        assertNotNull(mockObject);
    }

    @Test
    public void testStubbingMethods() {
        IOngoingStubbing stub = mockitoCore.stub();
        assertNull(stub);
    }

    @Test
    public void testWhen() {
        OngoingStubbing stubbing = mockitoCore.when(mock(String.class));
        assertNull(stubbing);
    }

    @Test
    public void testVerify() {
        Object mockObject = mock(String.class);
        assertSame(mockObject, mockitoCore.verify(mockObject, VerificationMode.atLeastOnce()));
    }

    @Test
    public void testReset() {
        Object mockObject = mock(String.class);
        mockitoCore.reset(mockObject);
        verify(mockObject, never());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions("nonMock");
    }

    @Test
    public void testInOrder() {
        assertNotNull(mockitoCore.inOrder(mock(String.class)));
    }

    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer(null));
    }

    @Test
    public void testStubVoid() {
        Object mockObject = mock(String.class);
        assertNotNull(mockitoCore.stubVoid(mockObject));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        assertNull(mockitoCore.getLastInvocation());
    }
}