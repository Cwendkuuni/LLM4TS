import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Element;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList parseErrorList;

    @BeforeEach
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseErrorList = new ParseErrorList();
    }

    @Test
    public void testInitialiseParseWithValidInput() {
        xmlTreeBuilder.initialiseParse("<?xml version='1.0' encoding='UTF-8'?>\n<root><child></child></root>", "", parseErrorList);
        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc, "Document should not be null");
        Element root = doc.rootElement();
        assertNotNull(root, "Root element should not be null");
    }
}