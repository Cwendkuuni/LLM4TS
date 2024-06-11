package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMock() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(settings, null);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Class<Object> typeToMock = Object.class;
        Object mockInstance = new Object();
        Object mock = mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);

        assertNotNull(mock);
        assertEquals(typeToMock, mock.getClass());

        settings.setTypeToMock(null);
        Object nullMock = mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);
        assertNull(nullMock);
    }

    @Test
    public void testDescribeClass() {
        Class<Object> type = Object.class;
        String description = "java.lang.Object, loaded by classloader : " + type.getClassLoader();
        assertEquals(description, ByteBuddyMockMaker.describeClass(type));

        Object instance = new Object();
        String instanceDescription = "java.lang.Object, loaded by classloader : " + instance.getClass().getClassLoader();
        assertEquals(instanceDescription, ByteBuddyMockMaker.describeClass(instance));

        String nullDescription = "null";
        assertEquals(nullDescription, ByteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        assertNull(mockMaker.getHandler(mock));

        Object invalidMock = new Object();
        assertNull(mockMaker.getHandler(invalidMock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockMaker.MockHandler newHandler = mock(MockMaker.MockHandler.class);

        mockMaker.resetMock(mock, newHandler, settings);

        assertEquals(newHandler, mockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker.ClassInstantiator classInstantiator = ByteBuddyMockMaker.initializeClassInstantiator();
        assertNotNull(classInstantiator);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerException() {
        MockMaker.MockHandler handler = mock(MockMaker.MockHandler.class);
        mockMaker.asInternalMockHandler(handler);
    }
}