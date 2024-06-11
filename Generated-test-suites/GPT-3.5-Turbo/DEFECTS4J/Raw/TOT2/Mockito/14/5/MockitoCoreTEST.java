import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MockSettings mockSettings = mock(MockSettings.class);
        assertNotNull(mockitoCore.mock(List.class, mockSettings));
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.stub());
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
        Object mock = mock(Object.class);
        VerificationMode mode = times(1);
        assertEquals(mock, mockitoCore.verify(mock, mode));
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);
        mockitoCore.reset(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = inOrder(mock(Object.class), mock(Object.class));
        mockitoCore.verifyNoMoreInteractionsInOrder(inOrder, null);
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.inOrder(mock(Object.class), mock(Object.class)));
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.doAnswer(null));
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
        assertNotNull(mockitoCore.getLastInvocation());
    }
}