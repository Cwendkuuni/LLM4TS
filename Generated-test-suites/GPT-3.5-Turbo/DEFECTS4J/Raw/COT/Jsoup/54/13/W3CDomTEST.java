import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class W3CDomTest {
    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<body><h1>Hello, World!</h1></body>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
        org.w3c.dom.Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("body", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document jsoupDoc2 = Parser.parseBodyFragment("");
        w3cDom.convert(jsoupDoc, jsoupDoc2);
        Assert.assertEquals(jsoupDoc.outerHtml(), jsoupDoc2.outerHtml());
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xml = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(xml);
        Assert.assertTrue(xml.contains("<h1>Hello, World!</h1>"));
    }
}