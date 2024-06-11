import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageHandlerTest {

    @Mock
    private Trace traceMock;

    @InjectMocks
    private MessageHandler messageHandler;

    @Before
    public void setup() {
        mockStatic(Trace.class);
        when(Trace.error(anyString())).thenReturn();
    }

    @Test
    public void testGetMessage_WithValidKeyAndParams_CallsGetMessageOptionalAndReturnsMessageText() {
        // Arrange
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("Key: validKey not found in message file", result);
        verify(traceMock).error("Key: validKey not found in message file");
    }

    @Test
    public void testGetMessage_WithValidKeyAndNullParams_CallsGetMessageOptionalAndReturnsMessageText() {
        // Arrange
        String key = "validKey";
        List<String> params = null;

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("Key: validKey not found in message file", result);
        verify(traceMock).error("Key: validKey not found in message file");
    }

    @Test
    public void testGetMessage_WithInvalidKeyAndValidParams_CallsGetMessageOptionalAndReturnsErrorMessageText() {
        // Arrange
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("Key: invalidKey not found in message file", result);
        verify(traceMock).error("Key: invalidKey not found in message file");
    }

    @Test
    public void testGetMessage_WithInvalidKeyAndNullParams_CallsGetMessageOptionalAndReturnsErrorMessageText() {
        // Arrange
        String key = "invalidKey";
        List<String> params = null;

        // Act
        String result = messageHandler.getMessage(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("Key: invalidKey not found in message file", result);
        verify(traceMock).error("Key: invalidKey not found in message file");
    }

    @Test
    public void testGetMessageOptional_WithValidKeyAndParams_ReturnsMessageTextWithSubstitutedParams() {
        // Arrange
        String key = "validKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("This is a valid message with param1", result);
    }

    @Test
    public void testGetMessageOptional_WithValidKeyAndNullParams_ReturnsMessageTextWithoutSubstitutingParams() {
        // Arrange
        String key = "validKey";
        List<String> params = null;

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNotNull(result);
        assertEquals("This is a valid message with $1$", result);
    }

    @Test
    public void testGetMessageOptional_WithInvalidKeyAndValidParams_ReturnsNull() {
        // Arrange
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_WithInvalidKeyAndNullParams_ReturnsNull() {
        // Arrange
        String key = "invalidKey";
        List<String> params = null;

        // Act
        String result = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetInstance_WithNewBasname_ReturnsNewInstance() {
        // Arrange
        String basename = "newBasename";
        MessageHandler instanceMock = mock(MessageHandler.class);
        when(MessageHandler.mInstances.get(basename)).thenReturn(null);
        whenNew(MessageHandler.class).withArguments(basename).thenReturn(instanceMock);

        // Act
        MessageHandler result = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(result);
        assertEquals(instanceMock, result);
        verify(MessageHandler.mInstances).put(basename, instanceMock);
    }

    @Test
    public void testGetInstance_WithExistingBasname_ReturnsExistingInstance() {
        // Arrange
        String basename = "existingBasename";
        MessageHandler instanceMock = mock(MessageHandler.class);
        when(MessageHandler.mInstances.get(basename)).thenReturn(instanceMock);

        // Act
        MessageHandler result = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(result);
        assertEquals(instanceMock, result);
        verifyZeroInteractions(MessageHandler.mInstances);
    }

    // Additional test cases can be added as per requirements

}