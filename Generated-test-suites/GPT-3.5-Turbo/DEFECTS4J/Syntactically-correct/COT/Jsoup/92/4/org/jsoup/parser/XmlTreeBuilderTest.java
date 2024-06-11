
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse_Reader_String() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String xml = "<root><element>hello world</element></root>";
        Document doc = treeBuilder.parse(new StringReader(xml), "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element rootElement = doc.getElementsByTag("root").first();
        List<Node> childNodes = rootElement.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Element element = (Element) childNodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("hello world", element.text());
    }

    @Test
    public void testParse_String_String() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String xml = "<root><element>hello world</element></root>";
        Document doc = treeBuilder.parse(xml, "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element rootElement = doc.getElementsByTag("root").first();
        List<Node> childNodes = rootElement.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Element element = (Element) childNodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("hello world", element.text());
    }

    @Test
    public void testParseFragment_String_String_Parser() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<element>hello world</element>";
        List<Node> nodes = treeBuilder.parseFragment(xmlFragment, "http://example.com", new Parser(treeBuilder));
        Assert.assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("hello world", element.text());
    }

    @Test
    public void testParseFragment_String_Element_String_Parser() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element context = new Element("parent");
        String xmlFragment = "<element>hello world</element>";
        List<Node> nodes = treeBuilder.parseFragment(xmlFragment, context, "http://example.com", new Parser(treeBuilder));
        Assert.assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("hello world", element.text());
    }
}

