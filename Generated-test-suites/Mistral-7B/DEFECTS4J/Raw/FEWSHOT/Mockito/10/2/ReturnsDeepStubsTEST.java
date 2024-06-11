package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ReturnsDeepStubsTest {

 private static final Class<?> DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testDeepStubsReturnTypeIsInstanceOf() {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());
 assertInstanceOf(DeepStubsClass.class, mock);
 }

 @Test
 public void testDeepStubsAnswerInvocationWithSameArgumentsAndReturnType() throws Throwable {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());
 Object stubbedMock = mock(StubbedDeepStubsClass.class);

 when(mock.deepMethod(stubbedMock)).thenReturn(stubbedMock);

 Object result = mock.deepMethod(stubbedMock);
 assertEquals(stubbedMock, result);
 }

 @Test
 public void testDeepStubsAnswerInvocationWithDifferentArgumentsButSameReturnType() throws Throwable {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());
 Object stubbedMock1 = mock(StubbedDeepStubsClass1.class);
 Object stubbedMock2 = mock(StubbedDeepStubsClass2.class);

 when(mock.deepMethod(stubbedMock1)).thenReturn(stubbedMock1);
 when(mock.deepMethod(stubbedMock2)).thenReturn(stubbedMock2);

 Object result1 = mock.deepMethod(stubbedMock1);
 assertEquals(stubbedMock1, result1);

 Object result2 = mock.deepMethod(stubbedMock2);
 assertEquals(stubbedMock2, result2);
 }

 @Test
 public void testDeepStubsAnswerInvocationWithSameArgumentsButDifferentReturnTypes() throws Throwable {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());
 Object stubbedMock1 = mock(StubbedDeepStubsClass1.class);
 Object stubbedMock2 = mock(StubbedDeepStubsClass2.class);

 when(mock.deepMethod1(stubbedMock1)).thenReturn(stubbedMock1);
 when(mock.deepMethod2(stubbedMock2)).thenReturn(stubbedMock2);

 Object result1 = mock.deepMethod1(stubbedMock1);
 assertEquals(stubbedMock1, result1);

 Object result2 = mock.deepMethod2(stubbedMock2);
 assertEquals(stubbedMock2, result2);

 assertNotSame(result1, result2);
 }

 @Test
 public void testDeepStubsAnswerInvocationWithNull() throws Throwable {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());

 when(mock.deepMethod(null)).thenReturn(null);

 Object result = mock.deepMethod(null);
 assertNull(result);
 }

 @Test
 public void testDeepStubsAnswerInvocationWithException() throws Throwable {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());

 doThrow(new RuntimeException("Test Exception")).when(mock).deepMethod(any());

 try {
 mock.deepMethod(any());
 fail("Expected an exception to be thrown.");
 } catch (RuntimeException e) {
 assertEquals("Test Exception", e.getMessage());
 }
 }

 @Test
 public void testDeepStubsSerialization() throws Exception {
 Object mock = mock(DeepStubsClass.class, new ReturnsDeepStubs());
 Object deepStub = newDeepStub(mock.getClass(), new GenericType[0]);

 // deep stub should be serializable
 byte[] serializedDeepStub = MockUtil.serialize(deepStub);
 Object deserializedDeepStub = MockUtil.deserialize(serializedDeepStub);

 assertSame(deepStub, deserializedDeepStub);
 }

 private static Object newDeepStub(Class<?> clazz, GenericType[] genericTypes) throws Exception {
 Object returnDeepStub = Mockito.mock(clazz, withSettings().defaultAnswer(new ReturnsDeepStubs()));

 // We need to use reflection to set the generic metadata, since it's a non-public field
 Object fieldValue = ReflectionUtils.getFieldValue(returnDeepStub, "returnTypeGenericMetadata");
 ReflectionUtils.setFieldValue(fieldValue, new ReturnsDeepStubsSerializationFallback(new GenericMetadataSupport(clazz, genericTypes)));

 return returnDeepStub;
}

 private static class DeepStubsClass {
 interface DeepStubsInterface<T> {
 T deepMethod(T arg);
 }

 interface StubbedDeepStubsClass1 extends DeepStubsInterface<StubbedDeepStubsClass1> {
 }

 interface StubbedDeepStubsClass2 extends DeepStubsInterface<StubbedDeepStubsClass2> {
 }
 }

 private static class StubbedDeepStubsClass1 implements DeepStubsInterface<StubbedDeepStubsClass1> {
 }

 private static class StubbedDeepStubsClass2 implements DeepStubsInterface<StubbedDeepStubsClass2> {
 }
}