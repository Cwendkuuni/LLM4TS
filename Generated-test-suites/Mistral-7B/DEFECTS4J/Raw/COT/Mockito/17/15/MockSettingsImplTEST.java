package org.mockito.internal.creation.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesNull() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesEmptyArray() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfaceNull() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[1];
 interfaces[0] = null;
 mockSettings.extraInterfaces(interfaces);
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfaceNotAnInterface() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[1];
 interfaces[0] = new Object() {};
 mockSettings.extraInterfaces(interfaces);
 }

 @Test
 public void testGetName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("TestName");
 Assert.assertEquals("TestName", mockSettings.getMockName().getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[2];
 interfaces[0] = String.class;
 interfaces[1] = Integer.class;
 mockSettings.extraInterfaces(interfaces);
 Assert.assertArrayEquals(new Class[]{String.class, Integer.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 Object instance = new Object();
 mockSettings.spiedInstance(instance);
 Assert.assertEquals(instance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testgetDefaultAnswer() {
 mockSettings = new MockSettingsImpl();
 Answer<Object> answer = arg -> arg.hashCode();
 mockSettings.defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefaultAnswerNull() {
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(null);
 }

 @Test
 public void testInitiateMockName() {
 mockSettings = new MockSettingsImpl();
 Class<?> classToMock = MockitoTest.class;
 mockSettings.initiateMockName(classToMock);
 Assert.assertNotNull(mockSettings.getMockName());
 }
}