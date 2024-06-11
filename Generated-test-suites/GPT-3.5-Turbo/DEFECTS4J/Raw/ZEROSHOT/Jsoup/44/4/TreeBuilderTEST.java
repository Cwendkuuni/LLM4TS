import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // do nothing for test purposes
                return true;
            }
        };
    }

    @Test
    public void parse_shouldReturnDocument() {
        Document document = treeBuilder.parse("", "http://example.com");

        assertNotNull(document);
    }

    @Test
    public void processStartTag_shouldReturnTrue() {
        boolean result = treeBuilder.processStartTag("div");

        assertTrue(result);
    }

    @Test
    public void processStartTag_withAttributes_shouldReturnTrue() {
        Attributes attributes = new Attributes();
        attributes.put("class", "content");

        boolean result = treeBuilder.processStartTag("div", attributes);

        assertTrue(result);
    }

    @Test
    public void processEndTag_shouldReturnTrue() {
        boolean result = treeBuilder.processEndTag("div");

        assertTrue(result);
    }

    @Test
    public void currentElement_withEmptyStack_shouldReturnNull() {
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }
}