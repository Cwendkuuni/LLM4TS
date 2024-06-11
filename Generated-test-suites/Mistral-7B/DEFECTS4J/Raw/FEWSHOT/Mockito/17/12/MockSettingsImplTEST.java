package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettings mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = new MockSettingsImpl().extraInterfaces(new Object[0]);
 }

 @Test
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.extraInterfaces);
 mockSettings = mockSettings.extraInterfaces(Integer.TYPE);
 assertArrayEquals(new Class[]{Integer.TYPE}, mockSettings.getExtraInterfaces());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfacesNullParameter() {
 mockSettings = new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfacesNonInterfaceType() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.extraInterfaces);
 mockSettings = mockSettings.extraInterfaces(String.class);
 assertArrayEquals(new Class[0], mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = new MockSettingsImpl().extraInterfaces(String.class, Integer.TYPE);
 assertArrayEquals(new Class[]{String.class, Integer.TYPE}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testName() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.getName());
 mockSettings = mockSettings.name("TestMock");
 assertEquals("TestMock", mockSettings.getName());
 }

 @Test
 public void testSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.getSpiedInstance());
 mockSettings = mockSettings.spiedInstance(new Object());
 assertEquals(new Object(), mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.getDefaultAnswer());
 mockSettings = mockSettings.defaultAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });
 assertEquals(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 }, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl().extraInterfaces(java.io.Serializable.class);
 assertTrue(mockSettings.isSerializable());
 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 MockName mockName = null;
 try {
 mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.name("TestMock").spiedInstance(new Object());
 mockSettings.initiateMockName(String.class);
 mockName = mockSettings.getMockName();
 } finally {
 assertEquals("TestMock_0", mockName.getName());
 }
 }
}