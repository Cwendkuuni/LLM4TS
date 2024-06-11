import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestClass> mockHandler;
 private MockHandler<MockHandlerTestClass> mockHandlerSpy;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockHandlerSpy =Mockito.spy(new MockHandler<>(mockSettings));
 invocationContainer = mockHandler.getInvocationContainer();
 }

 @Test
 public void testHandleInvocationForStubbing() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(mockHandler.handle(invocation)).thenReturn(null);

 InvocationMatcher invocationMatcher = ArgumentMatchers.anyInt();
 mockHandler.stubVoid(Mockito.mock(MockHandlerTestClass.class), invocationMatcher);

 InvocationOnMock invocationToStub = Mockito.mock(InvocationOnMock.class);
 when(invocationToStub.getMethod()).thenReturn(Mockito.method(int.class, int.class));
 when(invocationToStub.getArguments()).thenReturn(new Object[]{1, 2});

 List<Answer> answers = new ArrayList<>();
 Answer answer = AnswerSpy.createAnswer(3);
 answers.add(answer);

 mockHandler.setAnswersForStubbing(answers);

 int result = mockHandler.handle(invocationToStub);

 verify(mockHandler).stubVoid(Mockito.eq(MockHandlerTestClass.class), invocationMatcher);
 verify(mockHandler).setAnswersForStubbing(answers);
 verify(mockHandler).handle(invocationToStub).wasCalledaTime(1);

 Assert.assertEquals(3, result);
 }

 @Test
 public void testHandleInvocationForVerification() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 Mockito.doAnswer(AnswerSpy.createAnswer(1)).when(mockHandlerSpy).handle(ArgumentMatchers.any(InvocationOnMock.class));

 mockHandlerSpy.handle(invocation);

 verify(mockHandlerSpy).handle(invocation);
 }

 @Test
 public void testHandleInvocationForDefaultAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(mockHandler.handle(invocation)).thenCallRealMethod();

 Object result = mockHandler.handle(invocation);

 Assert.assertNotNull(result);
 }
}

 class MockHandlerTestClass {
 public int method(int a, int b) {
 return a + b;
 }
}

 class AnswerSpy {
 private static int count = 0;

 public static Answer createAnswer(int answer) {
 return new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 count++;
 return answer;
 }
 };
 }
}