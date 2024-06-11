
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", xmlDeclaration.getWholeDeclaration());
    }

    // Test for edge case - name is "xml" and attributes size is greater than 1
    @Test
    public void testGetWholeDeclarationWithXml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("<!name>", xmlDeclaration.toString());
    }

    // Test for exception - append throws IOException
    @Test(expected = IOException.class)
    public void testOuterHtmlHeadIOException() throws Exception {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        xmlDeclaration.outerHtmlHead(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException();
            }
        }, 1, new Document.OutputSettings());
    }

    // Test for exception - append throws IOException
    @Test(expected = IOException.class)
    public void testOuterHtmlTailIOException() throws Exception {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", true);
        xmlDeclaration.outerHtmlTail(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException();
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException();
            }
        }, 1, new Document.OutputSettings());
    }
}

