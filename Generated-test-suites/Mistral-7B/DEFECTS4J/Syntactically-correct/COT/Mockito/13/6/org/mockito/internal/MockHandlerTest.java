import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerCalledTimes;
import org.mockito.stubbing.AnswerReturningNull;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.Stubbing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mock;

 @Before
 public void setup() {
 mock = Mockito.spy(new MockitoTestClass());
 mockHandler = new MockHandler<>(mock.getClass().getDeclaredConstructor().newInstance());
 }

 @Test
 public void testHandle_NoAnswersOrVerificationMode_ReturnDefaultAnswer() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 when(mock.method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble())).thenReturn("Default answer");

 Object result = mockHandler.handle(invocation);

 assertThat(result).isEqualTo("Default answer");
 }

 @Test(expected = Throwable.class)
 public void testHandle_VerificationModeWithoutCorrespondingMock_ThrowRuntimeException() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 VerificationMode verificationMode = new MockAwareVerificationMode(new ClassWithMethod());
 mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_AnswerForStubbing_ReturnAnswer() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new AnswerReturningNull());

 Object result = mockHandler.handle(invocation);

 verify(invocation).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verifyNoMoreInteractions(invocation);

 assertThat(result).isNull();
 }

 @Test
 public void testHandle_AnswerForStubbingWithArgumentsCaptured_ReturnAnswerWithCapturedArguments() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 Object arg1 = invocationOnMock.getArgument(0);
 Object arg2 = invocationOnMock.getArgument(1);
 return arg1 + arg2.toString();
 }
 });

 Object result = mockHandler.handle(invocation);

 verify(invocation).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verifyNoMoreInteractions(invocation);

 assertThat(result).isEqualTo("arg1arg2");
 }

 @Test
 public void testHandle_VerificationModeAndCorrespondingMock_VerifyMethodCall() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 VerificationMode verificationMode = new MockAwareVerificationMode(mock);
 mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);

 verify(mock).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 }

 @Test
 public void testHandle_MultipleVerificationModes_VerifyAllMethodCalls() throws Throwable {
 Invocation invocation1 = Mockito.mock(ClassWithMethod.class);
 Invocation invocation2 = Mockito.mock(ClassWithMethod.class);
 VerificationMode verificationMode1 = new MockAwareVerificationMode(mock);
 VerificationMode verificationMode2 = new MockAwareVerificationMode(mock);
 mockingProgress.pushVerificationMode(verificationMode1);
 mockingProgress.pushVerificationMode(verificationMode2);

 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);

 verify(mock).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verify(mock).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 }

 @Test(expected = Throwable.class)
 public void testHandle_AnswerWithException_ThrowException() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 throw new RuntimeException("Exception thrown from answer");
 }
 });
 mockingProgress.pushVerificationMode(new MockAwareVerificationMode(mock));

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_AnswerCalledTimes_ReturnAnswerOnlySpecifiedNumberOfTimes() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new AnswerCalledTimes(2));

 Object result1 = mockHandler.handle(invocation);
 Object result2 = mockHandler.handle(invocation);

 verify(invocation).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verify(invocation).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verifyNoMoreInteractions(invocation);

 assertThat(result1).isNotNull();
 assertThat(result2).isNotNull();
 }

 @Test
 public void testHandle_AnswerReturns_ReturnSpecifiedValue() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new AnswerReturns("Specified value"));

 Object result = mockHandler.handle(invocation);

 verify(invocation).method1(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble());
 verifyNoMoreInteractions(invocation);

 assertThat(result).isEqualTo("Specified value");
 }

 @Test
 public void testHandle_VoidMethodStubbable_StubVoidMethod() throws Throwable {
 ClassWithVoidMethod classWithVoidMethod = Mockito.spy(new ClassWithVoidMethod());
 VoidMethodStubbable<ClassWithVoidMethod> voidMethodStubbable = mockHandler.voidMethodStubbable(classWithVoidMethod);

 voidMethodStubbable.stubVoid("voidMethod");

 classWithVoidMethod.voidMethod();

 verifyZeroInteractions(classWithVoidMethod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandle_InvalidAnswer_ThrowIllegalArgumentException() throws Throwable {
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new Object());

 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_AnswerWithCircularDependency_ThrowIllegalStateException() throws Throwable {
 ClassWithCircularDependency classWithCircularDependency = new ClassWithCircularDependency();
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing = mockHandler.getInvocationContainer().stub(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble()).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 return classWithCircularDependency.getMockHandler().handle(invocationOnMock);
 }
 });

 try {
 mockHandler.handle(invocation);
 } catch (Throwable e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testHandle_MethodsWithSameSignatureButDifferentParameterTypes_HandleCorrectMethod() throws Throwable {
 ClassWithSameSignatureMethods classWithSameSignatureMethods = new ClassWithSameSignatureMethods();
 Invocation invocation = Mockito.mock(ClassWithMethod.class);
 Stubbing<ClassWithMethod> stubbing1 = mockHandler.getInvocationContainer().stub(ArgumentMatchers.eq(classWithSameSignatureMethods)).thenAnswer(new AnswerReturningNull());
 Stubbing<ClassWithMethod> stubbing2 = mockHandler.getInvocationContainer().stub(ArgumentMatchers.<Object>notNull()).thenAnswer(new AnswerReturningNull());

 mockHandler.handle(invocation);

 verify(classWithSameSignatureMethods).method1(ArgumentMatchers.isA(String.class));
 verifyNoMoreInteractions(classWithSameSignatureMethods);
 }
}

class ClassWithMethod {
 public void method1(int i, double d) {
 // Method implementation
 }
}

class ClassWithVoidMethod {
 public void voidMethod() {
 // Method implementation
 }
}

class ClassWithCircularDependency {
 private MockHandler<ClassWithCircularDependency> mockHandler;

 public ClassWithCircularDependency() {
 this.mockHandler = new MockHandler<>(getClass());
 }

 public MockHandler<ClassWithCircularDependency> getMockHandler() {
 return mockHandler;
 }
}

class MockitoTestClass {
 private MockHandler<MockitoTestClass> mockHandler;

 public void method1(int i, double d) {
 // Method implementation
 }

 public MockHandler<MockitoTestClass> getMockHandler() {
 return mockHandler;
 }
}