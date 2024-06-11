package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 @Test
 public void testSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 MockSettings serializableMockSettings = mockSettings.serializable();
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testSerializableWithEmptyArray() {
 MockSettings mockSettings = new MockSettingsImpl();
 MockSettings serializableMockSettings = mockSettings.serializable();
 serializableMockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = Reporter.class)
 public void testSerializableWithNullClass() {
 MockSettings mockSettings = new MockSettingsImpl();
 MockSettings serializableMockSettings = mockSettings.serializable();
 serializableMockSettings.extraInterfaces(null);
 }

 @Test
 public void testExtraInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[]{java.io.Serializable.class, java.util.List.class};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, extraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesWithNullInterface() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[]{null};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(interfaces);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesWithNonInterfaceClass() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[]{String.class};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(interfaces);
 }

 @Test
 public void testGetMockName() {
 MockSettings mockSettings = new MockSettingsImpl();
 String name = "MockName";
 MockSettings nameMockSettings = mockSettings.name(name);
 MockName mockName = nameMockSettings.getMockName();
 assertEquals(name, mockName.getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[]{java.io.Serializable.class, java.util.List.class};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, extraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 MockSettings mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 MockSettings mockSettings = new MockSettingsImpl();
 String name = "MockName";
 MockSettings nameMockSettings = mockSettings.name(name);
 assertEquals(name, nameMockSettings.getName());
 }

 @Test
 public void testDefaultAnswer() {
 MockSettings mockSettings = new MockSettingsImpl();
 Answer<Object> defaultAnswer = org.mockito.stubbing.AnswerUtils.returnValues(1, 2, 3);
 MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, defaultAnswerMockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] interfaces = new Class[]{java.io.Serializable.class};
 MockSettings serializableMockSettings = mockSettings.extraInterfaces(interfaces);
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?> classToMock = String.class;
 MockSettings initiateMockNameMockSettings = mockSettings.initiateMockName(classToMock);
 assertNotNull(initiateMockNameMockSettings.getMockName());
 }

}