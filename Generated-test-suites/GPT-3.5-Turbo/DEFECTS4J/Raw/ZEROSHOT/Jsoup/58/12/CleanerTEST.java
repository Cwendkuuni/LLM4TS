import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("http://example.com");
        Element body = new Element("body");
        body.appendChild(new Element("div"));
        body.appendChild(new Element("p"));
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("http://example.com", cleanDocument.baseUri());
        assertNotNull(cleanDocument.body());

        assertEquals(2, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("p", cleanDocument.body().child(1).tagName());
    }

    @Test
    public void testIsValid_validDocument() {
        Document dirtyDocument = new Document("http://example.com");
        Element body = new Element("body");
        body.appendChild(new Element("div"));
        body.appendChild(new Element("p"));
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = new Document("http://example.com");
        Element body = new Element("body");
        body.appendChild(new Element("script"));
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }
}