import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class MessageHandlerTest {

    @Mock
    private Trace traceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Mocking the Trace class to avoid actual logging during the tests
        when(Trace.error(anyString())).thenAnswer(invocation -> {
            System.out.println(invocation.getArguments()[0]);
            return null;
        });
    }
    
    @After
    public void tearDown() {
        // Cleanup any shared resources
    }

    @Test
    public void getMessage_WhenKeyExists_ReturnsMessageText() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "existingKey";
        String expectedMessageText = "The message for existingKey";
    
        // Act
        String actualMessageText = messageHandler.getMessage(key, null);
    
        // Assert
        assertEquals(expectedMessageText, actualMessageText);
    }

    @Test
    public void getMessage_WhenKeyDoesNotExist_ReturnsErrorMessageAndLogsError() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "nonExistentKey";
        String expectedErrorMessage = "Key: " + key + " not found in message file";
    
        // Act
        String actualMessageText = messageHandler.getMessage(key, null);
    
        // Assert
        assertEquals(expectedErrorMessage, actualMessageText);
        verify(traceMock, times(1)).error(expectedErrorMessage);
    }

    @Test
    public void getMessageOptional_WhenKeyExists_ReturnsMessageTextWithParamsReplaced() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "existingKeyWithParams";
        String expectedParametrizedMessageText = "Hello World";
        List<String> params = Arrays.asList("Hello", "World");
    
        // Act
        String actualMessageText = messageHandler.getMessageOptional(key, params);
    
        // Assert
        assertEquals(expectedParametrizedMessageText, actualMessageText);
    }

    @Test
    public void getMessageOptional_WhenKeyDoesNotExist_ReturnsNull() {
        // Arrange
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "nonExistentKey";
    
        // Act
        String actualMessageText = messageHandler.getMessageOptional(key, null);
    
        // Assert
        assertNull(actualMessageText);
    }

    @Test
    public void getInstance_WhenInstanceDoesNotExist_CreatesNewInstanceAndReturnsIt() {
        // Arrange
        String basename = "myBasename";
        MessageHandler existingInstance = mock(MessageHandler.class);
        when(MessageHandler.getInstance(basename)).thenReturn(existingInstance);
    
        // Act
        MessageHandler actualInstance = MessageHandler.getInstance(basename);
    
        // Assert
        assertEquals(existingInstance, actualInstance);
        verify(MessageHandler.mInstances, times(1)).put(eq(basename), any(MessageHandler.class));
    }

    @Test
    public void addMessages_WhenMessagesFilesExist_AddsMessagesToMessageHandler() {
        // Arrange
        String basename = "myBasename";
        File dirFileMock = mock(File.class);
        when(dirFileMock.list(any(MessageHandler.MessagesFilter.class))).thenReturn(new String[] {"messages_en_US.properties"});;
        MessageHandler messageHandler = new MessageHandler(basename);
    
        // Act
        // No action required, as the addMessages method is called during object construction
    
        // Assert
        verify(dirFileMock, times(1)).list(any(MessageHandler.MessagesFilter.class));
        verify(messageHandler.mMessages, times(1)).put(any(String.class), any(String.class));
    } 
    
    @Test
    public void addMessages_WhenMessagesFilesDoNotExist_DoesNotAddMessagesToMessageHandler() {
        // Arrange
        String basename = "myBasename";
        File dirFileMock = mock(File.class);
        when(dirFileMock.list(any(MessageHandler.MessagesFilter.class))).thenReturn(null);;
        MessageHandler messageHandler = new MessageHandler(basename);
    
        // Act
        // No action required, as the addMessages method is called during object construction
    
        // Assert
        verify(dirFileMock, times(1)).list(any(MessageHandler.MessagesFilter.class));
        verify(messageHandler.mMessages, times(0)).put(any(String.class), any(String.class));
    }
}