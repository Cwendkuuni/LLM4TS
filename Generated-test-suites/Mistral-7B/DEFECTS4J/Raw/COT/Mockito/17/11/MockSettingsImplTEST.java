package org.mockito.internal.creation.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl();
 MockSettings serializableMockSettings = mockSettings.serializable();
 // Basic test for serializable method
 Assert.assertInstanceOf(MockSettings.class, serializableMockSettings);
 // Edge case test for serializable method with null argument
 try {
 mockSettings = new MockSettingsImpl();
 MockSettings nullArgumentSerializableMockSettings = mockSettings.serializable(null);
 fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface exception");
 } catch (AssertionError e) {
 Reporter reporter = (Reporter) e.getCause();
 Assert.assertEquals("extraInterfaces requires at least one interface", reporter.getMessage());
 }
 // Test exception handling for serializable method with non-interface argument
 try {
 Class<String> nonInterfaceClass = String.class;
 mockSettings = new MockSettingsImpl();
 MockSettings nonInterfaceArgumentSerializableMockSettings = mockSettings.serializable(nonInterfaceClass);
 fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces exception");
 } catch (AssertionError e) {
 Reporter reporter = (Reporter) e.getCause();
 Assert.assertEquals("extraInterfaces accepts only interfaces", reporter.getMessage());
 }
 }

 @Test
 public void testExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] emptyExtraInterfaces = new Class[0];
 MockSettings emptyExtraInterfacesMockSettings = mockSettings.extraInterfaces(emptyExtraInterfaces);
 // Basic test for extraInterfaces method with empty array
 Assert.assertSame(emptyExtraInterfaces, emptyExtraInterfacesMockSettings.getExtraInterfaces());
 // Test exception handling for extraInterfaces method with null argument
 try {
 mockSettings = new MockSettingsImpl();
 Class<?>[] nullArgumentExtraInterfaces = null;
 MockSettings nullArgumentExtraInterfacesMockSettings = mockSettings.extraInterfaces(nullArgumentExtraInterfaces);
 fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface exception");
 } catch (AssertionError e) {
 Reporter reporter = (Reporter) e.getCause();
 Assert.assertEquals("extraInterfaces requires at least one interface", reporter.getMessage());
 }
 // Test exception handling for extraInterfaces method with non-interface argument
 try {
 Class<String> nonInterfaceClass = String.class;
 mockSettings = new MockSettingsImpl();
 Class<?>[] nonInterfaceArgumentExtraInterfaces = {nonInterfaceClass};
 MockSettings nonInterfaceArgumentExtraInterfacesMockSettings = mockSettings.extraInterfaces(nonInterfaceArgumentExtraInterfaces);
 fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces exception");
 } catch (AssertionError e) {
 Reporter reporter = (Reporter) e.getCause();
 Assert.assertEquals("extraInterfaces accepts only interfaces", reporter.getMessage());
 }
 // Test edge case for extraInterfaces method with multiple interfaces
 mockSettings = new MockSettingsImpl();
 Class<?>[] multipleInterfacesExtraInterfaces = {Serializable.class, Serializable.class};
 MockSettings multipleInterfacesExtraInterfacesMockSettings = mockSettings.extraInterfaces(multipleInterfacesExtraInterfaces);
 // Basic test for extraInterfaces method with multiple interfaces
 Assert.assertArrayEquals(multipleInterfacesExtraInterfaces, multipleInterfacesExtraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl();
 String name = "testMockName";
 MockSettings nameMockSettings = mockSettings.name(name);
 MockName mockName = nameMockSettings.getMockName();
 // Basic test for getMockName method
 Assert.assertEquals(name, mockName.getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] emptyExtraInterfaces = new Class[0];
 MockSettings emptyExtraInterfacesMockSettings = mockSettings.extraInterfaces(emptyExtraInterfaces);
 // Basic test for getExtraInterfaces method with empty array
 Assert.assertArrayEquals(emptyExtraInterfaces, emptyExtraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
 // Basic test for getSpiedInstance method
 Assert.assertSame(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl();
 Class<?> serializableExtraInterface = Serializable.class;
 Class<?>[] serializableExtraInterfaces = {serializableExtraInterface};
 MockSettings serializableExtraInterfacesMockSettings = mockSettings.extraInterfaces(serializableExtraInterfaces);
 // Basic test for isSerializable method
 Assert.assertTrue(serializableExtraInterfacesMockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 mockSettings = new MockSettingsImpl();
 Class<String> classToMock = String.class;
 MockSettings initiateMockNameMockSettings = mockSettings.initiateMockName(classToMock);
 MockName initiateMockNameMockName = initiateMockNameMockSettings.getMockName();
 // Basic test for initiateMockName method
 Assert.assertNotNull(initiateMockNameMockName);
 }
}