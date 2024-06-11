import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document document = treeBuilder.parse(new StringReader("<html></html>"), "http://example.com");
        
        assertNotNull(document);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Node node = new Element(Tag.valueOf("div"), "http://example.com");
        
        treeBuilder.insertNode(node);
        Element currentElement = treeBuilder.currentElement();
        
        assertNotNull(currentElement);
        assertEquals("div", currentElement.nodeName());
    }
    
    // Other test cases for insert, insertNode, popStackToClose, parseFragment, etc.
    // Add edge cases and exception handling scenarios
    
}