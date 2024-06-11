package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
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
 private ClassInstantiator classInstantiatorMock;
 private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiatorMock = mock(ClassInstantiator.class);
 cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
 byteBuddyMockMaker = new ByteBuddyMockMaker() {
 {
 this.classInstantiator = classInstantiatorMock;
 this.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorMock;
 }
 };
 }

 @Test
 public void testCreateMock() {
 Class<?> mockType = mock(Class.class);
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(mockType, SerializableMode.SERIAL);
 MockHandler handler = mock(MockHandler.class);
 Object mockInstance = byteBuddyMockMaker.createMock(settings, handler);
 assertNotNull(mockInstance);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationAcrossClassloaders() {
 Class<?> mockType = mock(Class.class);
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(mockType, SerializableMode.ACROSS_CLASSLOADERS);
 MockHandler handler = mock(MockHandler.class);
 byteBuddyMockMaker.createMock(settings, handler);
 }

 @Test
 public void testEnsureMockIsAssignableToMockedType() {
 Class<?> mockType = mock(Class.class);
 Object mock = mock(Object.class);
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(mockType, SerializableMode.SERIAL);
 byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
 }

 @Test
 public void testDescribeClass() {
 Class<?> mockType = mock(Class.class);
 String description = byteBuddyMockMaker.describeClass(mockType);
 assertNotNull(description);
 }

 @Test
 public void testDescribeClassForNull() {
 String description = byteBuddyMockMaker.describeClass(null);
 assertEquals("null", description);
 }

 @Test
 public void testDescribeClassForObject() {
 Object mock = mock(Object.class);
 String description = byteBuddyMockMaker.describeClass(mock);
 String descriptionForType = byteBuddyMockMaker.describeClass(mock.getClass());
 assertEquals(descriptionForType, description);
 }

 @Test
 public void testGetHandler() {
 Object mock = mock(Object.class);
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 MockHandler newHandler = mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings<Object>(mock.getClass(), SerializableMode.SERIAL));
 MockHandler oldHandler = byteBuddyMockMaker.getHandler(mock);
 assertNull(oldHandler);
 assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
 }

 @Test
 public void testInitializeClassInstantiator() {
 try {
 byteBuddyMockMaker.initializeClassInstantiator();
 fail("Expected MockitoException to be thrown.");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test
 public void testAsInternalMockHandler() {
 MockHandler handler = mock(MockHandler.class);
 InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(handler);
 assertNotNull(internalMockHandler);
 }
}