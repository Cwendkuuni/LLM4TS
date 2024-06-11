import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void parse_withNullInput_throwsIllegalArgumentException() {
        ParseErrorList errors = new ParseErrorList();
        assertThrows(IllegalArgumentException.class, () -> treeBuilder.parse(null, "http://example.com", errors));
    }

    @Test
    public void parse_withNullBaseUri_throwsIllegalArgumentException() {
        ParseErrorList errors = new ParseErrorList();
        assertThrows(IllegalArgumentException.class, () -> treeBuilder.parse("html", null, errors));
    }

    @Test
    public void parse_withEmptyInput_returnsEmptyDocument() {
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("", "http://example.com", errors);
        assertNotNull(doc);
        assertTrue(doc.getAllElements().isEmpty());
    }

    @Test
    public void parse_withValidInput_returnsExpectedDocument() {
        String input = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, "http://example.com", errors);
        assertNotNull(doc);
        assertFalse(doc.getAllElements().isEmpty());
        assertEquals("html", doc.tagName());
        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals(1, doc.getElementsByTag("h1").size());
        assertEquals(1, doc.getElementsByTag("p").size());
    }

    @Test
    public void processStartTag_withValidName_returnsTrue() {
        assertTrue(treeBuilder.processStartTag("div"));
    }

    @Test
    public void processStartTag_withEmptyName_returnsTrue() {
        assertTrue(treeBuilder.processStartTag(""));
    }

    @Test
    public void processStartTag_withNullName_returnsTrue() {
        assertTrue(treeBuilder.processStartTag(null));
    }

    @Test
    public void processStartTag_withAttributes_returnsTrue() {
        Attributes attrs = new Attributes();
        attrs.put("id", "elementId");
        attrs.put("class", "elementClass");
        assertTrue(treeBuilder.processStartTag("div", attrs));
    }
    
    @Test
    public void processStartTag_withInvalidAttributes_returnsTrue() {
        Attributes attrs = new Attributes();
        attrs.put("", "elementId");
        attrs.put("class", null);
        assertTrue(treeBuilder.processStartTag("div", attrs));
    }

    @Test
    public void processEndTag_withValidName_returnsTrue() {
        assertTrue(treeBuilder.processEndTag("div"));
    }

    @Test
    public void processEndTag_withEmptyName_returnsTrue() {
        assertTrue(treeBuilder.processEndTag(""));
    }

    @Test
    public void processEndTag_withNullName_returnsTrue() {
        assertTrue(treeBuilder.processEndTag(null));
    }

    @Test
    public void currentElement_withEmptyStack_returnsNull() {
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void currentElement_withNonEmptyStack_returnsTopElement() {
        ArrayList<Element> stack = new ArrayList<>();
        Element element = new Element("div");
        stack.add(element);
        treeBuilder.stack = stack;
        assertEquals(element, treeBuilder.currentElement());
    }

}