package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.exceptions.misusing.NotAMockException;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator mockCreationValidator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockCreationValidator = new MockCreationValidator();
 mockUtil = new MockUtil(mockCreationValidator);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object mock = mockUtil.createMock(stringClass, settings);
 Assert.assertNotNull(mock);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 Class<?> nullClass = null;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(nullClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Integer> integerClass = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(String.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSettings() {
 Class<String> stringClass = String.class;
 MockSettingsImpl nullSettings = null;
 mockUtil.createMock(stringClass, nullSettings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(stringClass, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class};
 Object mock = mockUtil.createMock(stringClass, settings, extraInterfaces);
 Assert.assertNotNull(mock);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithUnsupportedExtraInterface() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Number.class};
 mockUtil.createMock(stringClass, settings, extraInterfaces);
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 Object nullMock = null;
 mockUtil.resetMock(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMock() {
 Object nonMock = new Object();
 mockUtil.resetMock(nonMock);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 Object nullMock = null;
 mockUtil.getMockHandler(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getMockHandler(nonMock);
 }

 @Test
 public void testIsMock() {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNonMock() {
 Object nonMock = new Object();
 Assert.assertFalse(mockUtil.isMock(nonMock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = (MethodInterceptorFilter) Proxy.getInvocationHandler(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 Object nullMock = null;
 mockUtil.getInterceptor(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getInterceptor(nonMock);
 }

 @Test
 public void testGetMockName() {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 Object nullMock = null;
 mockUtil.getMockName(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getMockName(nonMock);
 }

 @Test
 public void testValidateType() {
 mockUtil.createMock(String.class, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testValidateTypeWithInvalidType() {
 mockUtil.createMock(int.class, new MockSettingsImpl());
 }

 @Test
 public void testValidateExtraInterfaces() {
 mockUtil.createMock(String.class, new MockSettingsImpl(), new Class[0]);
 }

 @Test
 public void testValidateExtraInterfacesWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class};
 mockUtil.createMock(stringClass, settings, extraInterfaces);
 }

 @Test(expected = NotAMockException.class)
 public void testValidateExtraInterfacesWithInvalidExtraInterface() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Number.class};
 mockUtil.createMock(stringClass, settings, extraInterfaces);
 }

 @Test
 public void testValidateMockedType() {
 Class<String> stringClass = String.class;
 Object spiedInstance = new Object();
 ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new MockHandler<String>(new MockHandlerInterface<String>() {}), new MockSettingsImpl()), stringClass, new Class[0]);
 mockUtil.createMock(stringClass, new MockSettingsImpl(), spiedInstance);
 }

 @Test(expected = NotAMockException.class)
 public void testValidateMockedTypeWithNullSpiedInstance() {
 Class<String> stringClass = String.class;
 mockUtil.createMock(stringClass, new MockSettingsImpl(), null);
 }

 @Test(expected = NotAMockException.class)
 public void testValidateMockedTypeWithNonMockType() {
 Class<String> stringClass = String.class;
 Object spiedInstance = new Integer(1);
 mockUtil.createMock(stringClass, new MockSettingsImpl(), spiedInstance);
 }

 @Test
 public void testInitiateMockName() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.initiateMockName(stringClass);
 MockName mockName = settings.getMockName();
 Assert.assertEquals("MockitoMock_<String>", mockName.getName());
 }

}