import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testParse() {
        String xmlContent = "<root><child>Hello</child></root>";
        String baseUri = "http://example.com";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlContent), baseUri);
        assertNotNull(xmlDoc);
    }
}