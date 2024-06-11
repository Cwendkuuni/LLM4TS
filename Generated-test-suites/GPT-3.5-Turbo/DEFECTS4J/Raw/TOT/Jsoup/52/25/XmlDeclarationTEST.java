import org.jsoup.helper.Validate;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("xml", "https://www.example.com", false);
    }

    @Test
    public void testConstructorAndGetName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithNoAttributes() {
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("xml", wholeDeclaration);
    }
    
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");

        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHeadWithProcessingInstruction() throws IOException {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        xmlDeclaration.outerHtmlHead(accum, 0, out);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        xmlDeclaration.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }
}