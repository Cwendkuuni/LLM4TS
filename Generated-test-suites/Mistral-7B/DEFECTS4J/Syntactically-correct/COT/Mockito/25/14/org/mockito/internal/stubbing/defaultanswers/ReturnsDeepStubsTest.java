import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswer_NonMockableType() throws Exception {
 String nonMockableType = new String("nonMockableType");
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(null, nonMockableType);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(Answers.RETURNS_DEFAULTS.get(Object.class), result);
 }

 @Test
 public void testAnswer_SameInvocation() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, null);
 Object result = returnsDeepStubs.answer(invocation);
 assertEquals(mock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswer_NullInvocation() throws Exception {
 returnsDeepStubs.answer(null);
 }

 @Test
 public void testGetMock_NoMatchingStubbedInvocation() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, null);
 Object result = returnsDeepStubs.getMock(invocation);
assertEquals(new ReturnsEmptyValues().returnValueFor(Object.class), result);
 }

 @Test
 public void testGetMock_MatchingStubbedInvocation() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object stubbedMock = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, stubbedMock);
 Mockito.when(stubbedMock.answer(invocation)).thenReturn(new Object());
 Object result = returnsDeepStubs.getMock(invocation);
 assertEquals(stubbedMock, result);
 }

 @Test
 public void testRecordDeepStubMock_SameClass() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, null);
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, Mockito.mock(InvocationContainerImpl.class));
 assertEquals(mock, deepStub);
 }

 @Test
 public void testRecordDeepStubMock_DifferentClass() throws Exception {
 Object mockObject = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 Object mockList = mock(List.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mockObject, mockList);
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, Mockito.mock(InvocationContainerImpl.class));
 assertEquals(mockList, deepStub);
 }

 @Test
 public void testRecordDeepStubMock_NestedGenericType() throws Exception {
 Class<?> genericInterface = new Class<GenericsNest<String>>() {}.getGenericInterfaces()[0];
 Object mock = mock(genericInterface, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, null);
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, Mockito.mock(InvocationContainerImpl.class));
 assertEquals(mock, deepStub);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordDeepStubMock_AlreadyRecorded() throws Exception {
 Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class).invoke(mock, null);
 Object deepStub = returnsDeepStubs.getMock(invocation);
 returnsDeepStubs.recordDeepStubMock(invocation, Mockito.mock(InvocationContainerImpl.class));
 }

 @Test
 public void testActualParameterizedType_NullMock() throws Exception {
 assertEquals(null, returnsDeepStubs.actualParameterizedType(null));
 }

 @Test
 public void testActualParameterizedType_SimpleClass() throws Exception {
 Class<?> clz = String.class;
 assertEquals(Mockito.type(clz), returnsDeepStubs.actualParameterizedType(Mockito.mock(clz)).getType());
 }

 @Test
 public void testActualParameterizedType_GenericClass() throws Exception {
 Class<?> genericInterface = new Class<GenericsNest<String>>() {}.getGenericInterfaces()[0];
 assertEquals(Mockito.type(genericInterface), returnsDeepStubs.actualParameterizedType(Mockito.mock(genericInterface)).getType());
 }

 @Test
 public void testActualParameterizedType_GenericClassWithWildcard() throws Exception {
 Class<?> genericInterface = new Class<GenericsNest<?>>() {}.getGenericInterfaces()[0];
 assertEquals(Mockito.type(genericInterface), returnsDeepStubs.actualParameterizedType(Mockito.mock(genericInterface)).getType());
 }
}