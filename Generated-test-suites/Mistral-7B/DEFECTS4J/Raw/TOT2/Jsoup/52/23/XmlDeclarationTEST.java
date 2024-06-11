package org.jsoup.nodes.test;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("#declaration", "", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.getWholeDeclaration());

        XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", "", false, "", "1.0", "UTF-8");
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclarationWithVersion.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        new XmlDeclaration(null, "", false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new XmlDeclaration("", null, false);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIsProcessingInstruction() {
        new XmlDeclaration("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new XmlDeclaration("", "", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false, "", "", "");
        assertThrows(IllegalArgumentException.class, () -> xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testEmptyAttributeList() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        assertEquals("<declaration></declaration>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testIncorrectNumberOfAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false, "", "1.0", "UTF-8", "encoding", "ISO-8859-1");
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\" encoding=\"ISO-8859-1\"></declaration>", xmlDeclaration.getWholeDeclaration());
    }
}