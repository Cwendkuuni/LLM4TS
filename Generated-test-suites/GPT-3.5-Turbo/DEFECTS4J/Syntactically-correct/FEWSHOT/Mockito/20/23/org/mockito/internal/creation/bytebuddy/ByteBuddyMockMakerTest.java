package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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
    @Test
    public void testCreateMock() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);

        // Execute
        try {
            Object result = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

            // Verify
            verify(mockCreationSettings, times(1)).getSerializableMode();
            verify(mockCreationSettings, times(1)).getTypeToMock();
            verify(mockCreationSettings, times(1)).getExtraInterfaces();
            verifyNoMoreInteractions(mockCreationSettings);
            verify(mockHandler, times(1)).getClass();
            verifyNoMoreInteractions(mockHandler);
            assertEquals(Object.class, result.getClass());
        } catch (MockitoException e) {
            // Verify
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        } catch (Exception e) {
            // Verify
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        Object mock = new Object();

        // Execute
        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);

        // Verify
        verify(mockCreationSettings, times(1)).getTypeToMock();
        verifyNoMoreInteractions(mockCreationSettings);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testDescribeClass_withClass() {
        // Setup
        Class type = Object.class;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Execute
        String result = byteBuddyMockMaker.describeClass(type);

        // Verify
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_withNull() {
        // Setup
        Class type = null;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Execute
        String result = byteBuddyMockMaker.describeClass(type);

        // Verify
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_withInstance() {
        // Setup
        Object instance = new Object();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Execute
        String result = byteBuddyMockMaker.describeClass(instance);

        // Verify
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandler() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);

        // Execute
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Verify
        verify(mock, times(1)).getClass();
        verifyNoMoreInteractions(mock);
        assertEquals(null, result);
    }

    @Test
    public void testResetMock() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Execute
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Verify
        verify(mock, times(1)).getClass();
        verify(mock, times(1)).setMockitoInterceptor(any());
        verifyNoMoreInteractions(mock);
        verify(settings, times(1)).getTypeToMock();
        verifyNoMoreInteractions(settings);
        verify(newHandler, times(1)).getClass();
        verifyNoMoreInteractions(newHandler);
    }

    @Test
    public void testInitializeClassInstantiator_withObjenesisClassLoader() throws Exception {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        doReturn(true).when(globalConfiguration).enableClassCache();

        // Execute
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Verify
        assertEquals(ClassInstantiator.class, result.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_withoutObjenesisClassLoader() throws Exception {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Execute
        byteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler_withInternalMockHandler() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);

        // Execute
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(internalMockHandler);

        // Verify
        verify(internalMockHandler, times(1)).getClass();
        verifyNoMoreInteractions(internalMockHandler);
        assertEquals(InternalMockHandler.class, result.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_withMockHandler() {
        // Setup
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);

        // Execute
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }
}