package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        String nodeName = xmlDeclaration.nodeName();
        assertEquals("#declaration", nodeName);
    }

    // Add test cases for other methods here
}