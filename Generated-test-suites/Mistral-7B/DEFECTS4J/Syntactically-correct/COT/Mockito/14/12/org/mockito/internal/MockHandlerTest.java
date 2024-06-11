import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.AnswerWithJackson;
import org.mockito.stubbing.Stubber;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestClass> mockHandler;
 private MockitoInvocationHandler mockitoInvocationHandler;
 private InvocationOnMock invocation;
 private Stubber stubber;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>(new MockSettingsImpl());
 mockitoInvocationHandler = mock(MockitoInvocationHandler.class);
 }

 @Test
 public void testHandle_Stubbing() throws Throwable {
 // Given
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockitoInvocationHandler.handleInvocation(any(InvocationOnMock.class), any()))
 .thenReturn(mockHandler);
 stubber = mock(Stubber.class);
 doAnswer(stubber).when(mockitoInvocationHandler).handleInvocation(any(InvocationOnMock.class), any());

 // When
 mockHandler.handle(invocation);

 // Then
 verify(mockitoInvocationHandler).handleInvocation(any(InvocationOnMock.class), any());
 }

 @Test
 public void testHandle_Verification() throws Throwable {
 // Given
 Invocation invocation = mock(InvocationOnMock.class);
 VerificationMode verificationMode = verify(mockitoInvocationHandler);
 doReturn(verificationMode).when(mockHandler).handle(invocation);

 // When
 mockHandler.handle(invocation);

 // Then
 verify(mockitoInvocationHandler).handle(invocation);
 }

 @Test
 public void testHandle_DefaultAnswer() throws Throwable {
 // Given
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockitoInvocationHandler.handleInvocation(any(InvocationOnMock.class), any()))
 .thenReturn(mockHandler);
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new AnswerWithJackson() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Default answer";
 }
 });

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertEquals("Default answer", result);
 }

 @Test(expected = Throwable.class)
 public void testHandle_Exception() throws Throwable {
 // Given
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockitoInvocationHandler.handleInvocation(any(InvocationOnMock.class), any()))
 .thenReturn(mockHandler);
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenThrow(new RuntimeException("Exception"));

 // When & Then
 try {
 mockHandler.handle(invocation);
 } catch (RuntimeException e) {
 assertEquals("Exception", e.getMessage());
 }
 }

 @Test
 public void testVoidMethodStubbable() {
 // Given
 T mock = mock(MockHandlerTestClass.class);
 VoidMethodStubbable<MockHandlerTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 // Then
 assertNotNull(voidMethodStubbable);
 }

 @Test
 public void testSetAnswersForStubbing() {
 // Given
 List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
 mockHandler.setAnswersForStubbing(answers);
 Invocation invocation = mock(InvocationOnMock.class);

 // When
 Object result = mockHandler.handle(invocation);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 when(mockHandler.handle(invocation)).thenReturn(stubbedInvocationMatcher);

 // Then
 verify(stubbedInvocationMatcher).answer(eq(answers.get(0)));
 }
}