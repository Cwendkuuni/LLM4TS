package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerWithMockito;

public class MockSettingsImplTest {

 @Test
 public void testSerializableMockSettings() {
 MockSettings serializableMockSettings = new MockSettingsImpl().serializable();
 Class<?>[] extraInterfaces = serializableMockSettings.getExtraInterfaces();
 Assert.assertNotNull(extraInterfaces);
 Assert.assertEquals(1, extraInterfaces.length);
 Assert.assertTrue(java.io.Serializable.class.isInstance(extraInterfaces[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullExtraInterfacesMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyExtraInterfacesMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(new Class[0]);
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();
 }

 @Test
 public void testValidExtraInterfacesMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(MockSettingsImpl.class, java.io.Serializable.class);
Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
 Assert.assertNotNull(extraInterfaces);
 Assert.assertEquals(2, extraInterfaces.length);
 Assert.assertTrue(MockSettingsImpl.class.isInstance(extraInterfaces[0])) ;
 Assert.assertTrue(java.io.Serializable.class.isInstance(extraInterfaces[1]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullExtraInterfaceMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(new Class[]{ null, MockSettingsImpl.class });
 new Reporter().extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonInterfaceExtraInterfaceMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(new Class[]{ String.class, MockSettingsImpl.class });
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
 }

 @Test
 public void testNameMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().name("MockitoTest");
 String name = mockSettings.getMockName().getName();
 Assert.assertEquals("MockitoTest", name);
 }

 @Test
 public void testSpiedInstanceMockSettings() {
 Object spiedInstance = new Object();
 MockSettings mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 Object spiedInstanceReturned = mockSettings.getSpiedInstance();
 Assert.assertEquals(spiedInstance, spiedInstanceReturned);
 }

 @Test
 public void testDefaultAnswerMockSettings() {
 Answer<Object> defaultAnswer = new AnswerWithMockito<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "mocked answer";
 }
 };
 MockSettings mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);
 Answer<Object> defaultAnswerReturned = mockSettings.getDefaultAnswer();
 Assert.assertEquals(defaultAnswer, defaultAnswerReturned);
 }

 @Test
 public void testIsSerializableMockSettings() {
 MockSettings mockSettings1 = new MockSettingsImpl();
 Assert.assertFalse(mockSettings1.isSerializable());

 MockSettings mockSettings2 = new MockSettingsImpl().serializable();
 Assert.assertTrue(mockSettings2.isSerializable());
 }

 @Test
 public void testInitiateMockNameMockSettings() {
 Class classToMock = MockSettingsImpl.class;
 MockSettings mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 MockName mockName = mockSettings.getMockName();
 Assert.assertNotNull(mockName);
 Assert.assertEquals(classToMock.getName(), mockName.getName());
 }
}