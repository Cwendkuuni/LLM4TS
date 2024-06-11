import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }
    
    @Test
    public void testHeadReturnNotNull() {
        assertNotNull(document.head());
    }
    
    @Test
    public void testHeadReturnCorrectElement() {
        assertEquals("head", document.head().nodeName());
    }
    
    @Test
    public void testBodyReturnNotNull() {
        assertNotNull(document.body());
    }
    
    @Test
    public void testBodyReturnCorrectElement() {
        assertEquals("body", document.body().nodeName());
    }
    
    @Test
    public void testTitleWithExistingTitleElement() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testTitleWithNonExistingTitleElement() {
        document.head().remove();
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testTitleWithNullTitle() {
        document.title(null);
        assertEquals("", document.title());
    }
    
    @Test
    public void testTitleWithBlankTitle() {
        document.title("");
        assertEquals("", document.title());
    }
    
    @Test
    public void testCreateElement() {
        assertEquals("div", document.createElement("div").tagName());
    }
    
    @Test
    public void testNormaliseWithEmptyDocument() {
        document = new Document("");
        document.normalise();
        assertTrue(document.select("html").size() == 1);
        assertTrue(document.select("head").size() == 1);
        assertTrue(document.select("body").size() == 1);
    }
    
    @Test
    public void testNormaliseWithExistingStructure() {
        document.normalise();
        assertTrue(document.head().siblingIndex() == 0);
        assertTrue(document.body().siblingIndex() == 1);
    }
    
    @Test
    public void testOuterHtml() {
        document.text("Test Text");
        assertEquals("Test Text", document.outerHtml());
    }
    
    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}