import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_Success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        String mock = mockMaker.createMock(settings, handler);

        assertNotNull(mock);
        assertThat(mock, instanceOf(String.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassLoadersNotSupported() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Integer> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Double> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Long> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Double> settings = mock(MockCreationSettings.class);
        Double originalMock = 10.0;
        Double mock = mockMaker.ensureMockIsAssignableToMockedType(settings, originalMock);

        assertNotNull(mock);
        assertEquals(originalMock, mock);
    }

    @Test
    public void testGetHandler_ValidMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockHandler handler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(mock(MockCreationSettings.class), handler);

        MockHandler retrievedHandler = mockMaker.getHandler(mock);

        assertNotNull(retrievedHandler);
        assertEquals(handler, retrievedHandler);
    }

    @Test
    public void testGetHandler_InvalidMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        Object invalidMock = new Object();
        MockHandler retrievedHandler = mockMaker.getHandler(invalidMock);

        assertNull(retrievedHandler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<Integer> settings = mock(MockCreationSettings.class);
        MockHandler originalHandler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(settings, originalHandler);

        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);

        MockHandler retrievedHandler = mockMaker.getHandler(mock);

        assertNotNull(retrievedHandler);
        assertEquals(newHandler, retrievedHandler);
    }

}