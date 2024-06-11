package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.Runners;

import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 }

 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;
 Class<MockSettingsImpl> mockSettingsImplClass = MockSettingsImpl.class;

 MockSettingsImpl settings = mock(mockSettingsImplClass);
 when(settings.getSpiedInstance()).thenReturn("test");
 when(settings.getExtraInterfaces()).thenReturn(new Class[0]);

 String mockedString = mockUtil.createMock(stringClass, settings);
 verify(settings).validateType(eq(stringClass));
 verify(settings).validateExtraInterfaces(eq(stringClass), anyArray());
 verify(settings).validateMockedType(eq(stringClass), any());

 assertNotNull(mockedString);
 assertThat(mockedString, isInstanceOf(String.class));

 LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);
 settings.initiateMockName(stringClass);
 doReturn(lenientCopyTool).when(settings).getLenientCopyTool();

 String mockedStringWithCopyTool = mockUtil.createMock(stringClass, settings);
 assertSame(mockedString, mockedStringWithCopyTool);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Integer> intClass = Integer.class;
 mockUtil.createMock(intClass, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<MockSettingsImpl> mockSettingsImplClass = MockSettingsImpl.class;
 Class<MyInterface> myInterfaceClass = MyInterface.class;

 MockSettingsImpl settings = mock(mockSettingsImplClass);
 when(settings.getSpiedInstance()).thenReturn("test");
 when(settings.getExtraInterfaces()).thenReturn(new Class[]{myInterfaceClass});

 String mockedString = mockUtil.createMock(stringClass, settings);
 verify(settings).validateType(eq(stringClass));
 verify(settings).validateExtraInterfaces(eq(stringClass), arrayContaining(myInterfaceClass));

 assertNotNull(mockedString);
 assertThat(mockedString, isInstanceOf(String.class));

 MyInterface mockedMyInterface = (MyInterface) proxy(MyInterface.class);
 when(mockedMyInterface.myMethod()).thenReturn("mocked");

 verify(mockedString).myMethod();
 }

 @Test
 public void testResetMock() {
 String mockedString = mockUtil.createMock(String.class, new MockSettingsImpl());

 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mockedString);
 MethodInterceptorFilter newFilter = new MethodInterceptorFilter(mockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));
 MethodInterceptor oldFilter = mockHandler.getInterceptor();

 MethodInterceptor oldInterceptor = (MethodInterceptor) Proxy.getInvocationHandler(mockedString);
 assertSame(oldInterceptor, oldFilter);

 mockUtil.resetMock(mockedString);
 MethodInterceptor newInterceptor = (MethodInterceptor) Proxy.getInvocationHandler(mockedString);
 assertNotSame(oldInterceptor, newInterceptor);
 assertSame(newFilter, newInterceptor.getHandler());
 }

 @Test
 public void testGetMockHandler() {
 String mockedString = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertNotNull(mockUtil.getMockHandler(mockedString));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNull() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 String testString = new String("test");
 assertFalse(mockUtil.isMockitoMock(testString));
 try {
 mockUtil.getMockHandler(testString);
 fail();
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testIsMock() {
 String mockedString = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mockedString));
 String testString = new String("test");
 assertFalse(mockUtil.isMock(testString));
 }

 private <T> T proxy(Class<T> clazz) {
 return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, (InvocationHandler) noop());
 }

 private static InvocationHandler noop() {
 return invocation -> {};
 }
}