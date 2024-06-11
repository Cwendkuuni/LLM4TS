package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.junit.Before;
import org.junit.Test;


public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        mockHandler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(String.class);
        settings.setSerializableMode(SerializableMode.STANDARD);
        Object mock = mockMaker.createMock(settings, mockHandler);

        // Verify that the createMock method returns the expected mock instance
        assertEquals(mock, mockMaker.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(String.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        mockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(String.class);
        String mock = mockMaker.ensureMockIsAssignableToMockedType(settings, "mock");

        // Verify that the ensureMockIsAssignableToMockedType method returns the mock with the expected type
        assertEquals(String.class, mock.getClass());
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        when(mockMaker.getHandler(mock)).thenReturn(mockHandler);

        // Verify that the getHandler method returns the expected mock handler
        assertEquals(mockHandler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        settings.setTypeToMock(String.class);

        // Call the resetMock method
        mockMaker.resetMock(mock, mockHandler, settings);

        // Verify that the mock's mockitoInterceptor is set to a new MockMethodInterceptor with the provided mock handler and settings
        verify(mock, times(1)).getMockitoInterceptor().getMockHandler();
        verify(mock, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

}