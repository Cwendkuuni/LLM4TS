import org.jsoup.parser.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
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
    public void setup() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testResetDoctype() {
        doctype.name.append("example");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("1234");
        doctype.systemIdentifier.append("5678");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("1234");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "id";
        endTag.pendingAttributeValue.append("1234");
        endTag.pendingAttributeValueS = null;
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.attributes);
    }

    @Test
    public void testResetComment() {
        comment.data.append("example");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testResetCharacter() {
        character.data = "example";

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testResetCData() {
        cdata.data = "example";

        cdata.reset();

        assertNull(cdata.getData());
    }

    @Test
    public void testResetEOF() {
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(cdata.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(cdata.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(cdata.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(cdata.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(cdata.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsCData() {
        assertFalse(doctype.isCData());
        assertFalse(startTag.isCData());
        assertFalse(endTag.isCData());
        assertFalse(comment.isCData());
        assertFalse(character.isCData());
        assertTrue(cdata.isCData());
        assertFalse(eof.isCData());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertFalse(cdata.isEOF());
        assertTrue(eof.isEOF());
    }
}