package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;
    private Tokenizer tokeniser;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        tokeniser = new Tokenizer();
        tokeniser.reset();
    }

    @Test
    public void testProcessStartTag() {
        tokeniser.next(); // consume EOF
        Token startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertEquals("root", root.nodeName());
        assertNotNull(root.baseUri());
        assertNull(root.attributes());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        tokeniser.next(); // consume EOF
        Token startTag = new Token.StartTag("root", new Attribute("id", "123"));
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertEquals("root", root.nodeName());
        assertNotNull(root.baseUri());
        assertEquals("123", root.attributes().get("id").getValue());
    }

    @Test
    public void testProcessStartTagSelfClosing() {
        tokeniser.next(); // consume EOF
        Token startTag = new Token.StartTag("img", null, true);
        builder.process(startTag);
        Element img = (Element) builder.currentData();
        assertEquals("img", img.nodeName());
        assertNotNull(img.baseUri());
        assertNull(img.attributes());
        assertTrue(img.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        tokeniser.reset();

        // create root element
        tokeniser.next(); // EOF
        Token startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertNotNull(root);
        assertNull(builder.currentData());

        // create child element
        tokeniser.next(); // '<'
        tokeniser.next(); // 'c'
        tokeniser.next(); // 'h'
        tokeniser.next(); // 'i'
        tokeniser.next(); // 'l'
        tokeniser.next(); // 'd'
        tokeniser.next(); // '>'
        Token startChildTag = new Token.StartTag("child", null);
        builder.process(startChildTag);
        Element child = (Element) builder.currentData();
        assertNotNull(child);
        assertNotNull(builder.currentData());

        // create end tag
        tokeniser.next(); // '<'
        tokeniser.next(); // '/'
        tokeniser.next(); // 'c'
        tokeniser.next(); // 'h'
        tokeniser.next(); // 'i'
        tokeniser.next(); // 'l'
        tokeniser.next(); // 'd'
        tokeniser.next(); // '>'
        Token endTag = new Token.EndTag("child");
        builder.process(endTag);
        Element parent = (Element) builder.currentData();
        assertNotNull(parent);
        assertNull(builder.currentData());

        // check that child has been removed from stack
        Iterator<Element> it = builder.stack.descendingIterator();
        Element firstFound = null;
        while (it.hasNext()) {
            Element next = it.next();
            if (next.nodeName().equals("root")) {
                firstFound = next;
                break;
            }
        }
        assertNotNull(firstFound);
        it = builder.stack.descendingIterator();
        Element childFound = null;
        while (it.hasNext()) {
            Element next = it.next();
            if (next.nodeName().equals("child")) {
                childFound = next;
                break;
            }
        }
        assertNull(childFound);
    }

    @Test
    public void testProcessComment() {
        tokeniser.reset();

        // create root element
        tokeniser.next(); // EOF
        Token startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertNotNull(root);
        assertNull(builder.currentData());

        // create comment
        tokeniser.next(); // '<'
        tokeniser.next(); // '!'
        tokeniser.next(); // 'c'
        tokeniser.next(); // 'd'
        tokeniser.next(); // 'a'
        tokeniser.next(); // 't'
        tokeniser.next(); // 'a'
        tokeniser.next(); // ':'
        tokeniser.next(); // '{'
        String commentText = "This is a comment.";
        for (char c : commentText.toCharArray()) {
            tokeniser.next();
            Token characterToken = new Token.Character(c);
            builder.process(characterToken);
        }
        tokeniser.next(); // '}'
        Token commentToken = new Token.Comment(commentText);
        builder.process(commentToken);

        // check that comment has been inserted
        Iterator<Node> it = root.childNodes().iterator();
        Comment comment = null;
        while (it.hasNext()) {
            Node next = it.next();
            if (next instanceof Comment) {
                comment = (Comment) next;
                break;
            }
        }
        assertNotNull(comment);
        assertEquals(commentText, comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        tokeniser.reset();

        // create root element
        tokeniser.next(); // EOF
        Token startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertNotNull(root);
        assertNull(builder.currentData());

        // create text node
        tokeniser.next(); // '<'
        tokeniser.next(); // '!'
        tokeniser.next(); // 'c'
        tokeniser.next(); // 'd'
        tokeniser.next(); // 'a'
        tokeniser.next(); // 't'
        tokeniser.next(); // 'a'
        tokeniser.next(); // ':'
        tokeniser.next(); // '{'
        String text = "This is the text.";
        for (char c : text.toCharArray()) {
            tokeniser.next();
            Token characterToken = new Token.Character(c);
            builder.process(characterToken);
        }
        tokeniser.next(); // '}'
        Token characterToken = new Token.Character('\n');
        builder.process(characterToken);
        Text textNode = (Text) builder.currentData();
        assertNotNull(textNode);
        assertEquals(text, textNode.getData());
    }

    @Test
    public void testProcessDoctype() {
        tokeniser.reset();

        // create root element
        tokeniser.next(); // EOF
        Token startTag = new Token.StartTag("root", null);
        builder.process(startTag);
        Element root = (Element) builder.currentData();
        assertNotNull(root);
        assertNull(builder.currentData());

        // create doctype
        tokeniser.next(); // '<'
        tokeniser.next(); // '!'
        tokeniser.next(); // 'd'
        tokeniser.next(); // 'o'
        tokeniser.next(); // 'c'
        tokeniser.next(); // 't'
        tokeniser.next(); // 'y'
        tokeniser.next(); // 'p'
        tokeniser.next(); // 'e'
        tokeniser.next(); // '>'
        String publicId = "public";
        tokeniser.next(); // '<'
        tokeniser.next(); // '! '
        tokeniser.next(); // 'p'
        tokeniser.next(); // 'u'
        tokeniser.next(); // 'b'
        tokeniser.next(); // 'l'
        tokeniser.next(); // 'i'
        tokeniser.next(); // 'c'
        tokeniser.next(); // ':'
        String systemId = "system";
        tokeniser.next(); // '>'
        tokeniser.next(); // '>'
        Token doctypeToken = new Token.Doctype(publicId, systemId);
        builder.process(doctypeToken);
        DocumentType doctype = (DocumentType) builder.currentData();
        assertNotNull(doctype);
        assertEquals(publicId, doctype.name());
        assertEquals(publicId, doctype.publicIdentifier());
        assertEquals(systemId, doctype.systemIdentifier());
    }
}