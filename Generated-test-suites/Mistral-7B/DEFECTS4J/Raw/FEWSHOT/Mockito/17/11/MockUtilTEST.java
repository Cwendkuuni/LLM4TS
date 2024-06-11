package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

 private MockUtil mockUtil;

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 verify(mock, times(1)).mockObject();
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil = new MockUtil();
 Class<?> nullClass = null;
 mockUtil.createMock(nullClass, new MockSettingsImpl());
 fail("Should throw exception when classToMock is null.");
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidClassToMock() {
 mockUtil = new MockUtil();
 Class<Integer> intClass = Integer.class;
 mockUtil.createMock(intClass, new MockSettingsImpl());
 fail("Should throw exception when classToMock is not supported.");
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<PrintWriter> printWriterClass = PrintWriter.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{printWriterClass});
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 verify(mock, times(1)).mockObject();
 verify(mock, times(1)).mock(eq(PrintWriter.class));
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<PrintWriter> printWriterClass = PrintWriter.class;
 Class<Long> longClass = Long.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{printWriterClass, longClass});
 mockUtil.createMock(stringClass, settings);
 fail("Should throw exception when extraInterfaces contains unsupported types.");
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<String> stringClass = String.class;
 String spiedInstance = "mock instance";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 verify(mock, times(1)).mockObject();
 verify(mock, times(1)).mock(eq("mock instance"));
 }

 @Test
 public void testCreateMockWithEmptySpiedInstance() {
 Class<String> stringClass = String.class;
 String spiedInstance = "";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 verify(mock, times(1)).mockObject();
 }

 @Test
 public void testCreateMockWithNoSpiedInstance() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 verify(mock, times(1)).mockObject();
 }

 @Test
 public void testResetMock() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 verify(mock, times(0)).mockObject();
 }

 @Test
 public void testGetMockHandler() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(String.class);
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals("MockHandler for String", mockHandler.getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 fail("Should throw exception when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getMockHandler(nonMockitoMock);
 fail("Should throw exception when mock is not a Mockito mock.");
 }

 @Test
 public void testIsMock() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 assertFalse(mockUtil.isMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 fail("Should throw exception when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getInterceptor(nonMockitoMock);
 fail("Should throw exception when mock is not a Mockito mock.");
 }

 @Test
 public void testGetMockName() {
 Class<String> stringClass = String.class;
 String mock = Mockito.mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("Mock name for String", mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 fail("Should throw exception when mock is null.");
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getMockName(nonMockitoMock);
 fail("Should throw exception when mock is not a Mockito mock.");
 }
}