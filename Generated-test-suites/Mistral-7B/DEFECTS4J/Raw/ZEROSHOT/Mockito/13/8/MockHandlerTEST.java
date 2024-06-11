package org.mockito.internal.handlers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private VerificationModeFactory verificationModeFactory;

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Before
 public void setUp() {
 mock = mock(Object.class);
 mockHandler = new MockHandler<Object>(new MockSettingsImpl());
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // given
 Stub voidStub = mockHandler.voidMethodStubbable(mock).stub();

 // when
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod", Collections.emptyList()));

 // then
 // no verification or answer, as this is a void method
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // given
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method(1);
 assertEquals(2, result);
 }

 @Test
 public void testHandleMethodInvocationWithVerificationMode() {
 // given
 VerificationMode verificationMode = Mockito.verify(mock).method(Integer.class);

 // when
 mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method(1);
 // verifyMode should be consumed
 verify(mock, verificationMode).method(1);
 }

 @Test
 public void testHandleMethodInvocationWithMultipleVerificationModes() {
 // given
 VerificationMode verificationMode1 = Mockito.verify(mock).method1(Integer.class);
 VerificationMode verificationMode2 = Mockito.verify(mock).method2(Integer.class);

 // when
 mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method1(1);
 verify(mock, verificationMode1).method1(1);
 verify(mock).method2(1);
 verify(mock, verificationMode2).method2(1);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerAndVerificationMode() {
 // given
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });
 VerificationMode verificationMode = Mockito.verify(mock).method(Integer.class);

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method(1);
 // verifyMode should be consumed
 verify(mock, verificationMode).method(1);
 assertEquals(2, result);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerAndMultipleVerificationModes() {
 // given
 VerificationMode verificationMode1 = Mockito.verify(mock).method1(Integer.class);
 VerificationMode verificationMode2 = Mockito.verify(mock).method2(Integer.class);
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method1(1);
 verify(mock, verificationMode1).method1(1);
 verify(mock).method2(1);
 verify(mock, verificationMode2).method2(1);
 assertEquals(2, result);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerWithDefaultAnswer() {
 // given
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 verify(mock).method(1);
 assertEquals(2, result);

 // reset invocation container to handle the default answer
 mockHandler.getInvocationContainer().resetInvocationForPotentialStubbing(null);

 // when
 result = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 // no verification, as the default answer does not trigger verification
 verifyNoMoreInteractions(mock);
 assertEquals(0, result);
 }

 @Test
 public void testHandleMethodInvocationWithMultipleAnswers() {
 // given
 when(mock.method1(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });
 when(mock.method1(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) * 2;
 }
 });

 // when
 int result1 = (int) mockHandler.handle(new InvocationOnMock(mock, "method1", new Object[]{1}));
 int result2 = (int) mockHandler.handle(new InvocationOnMock(mock, "method1", new Object[]{1}));

 // then
 assertEquals(2, result1);
 assertEquals(2, result2);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerAndDefaultAnswer() {
 // given
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });
 when(mock.method(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) * 2;
 }
 });

 // when
 int result1 = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));
 int result2 = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 assertEquals(2, result1);
 assertEquals(2, result2);

 // reset invocation container to handle the first answer
 mockHandler.getInvocationContainer().resetInvocationForPotentialStubbing(new StubbedInvocationMatcher(mockHandler.getInvocationContainer(), new MethodStubbing("method", Integer.class), null));

 // when
 result1 = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 assertEquals(2, result1);

 // when
 result2 = (int) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[]{1}));

 // then
 assertEquals(3, result2);
 }

 @Test
 public void testHandleMethodInvocationWithAnswerAndDefaultAnswerWithMultipleVerificationModes() {
 // given
 VerificationMode verificationMode1 = Mockito.verify(mock).method1(Integer.class);
 VerificationMode verificationMode2 = Mockito.verify(mock).method2(Integer.class);
 when(mock.method1(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) + 1;
 }
 });
 when(mock.method1(Integer.class)).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Integer.class) * 2;
 }
 });

 // when
 int result1 = (int) mockHandler.handle(new InvocationOnMock(mock, "method1", new Object[]{1}));
 int result2 = (int) mockHandler.handle(new InvocationOnMock(mock, "method1", new Object[]{1}));

 // then
 assertEquals(2, result1);
 assertEquals(2, result2);

 // verify
 verify(mock).method1(1);
 verify(mock, verificationMode1).method1(1);
 verify(mock).method2(1);
 verify(mock, verificationMode2).method2(1);
 }
}