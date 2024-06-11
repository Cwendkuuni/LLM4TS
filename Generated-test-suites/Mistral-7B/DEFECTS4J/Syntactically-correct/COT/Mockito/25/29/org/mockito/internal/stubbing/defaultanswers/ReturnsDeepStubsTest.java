import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testReturnsExistingMock() {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.method1()).thenReturn("Some Value");

 Object result = returnsDeepStubs.answer(invocation);

 verify(mock).method1();
 assertEquals("Some Value", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturnsEmptyValuesForNonMockableType() {
 when(new MockCreationValidator().isTypeMockable(String.class)).thenReturn(false);
 Object result = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));

 verifyZeroInteractions(result);
 }

 @Test
 public void testReturnsNewMockForInvocation() {
 Object mockContainer = mock(InvocationContainerImpl.class);
 Object mockInvocation = mock(InvocationOnMock.class);
 when(mockContainer.getInvocationForStubbing()).thenReturn(mockInvocation);

 when(mockContainer.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

 Object result = returnsDeepStubs.answer(mockInvocation);

 Object newMock = mock(mockInvocation.getMethod().getReturnType(), ReturnsDeepStubs.class);
 verify(mockContainer).addAnswer(Mockito.isA(Answer.class), Mockito.anyBoolean());
 verify(newMock).answer(mockInvocation);
 assertEquals(newMock, result);
 }

 @Test
 public void testRecordDeepStubMock() {
 Object mockContainer = mock(InvocationContainerImpl.class);
 Object mockInvocation = mock(InvocationOnMock.class);
 Object mockReturnType = mock(String.class);
 when(mockContainer.getInvocationForStubbing()).thenReturn(mockInvocation);

 when(mockContainer.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{
         new StubbedInvocationMatcher() {
             @Override
             public boolean matches(InvocationOnMock invocation) {
                 return mockInvocation.getMethod().getName().equals("method1");
             }
         }
 });

 when(mockInvocation.getMethod()).thenReturn(new GenericDeclaration(){
             @Override
             public Type getGenericReturnType() {
                 return new Type[]{String.class};
             }
         }.getGenericReturnType());

 Object result = returnsDeepStubs.recordDeepStubMock(mockInvocation, mockContainer);

 verify(mockContainer).addAnswer(Mockito.isA(Answer.class), Mockito.anyBoolean());
 verify(mockReturnType).answer(mockInvocation);
 assertEquals(mockReturnType, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullParameter() {
 InvocationOnMock invocation = null;
 Object result = returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testSerialization() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object deepStub = returnsDeepStubs.answer(mock(InvocationOnMock.class));

 Object deepStubCopy = (ReturnsDeepStubs) DeepSerializationUtils.copy(deepStub);

 verify(mock).method1();
 verify(deepStub).answer(Mockito.any(InvocationOnMock.class));
 verify(deepStubCopy).answer(Mockito.any(InvocationOnMock.class));
 assertEquals(deepStub, deepStubCopy.answer(Mockito.any(InvocationOnMock.class)));
 }

 @Test
 public void testNestedGenerics() {
 interface GenericsNest<K extends Comparable<K>& Cloneable, T> extends Map<K, Set<T>> {
 }

 GenericsNest<String, Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();

 Object resultNumber = returnsDeepStubs.answer(mock(InvocationOnMock.class).getTarget().method("entrySet", mock(Class[].class)).invoke(mock));
 Object resultSet = returnsDeepStubs.answer(mock(InvocationOnMock.class).getTarget().method("iterator", new Class[0]).invoke(resultNumber));
 Object resultIterator = returnsDeepStubs.answer(mock(InvocationOnMock.class).getTarget().method("next", new Class[0]).invoke(resultSet));
 Object resultValue = returnsDeepStubs.answer(mock(InvocationOnMock.class).getTarget().method("getValue", new Class[0]).invoke(resultIterator));

 verify(mock).entrySet();
 verify(mock).method("iterator", new Class[0]).andReturn(resultSet);
 verify(resultSet).iterator();
 verify(resultIterator).next();
 assertEquals(number, resultValue);
 }
}