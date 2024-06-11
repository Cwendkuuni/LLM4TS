package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

 private Object mock;
 private InvocationContainerImpl container;

 @Test
 public void testAnswer() {
 mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mock.answer(eq(invocation))).thenReturn("result");

 assertEquals("result", ReturnsDeepStubs.getInstance().answer(invocation));
 }

 @Test
 public void testGetMock() throws Throwable {
 mock = mock(Object.class, new ReturnsDeepStubs());
 container = new MockUtil().getMockHandler(mock).getInvocationContainer();

 Class<?> clazz = Object.class;
 when(mock.answer(any(InvocationOnMock.class))).thenReturn(clazz);

 Object deepStub = ReturnsDeepStubs.getInstance().getMock(new InvocationOnMock(mock, anyMethod()));

 assertSame(mock, deepStub);
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 mock = mock(Object.class, new ReturnsDeepStubs());
 container = new MockUtil().getMockHandler(mock).getInvocationContainer();

 Class<?> clazz = Object.class;
 when(mock.answer(any(InvocationOnMock.class))).thenReturn(clazz);

 Object deepStub = ReturnsDeepStubs.getInstance().recordDeepStubMock(new InvocationOnMock(mock, eq(anyMethod())), container);

 assertSame(mock, deepStub);
 }

 @Test
 public void testReturnEmptyValueFor() throws Throwable {
 mock = mock(Object[].class);
 when(new ReturnsDeepStubs().returnValueFor(Object[].class)).thenReturn(new Object[0]);

 assertArrayEquals(new Object[0], ReturnsDeepStubs.getInstance().answer(any(InvocationOnMock.class)));
 }

 @Test
 public void testIsTypeMockable() {
 assertTrue(ReturnsDeepStubs.getInstance().isTypeMockable(String.class));
 assertFalse(ReturnsDeepStubs.getInstance().isTypeMockable(int[].class));
 }
}