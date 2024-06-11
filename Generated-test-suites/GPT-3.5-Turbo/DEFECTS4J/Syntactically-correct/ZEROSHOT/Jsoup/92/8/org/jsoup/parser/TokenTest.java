import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cdata;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.setName("name");
        doctype.setPubSysKey("pubSysKey");
        doctype.setPublicIdentifier("publicIdentifier");
        doctype.setSystemIdentifier("systemIdentifier");
        doctype.setForceQuirks(true);

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.setNameAttr("tagName", new org.jsoup.nodes.Attributes());

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.setName("tagName");

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.setData("data");
        comment.setBogus(true);

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        character.setData("data");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCDataReset() {
        cdata.setData("data");

        cdata.reset();

        assertEquals("", cdata.getData());
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", Token.TokenType.Doctype.toString());
        assertEquals("StartTag", Token.TokenType.StartTag.toString());
        assertEquals("EndTag", Token.TokenType.EndTag.toString());
        assertEquals("Comment", Token.TokenType.Comment.toString());
        assertEquals("Character", Token.TokenType.Character.toString());
        assertEquals("EOF", Token.TokenType.EOF.toString());
    }
}