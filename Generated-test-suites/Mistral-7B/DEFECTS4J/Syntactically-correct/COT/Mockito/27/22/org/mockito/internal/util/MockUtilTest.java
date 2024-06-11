package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.exceptions.misusing.NotAMockException;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {

 private MockCreationValidator creationValidator = new MockCreationValidator();
 private MockUtil mockUtil = new MockUtil(creationValidator);

 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new ArrayList<>());
 settings.setSpiedInstance(null);

 Class<?> mockClass = mock(String.class).getClass();
 Object mock = mockUtil.createMock(mockClass, settings);

 Assert.assertNotNull(mock);
 Assert.assertEquals(Mockito.class, mock.getClass().getInterfaces()[0]);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 Object mock = mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<?> mockClass = Object.class; // This class cannot be mocked with Mockito
 Object mock = mockUtil.createMock(mockClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidSpiedInstance() {
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl().setSpiedInstance(spiedInstance));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<?> mockClass = String.class;
 Class<?> extraInterface = Serializable.class;

 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new ArrayList<>()).add(extraInterface);
 settings.setSpiedInstance(null);

 Object mock = mockUtil.createMock(mockClass, settings);

 Assert.assertNotNull(mock);
 Assert.assertArrayEquals(new Class<?>[]{Mockito.class, extraInterface}, mock.getClass().getInterfaces());
 }

 @Test
 public void testResetMock() {
 String mock = mock(String.class);
 Object originalMockHandler = mockUtil.getMockHandler(mock);
 mockUtil.resetMock(mock);
 Object newMockHandler = mockUtil.getMockHandler(mock);

 Assert.assertNotSame(originalMockHandler, newMockHandler);
 }

 @Test
 public void testGetMockName() {
 String mock = mock(String.class);
 String expectedMockName = "mockString";

 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
 MockSettingsImpl settings = (MockSettingsImpl) mockHandler.getMockSettings();

 settings.setMockName(expectedMockName);

 String actualMockName = mockUtil.getMockName(mock);

 Assert.assertEquals(expectedMockName, actualMockName);
 }
}