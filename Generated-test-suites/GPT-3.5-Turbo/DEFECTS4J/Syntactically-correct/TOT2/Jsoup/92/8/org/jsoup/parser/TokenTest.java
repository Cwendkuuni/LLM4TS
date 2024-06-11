import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "pubSysTest";
        doctype.publicIdentifier.append("publicTest");
        doctype.systemIdentifier.append("systemTest");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.selfClosing = false;

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("div");
        tag.appendAttributeName("id");
        tag.setEmptyAttributeValue();
        tag.finaliseTag();

        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());
        assertTrue(tag.attributes.hasKey("id"));
        assertEquals("", tag.attributes.get("id"));
    }

    // Add more test cases for other methods

}