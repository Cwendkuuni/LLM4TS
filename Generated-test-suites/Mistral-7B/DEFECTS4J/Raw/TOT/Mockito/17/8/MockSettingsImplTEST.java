import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializableMethod() {
 Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
 MockSettingsImpl mockSettings = new MockSettingsImpl().serializable().extraInterfaces(extraInterfaces);
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesWithNull() {
 MockSettingsImpl mockSettings = new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesWithNonInterface() {
 Class<?> nonInterfaceClass = Mockito.class;
 MockSettingsImpl mockSettings = new MockSettingsImpl().extraInterfaces(nonInterfaceClass);
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class, Integer.class };
 MockSettingsImpl mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 assertArrayEquals(new Class[] { java.io.Serializable.class, Integer.class }, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettingsImpl mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test(expected = AssertionError.class)
 public void testDefaultAnswerWithNull() {
 Answer<Object> defaultAnswer = null;
 MockSettingsImpl mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);
 }

 @Test
 public void testGetDefaultAnswer() {
 Answer<Object> defaultAnswer = Mockito.answer(args -> args[0] * 2);
 MockSettingsImpl mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }
}