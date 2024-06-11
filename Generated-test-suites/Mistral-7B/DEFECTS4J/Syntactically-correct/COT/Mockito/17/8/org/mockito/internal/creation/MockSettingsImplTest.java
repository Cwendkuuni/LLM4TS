package org.mockito.internal.creation.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 // Test for serializable method
 @Test
 public void testSerializableMethod() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 // Test for extraInterfaces method with valid input
 @Test
 public void testExtraInterfacesMethodValidInput() {
 Class<?>[] interfaces = new Class[]{Serializable.class};
 mockSettings = mockSettings.extraInterfaces(interfaces);
 assertTrue(mockSettings.isSerializable());
 }

 // Test for extraInterfaces method with null input
 @Test(expected = Reporter.class)
 public void testExtraInterfacesMethodNullInput() {
 mockSettings = mockSettings.extraInterfaces(null);
 }

 // Test for extraInterfaces method with non interface input
 @Test(expected = Reporter.class)
 public void testExtraInterfacesMethodNonInterfaceInput() {
 Class<?> nonInterface = MockSettingsImpl.class;
 mockSettings = mockSettings.extraInterfaces(nonInterface);
 }

 // Test for extraInterfaces method with multiple inputs
 @Test
 public void testExtraInterfacesMethodMultipleInputs() {
 Class<?>[] interfaces = new Class[]{Serializable.class, Object.class};
 mockSettings = mockSettings.extraInterfaces(interfaces);
 Class<?>[] actualInterfaces = mockSettings.getExtraInterfaces();
 Assert.assertArrayEquals(interfaces, actualInterfaces);
 }

 // Test for getMockName method
 @Test
 public void testGetMockNameMethod() {
 mockSettings.initiateMockName(MockSettingsImpl.class);
 String expectedName = "MockSettingsImpl_0_Mockito_1";
 String actualName = mockSettings.getMockName().getName();
 assertEquals(expectedName, actualName);
 }

 // Test for getExtraInterfaces method
 @Test
 public void testGetExtraInterfacesMethod() {
 Class<?>[] expectedInterfaces = new Class[]{Serializable.class};
 mockSettings = mockSettings.serializable().extraInterfaces(expectedInterfaces);
 Class<?>[] actualInterfaces = mockSettings.getExtraInterfaces();
 Assert.assertArrayEquals(expectedInterfaces, actualInterfaces);
 }

 // Test for getSpiedInstance method
 @Test
 public void testGetSpiedInstanceMethod() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 Object actualSpiedInstance = mockSettings.getSpiedInstance();
 assertEquals(spiedInstance, actualSpiedInstance);
 }

 // Test for name method
 @Test
 public void testNameMethod() {
 String name = "TestName";
 mockSettings = mockSettings.name(name);
 String actualName = mockSettings.getMockName().getName();
 assertEquals(name, actualName);
 }

 // Test for defaultAnswer method with valid input
 @Test
 public void testDefaultAnswerMethodValidInput() {
 Answer<Object> answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 };
 mockSettings = mockSettings.defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 // Test for defaultAnswer method with null input
 @Test(expected = NullPointerException.class)
 public void testDefaultAnswerMethodNullInput() {
 mockSettings = mockSettings.defaultAnswer(null);
 }
}