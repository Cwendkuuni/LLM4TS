package org.mockito.internal.creation;

import static org.junit.Assert.*;
import static org.mockito.exceptions.base.MockitoException.classNotFound;
import static org.mockito.exceptions.base.MockitoException.interfaceDoesNotMatch;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 MockSettings mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testSerializableWithNullExtraInterfaces() {
 try {
 MockSettings mockSettings = new MockSettingsImpl().serializable(null);
 fail("Should have thrown an exception");
 } catch (MockitoException e) {
 assertEquals("Extra interfaces requires at least one interface", e.getMessage());
 }
 }

 @Test
 public void testSerializableWithNonInterfaceExtraInterfaces() {
 try {
 MockSettings mockSettings = new MockSettingsImpl().serializable(String.class);
 fail("Should have thrown an exception");
 } catch (MockitoException e) {
 assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
 }
 }

 @Test
 public void testSerializableWithMultipleInterfaces() {
 Class<?>[] interfaces = {Serializable.class, Cloneable.class};
 MockSettings mockSettings = new MockSettingsImpl().serializable(interfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl().name("MockName").spiedInstance(new Object());
 MockName expectedMockName = new MockName("MockName", Object.class);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName.getName(), actualMockName.getName());
 assertEquals(expectedMockName.getClassName(), actualMockName.getClassName());
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl().spiedInstance(new Object());
 assertEquals(mockSettings.getSpiedInstance(), mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 mockSettings = new MockSettingsImpl().name("MockName");
 assertEquals("MockName", mockSettings.getName());
 }

 @Test
 public void testSpiedInstanceNull() {
 try {
 MockSettings mockSettings = new MockSettingsImpl().spiedInstance(null);
 fail("Should have thrown an exception");
 } catch (IllegalArgumentException e) {
 assertEquals("Spied instance cannot be null", e.getMessage());
 }
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testDefaultAnswerNull() {
 try {
 MockSettings mockSettings = new MockSettingsImpl().defaultAnswer(null);
 fail("Should have thrown an exception");
 } catch (NullPointerException e) {
 assertEquals("Default answer cannot be null", e.getMessage());
 }
 }

 @Test
 public void testInitiateMockName() {
 try {
 mockSettings = new MockSettingsImpl().initiateMockName(String.class);
 fail("Should have thrown an exception");
 } catch (ClassNotFoundException e) {
 assertEquals("Class 'String' not found", e.getMessage());
 }
 }

 @Test
 public void testInitiateMockNameWithInterface() {
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(MockSettingsImpl.class);
 MockName expectedMockName = new MockName("MockSettingsImpl", MockSettingsImpl.class);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName.getName(), actualMockName.getName());
 assertEquals(expectedMockName.getClassName(), actualMockName.getClassName());
 }

 @Test
 public void testInitiateMockNameWithClassToMockNull() {
 try {
 mockSettings.initiateMockName(null);
 fail("Should have thrown an exception");
 } catch (NullPointerException e) {
 assertEquals("Class to mock cannot be null", e.getMessage());
 }
 }
}