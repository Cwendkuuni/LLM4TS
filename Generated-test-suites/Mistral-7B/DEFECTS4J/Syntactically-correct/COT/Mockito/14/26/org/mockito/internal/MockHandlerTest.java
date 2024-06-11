import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<MockHandlerTestClass> mockHandler;
 private MockHandler<MockHandlerTestClass> mockHandlerSpy;
 private MockHandler<MockHandlerTestClass> mockHandlerStub;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockHandler = new MockHandler<MockHandlerTestClass>(new MockSettingsImpl());
 mockHandlerSpy = new MockHandler<MockHandlerTestClass>(mockHandler);
 mockHandlerStub = new MockHandler<MockHandlerTestClass>(mockHandler);
 mockHandler.setAnswersForStubbing(Arrays.asList(new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 }, new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("exception");
 }
 }));
 }

 @Test
 public void testHandle_noAnswers_returnDefaultAnswer() {
 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenReturn(mockHandler);
 when(mockHandler.handle(invocation)).thenCallRealMethod();

 Object result = mock.handle(invocation);

 assertTrue(result instanceof MockHandler);
 assertEquals(mockHandler, result);
 }

 @Test
 public void testHandle_answerExists_returnAnswer() throws Throwable {
 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenReturn(new Object() {});

 Mockito.verify(mockHandler).answer(invocation);

 Object result = mock.handle(invocation);
 assertNotNull(result);
 }

 @Test
 public void testHandle_verificationMode_verifyInvocation() throws Throwable {
 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenReturn(new Object() {});

 VerificationMode verificationMode = Mockito.verify(mock).handle(invocation);

 assertNotNull(verificationMode);
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_exception_throwException() throws Throwable {
 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenAnswer(new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("exception");
 }
 });

 mock.handle(invocation);
 }

 @Test
 public void testVoidMethodStubbable_stubbingVoidMethods() throws Throwable {
 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 mockHandlerStub.voidMethodStubbable(mock).voidMethod();

 InvocationStub invocationStub = mockHandlerStub.invocationContainerImpl.getInvocations().get(0);
 assertEquals("voidMethod", invocationStub.getMethodName());
 assertEquals(mock, invocationStub.getMock());
 assertNull(invocationStub.getArguments());
 }

 @Test
 public void testHandle_edgeCase_nullInvocation() throws Throwable {
 MockHandler<MockHandlerTestClass> mockHandler = new MockHandler<MockHandlerTestClass>(new MockSettingsImpl());

 try {
 mockHandler.handle(null);
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testHandle_edgeCase_nonMatchingInvocation() throws Throwable {
 MockHandler<MockHandlerTestClass> mockHandler = new MockHandler<MockHandlerTestClass>(new MockSettingsImpl());
 Invocation invocation = Mockito.mock(Invocation.class);

 when(invocation.getMock()).thenReturn(Mockito.mock(MockHandlerTestClass.class));
 when(invocation.getMethodName()).thenReturn("nonExistingMethod");

 try {
 mockHandler.handle(invocation);
 fail("Should have thrown InvocationTargetException");
 } catch (InvocationTargetException e) {
 // Expected
 }
 }

 @Test
 public void testHandle_edgeCase_multipleAnswers() throws Throwable {
 MockHandler<MockHandlerTestClass> mockHandler = new MockHandler<MockHandlerTestClass>(new MockSettingsImpl());
 List<Answer> answers = Arrays.asList(new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer1";
 }
 }, new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer2";
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenReturn(answers.get(0));

 Object result1 = mock.handle(invocation);
 assertEquals("answer1", result1);

 when(mockHandler.handle(invocation)).thenReturn(answers.get(1));
 Object result2 = mock.handle(invocation);
 assertEquals("answer2", result2);
 }

 @Test
 public void testHandle_edgeCase_exceptionInStubbing() throws Throwable {
 MockHandler<MockHandlerTestClass> mockHandler = new MockHandler<MockHandlerTestClass>(new MockSettingsImpl());
 List<Answer> answers = Arrays.asList(new AnswerWithMockito<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("exception");
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 MockHandlerTestClass mock = Mockito.mock(MockHandlerTestClass.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 when(mock.handle(invocation)).thenCallRealMethod();
 when(mockHandler.handle(invocation)).thenReturn(answers.get(0));

 try {
 mock.handle(invocation);
 fail("Should have thrown RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }
}