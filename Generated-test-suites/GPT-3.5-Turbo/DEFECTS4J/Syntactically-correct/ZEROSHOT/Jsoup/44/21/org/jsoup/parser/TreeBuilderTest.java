import org.junit.Before;
import org.junit.Test;

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
    public void initialiseParse_ValidInputAndBaseUri_NoExceptionThrown() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();

        try {
            treeBuilder.initialiseParse(input, baseUri, errors);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParse_NullInput_NullPointerExceptionThrown() {
        String input = null;
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test(expected = NullPointerException.class)
    public void initialiseParse_NullBaseUri_NullPointerExceptionThrown() {
        String input = "input";
        String baseUri = null;
        ParseErrorList errors = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, errors);
    }

    @Test
    public void parse_ValidInputAndBaseUri_ReturnsDocument() {
        String input = "input";
        String baseUri = "baseUri";

        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
    }

    @Test
    public void parse_ValidInputBaseUriAndErrors_ReturnsDocument() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();

        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
    }

    @Test
    public void runParser_WithValidToken_ProcessesToken() {
        Token token = new Token(Token.TokenType.StartTag, "tagName");

        boolean processed = treeBuilder.process(token);

        assertTrue(processed);
    }

    @Test
    public void processStartTag_ValidName_ProcessesStartTag() {
        String name = "tagName";

        boolean processed = treeBuilder.processStartTag(name);

        assertTrue(processed);
    }

    @Test
    public void processStartTag_ValidNameAndAttributes_ProcessesStartTag() {
        String name = "tagName";
        Attributes attrs = new Attributes();

        boolean processed = treeBuilder.processStartTag(name, attrs);

        assertTrue(processed);
    }

    @Test
    public void processEndTag_ValidName_ProcessesEndTag() {
        String name = "tagName";

        boolean processed = treeBuilder.processEndTag(name);

        assertTrue(processed);
    }

    @Test
    public void currentElement_StackNotEmpty_ReturnsCurrentElement() {
        Element element = new Element("tagName");
        treeBuilder.stack.add(element);

        Element current = treeBuilder.currentElement();

        assertNotNull(current);
        assertEquals(element, current);
    }

    @Test
    public void currentElement_StackEmpty_ReturnsNull() {
        Element current = treeBuilder.currentElement();

        assertNull(current);
    }
}