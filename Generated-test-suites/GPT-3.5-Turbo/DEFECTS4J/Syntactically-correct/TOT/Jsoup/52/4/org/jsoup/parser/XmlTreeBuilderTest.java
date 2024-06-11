import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    XmlTreeBuilder builder;
    Document doc;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Heading</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals("http://example.com", doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        builder.process(startTag);

        assertEquals("<div></div>", doc.html());
    }

    @Test
    public void testProcess_EndTag() {
        doc.html("<div></div>");
        Token.EndTag endTag = new Token.EndTag("div");
        builder.process(endTag);

        assertEquals("", doc.html());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.process(comment);

        assertEquals("<!--This is a comment-->", doc.html());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("Test");
        builder.process(character);

        assertEquals("Test", doc.html());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.process(doctype);

        assertEquals("<!DOCTYPE html>", doc.html());
    }

    @Test
    public void testInsertNode() {
        Token.StartTag startTag = new Token.StartTag("div");
        builder.process(startTag);
        Token.Character character = new Token.Character("Test");
        builder.process(character);

        assertEquals("<div>Test</div>", doc.html());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Token.Attribute attribute = new Token.Attribute("class", "test");
        startTag.attributes.add(attribute);

        builder.insert(startTag);

        assertEquals("<div class=\"test\"></div>", doc.html());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        builder.insert(comment);

        assertEquals("<!--This is a comment-->", doc.html());
    }

    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("Test");
        builder.insert(character);

        assertEquals("Test", doc.html());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);

        assertEquals("<!DOCTYPE html>", doc.html());
    }

    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("div");
        builder.process(startTag);
        builder.popStackToClose(endTag);

        assertEquals("", doc.html());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);

        assertEquals("Test", nodes.get(0).outerHtml());
        assertEquals(0, errors.size());
    }

}