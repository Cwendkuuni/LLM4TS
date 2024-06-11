package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.base.MockitoException.classNotFound;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 MockSettings serializableMockSettings = mockSettings.serializable();
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testSerializableNullExtraInterfaces() {
 mockSettings.serializable(null);
 }

 @Test(expected = Reporter.class)
 public void testSerializableEmptyExtraInterfaces() {
 mockSettings.serializable();
 }

 @Test
 public void testSerializableWithSerializableInterface() {
 Class<?>[] serializableExtraInterfaces = new Class[]{java.io.Serializable.class};
 MockSettings serializableMockSettings = mockSettings.extraInterfaces(serializableExtraInterfaces);
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testSerializableWithNonInterfaceClass() {
 Class<?> nonInterfaceClass = Object.class;
 mockSettings.extraInterfaces(nonInterfaceClass);
 }

 @Test
 public void testGetExtraInterfaces() {
 extraInterfaces = new Class[]{java.io.Serializable.class, String.class};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetExtraInterfacesEmptyArray() {
 mockSettings = mockSettings.extraInterfaces();
 assertArrayEquals(new Class[0], mockSettings.getExtraInterfaces());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name("MyMock");
 assertEquals("MyMock", mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> defaultAnswer = mock(Answer.class);
 mockSettings = mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testDefaultAnswerNull() {
 mockSettings = mockSettings.defaultAnswer(null);
 assertNull(mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.extraInterfaces(java.io.Serializable.class);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testIsSerializableFalse() {
 mockSettings = mockSettings.extraInterfaces();
 assertFalse(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNullClassToMock() {
 mockSettings.initiateMockName(null);
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNonInterfaceClassToMock() {
 Class<?> nonInterfaceClass = Object.class;
 mockSettings = mockSettings.initiateMockName(nonInterfaceClass);
 }

 @Test
 public void testInitiateMockName() {
 String name = "MyMock";
 Class<?> classToMock = Object.class;
 mockSettings.initiateMockName(classToMock);
 MockName mockName = mockSettings.getMockName();
 assertEquals(name, mockName.getName());
 assertEquals(classToMock, mockName.getMockType());
 }
}