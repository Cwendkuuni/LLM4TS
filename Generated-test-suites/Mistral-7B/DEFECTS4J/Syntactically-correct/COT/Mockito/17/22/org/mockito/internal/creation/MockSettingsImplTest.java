import org.junit.Test;
import org.junit.Assert;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 Class<?>[] serializableExtraInterfaces = new Class[] { java.io.Serializable.class };
 mockSettings.extraInterfaces(serializableExtraInterfaces);
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfacesThrowsException() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testEmptyExtraInterfacesThrowsException() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[] {});
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfaceThrowsException() {
 mockSettings = new MockSettingsImpl();
 Class<?> nullInterface = null;
 mockSettings.extraInterfaces(new Class[] { nullInterface });
 }

 @Test(expected = Reporter.class)
 public void testNonInterfaceExtraInterfaceThrowsException() {
 mockSettings = new MockSettingsImpl();
 Class<?> nonInterfaceClass = String.class;
 mockSettings.extraInterfaces(new Class[] { nonInterfaceClass });
 }

 @Test
 public void testGetMockNameMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(String.class);
 Assert.assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class, String.class };
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 Object spiedInstance = new Object();
 mockSettings = new MockSettingsImpl();
 mockSettings.spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testNameMethod() {
 String name = "MockName";
 mockSettings = new MockSettingsImpl();
 mockSettings.name(name);
 Assert.assertEquals(name, mockSettings.getName());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> answer = mock -> "Default Answer";
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(answer);
 Assert.assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] serializableExtraInterfaces = new Class[] { java.io.Serializable.class };
 mockSettings.extraInterfaces(serializableExtraInterfaces);
 Assert.assertTrue(mockSettings.isSerializable());
 }
}