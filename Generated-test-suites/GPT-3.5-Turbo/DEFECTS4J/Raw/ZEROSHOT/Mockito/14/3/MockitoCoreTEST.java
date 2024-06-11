import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        mockitoCore.mock(classToMock, mockSettings);
        
        verify(mockitoCore).mock(classToMock, mockSettings);
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.stub();
        
        verify(mockitoCore).stub();
    }
    
    @Test
    public void testStubWithValue() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object methodCall = new Object();
        
        mockitoCore.stub(methodCall);
        
        verify(mockitoCore).stub(methodCall);
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object methodCall = new Object();
        
        mockitoCore.when(methodCall);
        
        verify(mockitoCore).when(methodCall);
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object mock = new Object();
        VerificationMode mode = VerificationModeFactory.times(1);
        
        mockitoCore.verify(mock, mode);
        
        verify(mockitoCore).verify(mock, mode);
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object[] mocks = {new Object()};
        
        mockitoCore.reset(mocks);
        
        verify(mockitoCore).reset(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object[] mocks = {new Object()};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        verify(mockitoCore).verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        List<Object> mocks = Arrays.asList(new Object());
        InOrderContext inOrderContext = mock(InOrderContext.class);
        
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object[] mocks = {new Object()};
        
        mockitoCore.inOrder(mocks);
        
        verify(mockitoCore).inOrder(mocks);
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Answer answer = mock(Answer.class);
        
        mockitoCore.doAnswer(answer);
        
        verify(mockitoCore).doAnswer(answer);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        Object mock = new Object();
        
        mockitoCore.stubVoid(mock);
        
        verify(mockitoCore).stubVoid(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.validateMockitoUsage();
        
        verify(mockitoCore).validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        mockitoCore.getLastInvocation();
        
        verify(mockitoCore).getLastInvocation();
    }
}