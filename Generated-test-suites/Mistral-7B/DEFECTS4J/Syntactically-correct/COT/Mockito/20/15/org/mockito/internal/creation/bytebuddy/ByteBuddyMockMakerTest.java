package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;

 @Before
 public void setUp() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockThrowsExceptionWhenSerializableModeIsAcrossClassLoaders() {
 MockCreationSettings settings = new MockCreationSettings<Object>()
 .forClass(Object.class)
 .serializableMode(SerializableMode.ACROSS_CLASSLOADERS);
 byteBuddyMockMaker.createMock(settings, mockitoMockHandler);
 }

 @Test
 public void testCreateMockReturnsMockedInstance() {
 Class<String> stringType = String.class;
 MockCreationSettings<String> settings = new MockCreationSettings<String>()
 .forClass(stringType)
 .serializableMode(SerializableMode.IN_THE_SAME_CLASSLOADER);
 MockHandler mockHandler = mockitoMockHandler;
 String mockedInstance = byteBuddyMockMaker.createMock(settings, mockitoMockHandler);
 assertNotNull(mockedInstance);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockThrowsClassCastException() {
 Class<Integer> intType = Integer.class;
 MockCreationSettings<Integer> settings = new MockCreationSettings<Integer>()
 .forClass(intType)
 .serializableMode(SerializableMode.IN_THE_SAME_CLASSLOADER);
 byteBuddyMockMaker.createMock(settings, mockitoMockHandler);
 // This test assumes that the created mock instance is not an instance of Integer type
 Object mockedInstance = byteBuddyMockMaker.createMock(settings, mockitoMockHandler);
 assertNotSame(intType, mockedInstance.getClass());
 try {
 int value = (int) mockedInstance;
 fail("ClassCastException expected");
 } catch (ClassCastException e) {
 // pass
 }
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMockThrowsInstantiationException() {
 Class<Object> objectType = Object.class;
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>()
 .forClass(objectType)
 .serializableMode(SerializableMode.IN_THE_SAME_CLASSLOADER);
 byteBuddyMockMaker.createMock(settings, mockitoMockHandler);
 }

 @Test
 public void testGetHandlerReturnsMockHandler() {
 Object mockedInstance = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>().forClass(Object.class), mockitoMockHandler);
 MockHandler handler = byteBuddyMockMaker.getHandler(mockedInstance);
 assertNotNull(handler);
 }

 @Test
 public void testResetMockResetsHandler() {
 Object mockedInstance = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>().forClass(Object.class), mockitoMockHandler);
 MockHandler newHandler = mockitoMockHandler;
 byteBuddyMockMaker.resetMock(mockedInstance, newHandler, new MockCreationSettings<Object>().forClass(Object.class));
 MockHandler resetHandler = byteBuddyMockMaker.getHandler(mockedInstance);
 assertNotSame(mockitoMockHandler, resetHandler);
 }

 @Test(expected = MockitoException.class)
 public void testInitializeClassInstantiatorThrowsMockitoException() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 byteBuddyMockMaker.initializeClassInstantiator();
 }

 private MockHandler mockitoMockHandler = mock(MockHandler.class);

}