package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.core.Callback;
import org.mockito.internal.util.MockName;

public class MockUtilTest {

 MockUtil mockUtil = new MockUtil();

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;

 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance("Test Spied Instance");
 settings.setExtraInterfaces(new Class<?>[] { stubInterface() });

 MockHandler<String> mockHandler = mockUtil.createMock(stringClass, settings);
 String mock = (String) mockHandler.proxy();

 assertEquals("Test Spied Instance", mock);
 verify(mock).toString();

 Class<?>[] ancillaryTypes = settings.getExtraInterfaces();
 assertArrayEquals(new Class<?>[] { stubInterface() }, ancillaryTypes);

 String returnedValue = mock.toString();
 assertEquals("Test Spied Instance", returnedValue);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> stringClass = String.class;

 try {
 mockUtil.createMock(stringClass, null);
 fail("Expected NotAMockException to be thrown");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMockWithInvalidType() {
 Class<?> invalidTypeClass = Integer.TYPE;

 try {
 mockUtil.createMock(invalidTypeClass, new MockSettingsImpl());
 fail("Expected CreationException to be thrown");
 } catch (CreationException e) {
 // Expected
 }
 }

 @Test
 public void testResetMock() {
 String mockString = mock(String.class);
 mockUtil.resetMock(mockString);

 verify(mockString, never()).toString();
 }

 @Test
 public void testGetMockHandler() {
 String mockString = mock(String.class);

 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mockString);
 assertNotNull(mockHandler);
 }

 @Test
 public void testIsMockitoMock() {
 String mockString = mock(String.class);
 assertTrue(mockUtil.isMockitoMock(mockString));
 }

 interface stubInterface {
 void doSomething();
 }

 @Test
 public void testIsMock() {
 String mockString = mock(String.class);
 assertTrue(mockUtil.isMock(mockString));
 }

 @Test
 public void testGetInterceptor() {
 String mockString = mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mockString);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockName() {
 String mockString = mock(String.class);
 MockName mockName = mockUtil.getMockName(mockString);
 assertNotNull(mockName);
 assertEquals("MockUtil_1", mockName.getMockName());
 }

 @Test
 public void testCreateMockWithDefaultSettings() {
 Class<String> stringClass = String.class;

 MockHandler<String> mockHandler = mockUtil.createMock(stringClass, new MockSettingsImpl());
 String mock = (String) mockHandler.proxy();

 assertEquals("", mock);
 verify(mock).toString();
 }
}