import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xml = "<xml></xml>";
        StringReader reader = new StringReader(xml);
        Document doc = xmlTreeBuilder.parse(reader, "http://www.example.com");
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodeSize());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xml = "<xml></xml>";
        Document doc = xmlTreeBuilder.parse(xml, "http://www.example.com");
        Assert.assertEquals("http://www.example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodeSize());
    }

    @Test
    public void testInitialiseParse() {
        String xml = "<xml></xml>";
        StringReader reader = new StringReader(xml);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, "http://www.example.com", errors, settings);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("http://www.example.com", xmlTreeBuilder.doc.baseUri());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes().put("attr1", "value1");
        startTag.attributes().put("attr2", "value2");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.process(startTag);

        Element element = xmlTreeBuilder.currentElement();
        Assert.assertEquals("tag", element.nodeName());
        Assert.assertEquals("http://www.example.com", element.baseUri());
        Assert.assertEquals("value1", element.attributes().get("attr1"));
        Assert.assertEquals("value2", element.attributes().get("attr2"));
    }

    @Test
    public void testProcessWithEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("tag");
        Element element2 = new Element("tag");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.process(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(element2, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.process(commentToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.COMMENT, node.nodeType());
        Assert.assertEquals("comment", node.toString());
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token.Character characterToken = new Token.Character("data");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.process(characterToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.TEXT, node.nodeType());
        Assert.assertEquals("data", node.toString());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.process(doctypeToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.DOCUMENT_TYPE, node.nodeType());
        Assert.assertEquals("html", node.nodeName());
    }

    @Test
    public void testInsertNode() {
        Element parent = new Element("parent");
        xmlTreeBuilder.stack.add(parent);

        Element child = new Element("child");
        xmlTreeBuilder.insertNode(child);

        Assert.assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testInsertWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.setSelfClosing(true);

        xmlTreeBuilder.stack.add(new Element("parent"));
        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals("tag", element.nodeName());
        Assert.assertTrue(element.tag().isSelfClosing());
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(element, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.insert(commentToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.COMMENT, node.nodeType());
        Assert.assertEquals("comment", node.toString());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("data");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.insert(characterToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.TEXT, node.nodeType());
        Assert.assertEquals("data", node.toString());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html");

        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.insert(doctypeToken);

        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertEquals(NodeType.DOCUMENT_TYPE, node.nodeType());
        Assert.assertEquals("html", node.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        Element element3 = new Element("tag");

        xmlTreeBuilder.stack.add(element3);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals(element2, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>data</tag>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals(NodeType.ELEMENT, nodes.get(0).nodeType());
        Assert.assertEquals("tag", nodes.get(0).nodeName());
    }
}