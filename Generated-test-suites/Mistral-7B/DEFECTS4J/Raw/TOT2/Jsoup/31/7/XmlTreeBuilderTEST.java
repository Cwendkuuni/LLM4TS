import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag attr='value'></tag></xml>";
        String baseUri = "http://example.com";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);
        assertEquals("<xml><tag attr='value'></tag></xml>", doc.outerHtml());
    }
}