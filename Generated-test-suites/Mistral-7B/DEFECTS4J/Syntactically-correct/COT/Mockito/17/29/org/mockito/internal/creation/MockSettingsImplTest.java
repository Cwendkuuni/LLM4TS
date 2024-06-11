package org.mockito.internal.creation.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 Assert.assertNotNull(mockSettings.getExtraInterfaces());
 Assert.assertTrue(Arrays.asList(mockSettings.getExtraInterfaces()).contains(Serializable.class));
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testEmptyExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = Reporter.class)
 public void testNonInterfaceExtraInterface() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class, Integer.class};
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertNotNull(mockSettings.getExtraInterfaces());
 Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("testMockName");
 Assert.assertNotNull(mockSettings.getMockName());
 Assert.assertEquals("testMockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 Assert.assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> defaultAnswer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 };
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(defaultAnswer);
 Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = String.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.name("testMockName");
 mockSettings.initiateMockName(classToMock);
 Assert.assertNotNull(mockSettings.getMockName());
 Assert.assertEquals("testMockName", mockSettings.getMockName().getName());
 Assert.assertEquals(classToMock, mockSettings.getMockName().getMockType());
 }
}