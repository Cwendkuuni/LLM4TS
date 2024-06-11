package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.MockCreationValidator;
import org.mockito.internal.util.reflection.MethodInterceptorFilter.MockVerificationMode;
import org.mockito.internal.util.reflection.MockVerifier;
import org.mockito.internal.util.reflection.MethodInterceptorFilter.MockCreationMode;
import org.mockito.internal.util.reflection.MethodInterceptorFilter.MockCreationPolicy;
import org.mockito.internal.util.reflection.MethodInterceptorFilter.MockCreationShallowness;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private ClassImposterizer classImposterizer;
 private LenientCopyTool lenientCopyTool;

 @Before
 public void setUp() {
 creationValidator = new MockCreationValidator();
 mockUtil = new MockUtil(creationValidator);
 settings = new MockSettingsImpl();
 classImposterizer = ClassImposterizer.INSTANCE;
 lenientCopyTool = new LenientCopyTool();
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "mock instance";
 MockHandler<String> mockHandler = mock(String.class);
 MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
 Object mock = mockUtil.createMock(classToMock, settings);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock, mock.getClass());
 Assert.assertNotNull(mockHandler);
 verify(mockHandler).handle(any(InvocationOnMock.class));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.createMock(null, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidClassToMock() {
 Class<Integer> intClass = Integer.class;
 mockUtil.createMock(intClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(String.class, null);
 }

 @Test
 public void testCreateMockWithSerializableFlagSet() {
 Class<Serializable> serializableClass = Serializable.class;
 Object spiedInstance = new Object() {};
 Object mock = mockUtil.createMock(serializableClass, settings);
 Assert.assertNotNull(mock);
 Assert.assertTrue(Arrays.asList(mock.getClass().getInterfaces()).contains(Serializable.class));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<Serializable> serializableInterface = Serializable.class;
 Class<Printable> printableInterface = Printable.class;
 MockSettingsImpl settingsWithExtraInterfaces = new MockSettingsImpl();
 settingsWithExtraInterfaces.setExtraInterfaces(new Class<?>[]{serializableInterface, printableInterface});

 Object spiedInstance = "mock instance";
 Object mock = mockUtil.createMock(classToMock, settingsWithExtraInterfaces);
 
 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock, mock.getClass());
 Assert.assertTrue(Arrays.asList(mock.getClass().getInterfaces()).containsAll(Arrays.asList(serializableInterface, printableInterface)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<Integer> intClass = Integer.class;
 MockSettingsImpl settingsWithInvalidExtraInterface = new MockSettingsImpl();
 settingsWithInvalidExtraInterface.setExtraInterfaces(new Class<?>[]{intClass});
 
 mockUtil.createMock(classToMock, settingsWithInvalidExtraInterface);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullMockedType() {
 mockUtil.createMock(null, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNonMockInstance() {
 Object nonMockInstance = new Object() {};
 mockUtil.createMock(Object.class, settings);
 }

 @Test
 public void testResetMock() {
 String mockString = mock(String.class);
 mockUtil.resetMock(mockString);
 verifyNoMoreInteractions(mockString);
 }

 @Test(expected = NullPointerException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test
 public void testIsMock() {
 Object mockInstance = mock(Object.class);
 Assert.assertTrue(mockUtil.isMock(mockInstance));
 }

 @Test
 public void testIsMockWithNullMock() {
 Assert.assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 Object mockInstance = mock(Object.class);
 MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mockInstance);
 Assert.assertNotNull(interceptorFilter);
 Assert.assertNull(interceptorFilter.getHandler());
 }

 @Test
 public void testGetInterceptorWithMockHandler() {
 Factory mockFactory = mock(Factory.class);
 Callback mockCallback = mock(Callback.class);
 when(mockFactory.getCallback(0)).thenReturn(mockCallback);
 Object mockInstance = mockFactory;
 MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mockInstance);
 Assert.assertNotNull(interceptorFilter);
 Assert.assertNotNull(interceptorFilter.getHandler());
 Assert.assertEquals(mockHandler, interceptorFilter.getHandler());
 }

 @Test
 public void testGetMockName() {
 String mockName = "mockName";
 Factory mockFactory = mock(Factory.class);
 when(mockFactory.getMockName()).thenReturn(mockName);
 Object mockInstance = mockFactory;
 String actualMockName = mockUtil.getMockName(mockInstance);
 Assert.assertEquals(mockName, actualMockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test
 public void testValidateType() {
 try {
 mockUtil.validateType(Object.class);
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }
 
 try {
 mockUtil.validateType(String[].class);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Argument type 'class [Ljava.lang.String;' is a multi-dimensional array type and cannot be mocked.", e.getMessage());
 }
 
 try {
 mockUtil.validateType(int[].class);
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }
}

 @Test
 public void testValidateExtraInterfaces() {
 try {
 mockUtil.validateExtraInterfaces(Object.class, new Class[0]);
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateExtraInterfaces(String.class, new Class<?>[] {Object.class});
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateExtraInterfaces(String.class, new Class<?>[] {String[].class});
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Argument 'extraInterfaces' contains a class of type 'class [Ljava.lang.String;'' which is a multi-dimensional array type and cannot be mocked.", e.getMessage());
 }
}

 @Test
 public void testValidateMockedType() {
 try {
 mockUtil.validateMockedType(Object.class, null);
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, new String[]{"mockString"});
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, new Object() {});
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, mock(String.class));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, mockFactory(Factory.class).proxy(String.class));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, mockImposterizedMock(String.class, classImposterizer));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, new ClassMocker().createMock(String.class));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, new JMockMock("mockString"));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, lenientCopyTool.getSerializableCopy(mock(Serializable.class)));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception to be thrown");
 }

 try {
 mockUtil.validateMockedType(String.class, new Object[]{new Object[]{}});
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Argument 'mockedType' is an array type, and arrays cannot be mocked.", e.getMessage());
 }
}

 @Test
 public void testHandle() {
 Object mock = mock(Object.class);
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings);
 MethodInterceptor interceptor = mock(MethodInterceptor.class);
 when(filter.getHandler()).thenReturn(interceptor);
 MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
 verify(mockHandler, times(0)).handle(any(InvocationOnMock.class));
 
 mockUtil.resetMock(mock);
 mockUtil.resetMock(mock);
 
 when(interceptor.invoke(any(InvocationOnMock.class), any(Object[].class))).thenCallRealMethod();
 mockUtil.handle(mock, filter);
 
 verify(mockHandler).handle(any(InvocationOnMock.class));
 }

 @Test
 public void testHandleWithNullMock() {
 mockUtil.handle(null, mock(MethodInterceptorFilter.class));
 }

 @Test
 public void testHandleWithNullMethodInterceptorFilter() {
 mockUtil.handle(mock(Object.class), null);
 }

 @Test
 public void testHandleWithInvocationTargetException() throws InvocationTargetException, IllegalAccessException {
 Object mock = mock(Object.class);
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings);
 MethodInterceptor interceptor = mock(MethodInterceptor.class);
 when(filter.getHandler()).thenReturn(interceptor);
 doThrow(new InvocationTargetException(new RuntimeException("test exception"), null)).when(interceptor).invoke(any(InvocationOnMock.class), any(Object[].class));
 MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
 
 mockUtil.handle(mock, filter);
 
 verifyZeroInteractions(mockHandler);
 }

 @Test
 public void testHandleWithMockCreationMode() throws Exception {
 Object mock = mockFactory(Object.class).proxy();
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings.withCreationMode(MockCreationMode.STRICT));
 mockUtil.handle(mock, filter);
 verify(mock).toString();
 }

 @Test
 public void testHandleWithMockCreationPolicy() throws Exception {
 Object mock = mockFactory(Object.class).proxy();
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings.withCreationPolicy(MockCreationPolicy.DEEP));
 mockUtil.handle(mock, filter);
 verify(mock, times(1)).toString();
 verify(mock, times(1)).hashCode();
 }

 @Test
 public void testHandleWithMockCreationShallowness() throws Exception {
 Object mock = mockFactory(Object.class).proxy();
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings.withCreationShallowness(MockCreationShallowness.PROXY_BASE));
 mockUtil.handle(mock, filter);
 verify(mock).toString();
 verifyZeroInteractions(mock.getClass().getSuperclass());
 }

 @Test
 public void testHandleWithMockVerificationMode() throws Exception {
 Object mock = mockFactory(Object.class).proxy();
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings.withVerificationMode(MockVerificationMode.VERIFY_NEVER));
 mockUtil.handle(mock, filter);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testHandleWithMockCreationModeAndVerificationMode() throws Exception {
 Object mock = mockFactory(Object.class).proxy();
 MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings.withCreationMode(MockCreationMode.STRICT).withVerificationMode(MockVerificationMode.VERIFY_NEVER));
 mockUtil.handle(mock, filter);
 verifyZeroInteractions(mock);
 }
}