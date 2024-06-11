import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
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
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        assertNotNull(mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing));
    }

    @Test
    public void testMockWithoutReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        assertNotNull(mockitoCore.mock(classToMock, mockSettings));
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.stub());
    }

    @Test
    public void testDeprecatedStub() {
        MockitoCore mockitoCore = new MockitoCore();
        T methodCall = Mockito.mock(T.class);
        assertNotNull(mockitoCore.stub(methodCall));
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        T methodCall = Mockito.mock(T.class);
        assertNotNull(mockitoCore.when(methodCall));
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        T mock = Mockito.mock(T.class);
        VerificationMode mode = Mockito.times(1);
        assertNotNull(mockitoCore.verify(mock, mode));
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        T[] mocks = new T[]{Mockito.mock(T.class)};
        mockitoCore.reset(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        T[] mocks = new T[]{Mockito.mock(T.class)};
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        T[] mocks = new T[]{Mockito.mock(T.class)};
        assertNotNull(mockitoCore.inOrder(mocks));
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = Mockito.mock(Answer.class);
        assertNotNull(mockitoCore.doAnswer(answer));
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        T mock = Mockito.mock(T.class);
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