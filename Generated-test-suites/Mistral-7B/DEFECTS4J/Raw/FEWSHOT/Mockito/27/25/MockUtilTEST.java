package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 creationValidator = mock(MockCreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "mock instance";

 mockUtil.createValidator().validateType(stringClass);
 mockUtil.createValidator().validateExtraInterfaces(stringClass, new Class[0]);
 mockUtil.createValidator().validateMockedType(stringClass, spiedInstance);

 String mock = mockUtil.create(stringClass, settings);
 assertNotNull(mock);
 assertEquals("mock instance", spiedInstance);

 verify(creationValidator).validateType(stringClass);
 verify(creationValidator).validateExtraInterfaces(eq(stringClass), isA(Class[].class));
 verifyNoMoreInteractions(creationValidator);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.create(null, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<AnotherInterface> anotherInterfaceClass = AnotherInterface.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "mock instance";

 settings.setExtraInterfaces(new Class[]{anotherInterfaceClass});

 mockUtil.createValidator().validateType(stringClass);
 mockUtil.createValidator().validateExtraInterfaces(stringClass, new Class[]{anotherInterfaceClass});
 mockUtil.createValidator().validateMockedType(stringClass, spiedInstance);

 String mock = mockUtil.create(stringClass, settings);
 assertNotNull(mock);

 verify(creationValidator).validateType(stringClass);
 verify(creationValidator).validateExtraInterfaces(eq(stringClass), arrayContaining(anotherInterfaceClass));
 verifyNoMoreInteractions(creationValidator);
 }

 @Test
 public void testCreateMockWithSerializableSettings() {
 Class<String> stringClass = String.class;
 Class<AnotherInterface> anotherInterfaceClass = AnotherInterface.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "mock instance";

 settings.setSerializable(true);
 settings.setExtraInterfaces(new Class[]{anotherInterfaceClass});

 mockUtil.createValidator().validateType(stringClass);
 mockUtil.createValidator().validateExtraInterfaces(stringClass, new Class[]{Serializable.class, anotherInterfaceClass});
 mockUtil.createValidator().validateMockedType(stringClass, spiedInstance);

 String mock = mockUtil.create(stringClass, settings);
 assertNotNull(mock);

 verify(creationValidator).validateType(stringClass);
 verify(creationValidator).validateExtraInterfaces(eq(stringClass), contains(Serializable.class, anotherInterfaceClass));
 verifyNoMoreInteractions(creationValidator);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(String.class);
 mockUtil.reset(mock);

 assertNotNull(mock);
 verify(mock, never()).toString();
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(String.class);
 MockHandlerInterface handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(String.class);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMock() {
 Object mock = mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Factory.class);
 Proxy proxy = (Proxy) mock;
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 assertEquals(proxy.getHandler(), interceptor.getHandler());
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(mockUtil.getMockName(null));
 }
}