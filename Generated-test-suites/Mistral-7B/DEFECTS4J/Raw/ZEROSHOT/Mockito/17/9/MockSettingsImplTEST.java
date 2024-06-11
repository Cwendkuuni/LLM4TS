import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces = new Class[]{Serializable.class};
 private String name = "MockName";
 private Object spiedInstance = new Object();
 private Answer<Object> defaultAnswer = Mockito.answer(InvocationOnMock.returnValue(42));

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl()
 .serializable()
 .extraInterfaces(extraInterfaces)
 .name(name)
 .spiedInstance(spiedInstance)
 .defaultAnswer(defaultAnswer);
 }

 @Test
 public void testSerializable() {
 MockSettings serializableSettings = mockSettings.serializable();
 assertEquals(serializableSettings, mockSettings.serializable());
 assertTrue(serializableSettings.isSerializable());
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] expectedExtraInterfaces = new Class[]{Serializable.class};
 MockSettings settingsWithExtraInterfaces = mockSettings.extraInterfaces(expectedExtraInterfaces);
 assertSame(settingsWithExtraInterfaces, mockSettings.extraInterfaces(expectedExtraInterfaces));
 assertArrayEquals(expectedExtraInterfaces, settingsWithExtraInterfaces.getExtraInterfaces());
 }

 @Test(expected = AssertionError.class)
 public void testNullExtraInterfaces() {
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testNonInterfaceExtraInterface() {
 mockSettings.extraInterfaces(String.class);
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateInterface() {
 mockSettings.extraInterfaces(new Class[]{Serializable.class, Serializable.class});
 }

 @Test
 public void testGetName() {
 assertEquals(name, mockSettings.getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testGetDefaultAnswer() {
 assertNull(mockSettings.getDefaultAnswer());
 }

 @Test
 public void testSetDefaultAnswer() {
 Answer<Object> newDefaultAnswer = Mockito.answer(InvocationOnMock.returnValue(43));
 MockSettings settingsWithDefaultAnswer = mockSettings.defaultAnswer(newDefaultAnswer);
 assertEquals(newDefaultAnswer, settingsWithDefaultAnswer.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 assertTrue(mockSettings.isSerializable());
 assertTrue(mockSettings.serializable().isSerializable());
 assertFalse(mockSettings.serializable().extraInterfaces(null).isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 MockName expectedMockName = new MockName(name, classToMock);
 MockName actualMockName = mockSettings.getMockName();
 assertEquals(expectedMockName, actualMockName);
 }
}