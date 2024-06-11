package org.mockito.internal.creation.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettings mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings instanceof MockSettingsImpl);
 assertFalse(mockSettings.isSerializable());

 mockSettings = new MockSettingsImpl().serializable();
 assertFalse(mockSettings.isSerializable());

 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfacesThrowsException() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testEmptyExtraInterfacesThrowsException() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfaceThrowsException() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[1];
 extraInterfaces[0] = null;
 mockSettings.extraInterfaces(extraInterfaces);
 }

 @Test(expected = Reporter.class)
 public void testNonInterfaceExtraInterfaceThrowsException() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[1];
 extraInterfaces[0] = String.class;
 mockSettings.extraInterfaces(extraInterfaces);
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl().serializable().extraInterfaces(MockSettingsImpl.class, Serializable.class);
 Class<?>[] expectedExtraInterfaces = new Class[2];
 expectedExtraInterfaces[0] = MockSettingsImpl.class;
 expectedExtraInterfaces[1] = Serializable.class;
 assertArrayEquals(expectedExtraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 Object spiedInstance = mock(Object.class);
 mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testSetAndGetNameMethod() {
 mockSettings = new MockSettingsImpl().name("mockName");
 assertSame("mockName", mockSettings.getName());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertSame(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableMethod() {
 mockSettings = new MockSettingsImpl().serializable().extraInterfaces(Serializable.class);
 assertTrue(mockSettings.isSerializable());

 mockSettings = new MockSettingsImpl().serializable();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockNameMethod() {
 Class<Object> classToMock = Object.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetMockNameMethod() {
 Class<Object> classToMock = Object.class;
 mockSettings = new MockSettingsImpl().name("mockName").initiateMockName(classToMock);
 assertSame(new MockName("mockName", classToMock), mockSettings.getMockName());
 }
}