package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.MockSettings.MockSettingsBuilder;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerWithqq;

public class MockSettingsImplTest {

 @Test
 public void testSerializable() {
 MockSettings settings = new MockSettingsImpl().serializable();
 assertTrue(settings instanceof MockSettingsImpl);
 }

 @Test
 public void testSerializableWithExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class, Integer.class };
 MockSettings settings = new MockSettingsImpl().serializable().extraInterfaces(extraInterfaces);
 assertTrue(settings instanceof MockSettingsImpl);
 assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 new MockSettingsImpl().extraInterfaces(new Object[] {});
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresNonNull() {
 new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 new MockSettingsImpl().extraInterfaces(String.class);
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class, Integer.class };
 MockSettings settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
 }

 @Test
 public void testName() {
 MockSettings settings = new MockSettingsImpl().name("Mock Name");
 assertEquals("Mock Name", settings.getMockName().getName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettings settings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertSame(spiedInstance, settings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = new AnswerWithqq<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "Mock Answer";
 }
 };
 MockSettings settings = new MockSettingsImpl().defaultAnswer(answer);
 assertSame(answer, settings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 MockSettings settings = new MockSettingsImpl().extraInterfaces(java.io.Serializable.class);
 assertTrue(settings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = MockitoTestRunner.class;
 MockSettings settings = new MockSettingsImpl();
 settings.initiateMockName(classToMock);
 assertNotNull(settings.getMockName());
 }

 @Test
 public void testInitiateMockNameWithName() {
 Class<?> classToMock = MockitoTestRunner.class;
 String name = "Mock Name";
 MockSettings settings = new MockSettingsImpl().name(name).initiateMockName(classToMock);
 assertEquals(name, settings.getMockName().getName());
 }
}