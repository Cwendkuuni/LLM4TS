import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setup() {
 mock = Mockito.mock(DummyClass.class);
 mockHandler = new MockHandler<DummyClass>(mock);
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // test void method invocation with no stubbing
 mockHandler.handle(Mockito.mock(Invocation.class));
 // test void method invocation with stubbing
 when(mock.someVoidMethod()).thenAnswer(invocation -> System.out.println("Void method invoked"));
 mockHandler.handle(mock.someVoidMethod());
 verify(mock).someVoidMethod();
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // test method invocation with answer
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenAnswer(invocation -> invocation.getArgumentAt(0, int.class) + 1);
 int result = mockHandler.handle(mock.someMethod(42));
 verify(mock).someMethod(ArgumentMatchers.eq(43));
 }

 @Test
 public void testHandleMethodInvocationWithVerificationMode() {
 // test method invocation with verification mode
 verify(mock).someMethod(ArgumentMatchers.anyInt());
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenCallRealMethod();
 int result = mockHandler.handle(mock.someMethod(42));
 verify(mock).someMethod(ArgumentMatchers.eq(42));
 }

 @Test(expected = RuntimeException.class)
 public void testHandleMethodInvocationWithDifferentMock() {
 DummyClass otherMock = Mockito.mock(DummyClass.class);
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenCallRealMethod();
 mockHandler = new MockHandler<DummyClass>(otherMock);
 mockHandler.handle(mock.someMethod(42));
 }

 @Test
 public void testHandleSetAnswersForStubbing() {
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> invocation.getArgumentAt(0, int.class) * 2);
 mockHandler.setAnswersForStubbing(answers);
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenAnswer(Answer.returning(mockHandler));
 int result = mockHandler.handle(mock.someMethod(42));
 verify(mock).someMethod(ArgumentMatchers.eq(84));
 }
}

class DummyClass {
 void someVoidMethod() {
 }

 int someMethod(int arg) {
 return arg;
 }
}