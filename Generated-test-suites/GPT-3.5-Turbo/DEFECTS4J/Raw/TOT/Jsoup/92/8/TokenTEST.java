import org.jsoup.parser.Attributes;
import org.jsoup.parser.Token;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private static Token.Doctype doctypeToken;
    private static Token.StartTag startTagToken;
    private static Token.EndTag endTagToken;
    private static Token.Comment commentToken;
    private static Token.Character characterToken;

    @BeforeClass
    public static void setup() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
    }

    // Doctype

    @Test
    public void testDoctypeReset() {
        doctypeToken.reset();
        assertEquals("", doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertEquals("", doctypeToken.getPublicIdentifier());
        assertEquals("", doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
        doctypeToken.name.append("html");
        assertEquals("html", doctypeToken.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        doctypeToken.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctypeToken.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctypeToken.publicIdentifier.append("\"-//W3C//DTD HTML 4.01 Transitional//EN\"");
        assertEquals("\"-//W3C//DTD HTML 4.01 Transitional//EN\"", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctypeToken.systemIdentifier.append("\"http://www.w3.org/TR/html4/loose.dtd\"");
        assertEquals("\"http://www.w3.org/TR/html4/loose.dtd\"", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctypeToken.isForceQuirks());
        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());
    }

    // StartTag

    @Test
    public void testStartTagReset() {
        startTagToken.reset();
        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertEquals("", startTagToken.pendingAttributeValue.toString());
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testStartTagName() {
        startTagToken.name = "div";
        assertEquals("div", startTagToken.name());
    }

    @Test
    public void testStartTagNormalName() {
        startTagToken.name = "DIV";
        assertEquals("div", startTagToken.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTagToken.isSelfClosing());
        startTagToken.selfClosing = true;
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        startTagToken.attributes = attributes;
        assertEquals(attributes, startTagToken.getAttributes());
    }

    // EndTag

    @Test
    public void testEndTagReset() {
        endTagToken.reset();
        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertNull(endTagToken.pendingAttributeName);
        assertEquals("", endTagToken.pendingAttributeValue.toString());
        assertNull(endTagToken.pendingAttributeValueS);
        assertFalse(endTagToken.hasEmptyAttributeValue);
        assertFalse(endTagToken.hasPendingAttributeValue);
        assertFalse(endTagToken.selfClosing);
        assertNull(endTagToken.attributes);
    }

    // Comment

    @Test
    public void testCommentReset() {
        commentToken.reset();
        assertEquals("", commentToken.getData());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testCommentGetData() {
        commentToken.data.append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
    }

    // Character

    @Test
    public void testCharacterReset() {
        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testCharacterData() {
        characterToken.data("This is a character");
        assertEquals("This is a character", characterToken.getData());
    }

    // EOF

    @Test
    public void testEOFReset() {
        EOF eofToken = new EOF();
        assertEquals(eofToken, eofToken.reset());
    }

}