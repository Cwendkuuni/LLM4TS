import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testParse() {
        String xml = "<root><child>Text</child></root>";
        Document xmlDoc = Jsoup.parse(xml, "", Parser.xmlParser());
        Assert.assertNotNull(xmlDoc);
    }

    @Test
    public void testParseFragment() {
        String fragment = "<child>Text</child>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, "", new Parser(xmlTreeBuilder));
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testInsert() {
        String startTag = "<child>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element el = xmlTreeBuilder.insert(new Token.StartTag(startTag));
        Assert.assertNotNull(el);
    }

    @Test
    public void testInsertComment() {
        String comment = "Test Comment";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(new Token.Comment(comment));
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        String data = "Text";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(new Token.Character(data));
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        String name = "main";
        String publicIdentifier = "";
        String systemIdentifier = "";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(new Token.Doctype(name, publicIdentifier, systemIdentifier));
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testProcess() {
        Token token = new Token.StartTag("<child>");
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertTrue(result);
    }
}