import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockitoInvocationHandler mockitoInvocationHandler;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockitoInvocationHandler = Mockito.mock(MockitoInvocationHandler.class);
 }

 @Test
 public void testHandle_NoAnswersForStubbing() throws Throwable {
 Invocation invocation = new Invocation(null, null, null, new Object[0]);
 Object result = mockHandler.handle(invocation);

 // Verify that no verification mode was pulled and no stubbing was performed
 verify(mockitoInvocationHandler, Mockito.never()).verify(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).stub(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenAnswer(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenReturn(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenThrow(Mockito.any());

 assertNull(result);
 }

 @Test
 public void testHandle_StubbingVoid() throws Throwable {
 Invocation invocation = new Invocation(Mockito.mock(Class.class), "voidMethod", new Object[0], null);
 Answer answer = Mockito.answer(invocation.getArguments()[0]);
 mockHandler.setAnswersForStubbing(new ArrayList<Answer>() {{
 add(answer);
 }});

 Object result = mockHandler.handle(invocation);

 // Verify that the void method was stubbed with the provided answer
 verify(mockitoInvocationHandler, Mockito.never()).verify(Mockito.any());
 verify(mockitoInvocationHandler).stub(ArgumentMatchers.isA(Class.class)).toReturn(null);
 verify(mockitoInvocationHandler).voidMethod();

 assertNull(result);
 }

 @Test
 public void testHandle_VerificationMode() throws Throwable {
 Invocation invocation = new Invocation(mockitoInvocationHandler, "methodWithArg", new Object[]{"arg"}, null);
 VerificationMode verificationMode = Mockito.verifyZeroInteractions(mockitoInvocationHandler);
 mockHandler.mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);

 // Verify that the verification was performed
 verify(mockitoInvocationHandler, verificationMode).methodWithArg(ArgumentMatchers.eq("arg"));
 }

 @Test
 public void testHandle_MultipleVerificationModes() throws Throwable {
 Invocation invocation = new Invocation(mockitoInvocationHandler, "methodWithArg", new Object[]{"arg1"}, null);
 Invocation invocation2 = new Invocation(mockitoInvocationHandler, "methodWithArg", new Object[]{"arg2"}, null);
 VerificationMode verificationMode1 = Mockito.verifyZeroInteractions(mockitoInvocationHandler);
 VerificationMode verificationMode2 = Mockito.verify(mockitoInvocationHandler).methodWithArg(ArgumentMatchers.eq("arg2"));
 mockHandler.mockingProgress.pushVerificationMode(verificationMode1);
 mockHandler.mockingProgress.pushVerificationMode(verificationMode2);

 mockHandler.handle(invocation);
 mockHandler.handle(invocation2);

 // Verify that both verifications were performed in the correct order
 verify(mockitoInvocationHandler, verificationMode1).methodWithArg(ArgumentMatchers.eq("arg1"));
 verify(mockitoInvocationHandler, verificationMode2).methodWithArg(ArgumentMatchers.eq("arg2"));
 }

 @Test
 public void testHandle_AnswerReturn() throws Throwable {
 Invocation invocation = new Invocation(mockitoInvocationHandler, "methodWithArg", new Object[]{"arg"}, null);
 Object returnValue = "returnValue";
 Answer answer = Mockito.answer(returnValue);
 mockHandler.setAnswersForStubbing(new ArrayList<Answer>() {{
 add(answer);
 }});

 Object result = mockHandler.handle(invocation);

 // Verify that the answer was returned
 verify(mockitoInvocationHandler, Mockito.never()).verify(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).stub(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenAnswer(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenReturn(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenThrow(Mockito.any());

 assertEquals(returnValue, result);
 }

 @Test
 public void testHandle_AnswerCaptureArguments() throws Throwable {
 Invocation invocation = new Invocation(mockitoInvocationHandler, "methodWithArg", new Object[]{"arg"}, null);
 String captureArg = "captureArg";
 Answer answer = Mockito.answer(new Capture<String>() {
 @Override public String value() {
 return captureArg;
 }});
 mockHandler.setAnswersForStubbing(new ArrayList<Answer>() {{
 add(answer);
 }});

 mockHandler.handle(invocation);

 // Verify that the answer captured the argument
 verify(mockitoInvocationHandler, Mockito.never()).verify(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).stub(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenAnswer(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenReturn(Mockito.any());
 verify(mockitoInvocationHandler, Mockito.never()).thenThrow(Mockito.any());

 assertEquals(captureArg, invocation.getArguments()[0]);
 }
}