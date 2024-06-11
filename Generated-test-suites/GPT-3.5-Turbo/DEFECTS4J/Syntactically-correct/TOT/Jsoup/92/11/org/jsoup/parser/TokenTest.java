import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.CData cdataToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        cdataToken = new Token.CData();
        eofToken = new Token.EOF();
    }

    /* Test cases for Doctype */

    @Test
    public void testDoctypeReset() {
        doctypeToken.name.append("html");
        doctypeToken.pubSysKey = "pub";
        doctypeToken.publicIdentifier.append("pubId");
        doctypeToken.systemIdentifier.append("sysId");
        doctypeToken.forceQuirks = true;

        doctypeToken.reset();

        assertEquals("", doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertEquals("", doctypeToken.getPublicIdentifier());
        assertEquals("", doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }

    /* Test cases for StartTag */

    @Test
    public void testStartTagReset() {
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        startTagToken.nameAttr("tagname", attributes);
        startTagToken.selfClosing = true;

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

    /* Test cases for EndTag */

    @Test
    public void testEndTagReset() {
        endTagToken.tagName = "endtagname";
        endTagToken.normalName = "endtagname";

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

    /* Test cases for Comment */

    @Test
    public void testCommentReset() {
        commentToken.data.append("commentdata");
        commentToken.bogus = true;

        commentToken.reset();

        assertEquals("", commentToken.getData());
        assertFalse(commentToken.bogus);
    }

    /* Test cases for Character */

    @Test
    public void testCharacterReset() {
        characterToken.data("characterdata");

        characterToken.reset();

        assertNull(characterToken.getData());
    }

    /* Test cases for CData */

    @Test
    public void testCDataToString() {
        cdataToken.data("cdatadata");

        String expected = "<![CDATA[cdatadata]]>";
        String actual = cdataToken.toString();

        assertEquals(expected, actual);
    }

    /* Test cases for EOF */

    @Test
    public void testEOFReset() {
        eofToken.reset();

        assertEquals(eofToken, eofToken.reset());
    }
}