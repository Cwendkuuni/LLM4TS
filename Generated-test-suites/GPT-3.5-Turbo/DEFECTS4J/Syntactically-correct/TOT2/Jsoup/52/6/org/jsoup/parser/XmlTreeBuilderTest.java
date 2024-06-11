import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertTrue(xmlTreeBuilder.process(new Token()));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Node("test");
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), node);
    }

    // Additional test cases for other methods can be added here

}