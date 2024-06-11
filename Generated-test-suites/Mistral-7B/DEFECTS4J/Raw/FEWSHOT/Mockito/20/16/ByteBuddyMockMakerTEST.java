package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.exceptions.base.MockitoException.classCastError;
import static org.mockito.internal.util.StringJoiner.join;

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

    @Test
    public void testCreateMockWithValidSettingsAndMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mockitoMock -> {};
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(Object.class).build();
        Object mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mockitoMock -> {};
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(Object.class).withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithInvalidMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mockHandler = new Object();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(Object.class).build();
        try {
            mockMaker.createMock(settings, mockHandler);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals(classCastError(Object.class, MockHandler.class, "mockHandler"), e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithMockedTypeNotAssignableToInstance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mockitoMock -> {};
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(String.class).withTypeToMock(Object.class).build();
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :", e.getMessage());
        }
    }

    @Test
    public void testGetHandlerWithValidMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mockitoMock -> {};
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withType(Object.class).build(), handler);
        MockHandler returnedHandler = mockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMockWithNullMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withType(Object.class).build(), mockitoMock -> {});
        mockMaker.resetMock(mock, null, new MockCreationSettings.Builder().build());
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMockWithEmptyMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withType(Object.class).build(), mockitoMock -> {});
        mockMaker.resetMock(mock, new MockHandler() {}, new MockCreationSettings.Builder().build());
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMockWithValidMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler oldHandler = mockitoMock -> {};
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder().withType(Object.class).build(), oldHandler);
        MockHandler newHandler = mockitoMock -> {};
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().build());
        MockHandler returnedHandler = mockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assertEquals(newHandler, returnedHandler);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        new ByteBuddyMockMaker();
    }
}