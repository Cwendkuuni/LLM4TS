package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
import org.mockito.MockSettings.MockSettingsBuilder;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 @Test
 public void testSerializableMockSettings() {
 MockSettings mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = MockitoException.class)
 public void testEmptyExtraInterfaces() {
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl();
 mockSettingsBuilder.extraInterfaces();
 }

 @Test(expected = MockitoException.class)
 public void testNullExtraInterface() {
 Class<?>[] extraInterfaces = new Class<?>[]{null};
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl();
 mockSettingsBuilder.extraInterfaces(extraInterfaces);
 }

 @Test(expected = MockitoException.class)
 public void testNonInterfaceExtraInterface() {
 Class<?>[] extraInterfaces = new Class<?>[]{String.class};
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl();
 mockSettingsBuilder.extraInterfaces(extraInterfaces);
 }

 @Test
 public void testSetExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class<?>[]{String.class, Serializable.class};
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl();
 MockSettings mockSettings = mockSettingsBuilder.extraInterfaces(extraInterfaces).build();
 assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetName() {
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl().name("mockName").build();
 MockSettings mockSettings = mockSettingsBuilder;
 assertNull(mockSettings.getName());
 mockSettings.initiateMockName(String.class);
 String name = mockSettings.getName();
 assertEquals("mockName", name);
 }

 @Test
 public void testSetAndGetSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl().spiedInstance(spiedInstance).build();
 MockSettings mockSettings = mockSettingsBuilder;
 assertNull(mockSettings.getSpiedInstance());
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testSetDefaultAnswer() {
 Answer<Object> answer = mock(Answer.class);
 MockSettingsBuilder mockSettingsBuilder = new MockSettingsImpl();
 MockSettings mockSettings = mockSettingsBuilder.defaultAnswer(answer).build();
 assertNull(mockSettings.getDefaultAnswer());
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 Class<?>[] extraInterfaces = new Class<?>[]{Serializable.class};
 MockSettings mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces).build();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = String.class;
 MockSettings mockSettings = new MockSettingsImpl().build();
 assertNull(mockSettings.getMockName());
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}