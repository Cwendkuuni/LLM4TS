package org.jsoup.safety;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that only allowed elements are present in the clean document
        for (Element element : cleanDocument.select("*")) {
            assert Whitelist.basic().isSafeTag(element.tagName());
        }
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><div>Hello</div></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert that the document is valid according to the whitelist
        assert isValid;
    }
}