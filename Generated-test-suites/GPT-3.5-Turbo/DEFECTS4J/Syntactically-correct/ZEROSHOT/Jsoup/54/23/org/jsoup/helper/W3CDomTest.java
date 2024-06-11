import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body><div></div></body></html>", "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertTrue(w3cDoc instanceof org.w3c.dom.Document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body><div></div></body></html>", "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        org.w3c.dom.Element root = w3cDoc.getDocumentElement();
        assertNotNull(root);

        org.w3c.dom.NodeList rootChildNodes = root.getChildNodes();
        assertEquals(2, rootChildNodes.getLength());

        org.w3c.dom.Element head = (org.w3c.dom.Element) rootChildNodes.item(0);
        assertNotNull(head);

        org.w3c.dom.Element body = (org.w3c.dom.Element) rootChildNodes.item(1);
        assertNotNull(body);
    }

    @Test
    public void testW3CBuilder_Head_Element() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = org.jsoup.parser.Parser.parseBodyFragment("<div></div>").body();
        org.w3c.dom.Document doc = org.w3c.dom.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element dest = doc.createElement("root");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceEl, 0);

        org.w3c.dom.Element createdEl = (org.w3c.dom.Element) dest.getFirstChild();
        assertNotNull(createdEl);
        assertEquals("div", createdEl.getTagName());
    }

    @Test
    public void testW3CBuilder_Head_TextNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");
        org.w3c.dom.Document doc = org.w3c.dom.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element dest = doc.createElement("root");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceText, 0);

        org.w3c.dom.Text createdText = (org.w3c.dom.Text) dest.getFirstChild();
        assertNotNull(createdText);
        assertEquals("text", createdText.getWholeText());
    }

    @Test
    public void testW3CBuilder_Head_Comment() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");
        org.w3c.dom.Document doc = org.w3c.dom.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element dest = doc.createElement("root");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceComment, 0);

        org.w3c.dom.Comment createdComment = (org.w3c.dom.Comment) dest.getFirstChild();
        assertNotNull(createdComment);
        assertEquals("comment", createdComment.getData());
    }

    @Test
    public void testW3CBuilder_Head_DataNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");
        org.w3c.dom.Document doc = org.w3c.dom.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element dest = doc.createElement("root");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceData, 0);

        org.w3c.dom.Text createdData = (org.w3c.dom.Text) dest.getFirstChild();
        assertNotNull(createdData);
        assertEquals("data", createdData.getWholeData());
    }

    @Test
    public void testW3CBuilder_Tail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = org.jsoup.parser.Parser.parseBodyFragment("<div></div>").body();
        org.w3c.dom.Document doc = org.w3c.dom.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        org.w3c.dom.Element dest = doc.createElement("root");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);

        assertNotNull(w3cBuilder.dest);
        assertEquals(dest, w3cBuilder.dest);
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element sourceEl = org.jsoup.parser.Parser.parseBodyFragment("<div id='id' class='class' data-attr='value'></div>").body();
        w3cBuilder.dest = new org.w3c.dom.DocumentImpl().createElement("root");
        org.w3c.dom.Element destEl = (org.w3c.dom.Element) w3cBuilder.dest;

        w3cBuilder.copyAttributes(sourceEl, destEl);

        assertEquals("id", destEl.getAttribute("id"));
        assertEquals("class", destEl.getAttribute("class"));
        assertEquals("value", destEl.getAttribute("data-attr"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element el = org.jsoup.parser.Parser.parseBodyFragment("<div xmlns='urn:namespaceUri'></div>").body();

        String prefix = w3cBuilder.updateNamespaces(el);

        assertEquals("", prefix);
        assertTrue(w3cBuilder.namespaces.containsKey(""));
        assertEquals("urn:namespaceUri", w3cBuilder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parse("<html><head></head><body><div></div></body></html>", "");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String output = w3cDom.asString(w3cDoc);

        assertNotNull(output);
        assertTrue(output.contains("<html>"));
        assertTrue(output.contains("<head>"));
        assertTrue(output.contains("<body>"));
        assertTrue(output.contains("<div>"));
    }
}