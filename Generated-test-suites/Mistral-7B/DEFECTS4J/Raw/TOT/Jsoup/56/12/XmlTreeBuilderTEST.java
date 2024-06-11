import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParseValidXML() {
        String xmlInput = "<root><element attr='value'><subElement attr2='value2' /></subElement></element></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.rootElement();
        Element element = root.element("element");
        Element subElement = element.element("subElement");

        assertNotNull(xmlDoc);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
        assertEquals("value", element.attribute("attr"));
        assertEquals("value2", subElement.attribute("attr2"));
    }

    @Test
    public void testParseInvalidXMLWithErrors() {
        String xmlInput = "<root><element><subElement /></subElement></element>"; // missing end tag for subElement
        ParseErrorList errorList = new ParseErrorList();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "", errorList, XmlTreeBuilder.ParseSettings.preserveCase);

        assertNotNull(xmlDoc);
        assertEquals(1, errorList.size());
        assertEquals("Unbalanced tags: expected '</subElement>'", errorList.get(0).getMessage());
    }

    @Test
    public void testParseEmptyXML() {
        String xmlInput = "<root />";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        assertNotNull(xmlDoc);
        assertEquals(Element.class, xmlDoc.rootElement().getClass());
        assertTrue(xmlDoc.rootElement().elementChildren().isEmpty());
    }

    @Test
    public void testParseXMLWithComments() {
        String xmlInput = "<root><!-- comment1 --><element attr='value'><!-- comment2 --><subElement attr2='value2' />--></subElement></element><!-- comment3 --></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.rootElement();
        Element element = root.element("element");
        Element subElement = element.element("subElement");

        List<Node> commentNodes = xmlDoc.getElementsByTag("comment");
        assertEquals(3, commentNodes.size());

        assertNotNull(xmlDoc);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
        assertEquals("value", element.attribute("attr"));
        assertEquals("value2", subElement.attribute("attr2"));
    }

    @Test
    public void testParseXMLWithDoctype() {
        String xmlInput = "<!DOCTYPE root SYSTEM \"mydtd.dtd\">\n<root><element attr='value'><subElement attr2='value2' /></subElement></element></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        DocumentType documentType = xmlDoc.documentType();
        Element root = xmlDoc.rootElement();
        Element element = root.element("element");
        Element subElement = element.element("subElement");

        assertNotNull(xmlDoc);
        assertNotNull(documentType);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
        assertEquals("root", documentType.nodeName());
        assertEquals("mydtd.dtd", documentType.systemIdentifier());
        assertEquals("value", element.attribute("attr"));
        assertEquals("value2", subElement.attribute("attr2"));
    }
}