import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptorHandler;
import org.mockito.internal.util.reflection.MethodInterceptorNotifier;
import org.mockito.internal.util.reflection.MethodInterceptorProxy;
import org.mockito.internal.util.reflection.ProxyFactory;
import org.mockito.internal.util.reflection.ProxyUtils;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockUtilTest {

 private MockCreationValidator mockCreationValidator;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 mockCreationValidator = MockCreationValidator.instance();
 mockUtil = new MockUtil(mockCreationValidator);
 }

 @Test
 public void testCreateMock_ValidArguments() {
 Class<String> mockClass = String.class;
 Object spiedInstance = "mock instance";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 String mock = (String) mockUtil.createMock(mockClass, settings);

 assertTrue("Mock should not be null", mock != null);
 assertNotSame("Mock should not be the same instance as the spied one", mock, spiedInstance);
 assertEquals("Mock should have the same name as the spied instance", spiedInstance.getClass().getSimpleName(), Whitebox.getInternalState(mockUtil.getMockHandler(mock).getMockSettings()).getMockName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_NullClass() {
 Class<Object> nullClass = null;
 MockSettingsImpl settings = new MockSettingsImpl();

 try {
 mockUtil.createMock(nullClass, settings);
 } catch (IllegalArgumentException e) {
 assertEquals("Class argument cannot be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_NullSetting() {
 MockSettingsImpl nullSettings = null;

 try {
 mockUtil.createMock(String.class, nullSettings);
 } catch (IllegalArgumentException e) {
 assertEquals("Settings should not be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_ClassNotSupported() {
 Class<Integer> intClass = Integer.class;

 try {
 mockUtil.createMock(intClass, new MockSettingsImpl());
 } catch (IllegalArgumentException e) {
 assertEquals("Class " + intClass.getName() + " is not supported by Mockito", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_ExtraInterfaces_Null() {
 Class<?>[] extraInterfaces = null;
 Class<String> mockClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();

 try {
 mockUtil.createMock(mockClass, settings, extraInterfaces);
 } catch (IllegalArgumentException e) {
 assertEquals("Extra interfaces should not be null", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_ExtraInterfaces_InvalidType() {
 Class<String> mockClass = String.class;
 Class<Integer> intInterface = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {intInterface};

 try {
 mockUtil.createMock(mockClass, settings, extraInterfaces);
 } catch (IllegalArgumentException e) {
 assertEquals("Interface " + intInterface.getName() + " is not compatible with the mock type " + mockClass.getName(), e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_InvalidSpiedInstance() {
 Class<String> mockClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(null);

 try {
 mockUtil.createMock(mockClass, settings);
 } catch (IllegalArgumentException e) {
 assertEquals("Argument spiedInstance should not be null", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testResetMock() {
 String mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 assertSame("Mock should have the same instance after reset", mock, Mockito.mock(String.class));
 }

 @Test(expected = NullPointerException.class)
 public void testResetMock_NullMock() {
 String nullMock = null;
 mockUtil.resetMock(nullMock);
 }

 @Test
 public void testGetMockHandler_Mock() {
 String mock = Mockito.mock(String.class);
 MethodInterceptor interceptor = Whitebox.getInternalState(mockUtil.getInterceptor(mock));
 MethodInterceptorHandler handler = (MethodInterceptorHandler) Whitebox.getInternalState(interceptor.getHandler());
 MockHandlerInterface mockHandler = (MockHandlerInterface) handler.getMockHandler();
 assertEquals("Mock handlers should be equal", mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NotAMock() {
 Object notAMock = new Object();
 mockUtil.getMockHandler(notAMock);
 }

 @Test
 public void testIsMock_Mock() {
 String mock = Mockito.mock(String.class);
 assertTrue("Mock should be recognized as a mock", mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NotAMock() {
 Object notAMock = new Object();
 assertFalse("Not a mock should not be recognized as a mock", mockUtil.isMock(notAMock));
 }

 @Test
 public void testGetInterceptor_Mock() {
 String mock = Mockito.mock(String.class);
 MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
 assertNotNull("Interceptor should not be null", interceptor);
 }

 @Test
 public void testGetInterceptor_NotAMock() {
 Object notAMock = new Object();
 MethodInterceptor interceptor = mockUtil.getInterceptor(notAMock);
 assertNull("Interceptor should be null for not a mock", interceptor);
 }

 @Test
 public void testGetMockName_Mock() {
 String mock = Mockito.mock(String.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals("Mock name should be set to the class name", mock.getClass().getSimpleName(), mockName.getName());
 }

 @Test
 public void testGetMockName_NullMock() {
 Object nullMock = null;
 MockName mockName = mockUtil.getMockName(nullMock);
 assertNull("Mock name should be null for null mock", mockName);
 }
}