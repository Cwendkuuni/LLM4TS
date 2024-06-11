package org.mockito.internal.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.VoidMethodStubbable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private VoidMethodStubbable<Object> voidMethodStubbable;
 private Object mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));
 mock = mock(Object.class);
 }

 @After
 public void tearDown() {
 mockHandler = null;
 voidMethodStubbable = null;
 mock = null;
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 // given
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), Mockito.any()))
 .thenReturn(Mockito.eq(new StubbedInvocationMatcher("testMatcher", null, null, null, null)));

 // when
 mockHandler.handle(Mockito.any());

 // then
 // verify no verification was executed since handle() method returns null
 // when stubbing is used
 verify(mockHandler.getInvocationContainer()).getInvocations().isEmpty();
 }

 @Test
 public void testHandle_verification() throws Throwable {
 // given
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), Mockito.any()))
 .thenReturn(Mockito.eq(new StubbedInvocationMatcher("testMatcher", null, null, null, null)));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(VerificationMode.times(1));

 // when
 mockHandler.handle(Mockito.any());

 // then
 // verify that the verification was executed
 verify(mockHandler.getInvocationContainer()).getInvocations().size() > 0;
 }

 @Test
 public void testHandle_voidMethodStubbing() throws Throwable {
 // given
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), Mockito.any()))
 .thenReturn(Mockito.eq(new StubbedInvocationMatcher("testMatcher", null, null, null, null)));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockHandler.handle(Mockito.any())).thenReturn(null);
 when(voidMethodStubbable.answer(Mockito.any())).thenReturn("void answer");

 // when
 voidMethodStubbable.answer(Mockito.any());

 // then
 // verify that the answer was returned
 verify(mockHandler.getInvocationContainer()).getAnswersForStubbing().contains(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "void answer";
 }
 });
 }

 @Test
 public void testHandle_answersForStubbing() throws Throwable {
 // given
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.matchersBinder.bindMatchers(MockitoMockingProgress.getArgumentMatcherStorage(), Mockito.any()))
 .thenReturn(Mockito.eq(new StubbedInvocationMatcher("testMatcher", null, null, null, null)));
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
 when(mockHandler.handle(Mockito.any())).thenReturn(null);
 when(mockHandler.getInvocationContainer().getAnswersForStubbing())
 .thenReturn(new ArrayList<Answer>() {{
 add(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "answer 1";
 }
 });
 add(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "answer 2";
 }
 });
 }});

 // when
 Object answer1 = mockHandler.handle(Mockito.any());
 Object answer2 = mockHandler.handle(Mockito.any());

 // then
 // verify that the answers were returned in the correct order
 verify(mockHandler.getInvocationContainer()).getAnswersForStubbing().get(0).answer(Mockito.any())
 .equals("answer 1");
 verify(mockHandler.getInvocationContainer()).getAnswersForStubbing().get(1).answer(Mockito.any())
 .equals("answer 2");
 }
}