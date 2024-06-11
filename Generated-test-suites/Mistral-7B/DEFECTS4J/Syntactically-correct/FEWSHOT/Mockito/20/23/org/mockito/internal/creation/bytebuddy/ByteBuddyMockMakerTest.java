package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.MockMakerAnnotations;

public class ByteBuddyMockMakerTest {

 private MockMaker mockMaker;

 @BeforeClass
 public static void setup() {
 ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
 mockMaker = byteBuddyMockMaker;
 }

 @Test
 public void testCreateMock() {
 Class<?> mockedType = String.class;
 MockHandler mockHandler = mock(MockHandler.class);
 SerializableMode serializableMode = SerializableMode.SERIAL;
 MockCreationSettings<String> mockCreationSettings = new MockCreationSettings.Builder().withTypeToMock(mockedType).withSerializableMode(serializableMode).build();

 try {
 String mock = mockMaker.createMock(mockCreationSettings, mockHandler);
 assertNotNull(mock);
 assertEquals(mockedType, mock.getClass());
 } catch (MockitoException e) {
 fail("Unexpected exception " + e.getMessage());
 }
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationAcrossClassloaders() {
 Class<?> mockedType = String.class;
 MockHandler mockHandler = mock(MockHandler.class);
 SerializableMode serializableMode = SerializableMode.ACROSS_CLASSLOADERS;
 MockCreationSettings<String> mockCreationSettings = new MockCreationSettings.Builder().withTypeToMock(mockedType).withSerializableMode(serializableMode).build();
 mockMaker.createMock(mockCreationSettings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 String mock = mockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build());
 MockHandler handler = mockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 String mock = mockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build());
 MockHandler newMockHandler = mock(MockHandler.class);
 mockMaker.resetMock(mock, newMockHandler, new MockCreationSettings.Builder().withTypeToMock(String.class).build());
 MockHandler oldMockHandler = mockMaker.getHandler(mock);
 assertNull(oldMockHandler);
 assertNotNull(newMockHandler);
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testCreateMockWithExtraInterfaces() {
 Class<?> mockedType = String.class;
 Class<?> extraInterface = Integer.class;
 MockHandler mockHandler = mock(MockHandler.class);
 SerializableMode serializableMode = SerializableMode.SERIAL;
 MockCreationSettings<String> mockCreationSettings = new MockCreationSettings.Builder().withTypeToMock(mockedType)
 .withSerializableMode(serializableMode)
 .withExtraInterfaces(extraInterface)
 .build();

 try {
 String mock = (String) mockMaker.createMock(mockCreationSettings, mockHandler);
 assertNotNull(mock);
 assertEquals(mockedType, mock.getClass());
 assertTrue(mock instanceof ExtraInterface);
 } catch (MockitoException e) {
 fail("Unexpected exception " + e.getMessage());
 }
 }

 @Test(expected = ClassCastException.class)
 public void testEnsureMockIsAssignableToMockedTypeWithInvalidCast() {
 Class<?> mockedType = Integer.class;
 Object mock = new Object();
 MockCreationSettings<Object> mockCreationSettings = new MockCreationSettings.Builder().withTypeToMock(mockedType).build();

 try {
 mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
 fail("Expected ClassCastException not thrown");
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test
 public void testDescribeClass() {
 Class<?> mockedType = String.class;
 assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@6d06d692", describeClass(mockedType));
 assertEquals("null", describeClass(null));
 }

 @Test
 public void testDescribeClassForObject() {
 Object mock = mock(Object.class);
 assertEquals("class java.lang.Object, loaded by classloader : sun.misc.Launcher$AppClassLoader@6d06d692", describeClass(mock));
 }

 @Test
 public void testInitializeClassInstantiator() {
 Class<?> classInstantiatorType = ByteBuddyMockMaker.ClassInstantiator.class;
 try {
 Class<?> classInstantiator = mockMaker.initializeClassInstantiator();
 assertEquals(classInstantiatorType, classInstantiator.getClass());
 } catch (Throwable e) {
 fail("Unexpected exception " + e.getMessage());
 }
 }

 @Test(expected = MockitoException.class)
 public void testInitializeClassInstantiatorWithMissingObjenesis() {
 try {
 mockMaker.initializeClassInstantiator();
 fail("Expected MockitoException not thrown");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test
 public void testAsInternalMockHandler() {
 MockHandler mockHandler = mock(MockHandler.class);
 InternalMockHandler internalMockHandler = mockMaker.asInternalMockHandler(mockHandler);
 assertInstanceOf(InternalMockHandler.class, internalMockHandler);
 }

 @Test(expected = MockitoException.class)
 public void testAsInternalMockHandlerWithInvalidMockHandler() {
 Object mockHandler = new Object();
 mockMaker.asInternalMockHandler(mockHandler);
 fail("Expected MockitoException not thrown");
 }
}