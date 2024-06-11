package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.assertThat;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator creationValidatorMock;
 private MockHandler<Object> mockHandlerMock;
 private InvocationNotifierHandler<Object> invocationNotifierHandlerMock;
 private MethodInterceptorFilter methodInterceptorFilterMock;
 private Factory mockFactory;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil(creationValidatorMock = mock(MockCreationValidator.class));
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 Object spiedInstance = spy(new Object() {});
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, null, null, null, false);

 mockUtil.createMock(classToMock, settings);

 Object mock = (Object) Proxy.getInvocationHandler(classToMock.cast(spiedInstance));
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);

 given(creationValidatorMock.validateType(classToMock)).willReturn(true);
 given(creationValidatorMock.validateExtraInterfaces(classToMock, ArgumentMatchers.array())).willReturn(true);
 given(creationValidatorMock.validateMockedType(classToMock, spiedInstance)).willReturn(true);
 given(new LenientCopyTool().copyToMock(spiedInstance, mock)).will(willReturn(mock));

 then(methodInterceptorFilterMock).wasCalled();
 verifyNoInteractions(invocationNotifierHandlerMock);

 verify(mockHandler).handle(ArgumentMatchers.any());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidType() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "testString";
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, null, null, null, false);

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidClass() {
 Class<Object> classToMock = null;
 Object spiedInstance = mock(Object.class);
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, null, null, null, false);

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidSpiedInstance() {
 Class<Object> classToMock = Object.class;
 Object spiedInstance = new Object() {};
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, null, null, null, false);

 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<MyInterface> classToMock = MyInterface.class;
 Object spiedInstance = mock(MyOtherInterface.class);
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, new Class[] {MyInterface.class}, null, null, false);

 Object mock = mockUtil.createMock(classToMock, settings);

 verify(mock).myMethod();
 }

 @Test
 public void testCreateMockWithSerializableSetting() {
 Class<MySerializableClass> classToMock = MySerializableClass.class;
 Object spiedInstance = new MySerializableClass();
 MockSettingsImpl settings = new MockSettingsImpl("testMockName", null, null, null, true, null);

 Object mock = mockUtil.createMock(classToMock, settings);

 verify(mock).mySerializableMethod();
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 MockHandler<Object> oldMockHandler = mockHandlerMock = mock(MockHandler.class);
 methodInterceptorFilterMock = new MethodInterceptorFilter(invocationNotifierHandlerMock = mock(InvocationNotifierHandler.class), settings -> new MockSettingsImpl("resetMockName", null, null, null, null, false));
 given(mockUtil.newMethodInterceptorFilter(ArgumentMatchers.any())).willReturn(methodInterceptorFilterMock);
 given(mock.getClass()).willReturn(Mockito.mock(Factory.class));
 given(mockitoMockitoFactory -> mockitoMockitoFactory.getHandler(mockitoMockitoFactory.proxy(ArgumentMatchers.<Class<?>>any(), ArgumentMatchers.eq(oldMockHandler))).getHandler()).willReturn(mockHandlerMock);

 mockUtil.resetMock(mock);

 verify(mock).myMethod();
 verifyNoMoreInteractions(mockHandlerMock);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(Object.class);
 given(mockUtil.getInterceptor(mock)).willReturn(methodInterceptorFilterMock);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockFalse() {
 Object notAMock = new Object() {};
 assertFalse(mockUtil.isMockitoMock(notAMock));
 }

 @Test
 public void testGetMockName() {
 Object mock = mockitoMockitoFactory.proxy(Object.class, new MockSettingsImpl("testMockName", null, null, null, null, false));
 String mockName = mockUtil.getMockName(mock);
 assertEquals("testMockName", mockName);
 }
}

interface MyInterface {
 void myMethod();
}

class MySerializableClass implements Serializable {
 void mySerializableMethod();
}