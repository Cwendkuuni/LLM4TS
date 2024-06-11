package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithOptions() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<!xml>", declaration.toString());
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadWithIOException() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        Appendable appendable = new Appendable() {
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }

            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }

            public Appendable append(char c) throws IOException {
                throw new IOException();
            }
        };
        declaration.outerHtmlHead(appendable, 0, new Document.OutputSettings());
    }

}