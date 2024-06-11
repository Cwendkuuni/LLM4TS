package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettings mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = mockSettings.extraInterfaces((Class[]) null);
 }

 @Test
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 mockSettings = mockSettings.extraInterfaces(Object.class);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(Object.class);
 }

 @Test
 public void testExtraInterfacesAcceptsMultipleInterfaces() {
 Class<?>[] extraInterfaces = { java.io.Serializable.class, String[].class };
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testGetMockName() {
 assertNull(mockSettings.getMockName());
 mockSettings = mockSettings.name("mockName");
 MockName expectedMockName = new MockName("mockName", MockitoTest.class);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName.getName(), actualMockName.getName());
 assertEquals(expectedMockName.getMockType(), actualMockName.getMockType());
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = { java.io.Serializable.class, String[].class };
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 assertNull(mockSettings.getSpiedInstance());
 mockSettings = mockSettings.spiedInstance(new Object());
 assertEquals(mockSettings.getSpiedInstance(), new Object());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name("mockName");
 assertEquals("mockName", mockSettings.getName());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 Class<?>[] extraInterfaces = { java.io.Serializable.class, String[].class };
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> mockClass = MockitoTest.class;
 mockSettings = mockSettings.initiateMockName(mockClass);
 MockName expectedMockName = new MockName("mockName", mockClass);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName.getName(), actualMockName.getName());
 assertEquals(expectedMockName.getMockType(), actualMockName.getMockType());
 }
}