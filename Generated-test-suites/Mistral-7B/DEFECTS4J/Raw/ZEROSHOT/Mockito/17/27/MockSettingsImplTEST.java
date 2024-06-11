package com.example.junit4; // replace with your package name

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MockSettingsImplTest {

 private MockSettings mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setUp() throws Exception {
 extraInterfaces = new Class[]{java.io.Serializable.class, String.class};
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertEquals(mockSettings.isSerializable(), true);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializableWithEmptyExtraInterfaces() {
 mockSettings = mockSettings.serializable();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializableWithNonInterfaceExtraInterface() {
 Class<?> nonInterface = Mockito.mock(String.class);
 mockSettings = mockSettings.serializable();
 mockSettings.extraInterfaces(nonInterface);
 }

 @Test(expected = IllegalStateException.class)
 public void testSerializableWithNullExtraInterface() {
 mockSettings = mockSettings.serializable();
 mockSettings.extraInterfaces(null);
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = mockSettings.serializable();
 mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetName() {
 name = "mockName";
 mockSettings = new MockSettingsImpl().name(name);
 assertEquals(name, mockSettings.getName());
 }

 @Test
 public void testSpiedInstance() {
 spiedInstance = mock(Object.class);
 mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 assertSame(java.util.Arrays.asList(extraInterfaces).contains(java.io.Serializable.class), mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = mock(Class.class);
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertSame(classToMock, mockSettings.getMockName().getMockClass());
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testNullName() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name(null);
 assertNull(mockSettings.getName());
 }

 @Test
 public void testInitiateMockNameWithSameNameAndClass() {
 Class<?> classToMock = mock(Class.class);
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 mockSettings.initiateMockName(classToMock);
 verifyNoMoreInteractions(classToMock);
 }

 @Test
 public void testInitiateMockNameWithDifferentNamesAndSameClass() {
 Class<?> classToMock = mock(Class.class);
 String name1 = "name1";
 String name2 = "name2";
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock).name(name1);
 mockSettings.initiateMockName(classToMock).name(name2);
 assertEquals(name1, mockSettings.getMockName(0).getName());
 assertEquals(name2, mockSettings.getMockName(1).getName());
 }

 @Test
 public void testInitiateMockNameWithSameNameButDifferentClasses() {
 Class<?> classToMock1 = mock(Class.class);
 Class<?> classToMock2 = mock(Class.class);
 String name = "name";
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock1).name(name);
 mockSettings.initiateMockName(classToMock2);
 assertNotSame(mockSettings.getMockName(0), mockSettings.getMockName(1));
 }

 @Test
 public void testInitiateMockNameWithNullClass() {
 Class<?> classToMock = null;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNull(mockSettings.getMockName());
 }
}