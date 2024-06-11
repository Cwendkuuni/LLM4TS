import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    private Token startTag;
    
    @Before
    public void setup() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("id", "test"));
        startTag = new Token.StartTag("div", attributes);
    }
    
    @Test
    public void testTokenType() {
        assertEquals("StartTag", startTag.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    // Additional test cases for other methods can be added here
    
}