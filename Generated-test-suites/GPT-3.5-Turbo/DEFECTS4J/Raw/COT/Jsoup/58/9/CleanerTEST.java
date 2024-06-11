import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test paragraph</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test paragraph</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test paragraph</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_emptyBody() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_emptyBody() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_noBody() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_noBody(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_unsafeTags() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Unsafe tag</h1></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body>Unsafe tag</body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_unsafeTags(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Unsafe tag</h1></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testClean_unsafeAttributes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p onclick=\"alert('XSS attack')\">Test paragraph</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test paragraph</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_unsafeAttributes(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p onclick=\"alert('XSS attack')\">Test paragraph</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testClean_enforcedAttributes() {
        Cleaner cleaner = new Cleaner(Whitelist.basicWithImages().addEnforcedAttribute("img", "width", "300"));
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><img src=\"example.jpg\"></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><img src=\"example.jpg\" width=\"300\"></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_enforcedAttributes(){
        Cleaner cleaner = new Cleaner(Whitelist.basicWithImages().addEnforcedAttribute("img", "width", "300"));
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><img src=\"example.jpg\"></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testClean_comments() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><!-- Comment --><p>Test paragraph</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test paragraph</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_comments(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><!-- Comment --><p>Test paragraph</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_xmlProcInstructions() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><?xml version=\"1.0\" encoding=\"UTF-8\"?><p>Test paragraph</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test paragraph</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_xmlProcInstructions(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><?xml version=\"1.0\" encoding=\"UTF-8\"?><p>Test paragraph</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testClean_dataNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS attack')</script><p>Test paragraph</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Test paragraph</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_dataNodes(){
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS attack')</script><p>Test paragraph</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }
}