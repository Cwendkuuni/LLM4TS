import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.appendChild(body);
        
        Element div = new Element("div", "");
        body.appendChild(div);
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body><div></div></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("");
        Element body = new Element("body", "");
        dirtyDocument.appendChild(body);
        
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    // More test cases can be added for other public methods of the Cleaner class

}