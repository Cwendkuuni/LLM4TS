package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 @Test
 public void testSerializableMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings instanceof MockSettingsImpl);
 assertNull(mockSettings.getExtraInterfaces());
 }

 @Test
 public void testSerializableMockSettingsWithExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
 MockSettings mockSettings = new MockSettingsImpl().serializable().extraInterfaces(extraInterfaces);
 assertTrue(mockSettings instanceof MockSettingsImpl);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test(expected = AssertionError.class)
 public void testNullExtraInterfaces() {
 new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testEmptyExtraInterfaces() {
 new MockSettingsImpl().extraInterfaces(new Class[0]);
 }

 @Test(expected = AssertionError.class)
 public void testNonInterfaceExtraInterface() {
 new MockSettingsImpl().extraInterfaces(String.class);
 }

 @Test
 public void testMultipleExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class, java.util.List.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 assertTrue(mockSettings instanceof MockSettingsImpl);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 String name = "mockName";
 MockSettings mockSettings = new MockSettingsImpl().name(name);
 MockName mockName = mockSettings.getMockName();
 assertEquals(name, mockName.getName());
 assertEquals(name, mockSettings.getName());
 }

 @Test(expected = AssertionError.class)
 public void testNullName() {
 new MockSettingsImpl().name(null);
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettings mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 assertEquals(spiedInstance, mockSettings.getMockName().getSpiedObject());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = arg -> arg;
 MockSettings mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 MockSettings serializableMockSettings = new MockSettingsImpl().serializable();
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = MockitoTest.class;
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test(expected = AssertionError.class)
 public void testNullClassToMock() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(null);
 }
}