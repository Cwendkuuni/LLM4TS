package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 mockHandler = new MockHandler<Object>(settings);
 }

 @Test
 public void testCreateMock() {
 Object mock = mockUtil.createMock(String.class, settings);
 assertNotNull(mock);
 verify(mockHandler).handle(Mockito.eq(Mockito.isA(String.class)), Mockito.eq(settings));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 settings.setExtraInterfaces(new Class[] { Integer.class });
 Object mock = mockUtil.createMock(String.class, settings);
 assertNotNull(mock);
 verify(mockHandler).handle(Mockito.eq(Mockito.isA(String.class)), Mockito.eq(settings));

 Mockito.verify(mock).answer(Mockito.eq("answer"));
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 String spiedInstance = "spiedInstance";
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(String.class, settings);
 assertNotNull(mock);
 verify(mockHandler).handle(Mockito.eq(Mockito.isA(String.class)), Mockito.eq(settings));

 Mockito.verify(mock).answer(Mockito.eq(spiedInstance));
 }

 @Test
 public void testResetMock() {
 mock = mockUtil.createMock(String.class, settings);
 mockUtil.resetMock(mock);
 MethodInterceptor interceptor = (MethodInterceptor) Proxy.getProxyClass(mock.getClass()).getInterfaces()[0]
 .getDeclaredMethod("interceptCalls").getAnnotation(Proxy.HidesMethods.class).getValue()[0]
 .getDeclaredMethod("getInterceptor").invoke(null);
 mockHandler = (MockHandler<Object>) interceptor.getHandler();

 String answer = "answer";
 mockHandler.answer(answer);
 verify(mock).answer(Mockito.eq(answer));

 mockUtil.resetMock(mock);
 verify(mock, Mockito.never()).answer(Mockito.any());
 }

 @Test
 public void testGetMockHandler() {
 mock = mockUtil.createMock(String.class, settings);
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockitoMockNullMock() {
 mockUtil.isMockitoMock(null);
 }

 @Test
 public void testIsMockitoMockObject() {
 mock = mockUtil.createMock(String.class, settings);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockitoMockNonMockitoMock() {
 Object nonMockitoMock = new Object();
 assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 mock = mockUtil.createMock(String.class, settings);
 MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
 assertNull(filter);
 }

 @Test
 public void testGetInterceptorMockHandler() {
 mock = mockUtil.createMock(String.class, settings);
 MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
 MethodInterceptor interceptor = (MethodInterceptor) Proxy.getProxyClass(mock.getClass())
 .getInterfaces()[0]
 .getDeclaredMethod("interceptCalls").getAnnotation(Proxy.HidesMethods.class).getValue()[0]
 .getDeclaredMethod("getInterceptor").invoke(null);
 assertEquals(interceptor, filter.getInterceptor());
 }

 @Test
 public void testGetMockName() {
 mock = mockUtil.createMock(String.class, settings);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("mock", mockName.getName());
 }

 @Test
 public void testGetMockNameCustomName() {
 settings.setMockName("customName");
 mock = mockUtil.createMock(String.class, settings);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals("customName", mockName.getName());
 }
}