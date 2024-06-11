import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        String result = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(result);
        assertTrue(Mockito.mockingProgress().getMocks().contains(result));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        
        assertNotNull(stubbing);
    }
    
    @Test
    public void testStubWithMethodCall() {
        String methodCall = "test";
        
        DeprecatedOngoingStubbing<String> stubbing = mockitoCore.stub(methodCall);
        
        assertNotNull(stubbing);
    }
    
    @Test
    public void testWhen() {
        String methodCall = "test";
        
        OngoingStubbing<String> stubbing = mockitoCore.when(methodCall);
        
        assertNotNull(stubbing);
    }
    
    @Test
    public void testVerify() {
        String mock = mock(String.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        String result = mockitoCore.verify(mock, mode);
        
        assertEquals(mock, result);
        verify(mockitoCore).validateMockitoUsage();
    }
    
    @Test
    public void testReset() {
        String mock1 = mock(String.class);
        String mock2 = mock(String.class);
        
        mockitoCore.reset(mock1, mock2);
        
        verify(mockitoCore).validateMockitoUsage();
        verify(mockitoCore).resetOngoingStubbing();
        verify(mockitoCore).resetMock(mock1);
        verify(mockitoCore).resetMock(mock2);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        String mock = mock(String.class);
        
        mockitoCore.verifyNoMoreInteractions(mock);
        
        verify(mockitoCore).validateMockitoUsage();
        verify(mockitoCore).assertMocksNotEmpty(any(Object[].class));
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = Mockito.mock(List.class);
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockitoCore).validateMockitoUsage();
    }
    
    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        
        assertNotNull(inOrder);
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);
        
        Stubber stubber = mockitoCore.doAnswer(answer);
        
        assertNotNull(stubber);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        
        VoidMethodStubbable<Object> stubbable = mockitoCore.stubVoid(mock);
        
        assertNotNull(stubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        
        assertNotNull(lastInvocation);
    }
}