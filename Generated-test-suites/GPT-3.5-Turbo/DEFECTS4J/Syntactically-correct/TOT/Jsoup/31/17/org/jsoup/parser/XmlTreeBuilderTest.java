package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    @Mock
    private Token token;
    @Mock
    private Token.StartTag startTag;
    @Mock
    private Token.EndTag endTag;
    @Mock
    private Token.Comment commentToken;
    @Mock
    private Token.Character characterToken;
    @Mock
    private Token.Doctype doctype;

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = spy(new XmlTreeBuilder());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<body></body>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Verify that stack.add(doc) is called
        verify(xmlTreeBuilder.stack).add(any(Document.class));
    }

    @Test
    public void testProcessStartTag() {
        when(token.type()).thenReturn(Token.TokenType.StartTag);
        when(token.asStartTag()).thenReturn(startTag);
        when(startTag.name()).thenReturn("tag-name");
        when(startTag.attributes()).thenReturn(new Attributes());
        when(startTag.isSelfClosing()).thenReturn(false);
        
        xmlTreeBuilder.process(token);
        
        // Verify that insert method is called
        verify(xmlTreeBuilder).insert(startTag);
        // Verify that stack.add(el) is called
        verify(xmlTreeBuilder.stack).add(any(Element.class));
    }

    @Test
    public void testProcessEndTag() {
        when(token.type()).thenReturn(Token.TokenType.EndTag);
        when(token.asEndTag()).thenReturn(endTag);
        when(endTag.name()).thenReturn("tag-name");
        
        Element element = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(element);

        xmlTreeBuilder.process(token);
        
        // Verify that popStackToClose method is called
        verify(xmlTreeBuilder).popStackToClose(endTag);
    }

    @Test
    public void testProcessComment() {
        when(token.type()).thenReturn(Token.TokenType.Comment);
        when(token.asComment()).thenReturn(commentToken);
        when(commentToken.getData()).thenReturn("comment data");
        
        xmlTreeBuilder.process(token);
        
        // Verify that insert method is called
        verify(xmlTreeBuilder).insert(commentToken);
    }

    @Test
    public void testProcessCharacter() {
        when(token.type()).thenReturn(Token.TokenType.Character);
        when(token.asCharacter()).thenReturn(characterToken);
        when(characterToken.getData()).thenReturn("character data");
        
        xmlTreeBuilder.process(token);
        
        // Verify that insert method is called
        verify(xmlTreeBuilder).insert(characterToken);
    }

    @Test
    public void testProcessDoctype() {
        when(token.type()).thenReturn(Token.TokenType.Doctype);
        when(token.asDoctype()).thenReturn(doctype);
        when(doctype.getName()).thenReturn("doctype name");
        when(doctype.getPublicIdentifier()).thenReturn("public identifier");
        when(doctype.getSystemIdentifier()).thenReturn("system identifier");
        
        xmlTreeBuilder.process(token);
        
        // Verify that insert method is called
        verify(xmlTreeBuilder).insert(doctype);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(endTag.name()).thenReturn("tag-name");

        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);

        when(xmlTreeBuilder.stack.descendingIterator()).thenReturn(
                new Iterator<Element>() {
                    private int count = 0;

                    @Override
                    public boolean hasNext() {
                        return count < 2;
                    }

                    @Override
                    public Element next() {
                        count++;
                        return count == 1 ? element1 : element2;
                    }

                    @Override
                    public void remove() {
                        // Not implemented
                    }
                });

        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that stack.remove(element2) is called
        verify(xmlTreeBuilder.stack).remove(element2);
        // Verify that stack.remove(element1) is not called
        verify(xmlTreeBuilder.stack, never()).remove(element1);
    }
}