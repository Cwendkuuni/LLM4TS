package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.exceptions.base.MockitoException.classNotFound;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 @Test
 public void testSerializable() {
 MockSettings settings = new MockSettingsImpl();
 MockSettings serializableSettings = settings.serializable();
 assertEquals(settings, serializableSettings);
 assertArrayEquals(new Class[]{}, serializableSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesEmptyArray() {
 MockSettings settings = new MockSettingsImpl();
 assertThrows(Reporter.class, () -> settings.extraInterfaces(new Class[0]));
 }

 @Test
 public void testExtraInterfacesNullClass() {
 MockSettings settings = new MockSettingsImpl();
 assertThrows(Reporter.class, () -> settings.extraInterfaces(null));
 }

 @Test
 public void testExtraInterfacesNonInterfaceClass() {
 MockSettings settings = new MockSettingsImpl();
 assertThrows(Reporter.class, () -> settings.extraInterfaces(String.class));
 }

 @Test
 public void testExtraInterfacesMultipleClasses() {
 MockSettings settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class, String.class};
 MockSettings settingsWithExtraInterfaces = settings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 MockSettings settings = new MockSettingsImpl();
 assertNull(settings.getMockName());
 }

 @Test
 public void testGetExtraInterfaces() {
 MockSettings settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
 settings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 MockSettings settings = new MockSettingsImpl();
 assertNull(settings.getSpiedInstance());
 }

 @Test
 public void testName() {
 MockSettings settings = new MockSettingsImpl();
 String name = "MockName";
 MockSettings namedSettings = settings.name(name);
 assertEquals(name, namedSettings.getName());
 }

 @Test
 public void testSpiedInstance() {
 MockSettings settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 MockSettings spiedInstanceSettings = settings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, spiedInstanceSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswerNull() {
 MockSettings settings = new MockSettingsImpl();
 assertThrows(NullPointerException.class, () -> settings.defaultAnswer(null));
 }

 @Test
 public void testDefaultAnswer() {
 MockSettings settings = new MockSettingsImpl();
 Answer<Object> defaultAnswer = mock(Answer.class);
 MockSettings settingsWithDefaultAnswer = settings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, settingsWithDefaultAnswer.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableTrue() {
 MockSettings settings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
 settings.extraInterfaces(extraInterfaces);
 assertTrue(settings.isSerializable());
 }

 @Test
 public void testIsSerializableFalse() {
 MockSettings settings = new MockSettingsImpl();
 assertFalse(settings.isSerializable());
 }

 @Test
 public void testInitiateMockNameNullClassToMock() {
 MockSettings settings = new MockSettingsImpl();
 assertThrows(NullPointerException.class, () -> settings.initiateMockName(null));
 }

 @Test
 public void testInitiateMockNameValidClass() {
 MockSettings settings = new MockSettingsImpl();
 String name = "MockName";
 Class<?> classToMock = String.class;
 settings.initiateMockName(classToMock);
 assertNotNull(settings.getMockName());
 assertEquals(name, settings.getMockName().getName());
 assertEquals(classToMock, settings.getMockName().getMockType());
 }
}