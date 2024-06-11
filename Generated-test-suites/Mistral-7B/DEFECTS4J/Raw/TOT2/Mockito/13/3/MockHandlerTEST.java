package org.mockito.internal.test;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;
import org.mockito.verification.*;

public class MockHandlerTest {

private MockHandler<Object> mockHandler;
private Object mock;

@Before
public void setUp() {
mock = Mockito.mock(Object.class);
mockHandler = new MockHandler<Object>(mock);
}

@Test
public void testHandleMethod() throws Throwable {
InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
Answer answer = Mockito.answer(new Object());
mockHandler.handle(invocation);
Mockito.verify(mockHandler).handle(invocation);
Mockito.verify(mockHandler).setAnswersForStubbing(answer);
Mockito.verify(mockHandler).invocationContainerImpl.setInvocationForPotentialStubbing(Mockito.eq(invocation));
}

@Test
public void testConstructorWithMockSettings() {
// empty test case
}

@Test
public void testConstructorWithOldMockHandler() {
MockHandler<Object> oldMockHandler = Mockito.mock(MockHandler.class);
MockHandler<Object> mockHandler = new MockHandler<Object>(oldMockHandler);
}

@Test(expected = NullPointerException.class)
public void testHandleNullInvocation() throws Throwable {
mockHandler.handle(null);
}

@Test(expected = IllegalArgumentException.class)
public void testHandleWithIncorrectMockObject() throws Throwable {
InvocationOnMock incorrectMockObject = Mockito.mock(Object.class);
mockHandler.handle(incorrectMockObject);
}

@Test
public void testHandleWithUnsupportedMethod() throws Throwable {
InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
when(invocation.getMethod()).thenReturn(new Method("unsupportedMethod", new Class[0]));
mockHandler.handle(invocation);
}

@Test(expected = IllegalArgumentException.class)
public void testStubbingNullAnswer() {
List<Answer> emptyAnswerList = new ArrayList<>();
mockHandler.setAnswersForStubbing(emptyAnswerList);
}

@Test(expected = IllegalArgumentException.class)
public void testStubbingVoidAnswerForNonVoidMethod() {
List<Answer> answers = new ArrayList<>();
answers.add(Mockito.answer(new VoidMethodStubbableImpl<Object>(mock, invocationContainerImpl)));
mockHandler.setAnswersForStubbing(answers);
}
}