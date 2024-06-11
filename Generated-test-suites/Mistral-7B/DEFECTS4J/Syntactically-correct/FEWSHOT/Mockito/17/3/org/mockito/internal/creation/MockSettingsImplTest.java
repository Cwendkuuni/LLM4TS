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
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Object[]{});
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresNonNullParameters() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(String.class);
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
 Class<?>[] extraInterfaces = {Serializable.class, String.class};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, extraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 MockSettings mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 MockSettings mockSettings = new MockSettingsImpl();
 Answer<Object> defaultAnswer = mock(Answer.class);
 MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, defaultAnswerMockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = {Serializable.class};
 MockSettings serializableMockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 MockSettings mockSettings = new MockSettingsImpl();
 Class<?> classToMock = MockitoTest.class;
 MockSettings initiateMockNameMockSettings = mockSettings.initiateMockName(classToMock);
 assertNotNull(initiateMockNameMockSettings.getMockName());
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNullClassToMock() {
 MockSettings mockSettings = new MockSettingsImpl();
 MockSettings initiateMockNameMockSettings = mockSettings.initiateMockName(null);
 }
}