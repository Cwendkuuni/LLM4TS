import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<div><p>Hello World!</p></div>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanedDocument);
        assertTrue(cleanedDocument.body().children().size() == 1);
        assertEquals("div", cleanedDocument.body().child(0).tagName());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Parser.parseBodyFragment("<p>This is a valid paragraph.</p>");
        Document invalidDocument = Parser.parseBodyFragment("<script>alert('XSS');</script>");

        assertTrue(cleaner.isValid(validDocument));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleaningVisitorHead() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element rootElement = new Element("div");
        Element destinationElement = new Element("p");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(rootElement, destinationElement);

        Element sourceElement = new Element("a");
        sourceElement.attr("href", "https://www.example.com");
        cleaningVisitor.head(sourceElement, 0);

        assertEquals(1, destinationElement.children().size());
        assertEquals("a", destinationElement.child(0).tagName());
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("div");
        Element destElement = new Element("p");
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("a");
        sourceElement.attr("href", "https://www.example.com");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals("a", elementMeta.el.tagName());
        assertEquals("https://www.example.com", elementMeta.el.attr("href"));
    }

}