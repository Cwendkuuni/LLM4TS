import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.DOMSource;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Mockito.mock(org.jsoup.nodes.Document.class);
        DocumentBuilder documentBuilder = Mockito.mock(DocumentBuilder.class);
        DocumentBuilderFactory documentBuilderFactory = Mockito.mock(DocumentBuilderFactory.class);
        Document doc = Mockito.mock(Document.class);
        Mockito.when(documentBuilderFactory.newInstance()).thenReturn(documentBuilderFactory);
        Mockito.when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
        Mockito.when(documentBuilder.newDocument()).thenReturn(doc);

        // Act
        Document result = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        Mockito.verify(documentBuilderFactory).setNamespaceAware(true);
        Mockito.verify(documentBuilder).newDocument();
        Assert.assertEquals(doc, result);
    }

    @Test
    public void testConvert() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document in = Mockito.mock(org.jsoup.nodes.Document.class);
        Document out = Mockito.mock(Document.class);
        org.jsoup.nodes.Element rootEl = Mockito.mock(org.jsoup.nodes.Element.class);
        Text text = Mockito.mock(Text.class);
        Element el = Mockito.mock(Element.class);
        Mockito.when(in.location()).thenReturn("http://example.com");
        Mockito.when(in.child(0)).thenReturn(rootEl);
        Mockito.when(out.createElementNS(Mockito.anyString(), Mockito.anyString())).thenReturn(el);
        Mockito.when(out.createTextNode(Mockito.anyString())).thenReturn(text);

        // Act
        w3cDom.convert(in, out);

        // Assert
        Mockito.verify(out).setDocumentURI("http://example.com");
        Mockito.verify(out).appendChild(el);
        Mockito.verify(el).setAttribute(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(out).appendChild(text);
    }

    @Test
    public void testConvert_WithCommentNode() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(Mockito.mock(Document.class));
        org.jsoup.nodes.Comment comment = Mockito.mock(org.jsoup.nodes.Comment.class);
        Comment commentNode = Mockito.mock(Comment.class);
        Mockito.when(comment.getData()).thenReturn("test comment");
        Mockito.when(w3cBuilder.doc.createComment(Mockito.anyString())).thenReturn(commentNode);

        // Act
        w3cBuilder.head(comment, 0);

        // Assert
        Mockito.verify(commentNode).setData("test comment");
        Mockito.verify(w3cBuilder.dest).appendChild(commentNode);
    }

    @Test
    public void testCopyAttributes() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(Mockito.mock(Document.class));
        org.jsoup.nodes.Element source = Mockito.mock(org.jsoup.nodes.Element.class);
        Element element = Mockito.mock(Element.class);
        Mockito.when(source.attributes()).thenReturn(new org.jsoup.nodes.Attributes());
        Mockito.when(source.attributes().iterator()).thenReturn(new org.jsoup.nodes.Attributes().iterator());
        Mockito.when(source.attributes().iterator().hasNext()).thenReturn(true, false);
        org.jsoup.nodes.Attribute attr = Mockito.mock(org.jsoup.nodes.Attribute.class);
        Mockito.when(source.attributes().iterator().next()).thenReturn(attr);
        Mockito.when(attr.getKey()).thenReturn("test-key");
        Mockito.when(attr.getValue()).thenReturn("test-value");
        Mockito.when(w3cBuilder.doc.createElementNS(Mockito.anyString(), Mockito.anyString())).thenReturn(element);

        // Act
        w3cBuilder.copyAttributes(source, element);

        // Assert
        Mockito.verify(element).setAttribute(Mockito.eq("test-key"), Mockito.eq("test-value"));
    }

    @Test
    public void testUpdateNamespaces() {
        // Arrange
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(Mockito.mock(Document.class));
        org.jsoup.nodes.Element el = Mockito.mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Attributes attributes = Mockito.mock(org.jsoup.nodes.Attributes.class);
        org.jsoup.nodes.Attribute attr1 = Mockito.mock(org.jsoup.nodes.Attribute.class);
        org.jsoup.nodes.Attribute attr2 = Mockito.mock(org.jsoup.nodes.Attribute.class);
        Mockito.when(el.attributes()).thenReturn(attributes);
        Mockito.when(attributes.iterator()).thenReturn(new org.jsoup.nodes.Attributes().iterator());
        Mockito.when(attributes.iterator().hasNext()).thenReturn(true, true, false);
        Mockito.when(attributes.iterator().next()).thenReturn(attr1, attr2);
        Mockito.when(attr1.getKey()).thenReturn("xmlns");
        Mockito.when(attr2.getKey()).thenReturn("xmlns:prefix");
        Mockito.when(attr1.getValue()).thenReturn("namespace1");
        Mockito.when(attr2.getValue()).thenReturn("namespace2");

        // Act
        String result = w3cBuilder.updateNamespaces(el);

        // Assert
        Mockito.verify(w3cBuilder.namespacesStack.peek()).put("", "namespace1");
        Mockito.verify(w3cBuilder.namespacesStack.peek()).put("prefix", "namespace2");
        Assert.assertEquals("", result);
    }

    @Test
    public void testAsString() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document doc = Mockito.mock(Document.class);
        DOMSource domSource = Mockito.mock(DOMSource.class);
        StringWriter writer = Mockito.mock(StringWriter.class);
        StreamResult result = Mockito.mock(StreamResult.class);
        TransformerFactory tf = Mockito.mock(TransformerFactory.class);
        Transformer transformer = Mockito.mock(Transformer.class);
        Mockito.when(doc).thenReturn(doc);
        Mockito.when(new DOMSource(doc)).thenReturn(domSource);
        Mockito.when(new StringWriter()).thenReturn(writer);
        Mockito.when(new StreamResult(writer)).thenReturn(result);
        Mockito.when(TransformerFactory.newInstance()).thenReturn(tf);
        Mockito.when(tf.newTransformer()).thenReturn(transformer);

        // Act
        String resultString = w3cDom.asString(doc);

        // Assert
        Mockito.verify(tf).newTransformer();
        Mockito.verify(transformer).transform(domSource, result);
        Assert.assertEquals(writer.toString(), resultString);
    }

}