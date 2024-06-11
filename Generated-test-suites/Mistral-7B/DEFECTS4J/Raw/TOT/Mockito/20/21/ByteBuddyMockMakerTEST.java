package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockitoHelper;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.join;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;
    private MockMakerMockitoHelper mockitoHelper;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        mockitoHelper = new MockMakerMockitoHelper(mockMaker);
    }

    @Test
    public void testCreateMockSuccess() {
        // Given
        Class<?> typeToMock = String.class;
        MockHandler mockHandler = arg -> {};
        MockCreationSettings settings = new MockCreationSettings.Builder().withTypeToMock(typeToMock).build();

        // When
        Object mock = mockMaker.createMock(settings, mockHandler);

        // Then
        assertNotNull(mock);
        MockHandler returnedMockHandler = mockitoHelper.verify(mockHandler, times(1)).handle(any());
        assertEquals(mockHandler, returnedMockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassLoaders() {
        // Given
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .withTypeToMock(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        // When & Then
        mockMaker.createMock(settings, mock(MockHandler.class));
    }

    @Test
    public void testGetHandler() {
        // Given
        Object mock = mock(String.class);

        // When
        MockHandler handler = mockMaker.getHandler(mock);

        // Then
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        // Given
        Object mock = mock(String.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .withTypeToMock(String.class)
                .build();

        // When
        mockMaker.resetMock(mock, newHandler, settings);

        // Then
        MockHandler returnedHandler = mockitoHelper.verify(newHandler, times(1)).handle(any());
        assertEquals(newHandler, returnedHandler);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorFailure() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // When & Then
        mockMaker.initializeClassInstantiator();
    }
}