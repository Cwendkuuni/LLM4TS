import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test");
        assertEquals("Test\n", chatFrame.getChatlog());
    }
    
    // Add more test cases for other public methods
    
}

