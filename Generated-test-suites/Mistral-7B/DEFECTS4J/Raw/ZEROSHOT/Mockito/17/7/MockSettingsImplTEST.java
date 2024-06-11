package com.example.junit4; // replace with your testing package

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
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
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testExtraInterfacesNull() {
 mockSettings = mockSettings.extraInterfaces((Class<?>[]) null);
 Assert.assertNotNull(new Reporter().extraInterfacesRequiresAtLeastOneInterfaceCall());
 }

 @Test
 public void testExtraInterfacesEmptyArray() {
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 Assert.assertNotNull(new Reporter().extraInterfacesRequiresAtLeastOneInterfaceCall());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExtraInterfacesNullClass() {
 mockSettings = mockSettings.extraInterfaces(null);
 Assert.assertNotNull(new Reporter().extraInterfacesDoesNotAcceptNullParametersCall());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExtraInterfacesNonInterface() {
 Class<?> nonInterface = new Object() {}.getClass();
 mockSettings = mockSettings.extraInterfaces(nonInterface);
 Assert.assertNotNull(new Reporter().extraInterfacesAcceptsOnlyInterfacesCall(nonInterface));
 }

 @Test
 public void testGetMockName() {
 mockSettings.initiateMockName(Mockito.class);
 Assert.assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] interfaces = new Class[] {Serializable.class, Mockito.class};
 mockSettings = mockSettings.extraInterfaces(interfaces);
 Assert.assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name("MockName");
 Assert.assertEquals("MockName", mockSettings.getName());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 mockSettings = mockSettings.defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 Class<?>[] interfaces = new Class[] {Serializable.class};
 mockSettings = mockSettings.extraInterfaces(interfaces);
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testIsSerializableWithoutSerializableInterface() {
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 Assert.assertFalse(mockSettings.isSerializable());
 }
}