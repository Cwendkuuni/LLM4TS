import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;
    private Map<String, String> attributes;

    @Before
    public void setup() {
        attributes = new HashMap<>();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        xmlDeclaration.attributes = attributes;
    }

    @Test
    public void testConstructor() {
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("http://example.com", xmlDeclaration.baseUri());
        assertEquals(false, xmlDeclaration.isProcessingInstruction);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
    }
}