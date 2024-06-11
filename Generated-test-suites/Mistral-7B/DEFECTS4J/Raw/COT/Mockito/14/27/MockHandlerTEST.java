import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private List<Answer> answers;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 answers = Arrays.asList(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 }, new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Exception");
 }
 }, new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 doNothing().when(invocation.getMock()).someMethod();
 return null;
 }
 });
 }

 @Test
 public void testHandle_Stubbing() throws Throwable {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});
 doAnswer(Answers.returnValue("Answer")).when(invocationContainer).handle(invocation);

 Object result = mockHandler.handle(invocation);
 assertEquals("Answer", result);
 verify(invocation).getMock();
 }

 @Test
 public void testHandle_Verification() throws Throwable {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});

 mockHandler.handle(invocation);
 verify(invocationContainer).getInvocations();
 verify(invocationContainer).getInvocations().size() > 0;
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_ExceptionHandling() throws Throwable {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});
 doAnswer(Answers.throwException(new RuntimeException("Exception"))).when(invocationContainer).handle(invocation);

 try {
 mockHandler.handle(invocation);
 fail();
 } catch (RuntimeException e) {
 assertEquals("Exception", e.getMessage());
 }
 }

 @Test
 public void testHandle_NoInteraction() throws Throwable {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});

 mockHandler.handle(invocation);
 verifyNoInteractions(invocation.getMock());
 }

 @Test
 public void testHandle_NoVerification() throws Throwable {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});

 mockHandler.handle(invocation);
 verifyZeroInteractions(invocationContainer);
 }

 @Test
 public void testHandle_VoidMethodStubbable() throws Throwable {
 T mock = mock(MockitoTestClass.class);
 VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.stubVoid("someMethod");

 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Void.TYPE));
 when(invocation.getArguments()).thenReturn(new Object[0]);

 mockHandler.handle(invocation);
 verify(mock).someMethod();
 }

 @Test
 public void testSetAnswersForStubbing() throws Throwable {
 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = Mockito.mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(MockitoTestClass.class, "someMethod", Integer.class, String.class));
 when(invocation.getArguments()).thenReturn(new Object[]{1, "arg"});

 Object result = mockHandler.handle(invocation);
 for (Answer answer : answers) {
 if (answer instanceof Answer) {
 assertEquals("Answer", result);
 } else if (answer instanceof Answer<?>) {
 verify(invocation).getMock().someMethod();
 }
 }
 }
}