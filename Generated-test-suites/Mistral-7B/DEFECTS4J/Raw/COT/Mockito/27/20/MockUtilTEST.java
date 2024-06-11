package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockitoMock;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.MethodInterceptorHandler;
import org.mockito.internal.util.reflection.MethodInterceptorInvocation;
import org.mockito.internal.util.reflection.MethodInterceptorWithArguments;
import org.mockito.internal.util.reflection.MockHandlerInterface;
import org.mockito.internal.util.reflection.MockHandler;
import org.mockito.internal.util.reflection.MockName;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.internal.util.reflection.ClassUtils;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.exceptions.misusing.NotAMockException;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.RETURNS_DEFAULTS;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 creationValidator = new MockCreationValidator();
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock() throws Exception {
 // Test creating a mock with no extra interfaces and no spied instance
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock, mock.getClass());

 // Test creating a mock with extra interfaces
 Class<?>[] extraInterfaces = new Class[] {Serializable.class};
 settings.setExtraInterfaces(extraInterfaces);
 T mockWithExtraInterfaces = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mockWithExtraInterfaces);
 Assert.assertEquals(classToMock, mockWithExtraInterfaces.getClass());
 Assert.assertTrue(Arrays.asList(mockWithExtraInterfaces.getClass().getInterfaces()).contains(Serializable.class));

 // Test creating a mock with a spied instance
 Object spiedInstance = new Object();
 settings.setSpiedInstance(spiedInstance);
 T mockWithSpiedInstance = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mockWithSpiedInstance);
 Assert.assertEquals(classToMock, mockWithSpiedInstance.getClass());
 LenientCopyTool.copyToMock(spiedInstance, mockWithSpiedInstance);
 Assert.assertEquals(spiedInstance.hashCode(), mockWithSpiedInstance.hashCode());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() throws Exception {
 Class<?> nullClass = null;
 mockUtil.createMock(nullClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClassToMock() throws Exception {
 Class<?> invalidClass = new Object() {};
 mockUtil.createMock(invalidClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidExtraInterfaces() throws Exception {
 Class<?>[] invalidExtraInterfaces = new Class[] {"InvalidClass"};
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(invalidExtraInterfaces);
 mockUtil.createMock(String.class, settings);
 }

 @Test
 public void testResetMock() throws Exception {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 MethodInterceptorHandler handler = (MethodInterceptorHandler) mockUtil.getInterceptor(mock);
 MethodInterceptorInvocation invocation = new MethodInterceptorInvocation(handler, "testMethod", new Object[0]);
 MethodInterceptorWithArguments arguments = handler.intercept(invocation);
 String result = (String) arguments.proceed();
 Assert.assertNull(result);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() throws Exception {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMockObject() throws Exception {
 Object nonMockObject = new Object();
 mockUtil.resetMock(nonMockObject);
 }

 @Test
 public void testIsMock() throws Exception {
 Object mock = mock(String.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 Object nonMockObject = new Object();
 Assert.assertFalse(mockUtil.isMock(nonMockObject));
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockWithNullObject() throws Exception {
 mockUtil.isMock(null);
 }

 @Test
 public void testGetInterceptor() throws Exception {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MethodInterceptorHandler handler = (MethodInterceptorHandler) mockUtil.getInterceptor(mock);
 Assert.assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() throws Exception {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockObject() throws Exception {
 Object nonMockObject = new Object();
 mockUtil.getInterceptor(nonMockObject);
 }

 @Test
 public void testGetMockName() throws Exception {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 Assert.assertNotNull(mockName.getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() throws Exception {
 mockUtil.getMockName(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockObject() throws Exception {
 Object nonMockObject = new Object();
 mockUtil.getMockName(nonMockObject);
 }
}