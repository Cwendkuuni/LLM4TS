import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, treeBuilder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
    }

    // Add more test cases for other public methods here

    // Edge cases and exception handling scenarios to be tested:

    // 1. Empty input in parse method

    // 2. Invalid baseURI in parse method

    // 3. Null input in initialiseParse method

    // 4. Different token types in process method

    // 5. Self-closing tag in insert method

    // 6. Comment with special characters in insert method

    // 7. Character with special characters in insert method

    // 8. Doctype with invalid identifiers in insert method

    // 9. Non-existing element in popStackToClose method

}