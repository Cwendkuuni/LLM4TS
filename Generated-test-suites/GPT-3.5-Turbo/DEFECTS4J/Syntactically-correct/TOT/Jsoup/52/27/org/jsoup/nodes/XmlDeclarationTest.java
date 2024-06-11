import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration1;
    private XmlDeclaration xmlDeclaration2;
    private XmlDeclaration xmlDeclaration3;

    @Before
    public void setUp() {
        xmlDeclaration1 = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration2 = new XmlDeclaration("declaration", "http://www.example.com", false);
        xmlDeclaration3 = new XmlDeclaration("other", "http://www.example.com", false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration1.nodeName());
        assertEquals("#declaration", xmlDeclaration2.nodeName());
        assertEquals("#declaration", xmlDeclaration3.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration1.name());
        assertEquals("declaration", xmlDeclaration2.name());
        assertEquals("other", xmlDeclaration3.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("xml version=\"1.0\"", xmlDeclaration1.getWholeDeclaration());
        assertEquals("declaration", xmlDeclaration2.getWholeDeclaration());
        assertEquals("other", xmlDeclaration3.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        Document.OutputSettings outSettings = new Document.OutputSettings();

        try {
            xmlDeclaration1.outerHtmlHead(sb1, 0, outSettings);
            xmlDeclaration2.outerHtmlHead(sb2, 0, outSettings);
            xmlDeclaration3.outerHtmlHead(sb3, 0, outSettings);
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

        assertEquals("<?xml version=\"1.0\">", sb1.toString());
        assertEquals("<declaration>", sb2.toString());
        assertEquals("<?other>", sb3.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?xml version=\"1.0\">", xmlDeclaration1.toString());
        assertEquals("<declaration>", xmlDeclaration2.toString());
        assertEquals("<?other>", xmlDeclaration3.toString());
    }
}