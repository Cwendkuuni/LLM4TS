import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerCalledWith;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertArrayEquals;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 name = "TestMock";
 extraInterfaces = new Class[]{Serializable.class};
 spiedInstance = mock(Object.class);
 defaultAnswer = Mockito.callRealMethod();
 }

 @Test(expected = MockitoException.class)
 public void testSerializable_nullExtraInterfaces() {
 mockSettings.serializable();
 }

 @Test(expected = MockitoException.class)
 public void testSerializable_emptyExtraInterfaces() {
 mockSettings.serializable();
 }

 @Test
 public void testSerializable_validExtraInterfaces() {
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = MockitoException.class)
 public void testExtraInterfaces_nullInterface() {
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = MockitoException.class)
 public void testExtraInterfaces_nonInterface() {
 mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testExtraInterfaces_multipleInterfaces() {
 Class<?>[] interfaces = {Serializable.class, Integer.class};
 mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testName() {
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test(expected = MockitoException.class)
 public void testName_nullName() {
 mockSettings.name(null);
 }

 @Test
 public void testSpiedInstance() {
 mockSettings.spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings.defaultAnswer(defaultAnswer);
 assertSame(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testInitiateMockName() throws IllegalAccessException, InstantiationException, InvocationTargetException {
 mockSettings.initiateMockName(String[].class);
 assertSame(mockName.getClass(), MockName.class);
 }

 @Test
 public void testIsSerializable() {
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings.extraInterfaces(new Class[]{Serializable.class, Integer.class});
 List<Class<?>> expected = Arrays.asList(Serializable.class, Integer.class);
 List<Class<?>> actual = Arrays.asList(mockSettings.getExtraInterfaces());
 assertSame(expected, actual);
 }

 @Test
 public void testName_emptyName() {
 String emptyName = "";
 mockSettings.name(emptyName);
 assertNull(mockSettings.getMockName());
 }

 @Test(expected = MockitoException.class)
 public void testName_duplicateName() {
 mockSettings.name(name);
 mockSettings.name("TestMock2");
 }

 @Test
 public void testDefaultAnswer_nullDefaultAnswer() {
 mockSettings.defaultAnswer(null);
 assertNull(mockSettings.getDefaultAnswer());
 }

 @Test
 public void testDefaultAnswer_customAnswer() {
 Answer<String> customAnswer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocationOnMock) throws Throwable {
 return "Custom answer";
 }
 };
 mockSettings.defaultAnswer(customAnswer);
 assertEquals("Custom answer", mockSettings.getDefaultAnswer().answer(mockitoStub()));
 }

 @Test
 public void testDefaultAnswer_defaultAnswer() {
 mockSettings.defaultAnswer(defaultAnswer);
 Answer<Object> actualAnswer = (Answer<Object>) Mockito.spy(mockSettings).defaultAnswer;
 assertEquals(defaultAnswer, actualAnswer);
 }

 @Test
 public void testDefaultAnswer_customAnswerWithArguments() {
 Answer<Integer> customAnswerWithArguments = Mockito.argThat(new AnswerCalledWith(Integer.class));
 mockSettings.defaultAnswer(customAnswerWithArguments);
 Integer testValue = 1;
 assertEquals(testValue, mockSettings.getDefaultAnswer().answer(mockitoStub(Integer.class), testValue));
 }
}