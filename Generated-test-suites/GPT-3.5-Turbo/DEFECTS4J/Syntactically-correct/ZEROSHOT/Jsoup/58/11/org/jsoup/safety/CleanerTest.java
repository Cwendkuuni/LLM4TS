import org.jsoup.helper.Validate;
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
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Test with a dirty document
        Document dirtyDocument = createDirtyDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertEquals(dirtyDocument.baseUri(), cleanDocument.baseUri());
        assertNotNull(cleanDocument.body());
        
        assertEquals(1, cleanDocument.body().childNodeSize());
        Element bodyElement = cleanDocument.body().child(0);
        assertEquals("a", bodyElement.tagName());
        assertEquals("clean-link", bodyElement.className());
        assertEquals("https://example.com", bodyElement.absUrl("href"));
    }

    @Test
    public void testIsValid() {
        // Test with a valid document
        Document validDocument = createValidDocument();
        assertTrue(cleaner.isValid(validDocument));
        
        // Test with an invalid document
        Document invalidDocument = createInvalidDocument();
        assertFalse(cleaner.isValid(invalidDocument));
    }

    private Document createDirtyDocument() {
        Document dirtyDocument = Document.createShell("https://www.example.com");
        Element bodyElement = dirtyDocument.body();

        Element linkElement = new Element(Tag.valueOf("a"), dirtyDocument.baseUri());
        linkElement.setClass("dirty-link", true);
        linkElement.attr("href", "javascript:alert('XSS')");
        bodyElement.appendChild(linkElement);

        return dirtyDocument;
    }

    private Document createValidDocument() {
        Document validDocument = Document.createShell("https://www.example.com");
        Element bodyElement = validDocument.body();

        Element linkElement = new Element(Tag.valueOf("a"), validDocument.baseUri());
        linkElement.setClass("clean-link", true);
        linkElement.attr("href", "https://example.com");
        bodyElement.appendChild(linkElement);

        return validDocument;
    }

    private Document createInvalidDocument() {
        Document invalidDocument = Document.createShell("https://www.example.com");
        Element headElement = invalidDocument.head();

        Element scriptElement = new Element(Tag.valueOf("script"), invalidDocument.baseUri());
        scriptElement.attr("src", "https://malicious.com/script.js");
        headElement.appendChild(scriptElement);

        return invalidDocument;
    }
}