package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithBothVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
    }
}