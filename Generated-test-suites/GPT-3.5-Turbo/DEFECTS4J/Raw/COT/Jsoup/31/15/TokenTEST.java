
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    // Tests for the Doctype class
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("PUBLIC");
        assertEquals("PUBLIC", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("SYSTEM");
        assertEquals("SYSTEM", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // Tests for the StartTag class
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    // Tests for the EndTag class
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag("div");
        assertEquals("div", endTag.name());
    }

    // Tests for the Comment class
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    // Tests for the Character class
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Test");
        assertEquals("Test", character.getData());
    }

    // Tests for the EOF class
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}
