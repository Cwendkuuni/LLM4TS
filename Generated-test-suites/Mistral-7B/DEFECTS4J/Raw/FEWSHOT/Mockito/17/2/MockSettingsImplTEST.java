package org.mockito.internal.creation;

import static org.junit.Assert.*;
import static org.mockito.exceptions.base.MockitoException.message;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 MockSettings mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testSerializable_NullExtraInterfaces() {
 new MockSettingsImpl().serializable(null);
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testSerializable_EmptyExtraInterfaces() {
 new MockSettingsImpl().serializable();
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();
 }

 @Test
 public void testSerializable_ValidExtraInterfaces() {
 Class<?>[] extraInterfaces = { java.io.Serializable.class };
 MockSettings mockSettings = new MockSettingsImpl().serializable(extraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testSerializable_NonInterfaceExtraInterface() {
 Class<?>[] extraInterfaces = { String.class };
 new MockSettingsImpl().serializable(extraInterfaces);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] extraInterfaces = { java.io.Serializable.class, String.class };
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testExtraInterfacesNull() {
 new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testExtraInterfacesEmpty() {
 new MockSettingsImpl().extraInterfaces();
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testExtraInterfacesNullInterface() {
 new MockSettingsImpl().extraInterfaces(null, java.io.Serializable.class);
 new Reporter().extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test(expected = org.mockito.exceptions.Reporter.class)
 public void testExtraInterfacesNonInterface() {
 new MockSettingsImpl().extraInterfaces(String.class);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
 }

 @Test
 public void testName() {
 mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.name("MockName");
 assertEquals("MockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mock -> "Answer";
 mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }
}