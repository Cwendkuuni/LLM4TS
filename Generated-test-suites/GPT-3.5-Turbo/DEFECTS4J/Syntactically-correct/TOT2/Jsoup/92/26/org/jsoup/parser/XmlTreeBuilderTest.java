import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(input), "");
        assertEquals(1, doc.childNodeSize());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.insertNode(element);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    // Add more test cases for other public methods here

}