
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import java.io.IOException;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;
    private String dirtyHtml;

    @Before
    public void setup() throws IOException {
        whitelist = Whitelist.basicWithImages();
        cleaner = new Cleaner(whitelist);
        dirtyHtml = "<html><body>" +
                        "<h1>Heading 1</h1>" +
                        "<p>Paragraph 1</p>" +
                        "<script>alert('XSS Attack');</script>" +
                    "</body></html>";
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Document expectedCleanDocument = Jsoup.parse("<html>" +
                                                        "<head></head>" +
                                                        "<body>" +
                                                            "<h1>Heading 1</h1>" +
                                                            "<p>Paragraph 1</p>" +
                                                        "</body>" +
                                                    "</html>");

        assertEquals(expectedCleanDocument.toString(), cleanDocument.toString());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Document validDocument = Jsoup.parse("<html><body><h1>Heading 1</h1></body></html>");
        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Document invalidDocument = Jsoup.parse("<html><body><div style='color:red'>Invalid</div></body></html>");
        boolean isValid = cleaner.isValid(invalidDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();
        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);

        Document expectedDocument = Jsoup.parse("<html>" +
                                                    "<head></head>" +
                                                    "<body>" +
                                                        "<h1>Heading 1</h1>" +
                                                        "<p>Paragraph 1</p>" +
                                                    "</body>" +
                                                "</html>");

        assertEquals(expectedDocument.toString(), cleanDocument.toString());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("class", "safe");
        sourceElement.attr("style", "color:red");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element expectedElement = new Element(Tag.valueOf("div"), "");
        expectedElement.attr("class", "safe");
        expectedElement.attr("style", "color:red");
        ElementMeta expectedElementMeta = new ElementMeta(expectedElement, 1);

        assertEquals(expectedElementMeta.el.toString(), elementMeta.el.toString());
        assertEquals(expectedElementMeta.numAttribsDiscarded, elementMeta.numAttribsDiscarded);
    }
}
