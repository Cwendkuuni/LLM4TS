import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws Exception {
        // Given
        Document jsoupDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

        // When
        String w3cDocumentString = w3CDom.asString(w3cDocument);

        // Then
        String expectedXml = IOUtils.resourceToString(getClass().getResource("/expected_output.xml"));
        Assert.assertEquals(expectedXml, w3cDocumentString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() throws Exception {
        // Given
        Document jsoupDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

        // When
        Document convertedJsoupDocument = w3CDom.fromJsoup(w3cDocument);

        // Then
        String convertedJsoupDocumentString = w3CDom.asString(convertedJsoupDocument);
        String expectedConvertedJsoupDocumentString = IOUtils.resourceToString(getClass().getResource("/expected_output.html"));
        Assert.assertEquals(expectedConvertedJsoupDocumentString, convertedJsoupDocumentString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        W3CDom w3CDom = new W3CDom();
        w3CDom.convert(null, null);
    }

    @Test
    public void testAsString() throws Exception {
        // Given
        Document w3cDocument = W3CDom.fromJsoup(Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>"));

        // When
        String w3cDocumentString = w3CDom.asString(w3cDocument);

        // Then
        String expectedXml = IOUtils.resourceToString(getClass().getResource("/expected_output.xml"));
        Assert.assertEquals(expectedXml, w3cDocumentString);

        // When
        Document jsoupDocument = w3CDom.fromJsoup(w3cDocument);
        String jsoupDocumentString = w3CDom.asString(jsoupDocument);

        // Then
        String expectedJsoupDocumentString = IOUtils.resourceToString(getClass().getResource("/expected_output.html"));
        Assert.assertEquals(expectedJsoupDocumentString, jsoupDocumentString);
    }
}