package org.jsoup.parser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Mock
    private XmlTreeBuilder xmlTreeBuilder;

    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assert that the document is added to the stack
        Mockito.verify(xmlTreeBuilder.stack).add(Mockito.any(Document.class));
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attributes");
        Element element = new Element(Tag.valueOf(startTag.name()), "", startTag.attributes);

        xmlTreeBuilder.stack = Mockito.mock(Stack.class);

        // Mock insertNode() method
        Mockito.doNothing().when(xmlTreeBuilder).insertNode(Mockito.any(Node.class));

        xmlTreeBuilder.process(startTag);

        // Verify that insertNode() is called with the expected element
        Mockito.verify(xmlTreeBuilder).insertNode(element);

        // Verify that the element is added to the stack if it is not self-closing
        if (!startTag.isSelfClosing()) {
            Mockito.verify(xmlTreeBuilder.stack).add(element);
        }
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack = Mockito.mock(Stack.class);

        // Mock popStackToClose() method
        Mockito.doNothing().when(xmlTreeBuilder).popStackToClose(endTag);

        xmlTreeBuilder.process(endTag);

        // Verify that popStackToClose() is called with the expected end tag
        Mockito.verify(xmlTreeBuilder).popStackToClose(endTag);
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("data");
        Comment comment = new Comment(commentToken.getData(), "");

        // Mock insertNode() method
        Mockito.doNothing().when(xmlTreeBuilder).insertNode(Mockito.any(Node.class));

        xmlTreeBuilder.process(commentToken);

        // Verify that insertNode() is called with the expected comment
        Mockito.verify(xmlTreeBuilder).insertNode(comment);
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("data");
        TextNode textNode = new TextNode(characterToken.getData(), "");

        // Mock insertNode() method
        Mockito.doNothing().when(xmlTreeBuilder).insertNode(Mockito.any(Node.class));

        xmlTreeBuilder.process(characterToken);

        // Verify that insertNode() is called with the expected text node
        Mockito.verify(xmlTreeBuilder).insertNode(textNode);
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        DocumentType doctypeNode = new DocumentType(doctypeToken.getName(), doctypeToken.getPublicIdentifier(), doctypeToken.getSystemIdentifier(), "");

        // Mock insertNode() method
        Mockito.doNothing().when(xmlTreeBuilder).insertNode(Mockito.any(Node.class));

        xmlTreeBuilder.process(doctypeToken);

        // Verify that insertNode() is called with the expected document type node
        Mockito.verify(xmlTreeBuilder).insertNode(doctypeNode);
    }

    @Test
    public void testPopStackToClose_ExistingElement() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf(endTag.name()), "", "");

        // Add existing element to the stack
        xmlTreeBuilder.stack = Mockito.mock(Stack.class);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator()).thenReturn(Mockito.mock(Iterator.class));
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().next()).thenReturn(element);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the element is removed from the stack
        Mockito.verify(xmlTreeBuilder.stack.descendingIterator(), Mockito.times(2)).remove();
    }

    @Test
    public void testPopStackToClose_NonExistingElement() {
        Token.EndTag endTag = new Token.EndTag("tag");

        // Add non-existing element to the stack
        xmlTreeBuilder.stack = Mockito.mock(Stack.class);
        Mockito.when(xmlTreeBuilder.stack.descendingIterator()).thenReturn(Mockito.mock(Iterator.class));
        Mockito.when(xmlTreeBuilder.stack.descendingIterator().hasNext()).thenReturn(false);

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the element is not removed from the stack
        Mockito.verify(xmlTreeBuilder.stack.descendingIterator(), Mockito.never()).remove();
    }

    // TODO: Add more test cases for the remaining methods in XmlTreeBuilder

}