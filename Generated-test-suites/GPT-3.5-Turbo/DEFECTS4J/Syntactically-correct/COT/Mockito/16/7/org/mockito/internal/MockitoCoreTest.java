import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.*;
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

public class MockitoCoreTest {

   @Test
   public void testMock() {
      MockitoCore mockitoCore = new MockitoCore();
      Class<T> classToMock = T.class;
      MockSettings mockSettings = new MockSettingsImpl();
      boolean shouldResetOngoingStubbing = false;
      T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
      assertNotNull(result);
   }
   
   @Test
   public void testMockWithSettings() {
      MockitoCore mockitoCore = new MockitoCore();
      Class<T> classToMock = T.class;
      MockSettings mockSettings = new MockSettingsImpl();
      T result = mockitoCore.mock(classToMock, mockSettings);
      assertNotNull(result);
   }
   
   @Test
   public void testStub() {
      MockitoCore mockitoCore = new MockitoCore();
      IOngoingStubbing result = mockitoCore.stub();
      assertNotNull(result);
   }
   
   @Deprecated
   @Test
   public void testStubDeprecated() {
      MockitoCore mockitoCore = new MockitoCore();
      T methodCall = new T();
      DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);
      assertNotNull(result);
   }
   
   @Test
   public void testWhen() {
      MockitoCore mockitoCore = new MockitoCore();
      T methodCall = new T();
      OngoingStubbing<T> result = mockitoCore.when(methodCall);
      assertNotNull(result);
   }
   
   @Test
   public void testVerify() {
      MockitoCore mockitoCore = new MockitoCore();
      T mock = new T();
      VerificationMode mode = new VerificationMode();
      T result = mockitoCore.verify(mock, mode);
      assertNotNull(result);
   }
   
   @Test
   public void testReset() {
      MockitoCore mockitoCore = new MockitoCore();
      T[] mocks = new T[3];
      mockitoCore.reset(mocks);
   }
   
   @Test
   public void testVerifyNoMoreInteractions() {
      MockitoCore mockitoCore = new MockitoCore();
      Object[] mocks = new Object[3];
      mockitoCore.verifyNoMoreInteractions(mocks);
   }
   
   @Test
   public void testInOrder() {
      MockitoCore mockitoCore = new MockitoCore();
      Object[] mocks = new Object[2];
      InOrder result = mockitoCore.inOrder(mocks);
      assertNotNull(result);
   }
   
   @Test
   public void testDoAnswer() {
      MockitoCore mockitoCore = new MockitoCore();
      Answer answer = new Answer();
      Stubber result = mockitoCore.doAnswer(answer);
      assertNotNull(result);
   }
   
   @Test
   public void testStubVoid() {
      MockitoCore mockitoCore = new MockitoCore();
      T mock = new T();
      VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);
      assertNotNull(result);
   }
   
   @Test
   public void testValidateMockitoUsage() {
      MockitoCore mockitoCore = new MockitoCore();
      mockitoCore.validateMockitoUsage();
   }
   
   @Test
   public void testGetLastInvocation() {
      MockitoCore mockitoCore = new MockitoCore();
      Invocation result = mockitoCore.getLastInvocation();
      assertNotNull(result);
   }
}