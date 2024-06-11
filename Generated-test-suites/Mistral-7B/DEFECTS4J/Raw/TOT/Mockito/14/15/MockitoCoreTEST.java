import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private StubberImpl stubberImpl;
 @Mock private ThreadSafeMockingProgress threadSafeMockingProgress;
 @Mock private Reporter reporter;
 @Mock private MockUtil mockUtil;
 @Mock private MockSettingsImpl mockSettings;
 @Mock private IOngoingStubbing ongoingStubbing;
 @Mock private DeprecatedOngoingStubbing<Object> deprecatedOngoingStubbing;
 @Mock private OngoingStubbing<Object> ongoingStubbingMock;
 @Mock private VerificationDataImpl verificationData;
 @Mock private VerificationModeFactory verificationModeFactory;
 @Mock private InOrderContext inOrderContext;
 @Mock private List<Object> mocks;
 @Mock private Object mock;
 @InjectMocks private MockitoCore testObject;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 Mockito.initMocks(this);
 }

 @Test
 public void testMockCreation() {
 when(mockUtil.createMock(ArgumentMatchers.eq(String.class), any(MockSettings.class))).thenReturn(mock);
 ArgumentCaptor<MockSettings> mockSettingsCaptor = ArgumentCaptor.forClass(MockSettings.class);
 testObject.mock(String.class, mockSettingsCaptor.capture());
 verify(mockUtil).createMock(ArgumentMatchers.eq(String.class), mockSettingsCaptor.getValue());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 testObject.mock(null, mockSettings);
 }

 @Test
 public void testStubbing() {
 testObject.stub();
 verify(stubberImpl).doReturn(mock).when(mockitoCore).stub();
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 testObject.stub(mock);
 verify(stubberImpl).doThenReturn(mock).when(mockitoCore).stub(any());
 }

 @Test
 public void testOngoingStubbing() {
 testObject.when(mock);
 verify(stubberImpl).doReturn(mock).when(mockitoCore).when(any());
 }

 @Test
 public void testVerify() {
 testObject.verify(mock, ArgumentMatchers.eq(VerificationMode.times(1)));
 verify(mockUtil).isMock(mock);
 }

 @Test
 public void testVerifyNullMock() {
 testObject.verify(null, ArgumentMatchers.eq(VerificationMode.never()));
 }

 @Test
 public void testVerifyNotAMock() {
 testObject.verify(new Object(), ArgumentMatchers.eq(VerificationMode.never()));
 }

 @Test
 public void testReset() {
 testObject.reset(mock);
 verifyZeroInteractions(mockUtil);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 testObject.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 testObject.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 ArgumentCaptor<List<Object>> mocksCaptor = ArgumentCaptor.forClass(List.class);
 when(mockUtil.getMockHandler(mock1)).thenReturn(mockHandler1);
 when(mockUtil.getMockHandler(mock2)).thenReturn(mockHandler2);
 testObject.verifyNoMoreInteractionsInOrder(mocksCaptor.capture(), eq(inOrderContext));
 List<Object> mocks = mocksCaptor.getValue();
 verifyNoMoreInteractions(mocks.get(0));
 verifyNoMoreInteractions(mocks.get(1));
 }

 @Test
 public void testInOrder() {
 ArgumentCaptor<Object[]> mocksCaptor = ArgumentCaptor.forClass(Object[].class);
 testObject.inOrder(mock1, mock2);
 verify(mockitoCore).inOrder(mocksCaptor.capture());
 Object[] mocks = mocksCaptor.getValue();
 verify(mocks[0]).doNothing();
 verify(mocks[1]).doNothing();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 testObject.doAnswer(answer).when(mock).someMethod();
 verify(mock).someMethod();
 verify(mockitoCore).doAnswer(answer);
 }

 @Test
 public void testStubVoid() {
 ArgumentCaptor<Object> mockCaptor = ArgumentCaptor.forClass(Object.class);
 testObject.stubVoid(mock);
 verify(mockUtil).getMockHandler(mock).voidMethodStubbable(mock);
 verify(mock).voidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 testObject.validateMockitoUsage();
 verify(mockitoCore).validateState();
 }

 @Test
 public void testGetLastInvocation() {
 testObject.stub(mock).andReturn("result");
 testObject.when(mock).someMethod().thenReturn("result");
 testObject.verify(mock);
 Invocation lastInvocation = testObject.getLastInvocation();
 assertEquals("result", lastInvocation.getArgument(0));
 assertEquals("someMethod", lastInvocation.getMethod().getName());
 }
}