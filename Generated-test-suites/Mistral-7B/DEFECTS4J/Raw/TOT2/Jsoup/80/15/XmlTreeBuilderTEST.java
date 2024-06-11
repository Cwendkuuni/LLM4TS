import static org.junit.Assert.assertEquals;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithReader() {
        // Given
        String xmlString = "<root><child>Hello</child></root>";
        Reader reader = new StringReader(xmlString);
        String baseUri = "http://example.com";

        // When
        Document xmlDoc = new XmlTreeBuilder().parse(reader, baseUri);

        // Then
        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        assertEquals("Hello", child.text());
    }
}