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
 mockSettings.extraInterfaces(new Object[0]);
 fail("Unexpected result. Reporter exception not thrown.");
 }

 @Test
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 Class<?>[] extraInterfaces = new Class[]{String.class};
 try {
 mockSettings.extraInterfaces(extraInterfaces);
 fail("Unexpected result. Reporter exception not thrown.");
 } catch (AssertionError e) {
 assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
 }

 Class<?>[] extraInterfaces2 = new Class[]{String[].class};
 try {
 mockSettings.extraInterfaces(extraInterfaces2);
 fail("Unexpected result. Reporter exception not thrown.");
 } catch (AssertionError e) {
 assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
 }
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[]{Serializable.class, Number.class};
 mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(new Class[]{Serializable.class, Number.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 assertNull(mockSettings.getMockName());
 }

 @Test
 public void testGetSpiedInstance() {
 assertNull(mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name("testMock");
 assertEquals("testMock", mockSettings.getName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());

 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = String.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}