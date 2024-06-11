
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.*;

@RunWith(Mockito.class)
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    // Test case for initialiseParse method
    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Check if the document is correctly placed on the stack
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        // Check if the stack size is 1 after initialising parse
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    // Test case for process method - StartTag
    @Test
    public void testProcess_StartTag() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.type).thenReturn(Token.TokenType.StartTag);
        Token.StartTag startTag = Mockito.mock(Token.StartTag.class);
        Mockito.when(token.asStartTag()).thenReturn(startTag);

        Tag tag = Mockito.mock(Tag.class);
        Mockito.when(startTag.name()).thenReturn("div");
        Mockito.when(Tag.valueOf(startTag.name())).thenReturn(tag);

        Element element = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.currentElement()).thenReturn(element);

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        // Verify that the element is inserted and returned correctly
        Mockito.verify(xmlTreeBuilder).insert(startTag);
        assertEquals(element, insertedElement);
    }

    // Test case for process method - EndTag
    @Test
    public void testProcess_EndTag() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.type).thenReturn(Token.TokenType.EndTag);
        Token.EndTag endTag = Mockito.mock(Token.EndTag.class);
        Mockito.when(token.asEndTag()).thenReturn(endTag);

        Element element = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.currentElement()).thenReturn(element);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the stack is correctly updated
        Mockito.verify(xmlTreeBuilder).popStackToClose(endTag);
        Mockito.verify(xmlTreeBuilder.stack).remove(element);
    }

    // Test case for process method - Comment
    @Test
    public void testProcess_Comment() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.type).thenReturn(Token.TokenType.Comment);
        Token.Comment comment = Mockito.mock(Token.Comment.class);
        Mockito.when(token.asComment()).thenReturn(comment);

        xmlTreeBuilder.insert(comment);

        // Verify that the comment node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(comment);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(Comment.class));
    }

    // Test case for process method - Character
    @Test
    public void testProcess_Character() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.type).thenReturn(Token.TokenType.Character);
        Token.Character character = Mockito.mock(Token.Character.class);
        Mockito.when(token.asCharacter()).thenReturn(character);

        xmlTreeBuilder.insert(character);

        // Verify that the text node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(character);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(TextNode.class));
    }

    // Test case for process method - Doctype
    @Test
    public void testProcess_Doctype() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.type).thenReturn(Token.TokenType.Doctype);
        Token.Doctype doctype = Mockito.mock(Token.Doctype.class);
        Mockito.when(token.asDoctype()).thenReturn(doctype);

        xmlTreeBuilder.insert(doctype);

        // Verify that the document type node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(doctype);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(DocumentType.class));
    }

    // Test case for insert method - StartTag with self-closing flag
    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = Mockito.mock(Token.StartTag.class);
        Mockito.when(startTag.name()).thenReturn("div");
        Mockito.when(startTag.isSelfClosing()).thenReturn(true);

        Tag tag = Mockito.mock(Tag.class);
        Mockito.when(Tag.valueOf(startTag.name())).thenReturn(tag);

        Element element = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.currentElement()).thenReturn(element);

        xmlTreeBuilder.insert(startTag);

        // Verify that the element is correctly inserted and self-closing flag is acknowledged
        Mockito.verify(xmlTreeBuilder).insert(startTag);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(Element.class));
        Mockito.verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
    }

    // Test case for insert method - StartTag without self-closing flag
    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = Mockito.mock(Token.StartTag.class);
        Mockito.when(startTag.name()).thenReturn("div");
        Mockito.when(startTag.isSelfClosing()).thenReturn(false);

        Tag tag = Mockito.mock(Tag.class);
        Mockito.when(Tag.valueOf(startTag.name())).thenReturn(tag);

        Element element = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.currentElement()).thenReturn(element);

        xmlTreeBuilder.insert(startTag);

        // Verify that the element is correctly inserted and added to the stack
        Mockito.verify(xmlTreeBuilder).insert(startTag);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(Element.class));
        Mockito.verify(xmlTreeBuilder.stack).add(element);
    }

    // Test case for insert method - Comment
    @Test
    public void testInsert_Comment() {
        Token.Comment comment = Mockito.mock(Token.Comment.class);
        Mockito.when(comment.getData()).thenReturn("This is a comment");

        xmlTreeBuilder.insert(comment);

        // Verify that the comment node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(comment);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(Comment.class));
    }

    // Test case for insert method - Character
    @Test
    public void testInsert_Character() {
        Token.Character character = Mockito.mock(Token.Character.class);
        Mockito.when(character.getData()).thenReturn("This is a character");

        xmlTreeBuilder.insert(character);

        // Verify that the text node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(character);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(TextNode.class));
    }

    // Test case for insert method - Doctype
    @Test
    public void testInsert_Doctypte() {
        Token.Doctype doctype = Mockito.mock(Token.Doctype.class);
        Mockito.when(doctype.getName()).thenReturn("html");
        Mockito.when(doctype.getPublicIdentifier()).thenReturn("-//W3C//DTD XHTML 1.0 Strict//EN");

        xmlTreeBuilder.insert(doctype);

        // Verify that the document type node is correctly inserted
        Mockito.verify(xmlTreeBuilder).insert(doctype);
        Mockito.verify(xmlTreeBuilder).insertNode(Mockito.any(DocumentType.class));
    }

    // Test case for popStackToClose method - Element found on stack
    @Test
    public void testPopStackToClose_ElementFound() {
        Token.EndTag endTag = Mockito.mock(Token.EndTag.class);
        Mockito.when(endTag.name()).thenReturn("div");

        Element element = Mockito.mock(Element.class);
        Mockito.when(element.nodeName()).thenReturn("div");

        Element nextElement = Mockito.mock(Element.class);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().hasNext()).thenReturn(true);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().next()).thenReturn(nextElement);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the first occurrence of the element is removed from the stack
        Mockito.verify(xmlTreeBuilder).popStackToClose(endTag);
        Mockito.verify(xmlTreeBuilder.stack, Mockito.times(2)).descendingIterator();
        Mockito.verify(xmlTreeBuilder.stack).remove(nextElement);
    }

    // Test case for popStackToClose method - Element not found on stack
    @Test
    public void testPopStackToClose_ElementNotFound() {
        Token.EndTag endTag = Mockito.mock(Token.EndTag.class);
        Mockito.when(endTag.name()).thenReturn("div");

        Element element = Mockito.mock(Element.class);
        Mockito.when(element.nodeName()).thenReturn("p");

        Mockito.when(xmlTreeBuilder.stack.descendingIterator().hasNext()).thenReturn(true);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().next()).thenReturn(element);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that no element is removed from the stack
        Mockito.verify(xmlTreeBuilder).popStackToClose(endTag);
        Mockito.verify(xmlTreeBuilder.stack, Mockito.times(1)).descendingIterator();
        Mockito.verify(xmlTreeBuilder.stack, Mockito.never()).remove(element);
    }
}
