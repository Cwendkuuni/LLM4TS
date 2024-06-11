import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestObject> mockHandler;
 private MockHandler<MockHandlerTestObject> mockHandlerSpy;
 private MockitoInvocationHandler mockitoInvocationHandlerSpy;
 private MockitoInvocationHandler mockitoInvocationHandler;
 private MockSettingsImpl mockSettings;
 private List<Answer> answers;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockHandlerSpy = spy(mockHandler);
 mockitoInvocationHandlerSpy = mock(MockitoInvocationHandler.class);
 mockitoInvocationHandler = new MockHandler(mockitoInvocationHandlerSpy);
 answers = new ArrayList<>();
 }

 @Test
 public void testHandle_voidMethodStubbing() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer answer = Answer.returnValue(new Object());
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 when(mockitoInvocationHandlerSpy.handle(ArgumentMatchers.eq(invocation))).thenAnswer(answers.get(0));

 MockHandlerTestObject mockObject = mock(MockHandlerTestObject.class);
 Object result = mockHandler.handle(Mockito.argThat(i -> i.getMockHandler() == mockHandler)).handle(invocation);

 verify(mockitoInvocationHandlerSpy).handle(invocation);
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.eq(invocation));
 assertNull(result);
 }

 @Test
 public void testHandle_verifyMode() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 VerificationMode verificationMode = Mockito.verify(mockObject, Mockito.times(1));
 mockHandler.handle(invocation);

 verify(mockitoInvocationHandlerSpy).handle(invocation);
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.eq(invocation));
 verificationMode.verify();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationMode_differentMock() throws Throwable {
 VerificationMode verificationMode = Mockito.verify(mockObject2, Mockito.times(1));
 mockHandler.handle(invocation);

 verificationMode.verify();
 }

 @Test
 public void testHandle_stubbedInvocation() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Answer answer = Answer.returnValue(new Object());
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 Stub stub = Mockito.stub(MockHandlerTestObject.class, "method").toReturn(new Object());
 when(mockObject.method()).thenStub(stub);

 Object result = mockHandler.handle(Mockito.argThat(i -> i.getMockHandler() == mockHandler)).handle(Mockito.argThat(i -> i.getMethodName().equals("method"))).handle(Mockito.argThat(i -> i.getArguments().contains(ArgumentMatchers.anyInt(1))));

 verify(mockitoInvocationHandlerSpy).handle(invocation);
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.argThat(i -> i.getMethodName().equals("method")));
 assertNotNull(result);
 }

 @Test
 public void testHandle_defaultAnswer() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = mockHandler.handle(invocation);

 verify(mockitoInvocationHandlerSpy).handle(invocation);
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockitoInvocationHandlerSpy).handle(ArgumentMatchers.eq(invocation));
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationMode_null() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 VerificationMode verificationMode = null;

 try {
 verificationMode.verify();
 } catch (IllegalStateException e) {
 throw e;
 }
 }

 @Test
 public void testVoidMethodStubbable() throws Throwable {
 VoidMethodStubbable<MockHandlerTestObject> voidMethodStubbable = mockHandler.voidMethodStubbable(mockObject);
 voidMethodStubbable.stubVoid();

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 }

 @Test
 public void testGetInvocationContainer() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

 assertNotNull(invocationContainer);
 }

 @Test
 public void testGetMockSettings() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();

 assertSame(mockSettings, mockHandler.mockSettings);
 }
}