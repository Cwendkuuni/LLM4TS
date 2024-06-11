package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
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
 public void testSerializableNullExtraInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testSerializableEmptyExtraInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = Reporter.class)
 public void testSerializableNonInterfaceExtraInterface() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testGetExtraInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {String.class, Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test(expected = Reporter.class)
 public void testNameNullName() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.name(null);
 }

 @Test
 public void testGetName() {
 MockSettings mockSettings = new MockSettingsImpl();
 String name = "mockName";
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test(expected = Reporter.class)
 public void testSpiedInstanceNullObject() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.spiedInstance(null);
 }

 @Test
 public void testGetSpiedInstance() {
 MockSettings mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test(expected = Reporter.class)
 public void testDefaultAnswerNullAnswer() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(null);
 }

 @Test
 public void testGetDefaultAnswer() {
 MockSettings mockSettings = new MockSettingsImpl();
 Answer<Object> defaultAnswer = org.mockito.stubbing.AnswerUtils.returnValues(1, 2, 3);
 mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {String.class, Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 assertTrue(mockSettings.isSerializable());

 MockSettings mockSettings2 = new MockSettingsImpl();
 Class<?>[] extraInterfaces2 = {String.class};
 mockSettings2.extraInterfaces(extraInterfaces2);
 assertFalse(mockSettings2.isSerializable());
 }

 @Test
 public void testInitiateMockNameNullClassToMock() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(null);
 }
}