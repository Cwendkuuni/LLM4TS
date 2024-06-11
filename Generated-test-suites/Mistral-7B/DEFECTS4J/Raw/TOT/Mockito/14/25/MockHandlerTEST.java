import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWrapper;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*; // import static for convenience

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mock = mock(DummyClass.class);
 }

 @Test
 public void testHandle_voidMethod_success() {
 // Given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .thenReturn(null);

 // When
 mockHandler.handle(mock.someMethod());

 // Then
 verify(mock).someMethod();
 }

 @Test
 public void testHandle_nonVoidMethod_success() {
 // Given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .thenReturn(42);

 // When
 int result = (int) mockHandler.handle(mock.someMethodThatReturnsInt());

 // Then
 verify(mock).someMethodThatReturnsInt();
 assertThat(result).isEqualTo(42);
 }

 @Test
 public void testHandle_stubbing_success() {
 // Given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });

 // When
 int result = (int) mockHandler.handle(mock.someMethodThatReturnsInt());

 // Then
 verify(mock).someMethodThatReturnsInt();
 assertThat(result).isEqualTo(42);
 }

 @Test
 public void testHandle_verification_success() {
 // Given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .then(doThrow(new RuntimeException("Test exception")));

 // When
 // Then exception is thrown
 try {
 mockHandler.handle(mock.someMethod());
 } catch (RuntimeException e) {
 // Then
 verify(mock).someMethod();
 }
 }

 @Test
 public void testHandle_multipleStubbings_success() {
 // Given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "First answer";
 }
 });
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 // Then
 .thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Second answer";
 }
 });

 // When
 String result1 = mockHandler.handle(mock.someMethod());
 String result2 = mockHandler.handle(mock.someMethod());

 // Then
 verify(mock).someMethod();
 verify(mock, times(2)).someMethod();
 assertThat(result1).isEqualTo("First answer");
 assertThat(result2).isEqualTo("Second answer");
 }

 @Test
 public void testHandle_answersList_success() {
 List<Answer> answers = Arrays.asList(new AnswerWrapper<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "First answer";
 }
 }, new AnswerWrapper<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Second answer";
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 // When
 String result1 = (String) mockHandler.handle(mock.someMethod());
 String result2 = (String) mockHandler.handle(mock.someMethod());

 // Then
 verify(mock).someMethod();
 verify(mock, times(2)).someMethod();
 assertThat(result1).isEqualTo("First answer");
 assertThat(result2).isEqualTo("Second answer");
 }
}

class DummyClass {
 public void someMethod() {}

 public int someMethodThatReturnsInt() {
 return 0;
 }
}