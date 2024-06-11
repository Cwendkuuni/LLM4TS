package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.InvocationNotifierHandler;
import org.mockito.internal.util.MethodInterceptorFilter;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.internal.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.jmock.ClassImposterizer.ProxyClass;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;

 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 }

 // Test for createMock method
 @Test
 public <T> void testCreateMock() {
 Class<T> classToMock = Mockito.mock(String.class);
 Proxy mock = mockUtil.createMock(classToMock, new MockSettingsImpl());
 assertTrue(isMockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClassToMock() {
 Class<Object> invalidClassToMock = Object.class;
 mockUtil.createMock(invalidClassToMock, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidSpiedInstance() {
 Object invalidSpiedInstance = new Object();
 mockUtil.createMock(Mockito.mock(String.class), new MockSettingsImpl().setSpiedInstance(invalidSpiedInstance));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = Mockito.mock(String.class);
 Class[] extraInterfaces = new Class[]{Serializable.class};
 mockUtil.createMock(classToMock, new MockSettingsImpl().setExtraInterfaces(extraInterfaces));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSerializableFalseAndExtraInterfaces() {
 Class<String> classToMock = Mockito.mock(String.class);
 mockUtil.createMock(classToMock, new MockSettingsImpl().setExtraInterfaces(new Class[0]).setSerializable(false));
 }

 // Test for resetMock method
 @Test
 public <T> void testResetMock() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNotNull(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMock() {
 Object nonMock = new Object();
 mockUtil.resetMock(nonMock);
 }

 // Test for getMockHandler method
 @Test
 public <T> void testGetMockHandler() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockHandler<T> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getMockHandler(nonMock);
 }

 // Test for isMockitoMock method
 @Test
 public <T> void testIsMockitoMock() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithMockitoProxy() {
 Proxy mock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithJMockProxy() {
 ClassImposterizer.ProxyClass mock = ClassImposterizer.createProxy("test.MockitoMockTest$1", new Class[0], new Class[]{}, new Class[0], new Class[0]);
 assertFalse(mockUtil.isMockitoMock(mock));
 }

 // Test for isMock method
 @Test
 public boolean testIsMock() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public boolean testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public boolean testIsMockWithNonMock() {
 Object nonMock = new Object();
 assertFalse(mockUtil.isMock(nonMock));
 }

 // Test for getInterceptor method
 @Test
 public <T> void testGetInterceptor() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getInterceptor(nonMock);
 }

 // Test for getMockName method
 @Test
 public <T> void testGetMockName() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMock() {
 Object nonMock = new Object();
 mockUtil.getMockName(nonMock);
 }
}