import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
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
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    @Test
    public void testMock() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Class<?> classToMock = Object.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // when
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(classToMock, result.getClass());
    }
    
    @Test
    public void testStub() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        
        // when
        IOngoingStubbing result = mockitoCore.stub();
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testStubDeprecated() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object methodCall = new Object();
        
        // when
        DeprecatedOngoingStubbing<Object> result = mockitoCore.stub(methodCall);
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testWhen() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object methodCall = new Object();
        
        // when
        OngoingStubbing<Object> result = mockitoCore.when(methodCall);
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testVerify() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        VerificationMode mode = null;
        
        // when
        Object result = mockitoCore.verify(mock, mode);
        
        // then
        Assert.assertEquals(mock, result);
    }
    
    @Test
    public void testReset() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[] { new Object() };
        
        // when
        mockitoCore.reset(mocks);
        
        // then
        // no exception should be thrown
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[] { new Object() };
        
        // when
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        // then
        // no exception should be thrown
    }
    
    @Test
    public void testInOrder() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = new Object[] { new Object() };
        
        // when
        InOrder result = mockitoCore.inOrder(mocks);
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testDoAnswer() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = null;
        
        // when
        Stubber result = mockitoCore.doAnswer(answer);
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testStubVoid() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        
        // when
        VoidMethodStubbable<Object> result = mockitoCore.stubVoid(mock);
        
        // then
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        
        // when
        mockitoCore.validateMockitoUsage();
        
        // then
        // no exception should be thrown
    }
    
    @Test
    public void testGetLastInvocation() {
        // given
        MockitoCore mockitoCore = new MockitoCore();
        
        // when
        Invocation result = mockitoCore.getLastInvocation();
        
        // then
        Assert.assertNull(result);
    }
}