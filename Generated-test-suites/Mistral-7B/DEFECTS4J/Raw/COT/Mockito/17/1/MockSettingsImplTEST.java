package org.mockito.internal.creation.test;

import static org.junit.Assert.*;

import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 // Test for serializable() method
 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 // Test for extraInterfaces() method with valid interface parameters
 @Test
 public void testExtraInterfacesValidInterfaces() {
 Class<?>[] extraInterfaces = new Class[]{Serializable.class, Mockito.class};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertEquals(Arrays.asList(extraInterfaces), Arrays.asList(mockSettings.getExtraInterfaces()));
 }

 // Test for extraInterfaces() method with null parameter
 @Test(expected = Reporter.class)
 public void testExtraInterfacesNullParameter() {
 mockSettings.extraInterfaces(null);
 }

 // Test for extraInterfaces() method with non-interface parameter
 @Test(expected = Reporter.class)
 public void testExtraInterfacesNonInterfaceParameter() {
 Class<?> nonInterfaceClass = Mockito.class;
 mockSettings.extraInterfaces(nonInterfaceClass);
 }

 // Test for name() method
 @Test
 public void testName() {
 mockSettings = mockSettings.name("MockName");
 assertEquals("MockName", mockSettings.getMockName().getName());
 }

 // Test for spiedInstance() method
 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 // Test for defaultAnswer() method with valid answer
 @Test
 public void testDefaultAnswerValidAnswer() {
 Answer<Object> answer = Mockito.answer((InvocationOnMock invocation) -> invocation.getArguments()[0]);
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 // Test for defaultAnswer() method with null answer
 @Test(expected = Reporter.class)
 public void testDefaultAnswerNullAnswer() {
 mockSettings.defaultAnswer(null);
 }

 // Test for isSerializable() method
 @Test
 public void testIsSerializable() {
 Class<?>[] serializableExtraInterfaces = new Class[]{Serializable.class};
 mockSettings = mockSettings.serializable().extraInterfaces(serializableExtraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 // Test for initiateMockName() method
 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}