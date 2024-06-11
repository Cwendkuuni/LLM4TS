import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document expected = Document.createShell(baseUri);
        Element htmlTag = expected.createElement("html");
        Element headTag = expected.createElement("head");
        Element titleTag = expected.createElement("title").text("Test");
        Element bodyTag = expected.createElement("body");
        Element h1Tag = expected.createElement("h1").text("Hello, World!");
        expected.appendChild(htmlTag);
        htmlTag.appendChild(headTag);
        headTag.appendChild(titleTag);
        htmlTag.appendChild(bodyTag);
        bodyTag.appendChild(h1Tag);

        Document actual = Parser.parse(html, baseUri);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        Document expected = Document.createShell(baseUri);
        Element h1Tag = expected.createElement("h1").text("Hello, World!");
        expected.appendChild(h1Tag);

        Document actual = Parser.parseBodyFragment(html, baseUri);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://www.example.com";
        Document expected = Document.createShell(baseUri);
        Element h1Tag = expected.createElement("h1").text("Hello, World!");
        expected.appendChild(h1Tag);

        Document actual = Parser.parseBodyFragmentRelaxed(html, baseUri);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);
        parser.parseComment();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast() instanceof Comment);
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "", false);
        parser.parseXmlDecl();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast() instanceof XmlDeclaration);
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "", false);
        parser.parseEndTag();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertTrue(stack.isEmpty());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast() instanceof Element);
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast() instanceof TextNode);
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "", false);
        parser.parseCdata();

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast() instanceof TextNode);
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", false);
        Element child = new Element("div");
        parser.addChildToParent(child, true);

        LinkedList<Element> stack = parser.getStack();
        Assert.assertFalse(stack.isEmpty());
        Assert.assertTrue(stack.getLast().equals(child));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        Element parent = new Element("html");
        Element child = new Element("body");
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(parent);
        stack.add(child);
        parser.setStack(stack);

        Element result = parser.popStackToSuitableContainer(child.tag());
        Assert.assertEquals(parent, result);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Element parent = new Element("html");
        Element child1 = new Element("head");
        Element child2 = new Element("body");
        Element child3 = new Element("h1");
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(parent);
        stack.add(child1);
        stack.add(child2);
        stack.add(child3);
        parser.setStack(stack);

        Element result = parser.popStackToClose(child1.tag());
        Assert.assertEquals(child1, result);
        Assert.assertEquals(3, parser.getStack().size());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        Element element = new Element("div");
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(element);
        parser.setStack(stack);

        Element result = parser.last();
        Assert.assertEquals(element, result);
    }

    // Helper methods to access private fields for testing

    private LinkedList<Element> getStack() {
        return stack;
    }

    private void setStack(LinkedList<Element> stack) {
        this.stack = stack;
    }
}