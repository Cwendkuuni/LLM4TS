package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParseString() {
        String input = "<html><head></head><body></body></html>";
        Document document = xmlTreeBuilder.parse(input, "");
        
        // Check if the document is parsed correctly
        assertEquals("html", document.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"), "", ParseSettings.preserveCase);
        Element element2 = new Element(Tag.valueOf("span"), "", ParseSettings.preserveCase);
        
        xmlTreeBuilder.insertNode(element1);
        xmlTreeBuilder.insertNode(element2);
        
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        // Check if the stack is correctly modified
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String input = "<div><p></p><p></p></div>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new Parser());

        // Check if the fragment is parsed correctly
        assertEquals(3, nodes.size());
    }
}