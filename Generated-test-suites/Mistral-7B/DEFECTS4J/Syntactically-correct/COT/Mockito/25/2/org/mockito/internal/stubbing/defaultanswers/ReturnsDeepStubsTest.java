import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerForPrimitiveType() throws Exception {
 int returnType = 42;
 Object mock = mock(Integer.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Integer.class, "testMethod", new Class[0]));
 invocation.setTarget(mock);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(returnType, result);
 }

 @Test
 public void testAnswerForObjectType() throws Exception {
 String returnType = "mockito";
 Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "testMethod", new Class[0]));
 invocation.setTarget(mock);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(returnType, result);
 }

 @Test
 public void testAnswerForClassWithGenericType() throws Exception {
 class MapEntry<K extends Comparable<K> & Cloneable, V extends Number> {
 }
 MapEntry<String, Double> mapEntry = mock(MapEntry.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(MapEntry.class, "getValue", new Class[0]));
 invocation.setTarget(mapEntry);
 Object result = returnsDeepStubs.answer(invocation);
 // Verify that a new mock for Double class was created
 verify(mapEntry).getValue();
 Double doubleMock = Mockito.mock(Double.class, Answers.RETURNS_DEEP_STUBS);
 container.addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return doubleMock;
 }
 }, false);
 assertEquals(doubleMock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswerForNonMockableType() throws Exception {
 int[] intArray = new int[1];
 Object mock = mock(Object[].class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Object[].class, "getClass", new Class[0]));
 invocation.setTarget(mock);
 Object result = returnsDeepStubs.answer(invocation);
 // This should throw an exception as int[] is not mockable
 // according to Mockito's MockCreationValidator
 }

}