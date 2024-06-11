package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

 private InternalMockHandler<Object> handler;
 private InvocationContainerImpl container;
 private Object mock;
 private GenericMetadataSupport returnTypeGenericMetadata;

 @Before
 public void setup() throws Exception {
 mock = mock(Object.class, new ReturnsDeepStubs());
 handler = new MockUtil().getMockHandler(mock);
 container = (InvocationContainerImpl) handler.getInvocationContainer();
 returnTypeGenericMetadata = new GenericMetadataSupport(Object.class);
 }

 @Test
 public void testAnswer() throws Exception {
 Object answer = ReturnsDeepStubs.answer(new InvocationOnMock(mock, new Object()[0], null));
 assertSame(mock, answer);
 }

 @Test
 public void testDeepStub() throws Exception {
 Object deepStub = ReturnsDeepStubs.deepStub(new InvocationOnMock(mock, new Object()[0], null), returnTypeGenericMetadata);
 assertSame(mock, deepStub);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 Object newMock = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 Object answer = container.getAnswer();
 assertSame(newMock, answer);
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 Object newMock = ReturnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
 assertSame(mockitoCore().mock(returnTypeGenericMetadata.rawType()), newMock);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 Object newMock = ReturnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata).mock(Object.class);
 assertSame(mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettings().defaultAnswer(ReturnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))), newMock);
 }

 @Test
 public void testStubbedInvocationMatcher() throws Exception {
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(new InvocationOnMock(mock, new Object()[0], null), new Object[0]);
 verify(container).addAnswer(Mockito.eq(new DeeplyStubbedAnswer(mock)), Mockito.eq(false));
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 GenericMetadataSupport actualParameterizedType = ReturnsDeepStubs.actualParameterizedType(mock);
 assertSame(returnTypeGenericMetadata, actualParameterizedType);
 }
}