package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeSet;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testProcess_StartTag() {
        Token startTagToken = new StartTag("elem", null, new AttributeSet(new Attribute("id", "1")));
        Element expectedElement = new Element("elem", null, new Attribute("id", "1"));

        builder.process(startTagToken);
        Element actualElement = builder.currentElement();

        assertEquals(expectedElement.tagName(), actualElement.tagName());
        assertEquals(expectedElement.baseUri(), actualElement.baseUri());
        assertEquals(expectedElement.attributes().size(), actualElement.attributes().size());
        assertSame(expectedElement.attributes().get("id"), actualElement.attributes().get("id"));
    }

    @Test
    public void testProcess_StartTag_SelfClosing() {
        Token startTagToken = new StartTag("elem", null, new AttributeSet(), true);

        builder.process(startTagToken);
        Element actualElement = builder.currentElement();

        assertNull(actualElement);
    }

    @Test
    public void testProcess_EndTag() {
        Token startTagToken = new StartTag("elem", null, null);
        Token endTagToken = new EndTag("elem");

        builder.process(startTagToken);
        builder.process(endTagToken);

        assertNull(builder.currentElement());
    }

    @Test
    public void testProcess_Comment() {
        Token commentToken = new CommentToken("This is a comment");

        builder.process(commentToken);
        Comment actualComment = builder.currentElement();

        assertEquals("This is a comment", actualComment.data());
        assertNull(actualComment.tagName());
        assertNull(actualComment.baseUri());
    }

    @Test
    public void testProcess_Character() {
        Token characterToken = new CharacterToken(' ', " ");

        builder.process(characterToken);
        TextNode actualTextNode = (TextNode) builder.currentElement();

        assertEquals(" ", actualTextNode.text());
        assertEquals(builder.currentElement().tagName(), actualTextNode.parent().tagName());
    }

    @Test
    public void testProcess_Doctype() {
        Token doctypeToken = new Doctype("html", "", "", null);

        builder.process(doctypeToken);
        DocumentType actualDocumentType = (DocumentType) builder.currentElement();

        assertEquals("html", actualDocumentType.name());
        assertNull(actualDocumentType.publicIdentifier());
        assertNull(actualDocumentType.systemIdentifier());
        assertNull(actualDocumentType.baseUri());
    }

    @Test
    public void testProcess_EOF() {
        // No action required, but we can check if the builder is in the correct state after processing all tokens.
        Token eofToken = new Token(Token.Type.EOF);

        builder.process(eofToken);

        assertEquals(Document.class, builder.currentElement().getClass());
    }

    @Test(expected = AssertionError.class)
    public void testProcess_UnexpectedTokenType() {
        Token unexpectedToken = new Token(Token.Type.CDSEction);

        builder.process(unexpectedToken);
    }

    @Test
    public void testPopStackToClose() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        builder.initialiseParse(null, null, null);
        builder.stack.add(doc);
        builder.stack.add(root);

        Element elem1 = new Element("elem1");
        builder.insert(new StartTag("elem1"));
        builder.stack.add(elem1);

        Element elem2 = new Element("elem2");
        builder.insert(new StartTag("elem2"));
        builder.stack.add(elem2);

        Element elem3 = new Element("elem3");
        builder.insert(new StartTag("elem3"));
        builder.stack.add(elem3);

        Element expectedElementsPopped[] = {elem3, elem2, elem1, root, doc};

        for (Token endTag : new Token[] { new EndTag("elem3"), new EndTag("elem2"), new EndTag("elem1") }) {
            builder.process(endTag);
            Iterator<Element> it = builder.stack.descendingIterator();
            Element poppedElement = it.next();
            it.remove();
            assertSame(poppedElement, expectedElementsPopped[0]);
            assertNull(builder.currentElement());
            expectedElementsPopped = Arrays.copyOfRange(expectedElementsPopped, 1, expectedElementsPopped.length);
        }

        assertNull(builder.stack.hasNext());
    }
}