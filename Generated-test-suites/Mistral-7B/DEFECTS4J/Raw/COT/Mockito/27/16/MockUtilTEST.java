package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.exceptions.misusing.NotAMockException;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator creationValidator = new MockCreationValidator();

 @Test
 public void testCreateMock_ValidArguments() {
 mockUtil = new MockUtil(creationValidator);
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = "test";
 T mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClassToMock() {
 mockUtil = new MockUtil(creationValidator);
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClassToMock() {
 mockUtil = new MockUtil(creationValidator);
 Class<?> invalidClass = Object.class;
 mockUtil.createMock(invalidClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSettings() {
 mockUtil = new MockUtil(creationValidator);
 mockUtil.createMock(String.class, null);
 }

 @Test
 public void testCreateMock_ExtraInterfaces() {
 mockUtil = new MockUtil(creationValidator);
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class};
 settings.setExtraInterfaces(extraInterfaces);
 Object spiedInstance = "test";
 T mock = mockUtil.createMock(classToMock, settings);
 Assert.assertArrayEquals(new Class<?>[]{Serializable.class}, mock.getClass().getInterfaces());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_SpiedInstanceIsNull() {
 mockUtil = new MockUtil(creationValidator);
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = null;
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock_ValidArguments() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mock(String.class);
 mockUtil.resetMock(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMock_NullMock() {
 mockUtil = new MockUtil(creationValidator);
 mockUtil.resetMock(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockHandler_NullMock() {
 mockUtil = new MockUtil(creationValidator);
 Object mock = null;
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testGetMockHandler_MockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mock(String.class);
 Assert.assertNotNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NotAMockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 Object notAMockitoMock = new Object();
 mockUtil.getMockHandler(notAMockitoMock);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsMock_NullMock() {
 mockUtil = new MockUtil(creationValidator);
 Object mock = null;
 mockUtil.isMock(mock);
 }

 @Test
 public void testIsMock_MockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mock(String.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInterceptor_NullMock() {
 mockUtil = new MockUtil(creationValidator);
 Object mock = null;
 mockUtil.getInterceptor(mock);
 }

 @Test
 public void testGetInterceptor_MockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mock(String.class);
 Object interceptor = Proxy.getInvocationHandler(mock);
 Object handler = mockUtil.getInterceptor(mock).getHandler();
 Assert.assertSame(interceptor, handler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInterceptor_NotAMockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 Object notAMockitoMock = spy(Object.class);
 mockUtil.getInterceptor(notAMockitoMock);
 }

 @Test
 public void testGetMockName_MockitoMock() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMockName_NullMock() {
 mockUtil = new MockUtil(creationValidator);
 Object mock = null;
 mockUtil.getMockName(mock);
 }

 @Test
 public void testCreateMock_WithSettings() {
 mockUtil = new MockUtil(creationValidator);
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS).build();
 Object spiedInstance = "test";
 T mock = mockUtil.createMock(classToMock, settings);
 }

}