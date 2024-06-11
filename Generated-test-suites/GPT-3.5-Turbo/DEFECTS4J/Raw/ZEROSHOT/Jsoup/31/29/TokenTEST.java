import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character("");
        eof = new Token.EOF();
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeGetterAndSetter() {
        doctype.name.append("name");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("publicId", doctype.getPublicIdentifier());
        assertEquals("systemId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        startTag.tagName = "tag";

        assertEquals("tag", startTag.name());
        assertEquals(startTag.tagName, startTag.name());
    }

    @Test
    public void testStartTagAttributes() {
        Token.Attributes attributes = new Token.Attributes();
        attributes.put(new Token.Attribute("attribute", "value"));
        startTag.attributes = attributes;

        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testStartTagSelfClosing() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagName() {
        endTag.tagName = "tag";

        assertEquals("tag", endTag.name());
        assertEquals(endTag.tagName, endTag.name());
    }

    @Test
    public void testCommentData() {
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterData() {
        character = new Token.Character("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFType() {
        assertTrue(eof.isEOF());
    }
}