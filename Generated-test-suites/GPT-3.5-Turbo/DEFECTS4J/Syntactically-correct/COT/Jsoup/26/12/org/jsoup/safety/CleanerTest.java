
import static org.junit.Assert.*;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

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
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello, World!</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html>\n <head></head>\n <body><h1>Hello, World!</h1></body>\n</html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello, World!</h1></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        source.appendChild(new Element(Tag.valueOf("p"), "").appendText("Paragraph"));
        source.appendChild(new TextNode("Text Node"));
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("h1"), "");
        sourceEl.attr("class", "heading");
        sourceEl.attr("style", "color: red;");
        
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        assertEquals("<h1 class=\"heading\" style=\"color: red;\"></h1>", dest.toString());
        assertEquals(0, meta.numAttribsDiscarded);
    }
}
