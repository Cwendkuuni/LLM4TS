package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.internal.util.reflection.AnnotationUtils;
import org.mockito.internal.util.reflection.AnnotatedElementUtils;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.misusing.NotAMockException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Arrays;

import static org.mockito.internal.util.reflection.ClassUtils.getAllInterfaces;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doCallRealMethod;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator mockCreationValidator;

 @Before
 public void init() {
 mockCreationValidator = mock(MockCreationValidator.class);
 mockUtil = new MockUtil(mockCreationValidator);
 }

 @Test
 public void testCreateMock_ValidArguments() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertNotSame(spiedInstance, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClassToMock() throws Exception {
 Class<?> classToMock = null;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClassToMock() throws Exception {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_ValidSettings() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(classToMock, settings);
 assert mock != null;
 assert mock != spiedInstance;
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSpiedInstance() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = null;
 mockUtil.createMock(classToMock, settings, spiedInstance);
 }

 @Test
 public void testCreateMock_Serializable() throws Exception {
 Class<Serializable> classToMock = Serializable.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertNotSame(spiedInstance, mock);
 }

 @Test
 public void testCreateMock_ExtraInterfaces() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[] {Serializable.class, Comparable.class});
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertNotSame(spiedInstance, mock);
 Assert.assertTrue(mock instanceof Serializable);
 Assert.assertTrue(mock instanceof Comparable);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullExtraInterfaces() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(null);
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_MockedType() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(classToMock, settings, spiedInstance);
 Assert.assertNotNull(mock);
 Assert.assertNotSame(spiedInstance, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSpiedInstance() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = null;
 mockUtil.createMock(classToMock, settings, spiedInstance);
 }

 @Test
 public void testCreateMock_InitiateMockName() throws Exception {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.initiateMockName(classToMock);
 String expected = "MockObject_" + classToMock.getName().hashCode();
 String actual = settings.getMockName();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testResetMock() throws Exception {
 Object mock = mock(Object.class);
 mockUtil.resetMock(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testResetMock_NullMock() {
 mockUtil.resetMock(null);
 }

 @Test
 public void testGetMockHandler_ValidMock() throws Exception {
 Object mock = mock(Object.class);
 Object handler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_InvalidMock() throws Exception {
 Object mock = new Object();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMockitoMock_ValidMock() throws Exception {
 Object mock = mock(Object.class);
 boolean result = mockUtil.isMockitoMock(mock);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsMockitoMock_NullMock() {
 boolean result = mockUtil.isMockitoMock(null);
 Assert.assertFalse(result);
 }

 @Test
 public void testIsMockitoMock_NotAMockitoMock() throws Exception {
 Object mock = new Object();
 boolean result = mockUtil.isMockitoMock(mock);
 Assert.assertFalse(result);
 }

 @Test
 public void testGetInterceptor_ValidMock() throws Exception {
 Object mock = mock(Object.class);
 Object interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptor_NullMock() {
 Object interceptor = mockUtil.getInterceptor(null);
 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetInterceptor_NotAMockitoMock() throws Exception {
 Object mock = new Object();
 Object interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetMockName_ValidMock() throws Exception {
 Object mock = mock(Object.class);
 String result = mockUtil.getMockName(mock);
 Assert.assertNotNull(result);
 }

 @Test
 public void testGetMockName_NullMock() {
 String result = mockUtil.getMockName(null);
 Assert.assertNull(result);
 }

 @Test
 public void testGetMockName_NoNameSet() throws Exception {
 Object mock = mock(Object.class);
 doThrow(new RuntimeException()).when(mockUtil).getMockName(mock);
 String result = mockUtil.getMockName(mock);
 Assert.assertNull(result);
 }
}