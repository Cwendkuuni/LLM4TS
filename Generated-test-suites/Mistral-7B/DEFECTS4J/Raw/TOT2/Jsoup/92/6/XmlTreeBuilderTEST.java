import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXmlFile() {
        String xmlFileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<element1>Hello World</element1>\n<element2><subElement>Sub Element</subElement></element2>\n</root>";
        Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlFileContent), "");
        assertNotNull(xmlDocument);
    }
}