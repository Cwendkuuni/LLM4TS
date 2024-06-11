package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettings mockSettings;

 @Test
 public void testSerializable() {
 MockSettings mockSettings = new MockSettingsImpl();
 assertEquals(mockSettings.serializable(), mockSettings);
 assertFalse(mockSettings.isSerializable());
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces();
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 Class<?>[] interfaces = new Class<?>[]{Integer.class, String.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(interfaces);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(Integer.class);
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] interfaces = new Class<?>[]{Serializable.class, Cloneable.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(interfaces);
 assertEquals(interfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testName() {
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.name("MockName");
 assertEquals("MockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Integer> answer = mock(Answer.class);
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 Class<?>[] interfaces = new Class<?>[]{Serializable.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(interfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNullClass() {
 Class<?> classToMock = null;
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 }

}