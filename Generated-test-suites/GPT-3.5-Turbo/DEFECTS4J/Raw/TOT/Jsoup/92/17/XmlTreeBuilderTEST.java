import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String input = "<root><child1>Text</child1><child2>Another Text</child2></root>";
        String baseUri = "https://example.com";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Document result = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        
        Assert.assertNotNull(result);
        Element root = result.getElementsByTag("root").first();
        Assert.assertNotNull(root);
        Assert.assertEquals("root", root.tagName());
        
        List<Node> children = root.childNodes();
        Assert.assertEquals(2, children.size());
        
        Element child1 = (Element) children.get(0);
        Assert.assertEquals("child1", child1.tagName());
        Assert.assertEquals("Text", child1.text());
        
        Element child2 = (Element) children.get(1);
        Assert.assertEquals("child2", child2.tagName());
        Assert.assertEquals("Another Text", child2.text());
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Test inserting StartTag
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.addAttribute("id", "myDiv");
        startTag.setSelfClosing(false);
        Element result = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(result);
        Assert.assertEquals("div", result.tagName());
        Assert.assertEquals("myDiv", result.attr("id"));

        // Test inserting Comment
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        List<Node> children = result.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals("<!--This is a comment-->", children.get(0).outerHtml());

        // Test inserting Character
        Token.Character character = new Token.Character("Text");
        xmlTreeBuilder.insert(character);
        children = result.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertEquals("Text", children.get(1).outerHtml());

        // Test inserting Doctype
        Token.Doctype doctype = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctype);
        children = result.childNodes();
        Assert.assertEquals(3, children.size());
        Assert.assertEquals("<!DOCTYPE html>", children.get(2).outerHtml());

        // Test inserting EndTag
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        children = result.childNodes();
        Assert.assertEquals(0, children.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child1>Text</child1><child2>Another Text</child2>";
        String baseUri = "https://example.com";
        Element context = new Element("div", baseUri);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());

        Element child1 = (Element) result.get(0);
        Assert.assertEquals("child1", child1.tagName());
        Assert.assertEquals("Text", child1.text());

        Element child2 = (Element) result.get(1);
        Assert.assertEquals("child2", child2.tagName());
        Assert.assertEquals("Another Text", child2.text());
    }

}