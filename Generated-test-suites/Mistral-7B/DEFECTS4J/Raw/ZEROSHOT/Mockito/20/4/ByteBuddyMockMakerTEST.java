package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.StringJoiner.join;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerResolver;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;
 private ClassInstantiator classInstantiatorMock;

 @Before
 public void setUp() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
 classInstantiatorMock = mock(ClassInstantiator.class);
 ((ByteBuddyMockMaker) byteBuddyMockMaker).cachingMockBytecodeGenerator = cachingMockBytebytecodeGeneratorMock;
 ((ByteBuddyMockMaker) byteBuddyMockMaker).classInstantiator = classInstantiatorMock;
 }

 @Test
 public void testCreateMock() throws InstantiationException, IllegalAccessException, ClassCastException, NoSuchMethodException, InvocationTargetException {
 Class<MyMockType> mockedType = MyMockType.class;
 Class<MyMockHandler> mockHandlerType = MyMockHandler.class;
 MockCreationSettings<MyMockType> settings = MockCreationSettings.builder()
 .forType(mockedType)
 .withSerializableMode(SerializableMode.DEFAULT)
 .build();
 MyMockHandler mockHandler = mock(MyMockHandler.class);
 MyMockType mock = byteBuddyMockMaker.createMock(settings, mockHandler);

 Assert.assertNotNull(mock);
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 Assert.assertEquals(mockHandler, handler);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockSerializableModeAcrossClassloaders() {
 Class<MyMockType> mockedType = MyMockType.class;
 Class<MyMockHandler> mockHandlerType = MyMockHandler.class;
 MockCreationSettings<MyMockType> settings = MockCreationSettings.builder()
 .forType(mockedType)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .build();
 MyMockHandler mockHandler = mock(MyMockHandler.class);
 try {
 byteBuddyMockMaker.createMock(settings, mockHandler);
 } catch (MockitoException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockClassCastException() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
 Class<MyMockType> mockedType = MyMockType.class;
 Class<MyMockHandler> mockHandlerType = MyMockHandler.class;
 Class<String> stringType = String.class;
 MockCreationSettings<MyMockType> settings = MockCreationSettings.builder()
 .forType(mockedType)
 .withSerializableMode(SerializableMode.DEFAULT)
 .build();
 MyMockHandler mockHandler = mock(MyMockHandler.class);
 MyMockType mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Class<MyMockType> actualType = mock.getClass();
 Assert.assertNotSame(mockedType, actualType);
 Assert.assertEquals(mockedType, actualType.getSuperclass());
 Object stringMock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertFalse(mockedType.isAssignableFrom(stringMock.getClass()));
 Assert.assertFalse(stringType.isAssignableFrom(mock.getClass()));
 Assert.assertFalse(stringType.isAssignableFrom(mockHandler.getClass()));
 }

 @Test
 public void testGetHandler() {
 MyMockType mock = byteBuddyMockMaker.createMock(MockCreationSettings.builder().forType(MyMockType.class).build(), mock(MyMockHandler.class));
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 Assert.assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 MyMockType mock = byteBuddyMockMaker.createMock(MockCreationSettings.builder().forType(MyMockType.class).build(), mock(MyMockHandler.class));
 MyMockHandler newMockHandler = mock(MyMockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newMockHandler, MockCreationSettings.builder().forType(MyMockType.class).build());
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 Assert.assertEquals(newMockHandler, handler);
 }

 @Test
 public void testDescribeClass() {
 Class<MyMockType> mockedType = MyMockType.class;
 Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.builder().forType(mockedType).build(), mock(MyMockHandler.class));
 String description = byteBuddyMockMaker.describeClass(mock);
 Assert.assertEquals("'org.mockitotest.MyMockType', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@7852e922'", description);
 }

 @Test
 public void testInitializeClassInstantiator() throws ClassNotFoundException {
 Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 Constructor<?> constructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = constructor.newInstance(true);
 Assert.assertEquals(ClassInstantiator.class, classInstantiator.getClass());
 }

 @Test(expected = MockitoException.class)
 public void testUnableToCreateMockInstance() {
 Class<MyMockType> mockedType = MyMockType.class;
 MockCreationSettings<MyMockType> settings = MockCreationSettings.builder()
 .forType(mockedType)
 .build();
 try {
 byteBuddyMockMaker.createMock(settings, mock(MyMockHandler.class));
 } catch (MockitoException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingObjenesisOnClasspath() {
 try {
 byteBuddyMockMaker.initializeClassInstantiator();
 } catch (IllegalStateException e) {
 throw new AssertionError("Unexpected exception", e);
 }
 }
}

class MyMockType {}
class MyMockHandler {}