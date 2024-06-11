import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<html><head></head><body><h1>Test</h1><div id=\"test\"></div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        w3CDom.convert(jsoupDoc, w3cDoc);

        Element root = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", root.getTagName());

        Element head = (Element) root.getElementsByTagName("head").item(0);
        Assert.assertNotNull(head);

        Element body = (Element) root.getElementsByTagName("body").item(0);
        Assert.assertNotNull(body);

        Elements h1Elements = body.getElementsByTag("h1");
        Assert.assertEquals(1, h1Elements.size());
        Element h1 = h1Elements.first();
        Assert.assertEquals("Test", h1.text());

        Elements divElements = body.getElementsByTag("div");
        Assert.assertEquals(1, divElements.size());
        Element div = divElements.first();
        Assert.assertEquals("test", div.id());

        Assert.assertEquals("UTF-8", w3cDoc.getXmlEncoding());
        Assert.assertEquals("1.0", w3cDoc.getXmlVersion());
    }

    @Test
    public void testConvert_withEmptyInput() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        org.jsoup.nodes.Document emptyDoc = Parser.htmlParser().parseInput("");
        w3CDom.convert(emptyDoc, w3cDoc);

        Element root = w3cDoc.getDocumentElement();
        Assert.assertNotNull(root);
    }

    @Test
    public void testAsString() {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        w3CDom.convert(jsoupDoc, w3cDoc);

        String w3cDocString = w3CDom.asString(w3cDoc);
        Assert.assertNotNull(w3cDocString);
        Assert.assertFalse(w3cDocString.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_withNullInput() {
        w3CDom.fromJsoup(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvert_withNullJsoupDoc() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document w3cDoc = builder.newDocument();

        w3CDom.convert(null, w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvert_withNullW3CDoc() {
        w3CDom.convert(jsoupDoc, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsString_withNullDoc() {
        w3CDom.asString(null);
    }
}