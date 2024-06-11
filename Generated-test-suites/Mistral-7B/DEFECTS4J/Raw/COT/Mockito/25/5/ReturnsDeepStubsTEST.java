package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

 private static final class MyGenericsNest<K extends Comparable<K> & Cloneable, V extends Number> implements Serializable, Map<K, Set<V>> {
 }

 @Test
 public void testAnswer_NonMockableType() {
 String result = new ReturnsDeepStubs().answer(Mockito.mock(String.class));
 Assertions.assertNull(result);
 }

 @Test
 public void testAnswer_MockableType() {
 MyGenericsNest mock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 MyGenericsNest nestedMock = mock.entrySet().iterator().next().getValue().iterator().next();
 Number number = nestedMock.iterator().next();
 Assertions.assertNotNull(number);
 }

 @Test
 public void testAnswer_InvocationWithEmptyContainer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 Object result = deepStubs.answer(invocation);
 Assertions.assertNull(result);
 }

 @Test
 public void testAnswer_InvocationWithStubbedInvocation() {
 MyGenericsNest mock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 MyGenericsNest nestedMock = mock.entrySet().iterator().next().getValue().iterator().next();
 MyGenericsNest stubbedMock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 MyGenericsNest nestedStubbedMock = stubbedMock.entrySet().iterator().next().getValue().iterator().next();

 Mockito.doAnswer(Mockito.mock(Answer.class)).when(nestedMock).iterator();
 Mockito.doAnswer(ArgumentMatchers.isInstance(stubbedMock)).when(mock).entrySet().iterator().next().getValue().iterator();

 Object result = mock.entrySet().iterator().next().getValue().iterator().next();
 Assertions.assertSame(nestedStubbedMock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswer_NullInvocation() {
 new ReturnsDeepStubs().answer(null);
 }

 @Test
 public void testGetMock_NonMockableType() {
 String result = new ReturnsDeepStubs().getMock(Mockito.mock(String.class));
 Assertions.assertNull(result);
 }

 @Test
 public void testGetMock_MockableTypeWithoutContainer() {
 MyGenericsNest mock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 Object result = new ReturnsDeepStubs().getMock(Mockito.mock(InvocationOnMock.class));
 Assertions.assertNotNull(result);
 Assertions.assertTrue(result instanceof MyGenericsNest);
 }

 @Test
 public void testGetMock_MockableTypeWithContainerAndStubbedInvocation() {
 MyGenericsNest mock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 MyGenericsNest nestedMock = mock.entrySet().iterator().next().getValue().iterator().next();
 MyGenericsNest stubbedMock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 MyGenericsNest nestedStubbedMock = stubbedMock.entrySet().iterator().next().getValue().iterator().next();

 Mockito.doAnswer(Mockito.mock(Answer.class)).when(nestedMock).iterator();
 Mockito.doAnswer(ArgumentMatchers.isInstance(stubbedMock)).when(mock).entrySet().iterator().next().getValue().iterator();

 Object result = new ReturnsDeepStubs().getMock(Mockito.mock(InvocationOnMock.class));
 Assertions.assertSame(nestedStubbedMock, result);
 }

 @Test
 public void testGetMock_MockableTypeWithContainerAndNoStubbedInvocation() {
 MyGenericsNest mock = Mockito.mock(MyGenericsNest.class, new ReturnsDeepStubs());
 Object result = new ReturnsDeepStubs().getMock(Mockito.mock(InvocationOnMock.class));
 Assertions.assertNotNull(result);
 Assertions.assertTrue(result instanceof MyGenericsNest);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMock_NullInvocation() {
 new ReturnsDeepStubs().getMock(null);
 }

 @Test
 public void testRecordDeepStubMock_NullInvocation() {
 InvocationOnMock invocation = null;
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 deepStubs.recordDeepStubMock(invocation, null);
 }

 @Test
 public void testRecordDeepStubMock_NonMockableType() {
 String invocationMock = Mockito.mock(String.class);
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 deepStubs.recordDeepStubMock(invocationMock, null);
 }

 @Test
 public void testRecordDeepStubMock_MockableType() {
 MyGenericsNest invocationMock = Mockito.mock(MyGenericsNest.class);
 InvocationContainerImpl container = new InvocationContainerImpl();
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 deepStubs.recordDeepStubMock(invocationMock, container);
 MyGenericsNest result = (MyGenericsNest) container.getAnswer(Mockito.mock(InvocationOnMock.class)).answer(Mockito.mock(InvocationOnMock.class));
 Assertions.assertSame(invocationMock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRecordDeepStubMock_NullContainer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 deepStubs.recordDeepStubMock(invocation, null);
 }
}