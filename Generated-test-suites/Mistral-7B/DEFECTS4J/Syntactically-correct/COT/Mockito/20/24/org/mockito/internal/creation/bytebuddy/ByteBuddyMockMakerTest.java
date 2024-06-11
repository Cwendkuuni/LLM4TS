package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRunner;
import org.mockito.quality.Strictness;

@RunWith(MockMakerRunner.class)
public class ByteBuddyMockMakerTest {

 private MockMaker mockMaker;
 private Class<Object> mockedType;
 private Class<Object> mockedProxyType;
 private Class<?> objenesisClass;
 private Constructor<ClassInstantiator> classInstantiatorConstructor;
 private GlobalConfiguration globalConfiguration;

 @Before
 public void setup() {
 mockMaker = new ByteBuddyMockMaker();
 mockedType = MockitoTestUtils.createTestType();
 mockedProxyType = mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(mockedType).build(), mockHandler -> {}).getClass();
 objenesisClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 classInstantiatorConstructor = objenesisClass.getDeclaredConstructor(boolean.class);
 globalConfiguration = new GlobalConfiguration();
 }

 @Test
 public void testCreateMock_SerializableMode_ACROSS_CLASSLOADERS() {
 assertThrows(MockitoException.class, () -> mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(mockedType).serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build(), mockHandler -> {}));
 }

 @Test
 public void testCreateMock_Success() {
 Object mock = mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(mockedType).build(), mockHandler -> {});
 assertNotNull(mock);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMock_ClassCastException() {
 Object mock = mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(MockObject.class).build(), mockHandler -> {});
 assertThrows(ClassCastException.class, () -> mockMaker.ensureMockIsAssignableToMockedType(new MockCreationSettings.Builder().theClass().forType(mockedType).build(), mock));
 }

 @Test
 public void testCreateMock_InstantiationException() {
 assertThrows(MockitoException.class, () -> mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(MockObject.class).build(), mockHandler -> {}));
 }

 @Test
 public void testGetHandler() {
 Object mock = mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(mockedType).build(), mockHandler -> {});
 MockHandler handler = mockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 Object mock = mockMaker.createMock(new MockCreationSettings.Builder().theClass().forType(mockedType).build(), mockHandler -> {});
 MockHandler newHandler = mockHandler -> {};
 mockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().theClass().forType(mockedType).build());
 MockHandler newHandlerAfterReset = mockMaker.getHandler(mock);
 assertSame(newHandler, newHandlerAfterReset);
 }

 @Test
 public void testDescribeClass() {
 assertNotNull(mockMaker.describeClass(mockedType));
 assertNotNull(mockMaker.describeClass(mockedProxyType));
 assertNotNull(mockMaker.describeClass(null));
 }

 @Test
 public void testInitializeClassInstantiator() {
 Object instance = null;
 try {
 instance = classInstantiatorConstructor.newInstance(true);
 } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 assertNotNull(instance);
 assertSame(ClassInstantiator.class, instance.getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitializeClassInstantiator_MissingObjenesis() {
 try {
 ClassInstantiator instance = classInstantiatorConstructor.newInstance(false);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 assertThrows(IllegalStateException.class, () -> mockMaker.initializeClassInstantiator());
 }
}