package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert. assertInstanceOf;
import static org.mockito.Mockito. mock;
import static org.mockito.Mockito. spy;
import static org.mockito.Mockito. verify;
import static org.mockito.Mockito. when;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.AnswerInvocationHandler;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private MockitoCore mockitoCore = new MockitoCore();

 @Test
 public void testAnswer() {
 MockSettings mockSettings = mock(MockSettings.class);
 Object mock = mockitoCore.mock(Object.class, withSettings(mockSettings));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object result = answer.answer(mockitoCore.createMockInvocationOnMock(mock, Object.class, "testMethod", new Object[0]));

 assertInstanceOf(Object.class, result);
 }

 @Test
 public void testGetMockWithNoStubbedInvocations() throws Exception {
 Object mock = mock(Object.class);
 GenericMetadataSupport returnTypeGenericMetadata = MockitoCore.getReturnTypeGenericMetadata(mock.getClass());
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(mockitoCore.createMockInvocationOnMock(mock, Object.class, "testMethod", new Object[0]), returnTypeGenericMetadata);

 assertInstanceOf(Object.class, result);
 }

 @Test
 public void testGetMockWithStubbedInvocations() throws Exception {
 Object mock = mock(Object.class);
 GenericMetadataSupport returnTypeGenericMetadata = MockitoCore.getReturnTypeGenericMetadata(mock.getClass());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(AnswerInvocationHandler.class), null, null, new StubbedInvocationMatcher[0]);
 StubbedInvocationMatcher stubbedInvocationMatcher = spy(new StubbedInvocationMatcher(null, null, null, null));
 container.addStubbedInvocation(stubbedInvocationMatcher);

 when(stubbedInvocationMatcher.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(mock(Integer.class));
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object result = returnsDeepStubs.getMock(mockitoCore.createMockInvocationOnMock(mock, Object.class, "testMethod", new Object[0]), returnTypeGenericMetadata);

 assertInstanceOf(Object.class, result);
 verify(stubbedInvocationMatcher).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testCreateNewDeepStubMock() throws Exception {
 Class<?> typeToMock = Object.class;
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(typeToMock, new Type[] { Object.class, Object.class }, new Type[] { Integer.class });
 Object mock = mockitoCore.mock(typeToMock, withSettingsUsing(returnTypeGenericMetadata));
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStubMock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

 assertInstanceOf(typeToMock, deepStubMock);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 Class<?> typeToMock = Object.class;
 Class<?>[] extraInterfaces = new Class[] { Integer.class };
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(typeToMock, new Type[] { Object.class, Object.class }, new Type[] { Integer.class });

 MockSettings mockSettings = withSettings(returnTypeGenericMetadata.rawSettings());
 MockSettings withSettings = mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));

 assertInstanceOf(MockSettings.class, withSettings);
 assertArrayEquals(new Class[] { Integer.class }, withSettings.extraInterfaces());
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Exception {
 Class<?> typeToMock = Object.class;
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(typeToMock, new Type[] { Object.class, Object.class }, new Type[] { Integer.class });

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 ReturnsDeepStubs innerDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

 assertInstanceOf(ReturnsDeepStubs.class, innerDeepStubs);
 assertSame(returnTypeGenericMetadata, innerDeepStubs.actualParameterizedType(ArgumentMatchers.any(Object.class)));
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 Object mock = mock(Object.class);
 GenericMetadataSupport returnTypeGenericMetadata = MockitoCore.getReturnTypeGenericMetadata(mock.getClass());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(AnswerInvocationHandler.class), null, null, new StubbedInvocationMatcher[0]);
 Answer<Object> answer = returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
 container.addAnswer(answer);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(mock, container);

 assertInstanceOf(Object.class, deepStubMock);
 verify(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 private Answer<Object> returnsDeepStubsAnswerUsing(GenericMetadataSupport returnTypeGenericMetadata) {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 return returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
 }

 private static <T> Type[] newTypeArray(Class<T>... types) {
 return new Type[] { (Type) types };
 }

 private static void assertArrayEquals(Object[] expected, Object[] actual) {
 if (expected.length != actual.length) {
 throw new AssertionError("Unexpected array length: " + expected.length + " != " + actual.length);
 }
 for (int i = 0; i < expected.length; i++) {
 if (!expected[i].equals(actual[i])) {
 throw new AssertionError("Unexpected array element: " + i + " = " + expected[i] + " != " + actual[i]);
 }
 }
 }
}