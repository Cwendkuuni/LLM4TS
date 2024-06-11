package org.mockito.internal.creation.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Object[0]);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] { null });
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] { String.class });
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] expected = new Class[] { java.io.Serializable.class };
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 mockSettings.extraInterfaces(expected);
 Assert.assertArrayEquals(expected, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = new MockSettingsImpl();
 mockSettings.spiedInstance(spiedInstance);
 Assert.assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("TestName");
 Assert.assertEquals("TestName", mockSettings.getMockName().getName());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = Answer.returnValues(1);
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(answer);
 Assert.assertSame(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 mockSettings.extraInterfaces(new Class[] { java.io.Serializable.class });
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class classToMock = MockitoTest.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 Assert.assertNotNull(mockSettings.getMockName());
 }
}