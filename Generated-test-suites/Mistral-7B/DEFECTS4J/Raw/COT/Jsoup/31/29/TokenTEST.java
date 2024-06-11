import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype token = new Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        Doctype token = new Doctype();
        token.name("html");
        token.publicIdentifier("_public");
        token.systemIdentifier("_system");
        assertEquals("html", token.getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Doctype token = new Doctype();
        token.name("html");
        token.publicIdentifier("_public");
        token.systemIdentifier("_system");
        assertEquals("_public", token.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Doctype token = new Doctype();
        token.name("html");
        token.publicIdentifier("_public");
        token.systemIdentifier("_system");
        assertEquals("_system", token.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Doctype token = new Doctype();
        token.name("html");
        token.publicIdentifier("_public");
        token.systemIdentifier("_system");
        token.isForceQuirks();
        assertTrue(token.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_isForceQuirks_false() {
        Doctype token = new Doctype();
        token.name("html");
        token.publicIdentifier("_public");
        token.systemIdentifier("_system");
        assertFalse(token.isForceQuirks());
    }

    // ... Similar test cases for other methods in the Token class

    @Test
    public void testTag_newAttribute() {
        Tag token = new StartTag("tag");
        token.newAttribute();
        token.newAttribute();
        Assert.assertNotNull(token.getAttributes());
        Assert.assertEquals(2, token.getAttributes().size());
    }

    @Test
    public void testTag_newAttribute_nullAttributeName() {
        Tag token = new StartTag("tag");
        token.newAttribute();
        token.newAttribute(null);
        Assert.assertNotNull(token.getAttributes());
        Assert.assertEquals(1, token.getAttributes().size());
        Assert.assertNull(token.getAttributes().getValue(""));
    }

    @Test
    public void testTag_newAttribute_emptyAttributeValue() {
        Tag token = new StartTag("tag");
        token.newAttribute("attr");
        token.newAttribute("attr", "");
        Assert.assertNotNull(token.getAttributes());
        Assert.assertEquals(1, token.getAttributes().size());
        Assert.assertEquals("", token.getAttributes().getValue("attr"));
    }

    @Test
    public void testTag_finaliseTag() {
        StartTag token = new StartTag("tag");
        token.finaliseTag();
        Assert.assertNotNull(token.getAttributes());
        Assert.assertEquals(0, token.getAttributes().size());
    }

    @Test
    public void testTag_appendTagName() {
        Tag token = new StartTag("tag");
        token.appendTagName("test");
        assertEquals("tagtest", token.name());
    }

    @Test
    public void testTag_appendAttributeName() {
        Tag token = new StartTag("tag");
        token.appendAttributeName("attr");
        token.appendAttributeName("attr");
        assertEquals("tag attr attr", token.toString());
    }

    @Test
    public void testTag_appendAttributeValue() {
        Tag token = new StartTag("tag");
        token.appendAttributeValue("value");
        token.appendAttributeValue("value");
        assertEquals("<tag attr=\"valuevalue\" />", token.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTag_appendAttributeValue_null() {
        Tag token = new StartTag("tag");
        token.appendAttributeValue(null);
    }

    @Test
    public void testTag_isSelfClosing() {
        StartTag token = new StartTag("img");
        token.selfClosing = true;
        assertTrue(token.isSelfClosing());
    }

    @Test
    public void testTag_isSelfClosing_false() {
        StartTag token = new StartTag("div");
        assertFalse(token.isSelfClosing());
    }

    // ... Add more test cases for other methods in the Token class, including edge cases and exception handling scenarios

    @Test(expected = IllegalStateException.class)
    public void testToken_isCharacter_nullData() {
        Character token = new Character(null);
    }

    @Test
    public void testToken_isStartTag_nullName() {
        StartTag token = new StartTag(null);
        assertFalse(token.isStartTag());
    }

    @Test
    public void testToken_isStartTag_emptyName() {
        StartTag token = new StartTag("");
        assertFalse(token.isStartTag());
    }

    @Test
    public void testToken_isEndTag_nullName() {
        EndTag token = new EndTag(null);
        assertFalse(token.isEndTag());
    }

    @Test
    public void testToken_isEndTag_emptyName() {
        EndTag token = new EndTag("");
        assertFalse(token.isEndTag());
    }

    @Test
    public void testToken_isComment_emptyData() {
        Comment token = new Comment();
        assertFalse(token.isComment());
    }

    @Test
    public void testToken_isComment_nullData() {
        Comment token = new Comment(null);
        assertFalse(token.isComment());
    }

    @Test
    public void testToken_isEOF() {
        EOF token = new EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testToken_isDoctype() {
        Doctype token = new Doctype();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testToken_isStartTag_invalidType() {
        Token token = new Token();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testToken_isEndTag_invalidType() {
        Token token = new Token();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testToken_isComment_invalidType() {
        Token token = new Token();
        assertFalse(token.isComment());
    }

    @Test
    public void testToken_isCharacter_invalidType() {
        Token token = new Token();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testToken_isEOF_invalidType() {
        Token token = new Token();
        assertFalse(token.isEOF());
    }
}