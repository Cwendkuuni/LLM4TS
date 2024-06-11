import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test");
    }

    @Test
    public void getMessage_validKeyAndParams_returnFormattedMessage() {
        List<String> params = new ArrayList<>();
        params.add("John");
        String expected = "Hello John";
        String result = messageHandler.getMessage("greeting", params);
        assert(result.equals(expected));
    }

    @Test
    public void getMessage_invalidKey_returnErrorMessage() {
        String expected = "Key: invalidKey not found in message file";
        String result = messageHandler.getMessage("invalidKey", null);
        assert(result.equals(expected));
    }

    @Test
    public void getMessageOptional_validKeyAndParams_returnFormattedMessage() {
        List<String> params = new ArrayList<>();
        params.add("Jane");
        String expected = "Hello Jane";
        String result = messageHandler.getMessageOptional("greeting", params);
        assert(result.equals(expected));
    }

    @Test
    public void getMessageOptional_invalidKey_returnNull() {
        String result = messageHandler.getMessageOptional("invalidKey", null);
        assert(result == null);
    }

    @Test
    public void getInstance_newBasename_createNewInstance() {
        String basename = "testInstance";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assert(instance != null);
    }

    @Test
    public void getInstance_existingBasename_returnExistingInstance() {
        String basename = "testInstance";
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        assert(instance1 == instance2);
    }

    @Test
    public void addMessages_validBasenameAndDir_addMessagesToHashtable() {
        String basename = "test";
        String dir = "testDir";
        messageHandler.addMessages(basename, dir);
        assert(messageHandler.getMessages().size() > 0);
    }

    @Test
    public void addMessages_invalidBasenameAndDir_noMessagesAdded() {
        String basename = "invalid";
        String dir = "invalidDir";
        messageHandler.addMessages(basename, dir);
        assert(messageHandler.getMessages().size() == 0);
    }
}