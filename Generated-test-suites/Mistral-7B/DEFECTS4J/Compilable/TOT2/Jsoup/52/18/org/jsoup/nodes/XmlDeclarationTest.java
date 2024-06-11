package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.name());
    }
}