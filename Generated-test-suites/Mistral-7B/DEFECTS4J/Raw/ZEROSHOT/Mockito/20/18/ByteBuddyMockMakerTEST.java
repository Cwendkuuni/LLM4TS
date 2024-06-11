package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private ClassInstantiator classInstantiator;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiator = mock(ClassInstantiator.class);
 cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
 when(classInstantiator.instantiate(any())).thenCallRealMethod();
 when(cachingMockBytecodeGenerator.get(any(), any()))
         .thenAnswer(invocation -> invocation.getArguments()[0]);
 }

 @Test
 public void testCreateMock() {
 Class<?> mockableType = String.class;
 MockCreationSettings settings = new MockCreationSettings(mockableType, SerializableMode.SERIAL);
 MockHandler mockHandler = mock(MockHandler.class);

 try {
 ByteBuddyMockMaker byteBuddyMockMaker = spy(new ByteBuddyMockMaker());
 doReturn(classInstantiator).when(byteBuddyMockMaker).initializeClassInstantiator();
 doReturn(cachingMockBytecodeGenerator).when(byteBuddyMockMaker).new CachingMockBytecodeGenerator();

 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 assertNotNull(mock);
 verify(classInstantiator).instantiate(eq(settings.getTypeToMock()));
 verifyNoMoreInteractions(classInstantiator);
 } catch (MockitoException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationModeAcrossClassloaders() {
 Class<?> mockableType = String.class;
 MockCreationSettings settings = new MockCreationSettings(mockableType, SerializableMode.ACROSS_CLASSLOADERS);
 MockHandler mockHandler = mock(MockHandler.class);

 ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
 throwAssertion(byteBuddyMockMaker::createMock, settings, mockHandler);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockClassCastException() {
 Class<?> mockableType = Object.class;
 MockCreationSettings settings = new MockCreationSettings(mockableType, SerializableMode.SERIAL);
 MockHandler mockHandler = mock(MockHandler.class);

 ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 assertNotNull(mock);
 verify(classInstantiator).instantiate(eq(settings.getTypeToMock()));
 verify(mockHandler).mockUsing(any());

 Class<?> mockedProxyType = mockBuddyMockMaker.cachingMockBytecodeGenerator.get(
         eq(mockableType), any());
 assertFalse(mockedProxyType.isAssignableFrom(mockableType));
 assertFalse(mockableType.isAssignableFrom(mockedProxyType));

 try {
 mock = mockedProxyType.cast(mock);
 fail("ClassCastException expected");
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testGetHandler() {
 Object mock = mock(String.class);
 assertEquals(mockBuddyMockMaker.getHandler(mock), mockHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testGetHandlerWithNullMock() {
 byteBuddyMockMaker.getHandler(null);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(String.class);
 MockHandler newMockHandler = mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings(String.class, SerializableMode.SERIAL);

 byteBuddyMockMaker.resetMock(mock, newMockHandler, settings);
 verify(mock).setMockitoInterceptor(any());
 }

 @Test(expected = NullPointerException.class)
 public void testResetMockWithNullMock() {
 byteBuddyMockMaker.resetMock(null, mockHandler(), new MockCreationSettings(String.class, SerializableMode.SERIAL));
 }

 @Test
 public void testInitializeClassInstantiator() {
 try {
 ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
 Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
 ByteBuddyMockMaker spyByteBuddyMockMaker = spy(byteBuddyMockMaker);
 doCallRealMethod().when(spyByteBuddyMockMaker).initializeClassInstantiator();

 byteBuddyMockMaker.initializeClassInstantiator();
 verify(spyByteBuddyMockMaker).initializeClassInstantiator();
 } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInitializeClassInstantiatorWithMissingObjenesis() {
 ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
 throwAssertion(byteBuddyMockMaker::initializeClassInstantiator);
 }

 @Test
 public void testAsInternalMockHandler() {
 MockHandler mockHandler = mock(MockHandler.class);
 assertEquals(internalMockHandler(mockHandler), byteBuddyMockMaker.asInternalMockHandler(mockHandler));
 }

 @Test(expected = MockitoException.class)
 public void testAsInternalMockHandlerWithNonInternalMockHandler() {
 MockHandler mockHandler = mock(NonInternalMockHandler.class);
 throwAssertion(byteBuddyMockMaker::asInternalMockHandler, mockHandler);
 }

 private static InternalMockHandler internalMockHandler(MockHandler handler) {
 return mock(InternalMockHandler.class);
 }

 private static void throwAssertion(MockMaker mockMaker, Object... args) {
 try {
 mockMaker.createMock(args);
 fail("MockitoException expected");
 } catch (MockitoException e) {
 // expected
 }
}

 private static MockHandler mockHandler() {
 return mock(MockHandler.class);
 }
}