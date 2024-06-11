package org.mockito.internal.creation.test;

import static org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 // Testing serializable() method
 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 // Testing extraInterfaces() method with valid arguments
 @Test
 public void testExtraInterfacesValid() {
 mockSettings = mockSettings.extraInterfaces(Serializable.class);
 assertArrayEquals(new Class[]{Serializable.class}, mockSettings.getExtraInterfaces());
 }

 // Testing extraInterfaces() method with empty argument
 @Test(expected = Reporter.class)
 public void testExtraInterfacesEmpty() {
 mockSettings.extraInterfaces(new Class[0]);
 }

 // Testing extraInterfaces() method with null argument
 @Test(expected = Reporter.class)
 public void testExtraInterfacesNull() {
 mockSettings.extraInterfaces(null);
 }

 // Testing extraInterfaces() method with non-interface argument
 @Test(expected = Reporter.class)
 public void testExtraInterfacesNonInterface() {
 mockSettings.extraInterfaces(String.class);
 }

 // Testing getMockName() method
 @Test
 public void testGetMockName() {
 mockSettings.name("MockName");
 assertEquals("MockName", mockSettings.getMockName().getName());
 }

 // Testing getExtraInterfaces() method
 @Test
 public void testGetExtraInterfaces() {
 mockSettings = mockSettings.extraInterfaces(Serializable.class, Integer.class);
 assertArrayEquals(new Class[]{Serializable.class, Integer.class}, mockSettings.getExtraInterfaces());
 }

 // Testing getSpiedInstance() method
 @Test
 public void testGetSpiedInstance() {
 mockSettings.spiedInstance(new Object());
 assertEquals(mockSettings.getSpiedInstance(), new Object());
 }

 // Testing name() method
 @Test
 public void testName() {
 mockSettings.name("MockName");
 assertEquals("MockName", mockSettings.getMockName().getName());
 }

 // Testing spiedInstance() method
 @Test
 public void testSpiedInstance() {
 mockSettings.spiedInstance(new Object());
 assertEquals(mockSettings.getSpiedInstance(), new Object());
 }

 // Testing defaultAnswer() method with valid answer
 @Test
 public void testDefaultAnswerValid() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 // Testing defaultAnswer() method with null answer
 @Test(expected = Reporter.class)
 public void testDefaultAnswerNull() {
 mockSettings.defaultAnswer(null);
 }

 // Testing isSerializable() method
 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.extraInterfaces(Serializable.class);
 assertTrue(mockSettings.isSerializable());
 }

 // Testing initMockName() method
 @Test
 public void testInitMockName() {
 Class classToMock = MockitoTest.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}