import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.appendChild(body);

        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", cleanedDocument.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.appendChild(body);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element("div", "");
        Element destination = new Element("div", "");
        root.appendChild(new Element("span", ""));

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("a", "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("<a></a>", elementMeta.el.toString());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}