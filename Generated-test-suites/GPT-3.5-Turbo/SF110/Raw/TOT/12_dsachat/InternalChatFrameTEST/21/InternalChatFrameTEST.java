import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class InternalChatFrameTest {
   
     private InternalChatFrame internalChatFrame;
     @Mock
     private MainFrame parent;

     @Before
     public void setUp() {
         internalChatFrame = new InternalChatFrame(parent);
     }

     @Test
     public void testAddText_regularLine_shouldAddLineToTextPane() {
         internalChatFrame.addText("Regular line");
         assertEquals("Regular line\n", internalChatFrame.getChatlog());
     }

     @Test
     public void testAddText_systemMessage_shouldAddLineToTextPaneWithSystemStyle() {
         internalChatFrame.addText("System message");
         assertEquals("System message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to the system style
     }

     @Test
     public void testAddText_whisperMessage_shouldAddLineToTextPaneWithWhisperStyle() {
         internalChatFrame.addText("Whisper message");
         assertEquals("Whisper message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to the whisper style
     }

     @Test
     public void testAddText_boldAndItalicMessage_shouldAddLineToTextPaneWithBoldAndItalicStyle() {
         internalChatFrame.addText("Bold *and* italic message");
         assertEquals("Bold and italic message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to bold and italic style
     }

     @Test
     public void testAddText_boldMessage_shouldAddLineToTextPaneWithBoldStyle() {
         internalChatFrame.addText("Bold *message*");
         assertEquals("Bold message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to bold style
     }

     @Test
     public void testAddText_italicMessage_shouldAddLineToTextPaneWithItalicStyle() {
         internalChatFrame.addText("Italic _message_");
         assertEquals("Italic message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to italic style
     }

     @Test
     public void testAddText_userMessage_shouldAddLineToTextPaneWithNameStyle() {
         internalChatFrame.addText("User message");
         assertEquals("User message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to name style
     }

     @Test
     public void testAddText_userMessageStartingWithYou_shouldAddLineToTextPaneWithNameOfParentHero() {
         internalChatFrame.addText("You message");
         assertEquals("ParentHeroName message\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to the name style of the parent hero
     }

     @Test
     public void testAddText_messageWithColorChange_shouldAddLineToTextPaneWithColorChangeStyle() {
         internalChatFrame.addText("Message with color change");
         assertEquals("Message with color change\n", internalChatFrame.getChatlog());
         //TODO: Verify that the style of the added text is set to the color change style
     }
   
     // Add more test cases for each method in InternalChatFrame class

}