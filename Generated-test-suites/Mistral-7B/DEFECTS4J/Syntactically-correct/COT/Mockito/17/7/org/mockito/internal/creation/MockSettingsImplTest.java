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

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces();
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0], null);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 Class<?> notAnInterface = new Object() {};
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(notAnInterface);
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("MockName");
 Assert.assertEquals("MockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] {Serializable.class, String.class};
 mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = MockitoTest.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 Assert.assertNotNull(mockSettings.getMockName());
 }
}