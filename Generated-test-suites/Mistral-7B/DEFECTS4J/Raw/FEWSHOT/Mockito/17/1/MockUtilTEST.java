package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodInterceptorFilter;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MockName;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private Object mock;
 private Class<?> classToMock;
 private Class<?>[] extraInterfaces;
 private MockSettingsImpl settings;

 @Before
 public void setup() {
 creationValidator = Mockito.spy(new CreationValidator());
 mockUtil = new MockUtil(creationValidator);
 mockHandler = Mockito.mock(MockHandler.class);
 classToMock = Mockito.mock(String.class);
 extraInterfaces = new Class[]{Integer.class};
 settings = Mockito.mock(MockSettingsImpl.class);
 Whitebox.setInternalState(settings, "extraInterfaces", extraInterfaces);
 Whitebox.setInternalState(settings, "spiedInstance", classToMock);
 }

 @Test
 public void testCreateMock() {
 Object mock = mockUtil.createMock(String.class, settings);
 assertNotNull(mock);
 assertFalse(isMockitoMock(mock));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Object mock = mockUtil.createMock(String.class, settings);
 Object mockWithExtraInterfaces = Proxy.newProxyInstance(
 ClassUtil.class.getClassLoader(),
 new Class[]{String.class, Integer.class},
 new MockInterceptor(mockHandler));
 Whitebox.setInternalState(mockWithExtraInterfaces, "handler", mockHandler);
 Mockito.when(mockUtil.createMock(String.class, settings)).thenReturn(mockWithExtraInterfaces);

 Object returnedMock = mockUtil.createMock(String.class, settings);
 assertNotNull(returnedMock);
 assertFalse(isMockitoMock(returnedMock));
 assertTrue(mockUtil.isMock(returnedMock));
 Mockito.verify(mockHandler).handle(Mockito.any(), Mockito.eq(String.class), Mockito.eq(settings));
 }

 @Test
 public void testCreateMockWithNullClass() {
 try {
 mockUtil.createMock(null, settings);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCreateMockWithNullSettings() {
 try {
 mockUtil.createMock(String.class, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testResetMock() {
 mockUtil.createMock(String.class, settings);
 mockUtil.resetMock(mock);
 Mockito.verify(mockHandler).reset();
 }

 @Test
 public void testResetMockWithNullMock() {
 mockUtil.createMock(String.class, settings);
 try {
 mockUtil.resetMock(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testGetMockHandler() {
 mockUtil.createMock(String.class, settings);
 Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test
 public void testGetMockHandlerWithNullMock() {
 mockUtil.createMock(String.class, settings);
 try {
 mockUtil.getMockHandler(null);
 } catch (NotAMockException e) {
 // expected
 }
 }

 @Test
 public void testIsMockitoMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 assertFalse(mockUtil.isMockitoMock(new Object()));
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 try {
 mockUtil.isMockitoMock(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testIsMock() {
 assertFalse(mockUtil.isMock(null));
 assertFalse(mockUtil.isMock(new Object()));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 mockUtil.createMock(String.class, settings);
 MethodInterceptorFilter methodInterceptorFilter = new MethodInterceptorFilter(mockHandler, settings);
 Whitebox.setInternalState(mock, "handler", methodInterceptorFilter);
 Whitebox.setInternalState(methodInterceptorFilter, "interceptor", mockHandler);
 Mockito.when(mockUtil.getInterceptor(mock)).thenReturn(methodInterceptorFilter.getInterceptor());
 assertEquals(mockHandler, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 mockUtil.createMock(String.class, settings);
 try {
 mockUtil.getInterceptor(null);
 } catch (NotAMockException e) {
 // expected
 }
 }

 @Test
 public void testGetMockName() {
 mockUtil.createMock(String.class, settings);
 Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
 Mockito.when(mockHandler.getMockSettings()).thenReturn(settings);
 Mockito.when(settings.getMockName()).thenReturn("mockName");
 assertEquals("mockName", mockUtil.getMockName(mock));
 }

 @Test
 public void testGetMockNameWithNullMock() {
 mockUtil.createMock(String.class, settings);
 try {
 mockUtil.getMockName(null);
 } catch (NotAMockException e) {
 // expected
 }
 }

 private static class MockInterceptor implements MethodInterceptor {
 public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
 return new Answer2<Object, InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return null;
 }

 @Override
 public Object answer(InvocationOnMock invocation, Object returnValue) {
 return returnValue;
 }
 }.answer(new InvocationOnMock(obj, method, args));
 }
 }
}