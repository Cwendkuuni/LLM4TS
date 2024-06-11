
import org.jsoup.helper.W3CDom;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoupConvertsJsoupDocumentToW3CDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello world!</h1></body></html>");

        // Act
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        String actual = w3cDom.asString(w3cDoc);
        assertEquals("<html><body><h1>Hello world!</h1></body></html>", actual);
    }

    @Test
    public void testConvertConvertsJsoupDocumentToW3CDocument() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello world!</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        org.w3c.dom.Document out = w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        String actual = w3cDom.asString(out);
        assertEquals("<html><body><h1>Hello world!</h1></body></html>", actual);
    }

    @Test
    public void testAsStringSerializesW3CDocumentToString() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello world!</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act
        String actual = w3cDom.asString(w3cDoc);

        // Assert
        assertEquals("<html><body><h1>Hello world!</h1></body></html>", actual);
    }

}
