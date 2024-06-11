
package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {
    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        TestClass mock = mockMaker.createMock(settings, handler);

        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = mock(MockHandler.class);

        try {
            TestClass mock = mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        MockHandler handler = mock(MockHandler.class);
        TestClass mock = mock(TestClass.class, handler);
        MockHandler returnedHandler = mockMaker.getHandler(mock);

        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        MockHandler newHandler = mock(MockHandler.class);
        TestClass mock = mock(TestClass.class);
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        settings.setTypeToMock(TestClass.class);

        try {
            mockMaker.resetMock(mock, newHandler, settings);
        } catch (ClassCastException e) {
            assertEquals("java.lang.ClassCastException: org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker cannot be cast to org.mockito.internal.InternalMockHandler", e.getMessage());
        }
    }
}

class TestClass {
    // test class
}
