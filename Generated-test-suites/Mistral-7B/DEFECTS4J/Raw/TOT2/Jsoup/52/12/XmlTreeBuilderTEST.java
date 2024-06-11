import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String xmlInput = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse(xmlInput, "", Parser.xmlParser(xmlTreeBuilder));
        Assert.assertNotNull(document);
        Assert.assertEquals("root", document.tagName());
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("child", document.childNodes().get(0).tagName());
    }
}