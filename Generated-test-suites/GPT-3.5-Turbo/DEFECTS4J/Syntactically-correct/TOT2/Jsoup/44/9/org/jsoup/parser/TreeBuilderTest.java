import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        treeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertEquals(treeBuilder.baseUri, "baseUri");
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
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
        assertTrue(treeBuilder.processStartTag("tag"));
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        Attributes attrs = new Attributes();
        attrs.put("attr1", "value1");
        assertTrue(treeBuilder.processStartTag("tag", attrs));
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        assertTrue(treeBuilder.processEndTag("tag"));
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {};
        assertNull(treeBuilder.currentElement());
    }
}