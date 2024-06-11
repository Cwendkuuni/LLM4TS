import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodInterceptorFilter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;

public class MockUtilTest {

 private CreationValidator creationValidator = new CreationValidator();
 private MockUtil mockUtil = new MockUtil(creationValidator);

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl(stringClass, null, null, null, null);
 String mock = mockUtil.createMock(stringClass, settings);
 Assert.assertNotNull(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullClassToMock() {
 Class<?> nullClass = null;
 MockSettingsImpl settings = new MockSettingsImpl(nullClass, null, null, null, null);
 mockUtil.createMock(nullClass, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullSettings() {
 MockSettingsImpl nullSettings = null;
 mockUtil.createMock(String.class, nullSettings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidClassToMock() {
 Class<?> invalidClass = new Object() {}.getClass();
 MockSettingsImpl settings = new MockSettingsImpl(invalidClass, null, null, null, null);
 mockUtil.createMock(invalidClass, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithExtraInterfacesNotArray() {
 Class<?> extraInterface = String.class;
 MockSettingsImpl settings = new MockSettingsImpl(String.class, new Class<?>[] { extraInterface }, null, null, null);
 mockUtil.createMock(String.class, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithSpiedInstanceNull() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl(stringClass, null, null, null, null);
 mockUtil.createMock(stringClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidInstance() {
 Object invalidInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl(String.class, null, null, null, null);
 mockUtil.createMock(invalidInstance.getClass(), settings);
 }

 @Test
 public void testResetMock() {
 String mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter filter = Mockito.isA(MethodInterceptorFilter.class);
 Assert.assertTrue(Mockito.verify(mock, Mockito.atLeastOnce()).getCallback(0, filter));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithInvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getMockHandler(invalidMock);
 }

 @Test
 public void testIsMockWithMock() {
 String mock = Mockito.mock(String.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNonMock() {
 Object nonMock = new Object();
 Assert.assertFalse(mockUtil.isMock(nonMock));
 }

 @Test
 public void testGetInterceptorWithMock() {
 String mock = Mockito.mock(String.class);
 MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNonMock() {
 Object nonMock = new Object();
 MethodInterceptor interceptor = mockUtil.getInterceptor(nonMock);
 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetMockNameWithMock() {
 String mock = Mockito.mock(String.class);
 MockName mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }
}