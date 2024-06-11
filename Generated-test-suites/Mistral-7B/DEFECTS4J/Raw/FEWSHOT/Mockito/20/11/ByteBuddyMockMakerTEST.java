package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.internal.util.StringJoiner.join;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMockWithNullType() {
        try {
            mockMaker.createMock(null, null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Type to mock must not be null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullHandler() {
        try {
            mockMaker.createMock(new MockCreationSettings<>(String.class), null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("MockHandler must not be null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        try {
            mockMaker.createMock(new MockCreationSettings<>(String.class, SerializableMode.ACROSS_CLASSLOADERS), null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithValidTypeAndHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);

        String mockedString = mockMaker.createMock(settings, mockHandler);
        assertNotNull(mockedString);
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        MockHandler mockHandler = mock(MockHandler.class);
        Class<?> extraInterface = Integer.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class, extraInterface);

        String mockedString = mockMaker.createMock(settings, mockHandler);
        assertNotNull(mockedString);
        assertInstanceOf(Integer.class, mockedString);
    }

    @Test
    public void testGetHandlerWithNullMock() {
        assertNull(mockMaker.getHandler(null));
    }

    @Test
    public void testGetHandlerWithValidMock() {
        Object mock = mock(String.class);
        MockHandler mockHandler = mockMaker.getHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testResetMockWithNullHandler() {
        Object mock = mock(String.class);
        mockMaker.resetMock(mock, null, null);
    }

    @Test
    public void testResetMockWithValidHandler() {
        Object mock = mock(String.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<>(String.class);

        mockMaker.resetMock(mock, newMockHandler, settings);
        assertEquals(newMockHandler, mockMaker.getHandler(mock));
    }
}