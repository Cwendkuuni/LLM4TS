package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

 private MockUtil util;

 @Before
 public void setUp() {
 util = new MockUtil();
 }

 @Test
 public void testCreateMock() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String mock = util.createMock(classToMock, settings);
 assertNotNull(mock);
 assertEquals("Mock for ", getMockName(mock).getName());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 util.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Integer> classToMock = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 util.createMock(classToMock, settings);
 fail("Mock creation should have thrown an exception.");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterface() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.addExtraInterface(String[].class);
 util.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstance() {
 String spiedInstance = "spied instance";
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 util.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(null);
 util.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock() {
 String value = "value";
 String mock = Mockito.mock(String.class);
 util.resetMock(mock);
 verify(mock, never()).equals(value);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 util.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithInvalidMock() {
 Object invalidMock = new Object();
 util.resetMock(invalidMock);
 }

 @Test
 public void testGetMockHandler() {
 String mock = Mockito.mock(String.class);
 assertEquals(util.getMockHandler(mock), Mockito.mock(MockHandlerInterface.class));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 util.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithInvalidMock() {
 Object invalidMock = new Object();
 util.getMockHandler(invalidMock);
 }

 @Test
 public void testIsMock() {
 String mock = Mockito.mock(String.class);
 assertTrue(util.isMock(mock));
 }

 @Test
 public void testIsMockWithNullObject() {
 assertFalse(util.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = Mockito.spy(Object.class);
 assertFalse(util.isMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 String mock = Mockito.mock(String.class);
 assertEquals(util.getInterceptor(mock), mockget(MethodInterceptorFilter.class));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 util.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithInvalidMock() {
 Object invalidMock = new Object();
 util.getInterceptor(invalidMock);
 }

 @Test
 public void testGetMockName() {
 String mock = Mockito.mock(String.class);
 assertEquals("Mock for [class java.lang.String]", util.getMockName(mock).getName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 util.getMockName(null);
 }

 @Test
 public void testGetMockNameWithNullName() {
 String mock = Mockito.mock(String.class);
 MockName mockName = util.getMockName(mock);
 assertNotNull(mockName.getName());
 Mockito.doReturn("newName").when(mockName).getName();
 assertEquals("newName", util.getMockName(mock).getName());
 }
}