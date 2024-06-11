package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("#declaration", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
}