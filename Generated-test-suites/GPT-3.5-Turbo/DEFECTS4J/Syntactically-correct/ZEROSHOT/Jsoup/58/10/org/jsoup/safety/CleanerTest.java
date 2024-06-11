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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><p>This is <b>unsafe</b> HTML.</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Assert.assertEquals("<html><head></head><body><p>This is <b>unsafe</b> HTML.</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><p>This is <b>unsafe</b> HTML.</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        Assert.assertEquals(false, isValid);
    }
}