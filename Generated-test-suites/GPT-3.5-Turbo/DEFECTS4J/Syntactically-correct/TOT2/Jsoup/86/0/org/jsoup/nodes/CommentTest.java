package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("Test <?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("Test <!-- comment -->");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("Test <?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();

        assertNotNull(declaration);
        assertEquals("xml", declaration.getDeclarationTag());
    }
}