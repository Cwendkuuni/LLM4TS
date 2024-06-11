package org.mockito.internal.creation.bytebuddy;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.reflection.Reflection.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker mockMaker;
 private GlobalConfiguration globalConfiguration;
 private ClassInstantiator classInstantiator;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

 @BeforeEach
 public void setUp() {
 mockMaker = new ByteBuddyMockMaker();
 globalConfiguration = new GlobalConfiguration();
 classInstantiator = mockMaker.initializeClassInstantiator();
 cachingMockBytecodeGenerator = mockMaker.new CachingMockBytecodeGenerator();
 }

 @Test
 public void testCreateMockWithNullSettings() {
 assertThrows(NullPointerException.class, () -> mockMaker.createMock(null, mock(MockHandler.class)));
 }

 @Test
 public void testCreateMockWithNullHandler() {
 assertThrows(NullPointerException.class, () -> mockMaker.createMock(new MockCreationSettings<>(), null));
 }

 @Test
 public void testCreateMockWithSerializableModeAcrossClassLoaders() {
 assertThrows(MockitoException.class, () -> mockMaker.createMock(new MockCreationSettings<>().serializableMode(SerializableMode.ACROSS_CLASSLOADERS), mock(MockHandler.class)));
 }

 @Test
 public void testCreateMockWithValidSettingsAndHandler() {
 MockCreationSettings<Object> settings = new MockCreationSettings<>().serializableMode(SerializableMode.IN_THE_SAME_CLASSLOADER);
 MockHandler handler = mock(MockHandler.class);
 Object mock = mockMaker.createMock(settings, handler);
 assertNotNull(mock);
 }

 @Test
 public void testCreateMockWithSameClassTypeToMockAndMockedType() {
 MockCreationSettings<Object> settings = new MockCreationSettings<>().typeToMock(String.class);
 MockHandler handler = mock(MockHandler.class);
 try {
 mockMaker.createMock(settings, handler);
 fail("Expected MockitoException");
 } catch (MockitoException e) {
 assertEquals("ClassCastException occurred while creating the mockito mock :", e.getMessage());
 }
 }

 @Test
 public void testCreateMockWithDifferentClassTypesToMockAndMockedType() {
 MockCreationSettings<Object> settings = new MockCreationSettings<>().typeToMock(Integer.class);
 MockHandler handler = mock(MockHandler.class);
 Object mock = mockMaker.createMock(settings, handler);
 assertNotNull(mock);
 }

 @Test
 public void testGetHandlerWithNullMock() {
 assertNull(mockMaker.getHandler(null));
 }

 @Test
 public void testGetHandlerWithMock() {
 Object mock = mockMaker.createMock(new MockCreationSettings<>(), mock(MockHandler.class));
 MockHandler handler = mockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMockWithNullMock() {
 mockMaker.resetMock(null, mock(MockHandler.class), new MockCreationSettings<>());
 }

 @Test
 public void testResetMockWithMockAndNullHandler() {
 Object mock = mockMaker.createMock(new MockCreationSettings<>(), mock(MockHandler.class));
 mockMaker.resetMock(mock, null, new MockCreationSettings<>());
 }

 @Test
 public void testResetMockWithMockAndHandler() {
 Object mock = mockMaker.createMock(new MockCreationSettings<>(), mock(MockHandler.class));
 MockHandler newHandler = mock(MockHandler.class);
 mockMaker.resetMock(mock, newHandler, new MockCreationSettings<>());
 }

 @Test
 public void testInitializeClassInstantiator() {
 try {
 Class<?> objenesisClassLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 Constructor<?> usingClassCacheConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache());
 } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
 throw new IllegalStateException("Mockito could not create mock: Objenesis is missing on the classpath.", e);
 }
 }

 @Test
 public void testInitializeClassInstantiatorWithFalseClassCache() {
 try {
 Class<?> objenesisClassLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 Constructor<?> usingClassCacheConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(false);
 } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
 // expected
 }
 }

 @Test
 public void testAsInternalMockHandlerWithNullMockHandler() {
 assertThrows(MockitoException.class, () -> mockMaker.asInternalMockHandler(null));
 }

 @Test
 public void testAsInternalMockHandlerWithMockHandler() {
 MockHandler handler = mock(MockHandler.class);
 InternalMockHandler internalMockHandler = mockMaker.asInternalMockHandler(handler);
 assertNotNull(internalMockHandler);
 }
}