package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockUtilTest {

 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockUtil = new MockUtil();
 }

 @Test
 public void testCreateMock_ValidArgs() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl();

 String mock = (String) mockUtil.createMock(classToMock, settings);

 // Basic functionality check
 Assert.assertNotNull(mock);
 Assert.assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClassToMock() {
 Class<String> classToMock = null;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClassToMock() {
 Class<String> classToMock = int.class;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSpiedInstance() {
 Class<String> classToMock = String.class;
 Object spiedInstance = null;
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_Serializable() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSerializable(true);

 String mock = (String) mockUtil.createMock(classToMock, settings);

 // Basic functionality check
 Assert.assertNotNull(mock);
 Assert.assertNotNull(mockUtil.getInterceptor(mock));

 // Serializable check
 Assert.assertTrue(mock instanceof Serializable);
 }

 @Test
 public void testResetMock() {
 String mock = mock(String.class);
 mockUtil.resetMock(mock);

 // Verify no calls were made
 verifyZeroInteractions(mock);
 }

 @Test
 public void testResetMock_WithInterceptor() {
 String mock = mock(String.class);
 MockHandler<String> mockHandler = new MockHandler<String>(null);
 MethodInterceptorFilter interceptorFilter = new MethodInterceptorFilter(new InvocationNotifierHandler<>(mockHandler, null), null);
 ((Factory) mock).setCallback(0, interceptorFilter);
 mockUtil.resetMock(mock);

 // Verify no calls were made
 verifyZeroInteractions(mock);

 // Verify interceptor was replaced
 MockHandler<String> newMockHandler = new MockHandler<String>(mockHandler);
 MethodInterceptorFilter newInterceptorFilter = new MethodInterceptorFilter(new InvocationNotifierHandler<>(newMockHandler, null), null);
 ((Factory) mock).setCallback(0, newInterceptorFilter);
 verify(mockHandler).reset();
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NullMock() {
 Object mock = null;

 mockUtil.getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NonMockitoMock() {
 Object mock = spy(String.class);
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMock_Null() {
 Object mock = null;

 Assert.assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NonMockitoMock() {
 Object mock = new Object() {};

 Assert.assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_MockitoMock() {
 Object mock = mock(String.class);

 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor_NullMock() {
 Object mock = null;

 mockUtil.getInterceptor(mock);
 }

 @Test
 public void testGetInterceptor_NonMockitoMock() {
 Object mock = spy(String.class);

 mockUtil.getInterceptor(mock);
 }

 @Test
 public void testGetInterceptor_MockitoMock() {
 Object mock = mock(String.class);
 Callback callback = Proxy.getInvocationHandler(mock);

 MethodInterceptorFilter interceptorFilter = (MethodInterceptorFilter) mockUtil.getInterceptor(mock);

 Assert.assertSame(callback, interceptorFilter.getHandler());
 }

 @Test
 public void testGetMockName_NullMock() {
 Object mock = null;

 Assert.assertNull(mockUtil.getMockName(mock));
 }

 @Test
 public void testGetMockName_MockitoMock() {
 Object mock = mock(String.class);
 MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);

 Assert.assertEquals(mockHandler.getMockName(), mockUtil.getMockName(mock));
 }
}