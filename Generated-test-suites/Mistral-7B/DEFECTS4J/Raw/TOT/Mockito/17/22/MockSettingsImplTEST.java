import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializableMethod() {
 // Test normal usage
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());

 // Test null extraInterfaces
 mockSettings.extraInterfaces(null);
 assertFalse(mockSettings.isSerializable());
 assertThrows(MockitoException.class, () -> mockSettings.serializable());

 // Test empty extraInterfaces
 mockSettings.extraInterfaces(new Class[0]);
 assertFalse(mockSettings.isSerializable());
 assertThrows(MockitoException.class, () -> mockSettings.serializable());

 // Test non-interface extraInterface
 mockSettings.extraInterfaces(String.class);
 assertFalse(mockSettings.isSerializable());
 assertThrows(MockitoException.class, () -> mockSettings.serializable());
 }

 @Test
 public void testExtraInterfacesMethod() {
 // Test normal usage with valid interfaces
 mockSettings.extraInterfaces(java.io.Serializable.class, java.util.Map.class);
 assertArrayEquals(new Class[]{java.io.Serializable.class, java.util.Map.class}, mockSettings.getExtraInterfaces());

 // Test null argument
 mockSettings.extraInterfaces(null);
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[0]));

 // Test null interface in the array
 mockSettings.extraInterfaces(new Class[]{java.io.Serializable.class, null});
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[0]));

 // Test non-interface class in the array
 mockSettings.extraInterfaces(new Class[]{java.io.Serializable.class, String.class});
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[0]));
 }

 @Test
 public void testNameMethod() {
 mockSettings.name("mockName");
 assertEquals("mockName", mockSettings.getMockName().getName());
 }

 @Test
 public void testSpiedInstanceMethod() {
 mockSettings.spiedInstance(new Object());
 assertEquals(mockSettings.getSpiedInstance(), new Object());
 }

 @Test
 public void testDefaultAnswerMethod() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializableMethod() {
 assertFalse(mockSettings.isSerializable());
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockNameMethod() {
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 assertEquals(classToMock.getName(), mockSettings.getMockName().getClassName());
 }
}