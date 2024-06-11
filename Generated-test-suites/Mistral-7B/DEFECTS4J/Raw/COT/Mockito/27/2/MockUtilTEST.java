package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.Mockito;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockUtil = new MockUtil(new MockCreationValidator());
 settings = new MockSettingsImpl();
 }

 @Test
 public void testCreateMock_ValidArguments() {
 // Test with a valid class and no extra interfaces
 MockUtil.createMock(String.class, settings);

 // Test with a valid class and extra interfaces
 settings.setExtraInterfaces(new Class[]{Serializable.class});
 MockUtil.createMock(String.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClass() {
 MockUtil.createMock(Integer.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidExtraInterfaces() {
 settings.setExtraInterfaces(new Class[]{Object.class});
 MockUtil.createMock(String.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidSpiedInstance() {
 settings.setSpiedInstance(new Object());
 MockUtil.createMock(String.class, settings);
 }

 @Test
 public void testResetMock() throws IllegalAccessException, InvocationTargetException {
 Object mock = mock(String.class);
 Object resetMock = mockUtil.resetMock(mock);
 Assert.assertNotSame(mock, resetMock);
 }

 @Test
 public void testGetMockHandler_ValidMock() {
 Object mock = mock(String.class);
 MockHandlerInterface handler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_InvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getMockHandler(invalidMock);
 }

 @Test
 public void testIsMock_ValidMock() {
 Object mock = mock(String.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_InvalidObject() {
 Assert.assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testIsMockitoMock_ValidMock() {
 Object mock = mock(String.class);
 Assert.assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMock_InvalidObject() {
 Assert.assertFalse(mockUtil.isMockitoMock(new Object()));
 }

 @Test
 public void testGetInterceptor_ValidMock() {
 Object mock = mock(String.class);
 MethodInterceptorFilter interceptor = (MethodInterceptorFilter) mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptor_InvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getInterceptor(invalidMock);
 }

 @Test
 public void testGetMockName_ValidMock() {
 Object mock = mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test
 public void testCreateMock_NamedMock() {
 settings.setMockName("myMock");
 MockUtil.createMock(String.class, settings);
 }

 @Test
 public void testCreateMock_NullName() {
 settings.setMockName(null);
 MockUtil.createMock(String.class, settings);
 }

 @Test
 public void testCreateMock_SerializableMock() {
 settings.setSerializable(true);
 MockUtil.createMock(String.class, settings);
 }

 @Test
 public void testCreateMock_NonSerializableMock() {
 settings.setSerializable(false);
 MockUtil.createMock(String.class, settings);
 }

 @Test
 public void testCreateMock_Spy() {
 Object spiedInstance = spy(String.class);
 MockUtil.createMock(String.class, settings);
 MockHandlerInterface handler = mockUtil.getMockHandler(spiedInstance);
 Assert.assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_SpyWithExtraInterfaces() {
 Object spiedInstance = spy(String.class);
 settings.setExtraInterfaces(new Class[]{Serializable.class});
 MockUtil.createMock(String.class, settings);
 }

 @Test(expected = NotAMockitoRuntimeException.class)
 public void testCreateMock_MockitoRuntimeException() {
 ClassImposterizer.INSTANCE.imposterise(null, String.class, null);
 }

 @Test(expected = RuntimeException.class)
 public void testCreateMock_RuntimeException() {
 ClassImposterizer.INSTANCE.imposterise(new RuntimeException("Test Exception"), String.class, null);
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMock_InstantiationException() {
 Class<?> clazz = Class.forName("java.lang.StringCantBeInstanciatedException");
 ClassImposterizer.INSTANCE.imposterise(new InstantiationException("Cannot instantiate: " + clazz.getName()), String.class, null);
 }

 @Test(expected = IllegalAccessException.class)
 public void testCreateMock_IllegalAccessException() {
 Class<?> clazz = Mockito.class;
 ClassImposterizer.INSTANCE.imposterise(new IllegalAccessException("Cannot access class: " + clazz.getName()), String.class, null);
 }

 @Test(expected = InvocationTargetException.class)
 public void testCreateMock_InvocationTargetException() {
 Class<?> clazz = Mockito.class;
 ClassImposterizer.INSTANCE.imposterise(new InvocationTargetException(new RuntimeException("Test Exception")), String.class, null);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateMock_ClassNotFoundException() {
 Class<?> clazz = Class.forName("java.lang.StringDoesNotExistException");
 ClassImposterizer.INSTANCE.imposterise(new ClassNotFoundException("Cannot find class: " + clazz.getName()), String.class, null);
 }

 @Test
 public void testCreateMock_NullClass() {
 Class<?> nullClass = null;
 Assert.assertNull(mockUtil.createMock(nullClass, settings));
 }

 @Test
 public void testCreateMock_NullSettings() {
 MockSettingsImpl nullSettings = null;
 Assert.assertNull(mockUtil.createMock(String.class, nullSettings));
 }

 @Test
 public void testCreateMock_NullClassAndSettings() {
 Assert.assertNull(mockUtil.createMock(null, null));
 }
}