import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("//W3C");
        doctype.systemIdentifier.append("//W3C//DTD HTML 4.0//EN");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertTrue(resetDoctype.name.toString().isEmpty());
        assertNull(resetDoctype.pubSysKey);
        assertTrue(resetDoctype.publicIdentifier.toString().isEmpty());
        assertTrue(resetDoctype.systemIdentifier.toString().isEmpty());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = "wrapper";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertTrue(resetStartTag.pendingAttributeValue.toString().isEmpty());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.isSelfClosing());
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        Token.EndTag resetEndTag = endTag.reset();

        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertTrue(resetComment.data.toString().isEmpty());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    @Test
    public void testCDataReset() {
        Token.CData cData = new Token.CData("This is a CDATA");

        Token.CData resetCData = cData.reset();

        assertNull(resetCData.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();

        assertNotNull(resetEOF);
    }

}