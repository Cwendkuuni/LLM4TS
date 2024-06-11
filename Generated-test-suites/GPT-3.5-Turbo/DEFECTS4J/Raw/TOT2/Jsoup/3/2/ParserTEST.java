
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseComment();
        Document doc = parser.doc;
        Element element = doc.body();

        assertEquals(1, element.childNodeSize());
        assertTrue(element.childNode(0) instanceof Comment);
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseXmlDecl();
        Document doc = parser.doc;
        Element element = doc.body();

        assertEquals(1, element.childNodeSize());
        assertTrue(element.childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseTextNode();
        Document doc = parser.doc;
        Element element = doc.body();

        assertEquals(1, element.childNodeSize());
        assertTrue(element.childNode(0) instanceof TextNode);
    }
}
