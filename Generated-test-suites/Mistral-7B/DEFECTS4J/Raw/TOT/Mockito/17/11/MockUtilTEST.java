package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 mockHandler = Mockito.mock(MockHandler.class);
 spiedInstance = new Object() {};
 }

 @Test
 public void testCreateMock() {
 Class<?> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance);
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertEquals(mockHandler, Mockito.mock(MockHandler.class).eq(mockUtil.getMockHandler(mock)));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 mockUtil.createMock(String.class, new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstanceOfDifferentType() {
 mockUtil.createMock(Integer.class, new MockSettingsImpl(Double.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, null));
 }

 @Test
 public void testResetMock() {
 T mock = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Object.class }, new MockHandler<Object>(new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance)));
 mockUtil.resetMock(mock);
 assertFalse(mockUtil.isMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMock() {
 Object nonMock = new Object() {};
 mockUtil.resetMock(nonMock);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = Mockito.mock(Object.class);
 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMock() {
 Object nonMock = new Object() {};
 mockUtil.getMockHandler(nonMock);
 }

 @Test
 public void testIsMock() {
 assertFalse(mockUtil.isMock(spiedInstance));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNull() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockObject() {
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptor() {
 T mock = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Object.class }, new MockHandler<Object>(new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, spiedInstance)));
 assertEquals(mockHandler, mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMock() {
 Object nonMock = new Object() {};
 mockUtil.getInterceptor(nonMock);
 }

 @Test
 public void testGetMockName() {
 MockSettingsImpl settings = new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, null);
 assertEquals("MockUtil_1_Object", mockUtil.getMockName(mockUtil.createMock(Object.class, settings).getClass()));
 }

 @Test
 public void testLenientCopy() {
 Object source = new Object() {
 @Override public String toString() { return "source"; }
 };
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, mockHandler, new CreationValidator(), null, null, null, null, null, null, source));
 assertEquals("source", mockUtil.getMockName(mock));
 LenientCopyTool.copyToMock(source, mock);
 assertEquals("MockUtil_1_Object", mockUtil.getMockName(mock));
 }
}