import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.*;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMock() {
        assertNotNull(mockitoCore.mock(MockitoCore.class, mock(MockitoCore.class)));
    }

    @Test
    public void testStub() {
        assertNull(mockitoCore.stub());
    }

    @Test
    public void testWhen() {
        assertNull(mockitoCore.when(null));
    }

    @Test
    public void testVerify() {
        assertNotNull(mockitoCore.verify(mock(MockitoCore.class), mock(VerificationMode.class)));
    }

    @Test
    public void testReset() {
        mockitoCore.reset(mock(MockitoCore.class));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(mock(MockitoCore.class));
    }

    @Test
    public void testInOrder() {
        assertNotNull(mockitoCore.inOrder(mock(MockitoCore.class)));
    }

    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer(mock(Stubber.class)));
    }

    @Test
    public void testStubVoid() {
        assertNotNull(mockitoCore.stubVoid(mock(MockitoCore.class)));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        assertNotNull(mockitoCore.getLastInvocation());
    }
}