import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesNullParameter() {
 mockSettings = new MockSettingsImpl().extraInterfaces(null);
 fail("IllegalStateException should be thrown but wasn't");
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesEmptyArray() {
 mockSettings = new MockSettingsImpl().extraInterfaces(new Class[0]);
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesNullInterface() {
 mockSettings = new MockSettingsImpl().extraInterfaces(new Class[] { null });
 new Reporter().extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesNonInterface() {
 mockSettings = new MockSettingsImpl().extraInterfaces(String.class);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
 }

 @Test
 public void testGetMockNameMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(String.class);
 assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl().extraInterfaces(new Class[] { Serializable.class });
 assertEquals(new Class[] { Serializable.class }, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 mockSettings = new MockSettingsImpl().spiedInstance(new Object());
 assertEquals(new Object(), mockSettings.getSpiedInstance());
 }

 @Test
 public void testNameMethod() {
 mockSettings = new MockSettingsImpl().name("TestName");
 assertEquals("TestName", mockSettings.getName());
 }

 @Test
 public void testSpiedInstanceMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.spiedInstance(new Object());
 assertEquals(new Object(), mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> answer = mock -> mock.toString();
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }
}