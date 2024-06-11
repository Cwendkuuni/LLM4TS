import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class W3CDomTest {

    @Test
    public void testFromJsoup_typicalUseCase() {
        org.jsoup.nodes.Document jsoupDoc = getTypicalJsoupDoc();
        W3CDom w3cDom = new W3CDom();

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
        // Assert that important attributes are correctly transformed
        Assert.assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }

    @Test
    public void testFromJsoup_nullInput() {
        W3CDom w3cDom = new W3CDom();

        try {
            Document w3cDoc = w3cDom.fromJsoup(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception, test passed
        }
    }

    @Test
    public void testConvert_typicalUseCase() {
        org.jsoup.nodes.Document jsoupDoc = getTypicalJsoupDoc();
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document outputW3cDoc = createEmptyW3CDoc();

        w3cDom.convert(jsoupDoc, outputW3cDoc);

        // Assert that the conversion is successful
        Assert.assertEquals(asString(w3cDoc), asString(outputW3cDoc));
    }

    @Test
    public void testConvert_stringInput() {
        String jsoupHtml = "<html><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(jsoupHtml);
        W3CDom w3cDom = new W3CDom();
        Document outputW3cDoc = createEmptyW3CDoc();

        w3cDom.convert(jsoupDoc, outputW3cDoc);

        // Assert that the conversion is successful
        Assert.assertEquals(jsoupHtml, asString(outputW3cDoc));
    }

    @Test
    public void testAsString_typicalUseCase() {
        org.jsoup.nodes.Document jsoupDoc = getTypicalJsoupDoc();
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String w3cDocString = w3cDom.asString(w3cDoc);

        // Assert that the transformation to string is successful
        Assert.assertNotNull(w3cDocString);
        // Assert that the resulting string is equal to the original document's string representation
        Assert.assertEquals(jsoupDoc.toString(), w3cDocString);
    }

    private org.jsoup.nodes.Document getTypicalJsoupDoc() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        return Jsoup.parse(html);
    }

    private Document createEmptyW3CDoc() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private String asString(Document document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }

}