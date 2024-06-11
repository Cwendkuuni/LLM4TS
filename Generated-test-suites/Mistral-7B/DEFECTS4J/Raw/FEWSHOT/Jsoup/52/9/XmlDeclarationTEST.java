package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("xml", null, false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        xmlDeclaration = new XmlDeclaration("xml", null, true);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n<!--\n" +
                "-->\n<body>\n<!--\n-->\n", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Appendable appendable = new StringBuilder();
        xmlDeclaration.outerHtmlHead(appendable, 0, null);
        assertEquals("<?!xml>\n", appendable.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Appendable appendable = new StringBuilder();
        xmlDeclaration.outerHtmlTail(appendable, 0, null);
        assertNull(appendable.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?!xml>", xmlDeclaration.toString());
    }
}