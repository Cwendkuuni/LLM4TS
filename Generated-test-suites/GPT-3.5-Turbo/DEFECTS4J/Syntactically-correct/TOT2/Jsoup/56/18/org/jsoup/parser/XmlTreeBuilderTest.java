import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertNotNull(builder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml>test</xml>";
        String baseUri = "http://example.com";
        assertNotNull(builder.parse(input, baseUri));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        builder.insertNode(element);
        assertEquals(element, builder.currentElement().childNode(0));
    }

    // Add more test cases for other methods as per requirements

}