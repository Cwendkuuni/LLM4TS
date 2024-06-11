import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.errors);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        Document doc = treeBuilder.parse("input", "baseUri");
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        assertTrue(treeBuilder.processStartTag("test"));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        assertTrue(treeBuilder.processStartTag("test", attrs));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        assertTrue(treeBuilder.processEndTag("test"));
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        assertNull(treeBuilder.currentElement());
    }
}