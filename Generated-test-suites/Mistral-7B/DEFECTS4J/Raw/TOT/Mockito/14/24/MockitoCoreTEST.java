import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockito;
import org.mockito.stubbing.OngoingStubbing;

import org.mockito.core.MockitoCore;
import org.mockito.core.ThreadSafeMockingProgress;
import org.mockito.exceptions.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.util.MockUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private ThreadSafeMockingProgress mockingProgress;
 private IOngoingStubbing ongoingStubbing;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 }

 @Test
 public void testMockCreation() {
 Class<String> stringClass = String.class;
 String mock = mockitoCore.mock(stringClass, new MockSettingsImpl());
 assertEquals("mock", mock.toString());
 }

 @Test
 public void testMultipleMockCreation() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 String mock1 = mockitoCore.mock(stringClass, new MockSettingsImpl());
 Integer mock2 = mockitoCore.mock(intClass, new MockSettingsImpl());
 assertEquals("mock1", mock1.toString());
 assertEquals("mock2", mock2.toString());
 }

 @Test
 public void testStubbing() {
 String mock = mockitoCore.mock(String.class);
 OngoingStubbing<String> stubbing = mockitoCore.when(mock).thenReturn("stubbedValue");
 assertEquals("stubbedValue", stubbing.getMock().toString());
 }

 @Test
 public void testStubbingWithArgument() {
 String mock = mockitoCore.mock(String.class);
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mock.concat(argumentCaptor.capture("argument"))).thenReturn("concatenatedValue");
 String argument = "argument";
 assertEquals("concatenatedValue", mockitoCore.verify(mock).concat(argument));
 assertEquals("argument", argumentCaptor.getValue());
 }

 @Test
 public void testAnswerWithMockito() {
 String mock = mockitoCore.mock(String.class);
 final String answer = "answer";
 OngoingStubbing<String> stubbing = mockitoCore.when(mock).thenAnswer(new AnswerWithMockito<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return answer;
 }
 });
 assertEquals(answer, stubbing.getMock().toString());
 }

 @Test
 public void testAnswer() {
 String mock = mockitoCore.mock(String.class);
 final String answer = "answer";
 OngoingStubbing<String> stubbing = mockitoCore.when(mock).thenAnswer(new Answer<String>() {
 @Override public String answer(InvocationOnMock invocation) throws Throwable {
 return answer;
 }
 });
 assertEquals(answer, stubbing.getMock().toString());
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.times(1));
 }

 @Test
 public void testVerify() {
 String mock = mockitoCore.mock(String.class);
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 @Test
 public void testVerifyTimes() {
 String mock = mockitoCore.mock(String.class);
 when(mock.toString()).thenReturn("mockValue");
 mockitoCore.verify(mock, VerificationMode.times(2)).toString();
 assertEquals("mockValue", mockitoCore.getLastInvocation().getArguments()[0]);
 }

 @Test
 public void testVerifyNever() {
 String mock = mockitoCore.mock(String.class);
 mockitoCore.verify(mock, never()).toString();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithOrder() {
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 InOrder inOrder = inOrder(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testReset() {
 String mock = mockitoCore.mock(String.class);
 when(mock.toString()).thenReturn("mockValue");
 OngoingStubbing<String> stubbing = mockitoCore.when(mock).thenReturn("stubbedValue");
 mockitoCore.verify(mock).toString();
 assertEquals("mockValue", mockitoCore.getLastInvocation().getArguments()[0]);
 reset(mock);
 assertEquals("null", mockitoCore.getLastInvocation().getArguments()[0]);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}