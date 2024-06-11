import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() throws Exception {
        String xmlString = "<xml><tag1>Hello</tag1><tag2>World</tag2></xml>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");
        assertNotNull(xmlDoc);
    }
}