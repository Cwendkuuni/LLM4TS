package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

 @Test
 public void testSerializableMockSettings() {
 MockSettings serializableMockSettings = new MockSettingsImpl().serializable();
 Assert.assertNotNull(serializableMockSettings.getExtraInterfaces());
 Assert.assertTrue(java.util.Arrays.asList(serializableMockSettings.getExtraInterfaces()).contains(java.io.Serializable.class));
 }

 @Test
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 try {
 mockSettings.extraInterfaces(new Object[0]);
 } catch (AssertionError e) {
 Assert.assertEquals("extraInterfaces requires at least one interface", e.getMessage());
 }
 }

 @Test
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 try {
 mockSettings.extraInterfaces(new Object[]{String.class});
 } catch (AssertionError e) {
 Assert.assertEquals("extraInterfaces accepts only interfaces", e.getMessage());
 }
 }

 @Test
 public void testExtraInterfacesAcceptsNulls() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class<?>[1];
 extraInterfaces[0] = null;
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertNull(mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesAcceptsMultipleInterfaces() {
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class<?>[2];
 extraInterfaces[0] = java.io.Serializable.class;
 extraInterfaces[1] = String.class;
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertArrayEquals(new Class<?>[]{java.io.Serializable.class, String.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettingsImpl mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 String name = "MockName";
 MockSettingsImpl mockSettings = new MockSettingsImpl().name(name);
 Assert.assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testDefaultAnswer() throws Exception {
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 MockSettingsImpl mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testDefaultAnswerWithNullAnswer() {
 try {
 new MockSettingsImpl().defaultAnswer(null);
 } catch (AssertionError e) {
 Assert.assertEquals("defaultAnswer requires a valid answer", e.getMessage());
 }
 }

 @Test
 public void testIsSerializable() {
 MockSettingsImpl mockSettings = new MockSettingsImpl().extraInterfaces(java.io.Serializable.class);
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockNameWithNullClassToMock() {
 try {
 new MockSettingsImpl().initiateMockName(null);
 } catch (AssertionError e) {
 Assert.assertEquals("classToMock must not be null", e.getMessage());
 }
 }
}