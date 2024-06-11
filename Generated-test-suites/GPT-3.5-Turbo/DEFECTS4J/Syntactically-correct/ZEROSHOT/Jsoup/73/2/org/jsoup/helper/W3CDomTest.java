import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.mockito.Mockito;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Mockito.mock(org.jsoup.nodes.Document.class);
        w3cDoc = Mockito.mock(Document.class);
    }

    @Test
    public void testFromJsoup() {
        Mockito.when(jsoupDoc.location()).thenReturn("https://example.com");
        Mockito.when(jsoupDoc.child(0)).thenReturn(Mockito.mock(org.jsoup.nodes.Element.class));

        Document result = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(result);
        assertEquals("https://example.com", result.getDocumentURI());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupIn = Mockito.mock(org.jsoup.nodes.Document.class);
        org.jsoup.nodes.Element rootEl = Mockito.mock(org.jsoup.nodes.Element.class);
        Element dest = Mockito.mock(Element.class);
        Mockito.when(jsoupIn.location()).thenReturn("https://example.com");
        Mockito.when(jsoupIn.child(0)).thenReturn(rootEl);
        Mockito.when(jsoupIn.child(0)).thenReturn(rootEl);
        Mockito.when(rootEl.tagName()).thenReturn("div");

        w3cDom.convert(jsoupIn, w3cDoc);

        Mockito.verify(w3cDoc).setDocumentURI("https://example.com");
        Mockito.verify(rootEl).tagName();
        Mockito.verify(w3cDoc).createElementNS("", "div");
        Mockito.verify(w3cDoc).appendChild(dest);
        Mockito.verify(dest).appendChild(Mockito.any(Element.class));
    }

    @Test
    public void testW3CBuilderHead_Element() {
        org.jsoup.nodes.Element sourceEl = Mockito.mock(org.jsoup.nodes.Element.class);
        Element el = Mockito.mock(Element.class);
        Element parentEl = Mockito.mock(Element.class);
        Element grandParentEl = Mockito.mock(Element.class);
        Text textNode = Mockito.mock(Text.class);

        Mockito.when(w3cDoc.createElementNS("", "tag")).thenReturn(el);
        Mockito.when(sourceEl.tagName()).thenReturn("tag");
        Mockito.when(sourceEl.attributes()).thenReturn(Mockito.mock(org.jsoup.nodes.Attributes.class));
        Mockito.when(sourceEl.parent()).thenReturn(parentEl);
        Mockito.when(parentEl.getParentNode()).thenReturn(grandParentEl);

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(sourceEl, 0);

        Mockito.verify(w3cDoc).createElementNS("", "tag");
        Mockito.verify(sourceEl).attributes();
        Mockito.verify(el).setAttribute(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(w3cDoc).appendChild(el);
        Mockito.verify(el).appendChild(Mockito.any(Element.class));
        Mockito.verify(textNode, Mockito.never()).setData(Mockito.anyString());
    }

    @Test
    public void testW3CBuilderHead_TextNode() {
        org.jsoup.nodes.TextNode sourceText = Mockito.mock(org.jsoup.nodes.TextNode.class);
        Text text = Mockito.mock(Text.class);

        Mockito.when(w3cDoc.createTextNode("text")).thenReturn(text);
        Mockito.when(sourceText.getWholeText()).thenReturn("text");

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(sourceText, 0);

        Mockito.verify(w3cDoc).createTextNode("text");
        Mockito.verify(w3cDoc).appendChild(text);
    }

    @Test
    public void testW3CBuilderHead_Comment() {
        org.jsoup.nodes.Comment sourceComment = Mockito.mock(org.jsoup.nodes.Comment.class);
        Comment comment = Mockito.mock(Comment.class);

        Mockito.when(w3cDoc.createComment("comment")).thenReturn(comment);
        Mockito.when(sourceComment.getData()).thenReturn("comment");

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(sourceComment, 0);

        Mockito.verify(w3cDoc).createComment("comment");
        Mockito.verify(w3cDoc).appendChild(comment);
    }

    @Test
    public void testW3CBuilderHead_DataNode() {
        org.jsoup.nodes.DataNode sourceData = Mockito.mock(org.jsoup.nodes.DataNode.class);
        Text node = Mockito.mock(Text.class);

        Mockito.when(w3cDoc.createTextNode("data")).thenReturn(node);
        Mockito.when(sourceData.getWholeData()).thenReturn("data");

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.head(sourceData, 0);

        Mockito.verify(w3cDoc).createTextNode("data");
        Mockito.verify(w3cDoc).appendChild(node);
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element sourceEl = Mockito.mock(org.jsoup.nodes.Element.class);
        Element dest = Mockito.mock(Element.class);
        Element parentEl = Mockito.mock(Element.class);

        Mockito.when(sourceEl.getParent()).thenReturn(parentEl);
        Mockito.when(parentEl.getParentNode()).thenReturn(dest);

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.dest = dest;
        builder.tail(sourceEl, 0);

        Mockito.verify(dest).getParentNode();
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Node source = Mockito.mock(org.jsoup.nodes.Node.class);
        Attribute attribute = Mockito.mock(Attribute.class);
        Element el = Mockito.mock(Element.class);

        Mockito.when(source.attributes()).thenReturn(Mockito.mock(org.jsoup.nodes.Attributes.class));
        Mockito.when(attribute.getKey()).thenReturn("key");
        Mockito.when(attribute.getValue()).thenReturn("value");
        Mockito.when(el.tagName()).thenReturn("tag");

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        builder.copyAttributes(source, el);

        Mockito.verify(source).attributes();
        Mockito.verify(el).setAttribute("key", "value");
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = Mockito.mock(org.jsoup.nodes.Element.class);
        Attribute attr = Mockito.mock(Attribute.class);
        Attributes attributes = Mockito.mock(Attributes.class);

        Mockito.when(el.attributes()).thenReturn(attributes);
        Mockito.when(attr.getKey()).thenReturn("xmlns");
        Mockito.when(attr.getValue()).thenReturn("namespace");
        Mockito.when(attributes.iterator()).thenReturn(Mockito.mock(java.util.Iterator.class));
        Mockito.when(attributes.size()).thenReturn(1);
        Mockito.when(attributes.get("xmlns")).thenReturn(attr);
        Mockito.when(el.tagName()).thenReturn("tag:div");

        W3CDom.W3CBuilder builder = w3cDom.new W3CBuilder(w3cDoc);
        String result = builder.updateNamespaces(el);

        assertEquals("", result);
        Mockito.verify(el).attributes();
        Mockito.verify(attributes).iterator();
        Mockito.verify(attributes).size();
        Mockito.verify(attributes).get("xmlns");
    }

    @Test
    public void testAsString() {
        Document doc = Mockito.mock(Document.class);
        DOMSource domSource = Mockito.mock(DOMSource.class);
        StringWriter writer = Mockito.mock(StringWriter.class);
        StreamResult result = Mockito.mock(StreamResult.class);
        TransformerFactory tf = Mockito.mock(TransformerFactory.class);
        Transformer transformer = Mockito.mock(Transformer.class);

        Mockito.when(doc.getDocumentElement()).thenReturn(Mockito.mock(Element.class));
        Mockito.when(writer.toString()).thenReturn("xml");

        W3CDom w3cDom = new W3CDom();
        String output = w3cDom.asString(doc);

        assertNotNull(output);
        assertEquals("xml", output);
    }

}