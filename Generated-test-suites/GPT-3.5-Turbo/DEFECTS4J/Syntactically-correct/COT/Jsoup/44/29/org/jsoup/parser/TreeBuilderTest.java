
// TreeBuilderTest.java

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @BeforeEach
    void setUp() {
        treeBuilder = new TreeBuilder(){};
    }

    @Test
    void initialiseParse() {
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList parseErrorList = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, parseErrorList);

        Document doc = treeBuilder.doc;
        CharacterReader reader = treeBuilder.reader;
        Tokeniser tokeniser = treeBuilder.tokeniser;
        ArrayList<Element> stack = treeBuilder.stack;
        String baseUriField = treeBuilder.baseUri;
        ParseErrorList errors = treeBuilder.errors;

        Assertions.assertEquals(baseUri, doc.baseUri());
        Assertions.assertEquals(input, reader.getString());
        Assertions.assertEquals(reader, tokeniser.reader);
        Assertions.assertEquals(errors, tokeniser.errors);
        Assertions.assertEquals(32, stack.size());
        Assertions.assertEquals(baseUri, baseUriField);
        Assertions.assertEquals(parseErrorList, errors);
    }

    @Test
    void parse() {
        String input = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList parseErrorList = ParseErrorList.noTracking();

        Document parsedDoc = treeBuilder.parse(input, baseUri, parseErrorList);

        Assertions.assertEquals(baseUri, parsedDoc.baseUri());
    }

    @Test
    void processStartTag() {
        String validName = "div";
        String nullName = null;

        Assertions.assertTrue(treeBuilder.processStartTag(validName));
        Assertions.assertFalse(treeBuilder.processStartTag(nullName));
    }

    @Test
    void processStartTagWithAttrs() {
        String validName = "div";
        Attributes validAttrs = new Attributes();
        Attributes nullAttrs = null;
        String nullName = null;

        Assertions.assertTrue(treeBuilder.processStartTag(validName, validAttrs));
        Assertions.assertFalse(treeBuilder.processStartTag(validName, nullAttrs));
        Assertions.assertFalse(treeBuilder.processStartTag(nullName, validAttrs));
    }

    @Test
    void processEndTag() {
        String validName = "div";
        String nullName = null;

        Assertions.assertTrue(treeBuilder.processEndTag(validName));
        Assertions.assertFalse(treeBuilder.processEndTag(nullName));
    }

    @Test
    void currentElement() {
        Assertions.assertNull(treeBuilder.currentElement());

        Element element = new Element("div");
        treeBuilder.stack.add(element);
        Assertions.assertEquals(element, treeBuilder.currentElement());
    }
}
