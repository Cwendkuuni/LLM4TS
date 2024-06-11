import org.junit.Assert.*; // for Assert methods
import org.junit.Test; // for Test annotation
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullExtraInterfacesInExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyExtraInterfacesInExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonInterfaceExtraInterfaceInExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 Class<?> nonInterfaceClass = new Object() {}.getClass();
 mockSettings.extraInterfaces(nonInterfaceClass);
 }

 @Test
 public void testGetExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] expectedExtraInterfaces = new Class[] {java.io.Serializable.class};
 mockSettings.extraInterfaces(expectedExtraInterfaces);
 assertArrayEquals(expectedExtraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstanceMethod() {
 mockSettings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testNameMethod() {
 mockSettings = new MockSettingsImpl();
 String name = "testName";
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> defaultAnswer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 };
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 assertTrue(mockSettings.isSerializable());
 Class<?>[] serializableExtraInterfaces = new Class[] {java.io.Serializable.class};
 mockSettings.extraInterfaces(serializableExtraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockNameMethod() {
 Class<?> classToMock = String.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}