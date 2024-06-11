import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @Test
    public void testInitialiseParse_ValidInputAndBaseUri_NoException() {
        try {
            treeBuilder.initialiseParse("", "http://example.com", null);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParse_NullInput_ThrowsNullPointerException() {
        treeBuilder.initialiseParse(null, "http://example.com", null);
    }

    @Test(expected = NullPointerException.class)
    public void testInitialiseParse_NullBaseUri_ThrowsNullPointerException() {
        treeBuilder.initialiseParse("", null, null);
    }

    @Test
    public void testParse_ValidInputAndBaseUri_ReturnsDocument() {
        Document document = treeBuilder.parse("", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParse_TrackingErrorsEnabled_ReturnsDocumentWithErrors() {
        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse("", "http://example.com", errors);
        assertNotNull(document);
        assertNotNull(errors.getAll());
    }

    @Test
    public void testProcess_EndToken_ReturnsFalse() {
        Token token = new Token() {
            @Override
            protected Token reset() {
                return null;
            }
        };
        boolean result = treeBuilder.process(token);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTag_ValidTagName_ReturnsFalse() {
        String tagName = "div";
        boolean result = treeBuilder.processStartTag(tagName);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTag_InvalidTagName_ReturnsFalse() {
        String tagName = "!@#$%";
        boolean result = treeBuilder.processStartTag(tagName);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTag_ValidTagNameAndAttributes_ReturnsFalse() {
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("id", "myId");
        attributes.put("class", "myClass");
        
        boolean result = treeBuilder.processStartTag(tagName, attributes);
        assertFalse(result);
    }

    @Test
    public void testProcessEndTag_ValidTagName_ReturnsFalse() {
        String tagName = "div";
        boolean result = treeBuilder.processEndTag(tagName);
        assertFalse(result);
    }

    @Test
    public void testCurrentElement_StackEmpty_ReturnsNull() {
        Element element = treeBuilder.currentElement();
        assertNull(element);
    }

    @Test
    public void testCurrentElement_StackNotEmpty_ReturnsElement() {
        treeBuilder.stack.add(new Element("div"));
        Element element = treeBuilder.currentElement();
        assertNotNull(element);
    }

}