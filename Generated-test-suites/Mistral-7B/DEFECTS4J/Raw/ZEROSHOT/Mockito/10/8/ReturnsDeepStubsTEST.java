package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.stubbing.Stubber.answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InternalMockHandler<Object> mockHandler;
 private InvocationContainerImpl container;
 private Object deepStub;
 private Object parentMock;

 @Before
 public void setUp() {
 mockHandler = Mockito.spy(new MockUtil().getMockHandler(mock(Object.class)));
 container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 returnsDeepStubs = new ReturnsDeepStubs();
 parentMock = mock(Object.class);
 }

 @Test
 public void testAnswer() throws Exception {
 Type returnType = Type.getRawType(Class.forName("java.util.Map"));
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(returnType);
 deepStub = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

 InvocationOnMock invocation = Mockito.mock(Object.class, withSettings().extraInterfaces(Map.class).serializable());
 InvocationOnMock invocationWithParent = Mockito.mock(Object.class, withSettings().serializable().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 invocationWithParent = Mockito.spy(invocationWithParent);
 invocationWithParent = Mockito.doReturn(parentMock).when(invocationWithParent).answer(invocation);
 container.addAnswer(answer(parentMock), false);

 Object resultWithParent = returnsDeepStubs.answer(invocationWithParent);
 Object resultWithoutParent = returnsDeepStubs.answer(invocation);

 assertSame(deepStub, resultWithParent);
 assertSame(deepStub, resultWithoutParent);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNotMockableType() throws Exception {
 Type notMockableType = Type.getRawType(int[].class);
 GenericMetadataSupport notMockableTypeGenericMetadata = new GenericMetadataSupport(notMockableType);
 returnsDeepStubs = new ReturnsDeepStubs();
 deepStub = returnsDeepStubs.newDeepStubMock(notMockableTypeGenericMetadata);

 InvocationOnMock invocation = Mockito.mock(Object.class, withSettings());
 Object result = returnsDeepStubs.answer(invocation);

 // Unsupported operation: java.lang.Class<int[]> cannot be instantiated
 assertNotNull(result);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 Object recordDeepStubAnswerMock = mock(Object.class);
 container.addAnswer(answer(recordDeepStubAnswerMock), false);
 deepStub = returnsDeepStubs.answer(Mockito.mock(Object.class, withSettings()));

 assertSame(recordDeepStubAnswerMock, deepStub);
 }
}