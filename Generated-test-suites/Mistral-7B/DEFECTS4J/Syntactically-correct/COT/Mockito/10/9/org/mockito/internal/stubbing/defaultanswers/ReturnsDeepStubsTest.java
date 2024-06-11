package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.withSettingsAnnotation;
import static org.mockito.internal.util.reflection.GenericMetadataSupport.inferFrom;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;
 private InvocationOnMock invocation;

 @Before
 public void setup() {
 mock = mock(Object.class, new ReturnsDeepStubs());
 }

 @Test
 public void testAnswerMethod() throws Throwable {
 Type returnType = inferFrom(mock.getClass()).getRawType();
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[0]);
 Object answer = returnsDeepStubs.answer(invocation);

 assert (answer == mock);
 verify(mock).answer(invocation);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedType() throws Throwable {
 Type unsupportedType = Integer.class;
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[0]);
 returnsDeepStubs = new ReturnsDeepStubs();
 Object answer = returnsDeepStubs.answer(invocation);

 // since this type is not mockable, return empty answer
 }

 @Test
 public void testDeepStub() throws Throwable {
 Object nestedMock = mock(Object.class, new ReturnsDeepStubs());
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[]{nestedMock});
 Object deepStubAnswer = returnsDeepStubs.deepStub(invocation, inferFrom(nestedMock.getClass()));

 assert (deepStubAnswer == nestedMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotFound() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[0]);
 returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStubAnswer = returnsDeepStubs.deepStub(invocation, inferFrom(String.class));
 }

 @Test
 public void testNewDeepStubMock() throws Throwable {
 Type returnType = Integer[].class;
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[]{new Integer[0]});
 Object newDeepStubMock = returnsDeepStubs.newDeepStubMock(inferFrom(returnType));

 assert (newDeepStubMock instanceof Integer[]);
 }

 @Test
 public void testWithSettingsUsing() throws Throwable {
 Type returnType = Integer[].class;
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[]{new Integer[0]});
 MockSettings settings = withSettings().serializable().defaultAnswer(Answers.RETURNS_DEEP_STUBS);
 Object newDeepStubMock = mock(returnType.getComponentType(), settings);

 assert (newDeepStubMock instanceof Integer);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Throwable {
 Type returnType = Integer[].class;
 invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(new Mockito().getTestMethod());
 invocation.setMock(mock);
 invocation.setArguments(new Object[]{new Integer[0]});
 Object returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(inferFrom(returnType));

 assert (returnsDeepStubsAnswer instanceof ReturnsDeepStubs);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Throwable {
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore(), mock, new Object[0]);
 Object recordDeepStubAnswer = returnsDeepStubs.recordDeepStubAnswer(mock, container);

 assert (recordDeepStubAnswer == mock);
 }

 @Test
 public void testActualParameterizedType() throws Throwable {
 Type type = mock.getClass();
 Object actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);

 assert (actualParameterizedType.equals(type));
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteReplace() throws Throwable {
 Object returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs = (ReturnsDeepStubs) returnsDeepStubs.writeReplace();
 }
}