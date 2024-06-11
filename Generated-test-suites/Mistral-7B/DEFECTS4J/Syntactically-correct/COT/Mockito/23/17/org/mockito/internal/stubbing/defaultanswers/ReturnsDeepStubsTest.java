import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<?>[] EMPTY_TYPE_ARRAY = new Class[0];

 @Test
 public void testAnswerMethodWithEmptyArguments() {
 MockSettings mockSettings = Mockito.settings();
 Object mock = mock(Object.class, mockSettings);

 Answer<Object> answer = new ReturnsDeepStubs();
 Object result = answer.answer(Mockito.mock(InvocationOnMock.class));

 verify(mock).answers(answer);
 verifyNoMoreInteractions(mock);
 assertSame(mock, result);
 }

 @Test
 public void testAnswerMethodWithGenericReturnType() {
 Class<?> genericType = List.class;
 GenericDeclaration genericDeclaration = Arrays.asList(Collections.class, List.class).get(1);
 Type genericReturnType = ((Type) genericDeclaration.getGenericReturnType());

 MockSettings mockSettings = Mockito.settings();
 Object mock = mock(genericType, mockSettings);

 Answer<Object> answer = new ReturnsDeepStubs();
 Object result = answer.answer(Mockito.mock(InvocationOnMock.class));

 verify(mock).answers(answer);
 verifyNoMoreInteractions(mock);
 assertSame(mock, result);

 // Verify that the actual generic type information is passed to the ReturnsDeepStubs instance
 ReturnsDeepStubs returnsDeepStubs = (ReturnsDeepStubs) answer;
 assertEquals(genericReturnType, returnsDeepStubs.actualParameterizedType(mock));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethodWithUnmockableType() {
 Class<?> unmockableType = String[].class;

 MockSettings mockSettings = Mockito.settings();
 Object mock = mock(unmockableType, mockSettings);

 Answer<Object> answer = new ReturnsDeepStubs();
 answer.answer(Mockito.mock(InvocationOnMock.class));
 }

 @Test
 public void testGetMockMethodWithEmptyContainer() {
 InvocationContainerImpl emptyContainer = new InvocationContainerImpl(null);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class), null);

 assertSame(new ReturnsEmptyValues(), result);
 }

 @Test
 public void testGetMockMethodWithStubbedInvocation() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = new InvocationContainerImpl(invocation);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

 container.setStubbedInvocation(stubbedInvocationMatcher);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class), null);

 assertSame(stubbedInvocationMatcher, result);
 }

 @Test
 public void testGetMockMethodWithDeepStub() {
 Class<?> deepStubType = List.class;
 MockSettings mockSettings = Mockito.settings();
 Object mock = mock(deepStubType, mockSettings);
 InvocationContainerImpl container = new InvocationContainerImpl(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class), null);

 assertSame(mock, result);

 verify(mock).answers(returnsDeepStubs);
 }

 @Test
 public void testCreateNewDeepStubMockMethod() {
 Class<?> deepStubType = List.class;
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(deepStubType);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

 assertInstanceOf(deepStubType, mock);
 assertSame(returnsDeepStubs, Mockito.answer(mock).getAnswer());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateNewDeepStubMockMethodWithNullReturnTypeMetadata() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.createNewDeepStubMock(null);
 }

 @Test
 public void testRecordDeepStubMockMethodWithEmptyContainer() {
 Object mock = mock(Object.class);
 InvocationContainerImpl emptyContainer = new InvocationContainerImpl(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.recordDeepStubMock(mock, emptyContainer);

 verifyNoInteractions(mock);
 }

 @Test
 public void testRecordDeepStubMockMethodWithExistingAnswer() {
 Object mock = mock(Object.class);
 Answer<Object> existingAnswer = Mockito.mock(Answer.class);
 InvocationContainerImpl container = new InvocationContainerImpl(mock);
 container.addAnswer(existingAnswer, false);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.recordDeepStubMock(mock, container);

 verify(mock).answers(returnsDeepStubs);
 }

 @Test
 public void testRecordDeepStubMockMethodWithNewAnswer() {
 Object mock = mock(Object.class);
 InvocationContainerImpl container = new InvocationContainerImpl(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

 assertSame(mock, result);
 verify(mock).answers(returnsDeepStubs);
 }
}