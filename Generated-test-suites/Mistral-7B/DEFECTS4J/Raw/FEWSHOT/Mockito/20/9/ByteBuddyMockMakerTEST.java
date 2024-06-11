package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.mockitoSession;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mockitoSession().stubber().createMock(String.class);

        try {
            MockCreationSettings settings = new MockCreationSettings.Builder().withTypeToMock(String.class).withSerializableMode(SerializableMode.SERIAL).build();
            Object mock = mockMaker.createMock(settings, mockHandler);
            assertNotNull(mock);
            assertNull(mockMaker.getHandler(mock));
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }

        try {
            MockCreationSettings settings = new MockCreationSettings.Builder().withTypeToMock(String.class).build();
            Object mock = mockMaker.createMock(settings, mockHandler);
            assertNotNull(mock);
            assertNotNull(mockMaker.getHandler(mock));
        } catch (MockitoException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            MockCreationSettings settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).build();
            Object mock = mockMaker.createMock(settings, mockHandler);
            fail("Expected MockitoException: ClassCastException occurred while creating the mockito mock :  class to mock : 'java.lang.Object', created class : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMock$1', proxy instance class : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMock$1', instance creation by : 'ClassInstantiator'");
        } catch (MockitoException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock : class to mock : 'java.lang.Object', created class : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMock$1', proxy instance class : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMock$1', instance creation by : 'ClassInstantiator'", e.getMessage());
        }

        try {
            MockCreationSettings settings = new MockCreationSettings.Builder().withTypeToMock(String[].class).build();
            Object mock = mockMaker.createMock(settings, mockHandler);
            fail("Expected MockitoException: Unable to create mock instance of type '[Ljava.lang.String;'");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type '[Ljava.lang.String;'", e.getMessage());
        }

        try {
            Class<?> clazz = Class.forName("org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            ByteBuddyMockMaker mockMakerInstance = (ByteBuddyMockMaker) constructor.newInstance();
            Object mock = mockMakerInstance.createMock(settings, mockHandler);
            fail("Expected MockitoException: Mockito could not create mock: Objenesis is missing on the classpath.");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Expected
        }
    }

    @Test
    public void testGetHandler() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mockitoSession().stubber().createMock(String.class);
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build(), mockHandler);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mockitoSession().stubber().createMock(String.class);
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build(), mockHandler);
        MockHandler newHandler = mockitoSession().stubber().createMock(String.class);
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().withTypeToMock(String.class).build());
        MockHandler resetHandler = mockMaker.getHandler(mock);
        assertNotNull(resetHandler);
        assertEquals(newHandler, resetHandler);
    }
}