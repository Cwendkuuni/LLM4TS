import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3c;

    @Before
    public void setUp() {
        w3c = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.location("http://example.com");

        Document w3cDoc = w3c.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        jsoupDoc.appendChild(jsoupEl);

        Document w3cDoc = w3c.fromJsoup(jsoupDoc);
        w3c.convert(jsoupDoc, w3cDoc);

        assertEquals(jsoupEl.tagName(), w3cDoc.getDocumentElement().getTagName());
        assertEquals(jsoupEl.childNodeSize(), w3cDoc.getDocumentElement().getChildNodes().getLength());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.head(jsoupEl, 0);

        assertEquals(jsoupEl.tagName(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));

        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("test");
        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.head(jsoupTextNode, 0);

        Text w3cText = (Text) w3cDoc.getDocumentElement().getFirstChild();
        assertEquals(jsoupTextNode.getWholeText(), w3cText.getWholeText());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("test");
        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.head(jsoupComment, 0);

        Comment w3cComment = (Comment) w3cDoc.getDocumentElement().getFirstChild();
        assertEquals(jsoupComment.getData(), w3cComment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));

        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("test");
        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.head(jsoupDataNode, 0);

        Text w3cText = (Text) w3cDoc.getDocumentElement().getFirstChild();
        assertEquals(jsoupDataNode.getWholeData(), w3cText.getWholeText());
    }

    @Test
    public void testW3CBuilderTailElement() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.head(jsoupEl, 0);
        builder.tail(jsoupEl, 0);

        assertNull(builder.dest);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Node jsoupNode = new org.jsoup.nodes.Element("div");
        jsoupNode.attributes().put("key", "value");

        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));
        Element w3cEl = w3cDoc.createElement("div");

        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        builder.copyAttributes(jsoupNode, w3cEl);

        Attr attr = w3cEl.getAttributeNode("key");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("div");
        jsoupEl.attributes().put("xmlns", "http://www.example.com");

        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));
        Element w3cEl = w3cDoc.createElement("div");

        w3c.W3CBuilder builder = new w3c.W3CBuilder(w3cDoc);
        String prefix = builder.updateNamespaces(jsoupEl);

        assertEquals("", prefix);
        assertEquals("http://www.example.com", builder.namespaces.get(prefix));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3c.fromJsoup(new org.jsoup.nodes.Document(""));
        w3cDoc.appendChild(w3cDoc.createTextNode("test"));

        String w3cString = w3c.asString(w3cDoc);

        assertEquals("test", w3cString);
    }
}