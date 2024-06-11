import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml></xml>", "http://example.com", null);
        Document doc = builder.getDocument();
        Element root = doc.child(0);
        assertEquals("xml", root.nodeName());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml><tag></tag></xml>", "http://example.com", null);
        Document doc = builder.getDocument();
        Element root = doc.child(0);
        Element child = root.child(0);
        assertEquals("tag", child.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml><tag></tag></xml>", "http://example.com", null);
        Document doc = builder.getDocument();
        Element root = doc.child(0);
        Element child = root.child(0);
        builder.process(new Token.EndTag("tag"));
        assertTrue(root.children().isEmpty());
    }

}