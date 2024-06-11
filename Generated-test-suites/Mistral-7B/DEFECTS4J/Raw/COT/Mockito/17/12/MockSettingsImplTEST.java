import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.Reporter;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testNullExtraInterfacesThrowsError() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testEmptyExtraInterfacesThrowsError() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = Reporter.class)
 public void testNullInterfaceThrowsError() {
 mockSettings = new MockSettingsImpl();
 Class<?> nullInterface = null;
 mockSettings.extraInterfaces(new Class[]{nullInterface});
 }

 @Test(expected = Reporter.class)
 public void testNonInterfaceThrowsError() {
 mockSettings = new MockSettingsImpl();
 Class<?> nonInterface = new Object() {}.getClass();
 mockSettings.extraInterfaces(new Class[]{nonInterface});
 }

 @Test
 public void testGetNameMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.name("testName");
 Assert.assertEquals("testName", mockSettings.getMockName().getName());
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> defaultAnswer = Answer.returnValues(new Object());
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(defaultAnswer);
 Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 Assert.assertFalse(mockSettings.isSerializable());
 mockSettings = new MockSettingsImpl().serializable();
 Assert.assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockNameMethod() {
 Class<?> classToMock = new Object().getClass();
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 Assert.assertNotNull(mockSettings.getMockName());
 }
}