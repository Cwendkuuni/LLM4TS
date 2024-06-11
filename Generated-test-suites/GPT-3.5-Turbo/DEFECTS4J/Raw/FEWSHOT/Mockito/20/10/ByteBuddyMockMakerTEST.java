package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings settings;
    private MockHandler handler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        settings = mock(MockCreationSettings.class);
        handler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock_SerializableModeAcrossClassloaders_ThrowsException() {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
            return;
        }
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testCreateMock_ClassCastExceptionThrown() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(ClassCastException.class);
        mockMaker.classInstantiator = classInstantiator;

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :\n" +
                    "  class to mock : null\n" +
                    "  created class : null\n" +
                    "  proxy instance class : null\n" +
                    "  instance creation by : null\n" +
                    "\n" +
                    "You might experience classloading issues, please ask the mockito mailing-list.\n" +
                    "", e.getMessage());
            return;
        }
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testCreateMock_InstantiationExceptionThrown() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any(Class.class))).thenThrow(InstantiationException.class);
        mockMaker.classInstantiator = classInstantiator;

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'null'", e.getMessage());
            return;
        }
        fail("Expected MockitoException to be thrown");
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        assertEquals("mockedType", mockMaker.ensureMockIsAssignableToMockedType(settings, "mockedType"));
    }

    @Test
    public void testDescribeClass() {
        assertEquals("'java.lang.String', loaded by classloader : 'java.net.URLClassLoader'", mockMaker.describeClass(String.class));
        assertEquals("null", mockMaker.describeClass(null));
    }

    @Test
    public void testGetHandler_InvalidMockInstance_ReturnsNull() {
        assertNull(mockMaker.getHandler("invalidMockInstance"));
    }

    @Test
    public void testGetHandler_ValidMockInstance_ReturnsMockHandler() {
        InternalMockHandler mockitoInterceptor = mock(InternalMockHandler.class);
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        when(((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor()).thenReturn(mockitoInterceptor);

        assertEquals(mockitoInterceptor, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        InternalMockHandler mockitoInterceptor = mock(InternalMockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mock(MockMethodInterceptor.MockAccess.class);

        mockMaker.resetMock(mock, mockitoInterceptor, settings);

        verify(((MockMethodInterceptor.MockAccess) mock)).setMockitoInterceptor(mockitoInterceptor);
    }
}