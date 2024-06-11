import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        mockingProgress = new ThreadSafeMockingProgress();
    }
    
    @Test
    public void testMock() {
        assertNotNull(mockitoCore.mock(String.class, MockSettings.defaultMockingSettings()));
    }
    
    @Test
    public void testStub() {
        OngoingStubbing ongoingStubbing = mockitoCore.when(mockitoCore.mock(String.class));
        assertNotNull(ongoingStubbing);
    }
    
    @Test
    public void testVerify() {
        Object mock = mock(String.class);
        assertNotNull(mockitoCore.verify(mock, Mockito.times(1)));
    }
    
    @Test
    public void testReset() {
        Object mock = mock(String.class);
        mockitoCore.reset(mock);
        
        // Verify that the mock was reset successfully
        verify(mock, Mockito.times(0)).mockMethod();
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(String.class);
        mockitoCore.verifyNoMoreInteractions(mock);
        
        // Verify that no more interactions occurred on the mock
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void testInOrder() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        
        assertNotNull(mockitoCore.inOrder(mock1, mock2));
    }
    
    @Test
    public void testDoAnswer() {
        assertNotNull(mockitoCore.doAnswer((Answer) invocation -> null));
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(String.class);
        VoidMethodStubbable voidMethodStubbable = mockitoCore.stubVoid(mock);
        
        assertNotNull(voidMethodStubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        // No exception should be thrown if Mockito usage is valid
    }
    
    @Test
    public void testGetLastInvocation() {
        assertNull(mockitoCore.getLastInvocation());
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        mockitoCore.verify("not a mock", Mockito.times(1));
    }
    
    @Test
    public void testVerifyNullMock() {
        assertNull(mockitoCore.verify(null, Mockito.times(1)));
    }
    
    @Test
    public void testVerifyNoMoreInteractionsNullMock() {
        mockitoCore.verifyNoMoreInteractions(null);
        
        // No exception should be thrown for null mock
    }
    
    @Test
    public void testVerifyNoMoreInteractionsNotAMock() {
        mockitoCore.verifyNoMoreInteractions("not a mock");
        
        // No exception should be thrown for not a mock
    }
    
    @Test
    public void testAssertMocksNotEmptyNullMocks() {
        Object[] mocks = null;
        mockitoCore.assertMocksNotEmpty(mocks);
        
        // No exception should be thrown for null mocks
    }
    
    @Test
    public void testAssertMocksNotEmptyEmptyMocks() {
        Object[] mocks = {};
        mockitoCore.assertMocksNotEmpty(mocks);
        
        // No exception should be thrown for empty mocks
    }
    
}