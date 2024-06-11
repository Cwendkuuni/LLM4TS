import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        errors = new ParseErrorList();
    }

    @Test
    public void testInitialiseParse_withValidInputAndBaseURI_returnsNonNullDocument() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        treeBuilder.initialiseParse(input, baseUri, errors);
        assertNotNull(treeBuilder.doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParse_withNullInput_throwsIllegalArgumentException() {
        String baseUri = "http://example.com";
        treeBuilder.initialiseParse(null, baseUri, errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialiseParse_withNullBaseURI_throwsIllegalArgumentException() {
        String input = "<html><body><h1>Test</h1></body></html>";
        treeBuilder.initialiseParse(input, null, errors);
    }

    @Test
    public void testProcessStartTag_withValidName_returnsTrue() {
        assertTrue(treeBuilder.processStartTag("div"));
    }

    @Test
    public void testProcessStartTag_withInvalidName_returnsFalse() {
        assertFalse(treeBuilder.processStartTag("!@#$"));
    }

    @Test
    public void testProcessStartTag_withValidNameAndAttributes_returnsTrue() {
        Attributes attrs = new Attributes();
        attrs.put("class", "test");
        assertTrue(treeBuilder.processStartTag("div", attrs));
    }

    @Test
    public void testProcessStartTag_withValidNameAndNullAttributes_returnsTrue() {
        assertTrue(treeBuilder.processStartTag("div", null));
    }

    @Test
    public void testProcessEndTag_withValidName_returnsTrue() {
        assertTrue(treeBuilder.processEndTag("div"));
    }

    @Test
    public void testProcessEndTag_withInvalidName_returnsFalse() {
        assertFalse(treeBuilder.processEndTag("!@#$"));
    }

    @Test
    public void testCurrentElement_withEmptyStack_returnsNull() {
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void testCurrentElement_withNonEmptyStack_returnsLastElement() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");

        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);

        assertEquals(element2, treeBuilder.currentElement());
    }
}