package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static final long serialVersionUID = -7105341425736035847L;

 @Test
 public void testAnswerWithEmptyValues() throws Exception {
     Object emptyValuesMock = mock(ReturnsEmptyValues.class, new Answers.ReturnsEmpty());
     Answer answer = new ReturnsDeepStubs();

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = answer.answer(invocation);

     assertSame(emptyValuesMock, result);
 }

 @Test
 public void testAnswerWithMockableType() throws Exception {
     Class<Map> mapClass = Map.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = ((Answer<Object>) mapMock).answer(invocation);

     assertSame(mapMock, result);
 }

 @Test
 public void testAnswerWithNonMockableType() throws Exception {
     Class<String> stringClass = String.class;
     Object stringMock = mock(String.class, new ReturnsEmptyValues());

     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     Object result = returnsDeepStubs.answer(Mockito.mock(Object.class));

     assertSame(stringMock, result);
 }

 @Test
 public void testAnswerWithAlreadyMockedType() throws Exception {
     Class<Map> mapClass = Map.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());

     when(mapMock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(1);

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = ((Answer<Object>) mapMock).answer(invocation);

     verify(mapMock).entrySet();
     verify(mapMock.entrySet()).iterator();
     verify(mapMock.entrySet().iterator()).next();
     verify(mapMock.entrySet().iterator().next()).getValue();
     verify(mapMock.entrySet().iterator().next().getValue()).iterator();
     verify(mapMock.entrySet().iterator().next().getValue().iterator()).next();

     assertSame(mapMock, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerWithUnsupportedOperation() throws Exception {
     Class<Number> numberClass = Number.class;
     Number numberMock = mock(numberClass, new ReturnsDeepStubs());

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = ((Answer<Object>) numberMock).answer(invocation);

     throw new UnsupportedOperationException("Answer with unsupported operation");
 }

 @Test
 public void testGetMockWithEmptyStubbedInvocations() throws Exception {
     Class<Map> mapClass = Map.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = new ReturnsDeepStubs().getMock(invocation, Mockito.type(Map.class));

     assertSame(mapMock, result);
 }

 @Test
 public void testGetMockWithStubbedInvocation() throws Exception {
     Class<Map> mapClass = Map.class;
     Class<Number> numberClass = Number.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());
     Number numberMock = mock(numberClass, new ReturnsDeepStubs());

     when(mapMock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(numberMock);

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = new ReturnsDeepStubs().getMock(invocation, Mockito.type(Map.class));

     verifyZeroInteractions(mapMock);
     verifyZeroInteractions(numberMock);

     assertSame(numberMock, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockWithNullInvocation() throws Exception {
     InvocationOnMock nullInvocation = null;
     new ReturnsDeepStubs().getMock(nullInvocation, Mockito.type(Object.class));
 }

 @Test(expected = ClassCastException.class)
 public void testGetMockWithInvalidType() throws Exception {
     Class<String> stringClass = String.class;
     Object stringMock = mock(stringClass, new ReturnsDeepStubs());

     InvocationOnMock invocation = Mockito.mock(Object.class);
     new ReturnsDeepStubs().getMock(invocation, Mockito.type(String[].class));
 }

 @Test
 public void testCreateNewDeepStubMockWithEmptyReturnTypeGenericMetadata() throws Exception {
     Class<Object> objectClass = Object.class;
     Object deepStubMock = new ReturnsDeepStubs().createNewDeepStubMock(Mockito.type(Object.class));

     assertNotNull(deepStubMock);
 }

 @Test
 public void testCreateNewDeepStubMockWithDeepStubsAnswerUsing() throws Exception {
     Class<Map> mapClass = Map.class;
     Class<Number> numberClass = Number.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());
     Number numberMock = mock(numberClass, new ReturnsDeepStubs());

     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
         @Override
         protected GenericMetadataSupport actualParameterizedType(Object mock) {
             if (mapMock.equals(mock)) {
                 return Mockito.type(Map.class);
             } else if (numberMock.equals(mock)) {
                 return Mockito.type(Number.class);
             } else {
                 return super.actualParameterizedType(mock);
             }
         }
     };
     Object deepStubMock = returnsDeepStubs.createNewDeepStubMock(mapMock);

     assertNotNull(deepStubMock);
 }

 @Test
 public void testRecordDeepStubMockWithEmptyContainer() throws Exception {
     Object deepStubMock = mock(Object.class, new ReturnsDeepStubs());
     InvocationContainerImpl emptyContainer = new InvocationContainerImpl(null);

     new ReturnsDeepStubs().recordDeepStubMock(deepStubMock, emptyContainer);
 }

 @Test
 public void testRecordDeepStubMockWithStubbedInvocation() throws Exception {
     Class<Map> mapClass = Map.class;
     Class<Number> numberClass = Number.class;
     Map<Object, Set<Number>> mapMock = mock(mapClass, new ReturnsDeepStubs());
     Number numberMock = mock(numberClass, new ReturnsDeepStubs());

     InvocationContainerImpl container = new InvocationContainerImpl(mapMock);
     new ReturnsDeepStubs().recordDeepStubMock(numberMock, container);

     InvocationOnMock invocation = Mockito.mock(Object.class);
     Object result = mapMock.answer(invocation);

     verify(numberMock).answer(invocation);

     assertSame(numberMock, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordDeepStubMockWithNullMock() throws Exception {
     InvocationContainerImpl container = new InvocationContainerImpl(null);
     new ReturnsDeepStubs().recordDeepStubMock(null, container);
 }
}