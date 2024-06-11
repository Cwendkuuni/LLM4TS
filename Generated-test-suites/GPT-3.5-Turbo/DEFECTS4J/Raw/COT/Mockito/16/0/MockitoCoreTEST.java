
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.MockUtil;
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
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {
    
    @Test
    public void testMock(){
        MockitoCore mockitoCore = new MockitoCore();
        Class classToMock = Class.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        
        mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Assert something
    }
    
    @Test
    public void testMock2(){
        MockitoCore mockitoCore = new MockitoCore();
        Class classToMock = Class.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        mockitoCore.mock(classToMock, mockSettings);
        
        // Assert something
    }
    
    @Test
    public void testStub(){
        MockitoCore mockitoCore = new MockitoCore();
        
        mockitoCore.stub();
        
        // Assert something
    }
    
    @Test
    public void testStub2(){
        MockitoCore mockitoCore = new MockitoCore();
        Object methodCall = new Object();
        
        DeprecatedOngoingStubbing<?> result = mockitoCore.stub(methodCall);
        
        // Assert something
    }
    
    @Test
    public void testWhen(){
        MockitoCore mockitoCore = new MockitoCore();
        Object methodCall = new Object();
        
        OngoingStubbing<?> result = mockitoCore.when(methodCall);
        
        // Assert something
    }
    
    @Test
    public void testVerify(){
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        VerificationMode mode = VerificationMode.STRICT;
        
        mockitoCore.verify(mock, mode);
        
        // Assert something
    }
    
    @Test
    public void testReset(){
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {new Object()};
        
        mockitoCore.reset(mocks);
        
        // Assert something
    }
    
    @Test
    public void testVerifyNoMoreInteractions(){
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {new Object()};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // Assert something
    }
    
    @Test
    public void testInOrder(){
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {new Object()};
        
        mockitoCore.inOrder(mocks);
        
        // Assert something
    }
    
    @Test
    public void testDoAnswer(){
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        
        mockitoCore.doAnswer(answer);
        
        // Assert something
    }
    
    @Test
    public void testStubVoid(){
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        
        mockitoCore.stubVoid(mock);
        
        // Assert something
    }
    
    @Test
    public void testValidateMockitoUsage(){
        MockitoCore mockitoCore = new MockitoCore();
        
        mockitoCore.validateMockitoUsage();
        
        // Assert something
    }
    
    @Test
    public void testGetLastInvocation(){
        MockitoCore mockitoCore = new MockitoCore();
        
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert something
    }
    
    
    
}

