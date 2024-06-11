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
 mockSettings.extraInterfaces(new Class[] { MockSettingsImpl.class });
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("TestMock");
 Assert.assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] { MockSettings.class, java.io.Serializable.class });
 Assert.assertArrayEquals(new Class[] { MockSettings.class, java.io.Serializable.class }, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] { java.io.Serializable.class });
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testInitiateMockNameNullClassToMock() {
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(null);
 }

 @Test
 public void testToString() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] { MockSettings.class, java.io.Serializable.class });
 Assert.assertNotNull(mockSettings.toString());
 }
}