import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @BeforeMethod
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id='test-div'></div></body></html>");
    }

    @Test
    public void testFromJsoup_validInput_returnsW3CDocument() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assert w3cDoc != null;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromJsoup_nullInput_throwsIllegalArgumentException() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_validInput_convertsToW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document expectedW3CDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, expectedW3CDoc);

            String jsoupHtml = jsoupDoc.html();
            org.w3c.dom.Document convertedDoc = builder.parse(new InputSource(new StringReader(jsoupHtml)));

            NodeList jsoupNodes = expectedW3CDoc.getElementsByTagName("*");
            NodeList convertedNodes = convertedDoc.getDocumentElement().getChildNodes();

            assert jsoupNodes.getLength() == convertedNodes.getLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString_validInput_returnsXmlString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assert xmlString != null;
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testAsString_nullInput_throwsIllegalStateException() {
        w3cDom.asString(null);
    }
}