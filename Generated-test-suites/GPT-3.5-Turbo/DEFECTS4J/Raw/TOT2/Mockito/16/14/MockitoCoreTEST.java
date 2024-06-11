import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockitoCore;
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

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.stubVoid;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettingsImpl();
        assertNotNull(mockitoCore.mock(Object.class, mockSettings));
    }
    
    @Test
    public void testStub() {
        assertNull(mockitoCore.stub());
    }
    
    @Test
    public void testStubDeprecated() {
        assertNull(mockitoCore.stub(new Object()));
    }
    
    @Test
    public void testWhen() {
        assertNull(mockitoCore.when(new Object()));
    }
    
    @Test
    public void testVerify() {
        Object mock = mock(Object.class);
        assertNotNull(mockitoCore.verify(mock, VerificationMode.strict()));
    }
    
    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        mockitoCore.reset(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        assertNotNull(mockitoCore.inOrder(mock1, mock2));
    }
    
    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer(invocation -> null));
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        assertNotNull(mockitoCore.stubVoid(mock));
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        // no assertion as this method doesn't return anything
    }

    @Test
    public void testGetLastInvocation() {
        assertNotNull(mockitoCore.getLastInvocation());
    }

}