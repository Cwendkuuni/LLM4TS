import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
     
    @Test
    public void testParse_ReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element>text</element></root>";
        Document document = builder.parse(new StringReader(xml), "https://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("root", document.nodeName());
        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("element").size());
    }
  
    @Test
    public void testParse_StringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element>text</element></root>";
        Document document = builder.parse(xml, "https://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("root", document.nodeName());
        Assert.assertEquals("https://example.com", document.baseUri());
        Assert.assertEquals(1, document.getElementsByTag("element").size());
    }
  
    @Test
    public void testInitialiseParse_ReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element>text</element></root>";
        builder.initialiseParse(new StringReader(xml), "https://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(builder.settings, ParseSettings.preserveCase);
        Assert.assertNotNull(builder.doc);
        Assert.assertEquals("root", builder.currentElement().nodeName());
    }
  
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element");
        Token token = new Token(startTag);
        builder.process(token);
        Assert.assertEquals(1, builder.currentElement().getAllElements().size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("element");
        Tag tag = Tag.valueOf("element", builder.settings);
        Element element = new Element(tag, "https://example.com", builder.settings.normalizeAttributes(startTag.attributes));
        builder.stack.add(element);
        builder.currentElement().appendChild(element);
        Assert.assertEquals(1, builder.currentElement().getAllElements().size());
    }
  
    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment.");
        builder.insert(commentToken);
        Assert.assertEquals(1, builder.currentElement().childNodes().size());
    }
  
    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Content");
        builder.insert(characterToken);
        Assert.assertEquals(1, builder.currentElement().childNodes().size());
    }
  
    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        builder.insert(doctypeToken);
        Assert.assertEquals(1, builder.currentElement().childNodes().size());
    }
  
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("element"), "https://example.com");
        builder.stack.add(element);
        builder.popStackToClose(new Token.EndTag("element"));
        Assert.assertEquals(0, builder.stack.size());
    }
  
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<element>text</element>";
        List<Node> nodes = builder.parseFragment(xml, "https://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
    }
}