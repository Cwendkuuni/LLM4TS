import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        assertNotNull(mockitoCore.mock(List.class, mockSettings));
    }
    
    @Test
    public void testStub() {
        assertNotNull(mockitoCore.stub());
    }
    
    @Test
    public void testStubWithMethodCall() {
        assertNotNull(mockitoCore.stub(new Object()));
    }
    
    @Test
    public void testWhen() {
        assertNotNull(mockitoCore.when(new Object()));
    }
    
    @Test
    public void testVerify() {
        assertNotNull(mockitoCore.verify(mockitoCore.mock(List.class, mockSettings), times(1)));
    }
    
    @Test
    public void testReset() {
        mockitoCore.reset(mockitoCore.mock(List.class, mockSettings));
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(mockitoCore.mock(List.class, mockSettings));
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockitoCore.mock(List.class, mockSettings)), inOrderContext);
    }
    
    @Test
    public void testInOrder() {
        assertNotNull(mockitoCore.inOrder(mockitoCore.mock(List.class, mockSettings)));
    }
    
    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer(answer));
    }
    
    @Test
    public void testStubVoid() {
        assertNotNull(mockitoCore.stubVoid(mockitoCore.mock(List.class, mockSettings)));
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