package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.creation.MockSettingsImpl.serialVersionUID;

public class MockSettingsImplTest {

 private MockSettings mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertNotNull(mockSettings.getExtraInterfaces());
 assertArrayEquals(new Class<?>[]{java.io.Serializable.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] interfaces = new Class<?>[]{java.io.Serializable.class, org.mockito.Mock.class};
 mockSettings = mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());

 interfaces = new Class<?>[]{null, java.io.Serializable.class};
 try {
 mockSettings = mockSettings.extraInterfaces(interfaces);
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();
 } catch (AssertionError e) {
 // expected
 }

 interfaces = new Class<?>[]{java.io.Serializable.class, new Object(){}};
 try {
 mockSettings = mockSettings.extraInterfaces(interfaces);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(new Object(){});
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testName() {
 name = "testName";
 mockSettings = mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 Object instance = new Object();
 mockSettings = mockSettings.spiedInstance(instance);
 assertEquals(instance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = MockitoAnswers.RETURNS_DEEP_STUBS;
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.extraInterfaces(new Class<?>[]{java.io.Serializable.class});
 assertTrue(mockSettings.isSerializable());

 mockSettings = mockSettings.extraInterfaces(new Class<?>[]{java.lang.String.class});
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> clazzToMock = Mockito.class;
 mockSettings.initiateMockName(clazzToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test(expected = AssertionError.class)
 public void testInitiateMockNameNullClass() {
 mockSettings.initiateMockName(null);
 }
}