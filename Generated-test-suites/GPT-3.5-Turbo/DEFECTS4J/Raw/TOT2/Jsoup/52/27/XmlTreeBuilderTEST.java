import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "https://example.com", new ParseErrorList());
        assertEquals(xmlTreeBuilder.doc.childNodes().size(), 1);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.StartTag, "tag");
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "https://example.com", new ParseErrorList());
        xmlTreeBuilder.insertNode(new Document());
        assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
    }

    // Add more test cases for other methods as needed

}