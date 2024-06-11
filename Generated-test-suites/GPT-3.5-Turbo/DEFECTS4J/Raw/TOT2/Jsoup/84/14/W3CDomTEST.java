import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("<html></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        String xmlString = w3cDom.asString(w3cDoc);

        assertNotNull(xmlString);
    }
}