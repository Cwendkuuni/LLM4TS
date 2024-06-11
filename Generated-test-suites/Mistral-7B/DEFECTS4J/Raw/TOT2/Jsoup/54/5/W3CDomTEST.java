import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given
        Document jsoupDocument = new org.jsoup.nodes.Document();

        // When
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Then
        assertEquals("<document/>", w3cDocument.getDocumentElement().getNodeValue()); // basic check

        // Edge cases and exceptions
        // 1. Test with a null jsoupDocument
        try {
            new W3CDom().fromJsoup(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // 2. Test with an empty jsoupDocument
        Document emptyJsoupDocument = new org.jsoup.nodes.Document();
        Document emptyW3cDocument = new W3CDom().fromJsoup(emptyJsoupDocument);
        assertEquals("<document/>", emptyW3cDocument.getDocumentElement().getNodeValue()); // basic check

        // 3. Test with a jsoupDocument containing elements
        Document jsoupDocumentWithElements = new org.jsoup.nodes.Document();
        jsoupDocumentWithElements.appendElement("html").appendElement("body").appendText("Hello World!");
        Document w3cDocumentWithElements = new W3CDom().fromJsoup(jsoupDocumentWithElements);
        assertEquals("<html><body>Hello World</body></html>", w3cDocumentWithElements.getDocumentElement().getNodeValue()); // basic check

        // 4. Test with a jsoupDocument containing attributes
        Document jsoupDocumentWithAttributes = new org.jsoup.nodes.Document();
        Element html = jsoupDocumentWithAttributes.appendElement("html");
        html.attr("id", "page");
        html.attr("class", "myClass");
        assertEquals("<html id=\"page\" class=\"myClass\"></html>", w3cDocumentWithAttributes.getDocumentElement().getNodeValue()); // basic check

        // 5. Test with a jsoupDocument containing comments
        Document jsoupDocumentWithComments = new org.jsoup.nodes.Document();
        jsoupDocumentWithComments.appendElement("html").appendElement("body").appendComment("This is a comment").appendText("Hello World!");
        Document w3cDocumentWithComments = new W3CDom().fromJsoup(jsoupDocumentWithComments);
        assertEquals("<html><body><!--This is a comment--></body><text>Hello World</text></html>", w3cDocumentWithComments.getDocumentElement().getNodeValue()); // basic check

        // 6. Test with a jsoupDocument containing CDATA sections
        Document jsoupDocumentWithCDATA = new org.jsoup.nodes.Document();
        jsoupDocumentWithCDATA.appendElement("html").appendElement("body").appendElement("script").html("<![CDATA[alert('Hello World');]]>");
        Document w3cDocumentWithCDATA = new W3CDom().fromJsoup(jsoupDocumentWithCDATA);
        assertEquals("<html><body><script><![CDATA[alert('Hello World');]]></script></body></html>", w3cDocumentWithCDATA.getDocumentElement().getNodeValue()); // basic check
    }

    @Test
    public void testConvert() {
        // Given
        Document jsoupDocument = new org.jsoup.nodes.Document();
        jsoupDocument.appendElement("html").appendElement("body").appendText("Hello World!");

        // When
        Document w3cDocument = new W3CDom();
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Then
        assertEquals("<html><body>Hello World</body></html>", w3cDocument.getDocumentElement().getNodeValue()); // basic check

        // Edge cases and exceptions
        // 1. Test with a null jsoupDocument
        try {
            new W3CDom().convert(null, w3cDocument);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // 2. Test with an empty jsoupDocument
        Document emptyJsoupDocument = new org.jsoup.nodes.Document();
        Document emptyW3cDocument = new Document();
        new W3CDom().convert(emptyJsoupDocument, emptyW3cDocument);
        assertEquals("<document/>", emptyW3cDocument.getDocumentElement().getNodeValue()); // basic check

        // 3. Test with a jsoupDocument containing elements
        Document jsoupDocumentWithElements = new org.jsoup.nodes.Document();
        jsoupDocumentWithElements.appendElement("html").appendElement("body").appendElement("div").appendText("Hello World!");
        Document w3cDocumentWithElements = new Document();
        new W3CDom().convert(jsoupDocumentWithElements, w3cDocumentWithElements);
        assertEquals("<html><body><div>Hello World</div></body></html>", w3cDocumentWithElements.getDocumentElement().getNodeValue()); // basic check

        // 4. Test with a jsoupDocument containing attributes
        Document jsoupDocumentWithAttributes = new org.jsoup.nodes.Document();
        jsoupDocumentWithAttributes.appendElement("html").appendElement("body").appendElement("div").attr("id", "myDiv").attr("class", "myClass");
        Document w3cDocumentWithAttributes = new Document();
        new W3CDom().convert(jsoupDocumentWithAttributes, w3cDocumentWithAttributes);
        assertEquals("<html><body><div id=\"myDiv\" class=\"myClass\"></div></body></html>", w3cDocumentWithAttributes.getDocumentElement().getNodeValue()); // basic check

        // 5. Test with a jsoupDocument containing comments
        Document jsoupDocumentWithComments = new org.jsoup.nodes.Document();
        jsoupDocumentWithComments.appendElement("html").appendElement("body").appendComment("This is a comment").appendElement("div").appendText("Hello World!");
        Document w3cDocumentWithComments = new Document();
        new W3CDom().convert(jsoupDocumentWithComments, w3cDocumentWithComments);
        assertEquals("<html><body><!--This is a comment--><div>Hello World</div></body></html>", w3cDocumentWithComments.getDocumentElement().getNodeValue()); // basic check

        // 6. Test with a jsoupDocument containing CDATA sections
        Document jsoupDocumentWithCDATA = new org.jsoup.nodes.Document();
        jsoupDocumentWithCDATA.appendElement("html").appendElement("body").appendElement("script").html("<![CDATA[alert('Hello World');]]>");
        Document w3cDocumentWithCDATA = new Document();
        new W3CDom().convert(jsoupDocumentWithCDATA, w3cDocumentWithCDATA);
        assertEquals("<html><body><script><![CDATA[alert('Hello World');]]></script></body></html>", w3cDocumentWithCDATA.getDocumentElement().getNodeValue()); // basic check
    }

    @Test
    public void testAsString() {
        // Given
        Document w3cDocument = new W3CDom().fromJsoup(new org.jsoup.nodes.Document());

        // When
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);

        // Then
        assertEquals("<document/>", w3cDocumentAsString); // basic check

        // Edge cases and exceptions
        // 1. Test with a null w3cDocument
        try {
            new W3CDom().asString(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // 2. Test with an empty w3cDocument
        Document emptyW3cDocument = new Document();
        String emptyW3cDocumentAsString = new W3CDom().asString(emptyW3cDocument);
        assertEquals("<document/>", emptyW3cDocumentAsString); // basic check

        // 3. Test with a w3cDocument containing elements
        Document w3cDocumentWithElements = new Document();
        w3cDocumentWithElements.appendChild(w3cDocumentWithElements.createProcessingInstruction("xml", "version=\"1.0\""));
        w3cDocumentWithElements.appendChild(w3cDocumentWithElements.createElement("root"));
        String w3cDocumentWithElementsAsString = new W3CDom().asString(w3cDocumentWithElements);
        assertEquals("<?xml version=\"1.0\"?><root/>", w3cDocumentWithElementsAsString); // basic check

        // 4. Test with a w3cDocument containing attributes
        Document w3cDocumentWithAttributes = new Document();
        w3cDocumentWithAttributes.appendChild(w3cDocumentWithAttributes.createProcessingInstruction("xml", "version=\"1.0\""));
        w3cDocumentWithAttributes.createElement("root").setAttribute("id", "myRoot").setAttribute("class", "myClass");
        String w3cDocumentWithAttributesAsString = new W3CDom().asString(w3cDocumentWithAttributes);
        assertEquals("<?xml version=\"1.0\"?><root id=\"myRoot\" class=\"myClass\"/>", w3cDocumentWithAttributesAsString); // basic check

        // 5. Test with a w3cDocument containing comments
        Document w3cDocumentWithComments = new Document();
        w3cDocumentWithComments.appendChild(w3cDocumentWithComments.createProcessingInstruction("xml", "version=\"1.0\""));
        w3cDocumentWithComments.createComment("This is a comment");
        String w3cDocumentWithCommentsAsString = new W3CDom().asString(w3cDocumentWithComments);
        assertEquals("<?xml version=\"1.0\"?><!--This is a comment-->", w3cDocumentWithCommentsAsString); // basic check

        // 6. Test with a w3cDocument containing CDATA sections
        Document w3cDocumentWithCDATA = new Document();
        w3cDocumentWithCDATA.appendChild(w3cDocumentWithCDATA.createProcessingInstruction("xml", "version=\"1.0\""));
        w3cDocumentWithCDATA.createElement("script").setTextContent("<![CDATA[alert('Hello World');]]>");
        String w3cDocumentWithCDATAAsString = new W3CDom().asString(w3cDocumentWithCDATA);
        assertEquals("<?xml version=\"1.0\"?><script><![CDATA[alert('Hello World');]]></script>", w3cDocumentWithCDATAAsString); // basic check
    }
}