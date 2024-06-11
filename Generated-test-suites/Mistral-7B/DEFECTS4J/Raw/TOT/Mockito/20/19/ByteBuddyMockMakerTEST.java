package org.mockito.internal.creation.bytebuddy; // or the package where the ByteBuddyMockMaker class is located

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InternalMockHandler;
import org.mockito.MockCreationSettings;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakeracker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockMaker mockMaker;
    private InternalMockHandler internalMockHandler;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockMaker = Mockito.mock(MockMaker.class);
        internalMockHandler = mock(InternalMockHandler.class);
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().withTypeToMock(String.class).build();

        // When
        String mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);

        // Then
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        verify(internalMockHandler).bindTo(handler);
        verify(internalMockHandler).uniqueMock(eq(String.class));
        verifyNoInteractions(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidSerializableMode() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().withTypeToMock(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();

        // When & Then
        byteBuddyMockMaker.createMock(settings, internalMockHandler);
    }

    @Test
    public void testGetHandler() {
        // Given
        String mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build(), internalMockHandler);

        // When
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        // Then
        assert (handler != null);
        assertEquals(internalMockHandler, handler);
    }

    @Test
    public void testResetMock() {
        // Given
        String mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().withTypeToMock(String.class).build(), internalMockHandler);
        ArgumentCaptor<MockCreationSettings> settingsCaptor = ArgumentCaptor.forClass(MockCreationSettings.class);
        ArgumentCaptor<MockHandler> newHandlerCaptor = ArgumentCaptor.forClass(MockHandler.class);

        // When
        byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), new MockCreationSettings.Builder().build());

        // Then
        verify(internalMockHandler).reset(settingsCaptor.capture(), newHandlerCaptor.capture());
        assertEquals(internalMockHandler, newHandlerCaptor.getValue().getMockitoInterceptor().getMockHandler());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorFailure() {
        // Given
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // When
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}