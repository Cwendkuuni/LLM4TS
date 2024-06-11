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
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockUtil = new MockUtil();
 }

 @Test
 public void testCreateMock_ValidScenario() {
 Class<MyClass> classToMock = MyClass.class;
 MyClass spiedInstance = new MyClass();
 MockSettingsImpl settings = new MockSettingsImpl();

 Object mock = mockUtil.createMock(classToMock, settings);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_ClassIsNull() {
 Class<MyClass> classToMock = null;
 MyClass spiedInstance = new MyClass();
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_ClassIsNotValid() {
 Class<String> classToMock = String.class;
 MyClass spiedInstance = new MyClass();
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_SpiedInstanceIsNull() {
 Class<MyClass> classToMock = MyClass.class;
 MyClass spiedInstance = null;
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_ClassIsAlreadyAMock() {
 Class<MyClass> classToMock = MyClass.class;
 MyClass spiedInstance = mock(MyClass.class);
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock_ValidScenario() {
 MyClass mock = mock(MyClass.class);
 mockUtil.resetMock(mock);

 MockHandlerInterface<MyClass> handler = mockUtil.getMockHandler(mock);

 verify(handler).reset();
 }

 @Test
 public void testResetMock_MockIsNull() {
 MyClass mock = null;
 mockUtil.resetMock(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMock_MockIsNotAMock() {
 Object mock = new Object();
 mockUtil.resetMock(mock);
 }

 @Test
 public void testGetMockHandler_ValidScenario() {
 MyClass mock = mock(MyClass.class);
 MockHandlerInterface<MyClass> handler = mockUtil.getMockHandler(mock);

 Assert.assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_MockIsNull() {
 MyClass mock = null;
 mockUtil.getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_MockIsNotAMock() {
 Object mock = new Object();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMock_ValidScenario() {
 MyClass mock = mock(MyClass.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_ObjectIsNotAMock() {
 Object mock = new Object();
 Assert.assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor_ValidScenario() {
 MyClass mock = mock(MyClass.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptor_MockIsNull() {
 MyClass mock = null;
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetInterceptor_MockIsNotAMock() {
 Object mock = new Object();
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetMockName_ValidScenario() {
 MyClass mock = mock(MyClass.class);
 String mockName = mockUtil.getMockName(mock);

 Assert.assertEquals("MockUtil created mock: MyClass", mockName);
 }

 @Test
 public void testGetMockName_MockIsNull() {
 MyClass mock = null;
 String mockName = mockUtil.getMockName(mock);

 Assert.assertNull(mockName);
 }

 @Test
 public void testCreateMock_MockFactoryWithCallback() {
 Class<MyClass> classToMock = MyClass.class;
 MyClass spiedInstance = new MyClass();
 MockSettingsImpl settings = new MockSettingsImpl();
 Factory mockFactory = createMockFactory(MyClass.class);

 Object mock = mockUtil.createMock(classToMock, settings, mockFactory);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());

 MethodInterceptorFilter interceptor = (MethodInterceptorFilter) Proxy.getInvocationHandler(mock);
 Assert.assertEquals(classToMock, interceptor.getTargetClass());
 }

 private Factory createMockFactory(Class<?> clazz) {
 Factory factory = Reflections.newProxyInstance(Factory.class, new Class[] { Factory.class }, (Proxy.InvocationHandler) (o, m, a) -> null);
 factory.setCallback(0, createMockInterceptor(clazz));
 return factory;
 }

 private MethodInterceptor createMockInterceptor(Class<?> clazz) {
 return new MethodInterceptor() {
 @Override
 public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
 throw new UnsupportedOperationException("MockInterceptor should not be invoked!");
 }
 };
 }

}