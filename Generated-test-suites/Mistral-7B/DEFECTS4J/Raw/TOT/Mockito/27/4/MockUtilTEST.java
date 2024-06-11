package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private MockHandler<Object> newMockHandler;
 private MethodInterceptorFilter filter;
 private Object spiedInstance;
 private Set<Class<?>> extraInterfaces;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 mockHandler = mock(MockHandler.class);
 newMockHandler = new MockHandler<>(mockHandler);
 filter = new MethodInterceptorFilter(mockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));
 spiedInstance = new Object() {};
 extraInterfaces = new HashSet<>(Arrays.asList(Serializable.class, HashSet.class));
 }

 @Test
 public void testCreateMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 assertInstanceOf(Object.class, mock);
 assertEquals(Object.class, mock.getClass());
 assertSame(spiedInstance, mockUtil.getMockHandler(mock).getMockName().getMock());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockNotValidType() {
 mockUtil.createMock(String.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterfaces() {
 mockUtil.createMock(Serializable.class, new MockSettingsImpl(Serializable.class, spiedInstance, extraInterfaces));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstance() {
 mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, null));
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockNotAMock() {
 Object notAMock = new Object() {};
 mockUtil.resetMock(notAMock);
 }

 @Test
 public void testNewMethodInterceptorFilter() {
 MockSettingsImpl settings = withSettings().defaultAnswer(RETURNS_DEEP_STUBS);
 filter = mockUtil.newMethodInterceptorFilter(settings);
 assertSame(settings, filter.getMockSettings());
 }

 @Test(expected = NullPointerException.class)
 public void testNewMethodInterceptorFilterNullSettings() {
 mockUtil.newMethodInterceptorFilter(null);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 assertSame(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNotAMock() {
 Object notAMock = new Object() {};
 mockUtil.getMockHandler(notAMock);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockNotAMock() {
 assertFalse(mockUtil.isMockitoMock(new Object() {}));
 }

 @Test
 public void testIsMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl(Object.class, spiedInstance, extraInterfaces));
 assertSame(filter, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetInterceptorNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetInterceptorNotAMock() {
 Object notAMock = new Object() {};
 assertNull(mockUtil.getInterceptor(notAMock));
 }

 @Test
 public void testCopyToMock() {
 Object mock = mockUtil.createMock(Serializable.class, new MockSettingsImpl(Serializable.class, spiedInstance, extraInterfaces));
 when(mockHandler.getMockName()).thenReturn(new MockName("MockName"));
 LenientCopyTool copyTool = mock(LenientCopyTool.class);
 mockUtil.copyToMock(spiedInstance, mock);
 verify(copyTool).copyToMock(argThat(isA(spiedInstance.getClass())), eq(mock));
 }
}