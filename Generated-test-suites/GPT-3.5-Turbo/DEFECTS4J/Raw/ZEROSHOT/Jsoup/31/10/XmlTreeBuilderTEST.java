
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private final String baseUri = "https://example.com";
    private final String input = "<xml><foo attr=\"value\">Hello World</foo></xml>";

    private XmlTreeBuilder builder;
    private Document doc;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
        doc = Document.createShell(baseUri);
        builder.initialiseParse(input, baseUri, new ParseErrorList());
    }

    @After
    public void tearDown() {
        builder = null;
        doc = null;
    }

    @Test
    public void testInsertNode() {
        Element parentElement = doc.createElement("parent");
        Element childElement = doc.createElement("child");
        parentElement.appendChild(childElement);

        builder.insertNode(parentElement);

        assertEquals(parentElement, builder.currentElement());
        assertEquals(childElement, builder.currentElement().children().get(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("foo", "attr=value", true);
        Element insertedElement = builder.insert(startTag);

        assertEquals("foo", insertedElement.nodeName());
        assertEquals("value", insertedElement.attr("attr"));
        assertTrue(insertedElement.isSelfClosing());
        assertTrue(insertedElement.tag().isKnownTag());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Comment", "");
        builder.insert(commentToken);

        assertEquals("Comment", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Hello World");
        builder.insert(characterToken);

        assertEquals("Hello World", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        builder.insert(doctypeToken);

        assertEquals("<!DOCTYPE html>", builder.currentElement().childNode(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        Element parentElement1 = doc.createElement("parent1");
        Element parentElement2 = doc.createElement("parent2");
        Element childElement = doc.createElement("child");
        parentElement1.appendChild(parentElement2);
        parentElement2.appendChild(childElement);
        builder.stack.add(parentElement1);
        builder.stack.add(parentElement2);

        Token.EndTag endTag = new Token.EndTag("parent2");
        builder.popStackToClose(endTag);

        assertEquals(parentElement1, builder.currentElement());
        assertTrue(builder.stack.isEmpty());
    }
}
