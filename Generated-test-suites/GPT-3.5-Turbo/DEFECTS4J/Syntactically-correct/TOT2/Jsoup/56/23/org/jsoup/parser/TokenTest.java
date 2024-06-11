import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("example");
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.selfClosing = true;
        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(true, tag.selfClosing);
        assertEquals(null, tag.attributes);
    }

    // More test cases can be added for other methods in the Token class

}