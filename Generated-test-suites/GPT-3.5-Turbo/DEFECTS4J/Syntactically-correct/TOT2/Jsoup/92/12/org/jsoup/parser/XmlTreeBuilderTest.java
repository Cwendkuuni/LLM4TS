import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputXml = "<root><element1>data1</element1><element2>data2</element2></root>";
        Document doc = xmlTreeBuilder.parse(inputXml, "http://example.com");

        Element rootElement = doc.selectFirst("root");
        assertNotNull(rootElement);
        assertEquals(2, rootElement.children().size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div><span>Fragment</span></div>";
        Element context = new Element("div");
        
        Element element = new Element("span");
        context.appendChild(element);

        Node result = xmlTreeBuilder.parseFragment(inputFragment, context, "http://example.com", new Parser(null)).get(0);
        assertEquals("span", result.nodeName());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("div");
        xmlTreeBuilder.process(startTagToken);

        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
    }

    // Additional test methods for other public methods in XmlTreeBuilder class

}