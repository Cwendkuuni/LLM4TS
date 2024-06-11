import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.w3c.dom.*;

public class W3CDomTest {

  @Test
  public void testFromJsoup() {
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");

    W3CDom w3cDom = new W3CDom();
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

    assertNotNull(w3cDoc);
  }

  @Test
  public void testFromJsoup_NullInput() {
    org.jsoup.nodes.Document jsoupDoc = null;

    W3CDom w3cDom = new W3CDom();
    try {
      Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
      fail("Expected exception was not thrown.");
    } catch (NullPointerException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testConvert() {
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom w3cDom = new W3CDom();
      w3cDom.convert(jsoupDoc, w3cDoc);

      assertNotNull(w3cDoc);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testConvert_BlankLocation() {
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
    jsoupDoc.location("");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom w3cDom = new W3CDom();
      w3cDom.convert(jsoupDoc, w3cDoc);

      assertNotNull(w3cDoc);
      assertEquals("", w3cDoc.getDocumentURI());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testConvert_NonBlankLocation() {
    String location = "http://example.com";

    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
    jsoupDoc.location(location);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom w3cDom = new W3CDom();
      w3cDom.convert(jsoupDoc, w3cDoc);

      assertNotNull(w3cDoc);
      assertEquals(location, w3cDoc.getDocumentURI());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testConvert_NullInput() {
    org.jsoup.nodes.Document jsoupDoc = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom w3cDom = new W3CDom();
      try {
        w3cDom.convert(jsoupDoc, w3cDoc);
        fail("Expected exception was not thrown.");
      } catch (NullPointerException e) {
        assertNotNull(e.getMessage());
      }
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderHead_Element() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      w3cBuilder.head(sourceEl, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("div");
      assertEquals(1, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderHead_TextNode() {
    org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      w3cBuilder.head(sourceText, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("#text");
      assertEquals(1, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderHead_Comment() {
    org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      w3cBuilder.head(sourceComment, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("#comment");
      assertEquals(1, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderHead_DataNode() {
    org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
     
      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      w3cBuilder.head(sourceData, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("#text");
      assertEquals(1, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderTail_Element() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();

      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      w3cBuilder.head(sourceEl, 0);
      w3cBuilder.tail(sourceEl, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("div");
      assertEquals(0, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testW3CBuilderTail_NonElement() {
    org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text", "");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();

       W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
       w3cBuilder.head(sourceText, 0);
       w3cBuilder.tail(sourceText, 0);

      NodeList nodeList = w3cDoc.getElementsByTagName("#text");
      assertEquals(0, nodeList.getLength());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCopyAttributes_ValidInput() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");
    sourceEl.attr("id", "div1");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();

      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      Element el = w3cDoc.createElement("div");
      w3cBuilder.copyAttributes(sourceEl, el);

      assertNotNull(el.getAttributes().getNamedItem("id"));
      assertEquals("div1", el.getAttributes().getNamedItem("id").getNodeValue());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCopyAttributes_InvalidInput() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");
    sourceEl.attr("id@#$%", "div1");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();

      W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
      Element el = w3cDoc.createElement("div");
      w3cBuilder.copyAttributes(sourceEl, el);

      assertNull(el.getAttributes().getNamedItem("id@#$%"));
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testUpdateNamespaces_WithNamespaceDeclaration() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");
    sourceEl.attr("xmlns:ns1", "urn:example:ns1");

    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
    String prefix = w3cBuilder.updateNamespaces(sourceEl);

    assertEquals("ns1", prefix);
    assertTrue(w3cBuilder.namespacesStack.peek().containsKey("ns1"));
    assertEquals("urn:example:ns1", w3cBuilder.namespacesStack.peek().get("ns1"));
  }

  @Test
  public void testUpdateNamespaces_WithoutNamespaceDeclaration() {
    org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div", "");

    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
    String prefix = w3cBuilder.updateNamespaces(sourceEl);

    assertEquals("", prefix);
    assertFalse(w3cBuilder.namespacesStack.peek().containsKey(""));
  }

  @Test
  public void testAsString() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document w3cDoc = builder.newDocument();
      Element rootEl = w3cDoc.createElement("root");
      w3cDoc.appendChild(rootEl);
      Text textNode = w3cDoc.createTextNode("text");
      rootEl.appendChild(textNode);

      W3CDom w3cDom = new W3CDom();
      String result = w3cDom.asString(w3cDoc);

      assertNotNull(result);
      assertTrue(result.contains("<root>"));
      assertTrue(result.contains("text"));
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testAsString_NullInput() {
    Document doc = null;

    W3CDom w3cDom = new W3CDom();
    try {
      String result = w3cDom.asString(doc);
      fail("Expected exception was not thrown.");
    } catch (NullPointerException e) {
      assertNotNull(e.getMessage());
    }
  }

}