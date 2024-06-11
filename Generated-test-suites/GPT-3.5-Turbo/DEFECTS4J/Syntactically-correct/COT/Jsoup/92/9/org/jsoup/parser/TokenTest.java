import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01//EN";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
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
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;

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
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "Hello, world!";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testResetCData() {
        Token.CData cdata = new Token.CData("This is CDATA");

        cdata.reset();

        assertNull(cdata.data);
    }

    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }
}