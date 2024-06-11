import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testCommentConstructorString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentConstructorStringBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test Comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(true);
        try {
            comment.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        assertEquals("<!--Test Comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test Comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(sb, 0, settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("!XML Declaration");
        assertEquals(true, comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("Regular Comment");
        assertEquals(false, comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("!XML Declaration");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertEquals("XML", decl.tagName());
        assertEquals(true, decl.isProcessingInstruction());
        assertEquals(0, decl.attributes().size());
    }

    @Test
    public void testAsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("Regular Comment");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertEquals(null, decl);
    }
}