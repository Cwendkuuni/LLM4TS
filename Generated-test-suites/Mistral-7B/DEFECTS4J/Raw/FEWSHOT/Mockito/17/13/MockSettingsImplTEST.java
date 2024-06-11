package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettings mockSettings;

 @Before
 public void setup() {
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
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 mockSettings.extraInterfaces(new Object[] {new Object() {}});
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] {MockSettings.class, java.io.Serializable.class};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesNullParameter() {
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesNullInterface() {
 Class<?>[] extraInterfaces = new Class[] {null};
 mockSettings.extraInterfaces(extraInterfaces);
 }

 @Test
 public void testGetMockName() {
 assertEquals(null, mockSettings.getMockName());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name("mockName");
 assertEquals("mockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = mockito -> "answer";
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 mockSettings = mockSettings;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }

 @Test(expected = AssertionError.class)
 public void testInitiateMockNameNullParameter() {
 mockSettings.initiateMockName(null);
 }

}