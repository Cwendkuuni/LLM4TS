package org.mockito.internal.creation.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = AssertionError.class)
 public void testNullExtraInterfacesThrowsException() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 fail("Exception expected: Reporter.extraInterfacesRequiresAtLeastOneInterface");
 }

 @Test(expected = AssertionError.class)
 public void testNullExtraInterfaceThrowsException() {
 Class<?>[] extraInterfaces = new Class<?>[1];
 extraInterfaces[0] = null;
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(extraInterfaces);
 fail("Exception expected: Reporter.extraInterfacesDoesNotAcceptNullParameters");
 }

 @Test(expected = AssertionError.class)
 public void testNonInterfaceExtraInterfaceThrowsException() {
 Class<?>[] extraInterfaces = new Class<?>[1];
 extraInterfaces[0] = String.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(extraInterfaces);
 fail("Exception expected: Reporter.extraInterfacesAcceptsOnlyInterfaces");
 }

 @Test
 public void testGetMockNameMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("TestName");
 MockName mockName = mockSettings.getMockName();
 assertEquals("TestName", mockName.getName());
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 Class<?>[] extraInterfaces = new Class<?>[1];
 extraInterfaces[0] = Serializable.class;
 mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 assertArrayEquals(new Class<?>[] { Serializable.class }, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 Object spiedInstance = new Object();
 mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testNameMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("TestName");
 assertEquals("TestName", mockSettings.getName());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Test Answer";
 }
 };
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }
}