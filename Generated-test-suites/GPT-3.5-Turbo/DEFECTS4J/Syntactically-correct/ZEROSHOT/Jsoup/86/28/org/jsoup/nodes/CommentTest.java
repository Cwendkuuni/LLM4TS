import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals(xmlDeclarationComment.baseUri(), xmlDeclaration.baseUri());

        Document doc = Jsoup.parse("<!doctype html>", xmlDeclarationComment.baseUri(), Parser.xmlParser());
        Element el = doc.child(0);
        XmlDeclaration expectedDeclaration = new XmlDeclaration(NodeUtils.parser(doc).settings().normalizeTag(el.tagName()), true);
        expectedDeclaration.attributes().addAll(el.attributes());

        assertEquals(expectedDeclaration.getNodeName(), xmlDeclaration.getNodeName());
        assertEquals(expectedDeclaration.getAttributes().size(), xmlDeclaration.getAttributes().size());
        assertEquals(expectedDeclaration.getAttributes().toString(), xmlDeclaration.getAttributes().toString());
    }
}