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
 public void testSerializableNullParameter() {
 new MockSettingsImpl().serializable(null);
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] emptyInterfaces = new Class[0];
 MockSettings mockSettings = new MockSettingsImpl();

 mockSettings = mockSettings.extraInterfaces(emptyInterfaces);
 assertArrayEquals(emptyInterfaces, mockSettings.getExtraInterfaces());

 Class<?> interfaceOne = java.util.ArrayList.class;
 Class<?> interfaceTwo = java.io.Serializable.class;
 Class<?>[] interfaces = {interfaceOne, interfaceTwo};
 MockSettings settingsWithInterfaces = mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, settingsWithInterfaces.getExtraInterfaces());

 Class<?> nullClass = null;
 MockSettings settingsWithNullClass = mockSettings.extraInterfaces(nullClass);
 assertArrayEquals(interfaces, settingsWithInterfaces.getExtraInterfaces());

 Class<?> nonInterfaceClass = new Object() {};
 MockSettings settingsWithNonInterfaceClass = mockSettings.extraInterfaces(nonInterfaceClass);
 assertEquals(interfaces, settingsWithInterfaces.getExtraInterfaces());
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(nonInterfaceClass);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesNullParameterInLoop() {
 Class<?>[] interfaces = new Class[10];
 for (int i = 0; i < 10; i++) {
 interfaces[i] = null;
 }
 new MockSettingsImpl().extraInterfaces(interfaces);
 }

 @Test
 public void testName() {
 String name = "MockName";
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test(expected = Reporter.class)
 public void testNameNullParameter() {
 new MockSettingsImpl().name(null);
 }

 @Test
 public void testSpiedInstance() {
 Object instance = new Object();
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.spiedInstance(instance);
 assertEquals(instance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mock(Answer.class);
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test(expected = Reporter.class)
 public void testDefaultAnswerNullParameter() {
 new MockSettingsImpl().defaultAnswer(null);
 }

 @Test
 public void testIsSerializable() {
 Class<?>[] emptyInterfaces = new Class[0];
 MockSettings mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());

 Class<?> interfaceOne = java.util.ArrayList.class;
 Class<?> interfaceTwo = java.io.Serializable.class;
 Class<?>[] interfaces = {interfaceOne, interfaceTwo};
 MockSettings settingsWithInterfaces = mockSettings.extraInterfaces(interfaces);
 assertTrue(settingsWithInterfaces.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNullParameter() {
 new MockSettingsImpl().initiateMockName(null);
 }
}