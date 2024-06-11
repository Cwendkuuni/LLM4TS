package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;
 private Class<?> mockClass;
 private Object spiedInstance;
 private MockSettingsImpl mockSettings;
 private Factory mockFactory;

 @Before
 public void setUp() {
 mockClass = Mockito.mock(String.class);
 spiedInstance = "mock instance";
 mockSettings = new MockSettingsImpl();
 mockSettings.setSpiedInstance(spiedInstance);
 mockFactory = mock(Factory.class);
 when(mockFactory.getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));
 }

 @Test
 public void testCreateMock() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 T mock = mockUtil.createMock(mockClass, mockSettings);
 assertNotNull(mock);
 verify(mockFactory).setCallback(0, argThat(isInstanceOf(MethodInterceptorFilter.class)));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 mockUtil.createMock(null, mockSettings);
 fail("Should throw NotAMockException when classToMock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 mockUtil.createMock(int.class, mockSettings);
 fail("Should throw NotAMockException when classToMock is not a valid class.");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidSettings() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 mockSettings.setSpiedInstance(null);
 mockUtil.createMock(mockClass, mockSettings);
 fail("Should throw NotAMockException when spiedInstance is null.");
 }

 @Test
 public void testResetMock() {
 mockUtil = new MockUtil(creationValidator);
 T mock = mockUtil.createMock(mockClass, mockSettings);
 mockUtil.resetMock(mock);
 verify(mockFactory).setCallback(0, argThat(isA(MethodInterceptorFilter.class)));
 }

 @Test(expected = NullPointerException.class)
 public void testResetMockWithNullMock() {
 mockUtil = new MockUtil(creationValidator);
 mockUtil.resetMock(null);
 fail("Should throw NullPointerException when mock is null.");
 }

 @Test
 public void testGetMockHandler() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 T mock = mockUtil.createMock(mockClass, mockSettings);
 MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil = new MockUtil(creationValidator);
 mockUtil.getMockHandler(null);
 fail("Should throw NotAMockException when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithInvalidMock() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 Object invalidMock = new Object();
 mockUtil.getMockHandler(invalidMock);
 fail("Should throw NotAMockException when mock is not a Mockito mock.");
 }

 @Test
 public void testIsMock() {
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockitoMock() {
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testGetInterceptor() {
 T mock = mockUtil.createMock(mockClass, mockSettings);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 fail("Should throw NotAMockException when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithInvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getInterceptor(invalidMock);
 fail("Should throw NotAMockException when mock is not a Mockito mock.");
 }

 @Test
 public void testGetMockName() {
 MockUtil mockUtil = new MockUtil(creationValidator);
 T mock = mockUtil.createMock(mockClass, mockSettings);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockitoMock_" + mockClass.getName() + "_" + System.identityHashCode(mock), mockName.getName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 fail("Should throw NotAMockException when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithInvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getMockName(invalidMock);
 fail("Should throw NotAMockException when mock is not a Mockito mock.");
 }
}