package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockWithNullSettingsAndHandler() {
        assertNull(new ByteBuddyMockMaker().createMock(null, null));
    }

    @Test
    public void testCreateMockWithSettingsAndHandler() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(mockedInterface).build();

        MockitoMockMakerTestUtils.verifyMockCreation(new ByteBuddyMockMaker(), settings, mockHandler);
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        Class<?> extraInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(mockedInterface)
                .withExtraInterfaces(extraInterface)
                .build();

        MockitoMockMakerTestUtils.verifyMockCreation(new ByteBuddyMockMaker(), settings, mockHandler);
    }

    @Test
    public void testCreateMockWithDifferentSerializableModes() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);

        MockCreationSettings<Object> settingsSerializable = new MockCreationSettings.Builder()
                .withTypeToMock(mockedInterface)
                .withSerializableMode(SerializableMode.SERIALIZABLE)
                .build();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settingsSerializable, mockHandler);

        MockCreationSettings<Object> settingsAcrossClassLoaders = new MockCreationSettings.Builder()
                .withTypeToMock(mockedInterface)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settingsAcrossClassLoaders, mockHandler));
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        String expectedDescription = String.format("'%s', loaded by classloader : '%s'", mockedInterface.getCanonicalName(), mockedInterface.getClassLoader());

        assertEquals(expectedDescription, ByteBuddyMockMaker.describeClass(mockedInterface));
    }

    @Test
    public void testDescribeObject() {
        Object mock = new Object();
        String expectedDescription = ByteBuddyMockMaker.describeClass(mock.getClass()).toString();

        assertEquals(expectedDescription, ByteBuddyMockMaker.describeClass(mock));
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler expectedMockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        byteBuddyMockMaker.setMockitoInterceptor(mock, new MockMethodInterceptor(expectedMockHandler, null));

        MockHandler actualMockHandler = byteBuddyMockMaker.getHandler(mock);
        assertSame(expectedMockHandler, actualMockHandler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        byteBuddyMockMaker.setMockitoInterceptor(mock, new MockMethodInterceptor(newMockHandler, null));
        byteBuddyMockMaker.resetMock(mock, newMockHandler, null);

        verify(newMockHandler).reset();
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ClassInstantiator classInstantiator = byteBuddyMockMaker.initializeClassInstantiator();
        assertTrue(classInstantiator instanceof ClassInstantiator);
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(mockHandler);
        assertTrue(internalMockHandler instanceof InternalMockHandler);
    }

    @Test
    public void testCreateMock_SerializationAcrossClassloadersNotSupported() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(mockedInterface)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMock_ClassCastException() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(String[].class) // Different type from mockedInterface
                .build();

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertThrows(ClassCastException.class, () -> byteBuddyMockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMock_InstantiationException() {
        Class<?> mockedInterface = MockitoMockMakerTestUtils.createMockInterface();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(mockedInterface)
                .build();

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mockHandler));
    }
}