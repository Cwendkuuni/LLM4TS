import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(new Whitelist());
        dirtyDocument = Jsoup.parse("<html><head></head><body><div id=\"test\">Hello, World!</div></body></html>");
    }

    @Test
    public void clean_ValidDirtyDocument_ReturnsCleanDocument() {
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals(cleanDocument.baseUri(), dirtyDocument.baseUri());

        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();
        assertNotNull(cleanBody);

        List<Node> dirtyChildren = dirtyBody.childNodes();
        List<Node> cleanChildren = cleanBody.childNodes();
        assertEquals(dirtyChildren.size(), cleanChildren.size());

        for (int i = 0; i < dirtyChildren.size(); i++) {
            Node dirtyChild = dirtyChildren.get(i);
            Node cleanChild = cleanChildren.get(i);
            assertTrue(dirtyChild.getClass().equals(cleanChild.getClass()));

            if (dirtyChild instanceof Element) {
                Element dirtyElement = (Element) dirtyChild;
                Element cleanElement = (Element) cleanChild;

                assertEquals(dirtyElement.baseUri(), cleanElement.baseUri());
                assertEquals(dirtyElement.tagName(), cleanElement.tagName());
                assertEquals(dirtyElement.attributes().size(), cleanElement.attributes().size());

                for (Attribute dirtyAttr : dirtyElement.attributes()) {
                    assertTrue(cleanElement.attributes().hasKey(dirtyAttr.getKey()));
                    assertEquals(dirtyAttr.getValue(), cleanElement.attr(dirtyAttr.getKey()));
                }
            } else if (dirtyChild instanceof TextNode) {
                TextNode dirtyText = (TextNode) dirtyChild;
                TextNode cleanText = (TextNode) cleanChild;

                assertEquals(dirtyText.getWholeText(), cleanText.getWholeText());
                assertEquals(dirtyText.baseUri(), cleanText.baseUri());
            }
        }
    }

    @Test
    public void isValid_ValidDirtyDocument_ReturnsTrue() {
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void copySafeNodes_SourceAndDestinationNodes_ReturnsNumberOfDiscardedElements() {
        Element sourceElement = dirtyDocument.body();
        Element destinationElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void createSafeElement_SourceElement_ReturnsElementMetaObject() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}