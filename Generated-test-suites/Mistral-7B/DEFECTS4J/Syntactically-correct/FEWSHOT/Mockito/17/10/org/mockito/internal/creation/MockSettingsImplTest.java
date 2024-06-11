package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 @Test
 public void testSerializable() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 assertEquals(MockSettings.class, mockSettings.getClass());
 MockSettings serializableMockSettings = mockSettings.serializable();
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[]{null});
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[]{String.class, int.class});
 }

 @Test
 public void testExtraInterfaces() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class, org.mockito.Mock.class};
 mockSettings.extraInterfaces(extraInterfaces);
 Class<?>[] actualExtraInterfaces = mockSettings.getExtraInterfaces();
 assertArrayEquals(extraInterfaces, actualExtraInterfaces);
 }

 @Test
 public void testGetMockName() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(String.class);
 MockName expectedMockName = new MockName("", String.class);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName, actualMockName);
 }

 @Test
 public void testGetSpiedInstance() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 Object actualSpiedInstance = mockSettings.getSpiedInstance();
 assertEquals(spiedInstance, actualSpiedInstance);
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> defaultAnswer = mock(Answer.class);
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(defaultAnswer);
 Answer<Object> actualDefaultAnswer = mockSettings.getDefaultAnswer();
 assertEquals(defaultAnswer, actualDefaultAnswer);
 }

 @Test
 public void testIsSerializable() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 assertTrue(mockSettings.isSerializable());
 mockSettings.extraInterfaces(new Class[0]);
 assertFalse(mockSettings.isSerializable());
 }
}