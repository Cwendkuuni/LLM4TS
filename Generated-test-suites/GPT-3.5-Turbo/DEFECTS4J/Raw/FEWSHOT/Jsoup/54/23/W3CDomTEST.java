import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Document;

public class W3CDomTest {
  
  private W3CDom w3cDom;
  
  @Before
  public void setup() {
    w3cDom = new W3CDom();
  }
  
  @Test
  public void testFromJsoup() {
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    
    // Verify conversion
    assertEquals("html", w3cDoc.getDocumentElement().getNodeName());
  }
  
  @Test
  public void testConvert() {
    org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
    Document w3cDoc = mock(Document.class);
    Element w3cElement = mock(Element.class);
    when(w3cDoc.createElementNS(null, "html")).thenReturn(w3cElement);
    
    w3cDom.convert(jsoupDoc, w3cDoc);
    
    // Verify conversion
    verify(w3cDoc).createElementNS(null, "html");
    verify(w3cDoc).appendChild(w3cElement);
    verify(w3cElement).appendChild(w3cElement);
  }
  
  @Test
  public void testW3CBuilder_head_Element() {
    org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
    org.jsoup.nodes.Attributes jsoupAttributes = mock(org.jsoup.select.Attributes.class);
    org.jsoup.nodes.Attribute jsoupAttribute = mock(org.jsoup.nodes.Attribute.class);
    when(jsoupElement.attributes()).thenReturn(jsoupAttributes);
    when(jsoupElement.tagName()).thenReturn("div");
    when(jsoupAttributes.iterator()).thenReturn(Collections.singleton(jsoupAttribute).iterator());
    when(jsoupAttribute.getKey()).thenReturn("class");
    when(jsoupAttribute.getValue()).thenReturn("test-class");
    
    Document w3cDoc = mock(Document.class);
    Element w3cElement = mock(Element.class);
    when(w3cDoc.createElementNS(null, "div")).thenReturn(w3cElement);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    w3cBuilder.head(jsoupElement, 0);
    
    // Verify conversion
    verify(w3cElement).setAttribute("class", "test-class");
    verify(w3cElement).appendChild(w3cElement);
  }
  
  @Test
  public void testW3CBuilder_head_TextNode() {
    org.jsoup.nodes.TextNode jsoupText = mock(org.jsoup.nodes.TextNode.class);
    when(jsoupText.getWholeText()).thenReturn("test text");
    
    Document w3cDoc = mock(Document.class);
    Text w3cText = mock(Text.class);
    when(w3cDoc.createTextNode("test text")).thenReturn(w3cText);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    w3cBuilder.head(jsoupText, 0);
    
    // Verify conversion
    verify(w3cText).getWholeText();
    verify(w3cElement).appendChild(w3cText);
  }
  
  @Test
  public void testW3CBuilder_head_Comment() {
    org.jsoup.nodes.Comment jsoupComment = mock(org.jsoup.nodes.Comment.class);
    when(jsoupComment.getData()).thenReturn("test comment");
    
    Document w3cDoc = mock(Document.class);
    Comment w3cComment = mock(Comment.class);
    when(w3cDoc.createComment("test comment")).thenReturn(w3cComment);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    w3cBuilder.head(jsoupComment, 0);
    
    // Verify conversion
    verify(w3cComment).getData();
    verify(w3cElement).appendChild(w3cComment);
  }
  
  @Test
  public void testW3CBuilder_head_DataNode() {
    org.jsoup.nodes.DataNode jsoupData = mock(org.jsoup.nodes.DataNode.class);
    when(jsoupData.getWholeData()).thenReturn("test data");
    
    Document w3cDoc = mock(Document.class);
    Text w3cText = mock(Text.class);
    when(w3cDoc.createTextNode("test data")).thenReturn(w3cText);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    w3cBuilder.head(jsoupData, 0);
    
    // Verify conversion
    verify(w3cData).getWholeData();
    verify(w3cElement).appendChild(w3cData);
  }
  
  @Test
  public void testW3CBuilder_tail_Element() {
    org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
    org.w3c.dom.Element w3cElement = mock(org.w3c.dom.Element.class);
    when(w3cElement.getParentNode()).thenReturn(w3cElement);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(mock(Document.class));
    w3cBuilder.tail(jsoupElement, 0);
    
    // Verify conversion
    verify(w3cElement).getParentNode();
    verify(w3cElement).setParentNode(w3cElement);
  }
  
  @Test
  public void testCopyAttributes() {
    org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
    org.jsoup.nodes.Attributes jsoupAttributes = mock(org.jsoup.select.Attributes.class);
    org.jsoup.nodes.Attribute jsoupAttribute = mock(org.jsoup.nodes.Attribute.class);
    when(jsoupElement.attributes()).thenReturn(jsoupAttributes);
    when(jsoupElement.tagName()).thenReturn("div");
    when(jsoupAttributes.iterator()).thenReturn(Collections.singleton(jsoupAttribute).iterator());
    when(jsoupAttribute.getKey()).thenReturn("class");
    when(jsoupAttribute.getValue()).thenReturn("test-class");
    
    Document w3cDoc = mock(Document.class);
    Element w3cElement = mock(Element.class);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    w3cBuilder.copyAttributes(jsoupElement, w3cElement);
    
    // Verify conversion
    verify(w3cElement).setAttribute("class", "test-class");
  }
  
  @Test
  public void testUpdateNamespaces() {
    org.jsoup.nodes.Element jsoupElement = mock(org.jsoup.nodes.Element.class);
    org.jsoup.nodes.Attributes jsoupAttributes = mock(org.jsoup.select.Attributes.class);
    org.jsoup.nodes.Attribute jsoupAttribute = mock(org.jsoup.nodes.Attribute.class);
    when(jsoupElement.attributes()).thenReturn(jsoupAttributes);
    when(jsoupAttributes.iterator()).thenReturn(Collections.singleton(jsoupAttribute).iterator());
    when(jsoupAttribute.getKey()).thenReturn("xmlns:prefix");
    when(jsoupAttribute.getValue()).thenReturn("urn:prefix");
    when(jsoupElement.tagName()).thenReturn("prefix:tag");
    
    Document w3cDoc = mock(Document.class);
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    String prefix = w3cBuilder.updateNamespaces(jsoupElement);
    
    // Verify conversion
    verify(jsoupAttributes).iterator();
    assertEquals("prefix", prefix);
  }
  
  @Test
  public void testAsString() {
    Document w3cDoc = mock(Document.class);
    DOMSource domSource = mock(DOMSource.class);
    StringWriter writer = mock(StringWriter.class);
    StreamResult result = mock(StreamResult.class);
    TransformerFactory tf = mock(TransformerFactory.class);
    Transformer transformer = mock(Transformer.class);
    when(w3cDoc.getDocumentElement()).thenReturn(mock(Element.class));
    when(w3cDoc.appendChild(w3cElement)).thenReturn(w3cElement);
    when(domSource.transform(w3cDoc, result)).thenReturn(writer.toString());
    
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
    String result = w3cBuilder.asString(w3cDoc);
    
    // Verify conversion
    verify(domSource).transform(w3cDoc, result);
    assertEquals(writer.toString(), result);
  }
}