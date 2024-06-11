import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeConstruction() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctypeToken.tokenType());
    }

    @Test
    public void testDoctypeSettingName() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeSettingNameNull() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName(null);
    }

    @Test
    public void testDoctypeSettingPublicIdentifier() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().setPublicIdentifier("_public");
        Assert.assertEquals("_public", doctypeToken.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeSettingSystemIdentifier() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().setSystemIdentifier("_system");
        Assert.assertEquals("_system", doctypeToken.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeSettingForceQuirks() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagConstruction() {
        Token tagToken = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, tagToken.tokenType());
    }

    @Test
    public void testTagSettingName() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().name("div");
        Assert.assertEquals("div", tagToken.asStartTag().name());
    }

    @Test
    public void testTagSettingNameNull() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().name(null);
        Assert.assertNull(tagToken.asStartTag().name());
    }

    @Test
    public void testTagSettingSelfClosing() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().isSelfClosing(true);
        Assert.assertTrue(tagToken.asStartTag().isSelfClosing());
    }

    @Test
    public void testTagSettingAttributes() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().newAttribute();
        tagToken.asStartTag().appendAttributeName("class");
        tagToken.asStartTag().appendAttributeValue("test");
        tagToken.asStartTag().newAttribute();
        Assert.assertEquals(1, tagToken.asStartTag().getAttributes().size());
        Assert.assertEquals("class", tagToken.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tagToken = new Token.StartTag();
        tagToken.asStartTag().name("div");
        tagToken.asStartTag().newAttribute();
        tagToken.asStartTag().appendAttributeName("class");
        tagToken.asStartTag().appendAttributeValue("test");
        tagToken.asStartTag().finaliseTag();
        // check that attributes were set properly
        Assert.assertEquals(1, tagToken.asStartTag().getAttributes().size());
        Assert.assertEquals("class", tagToken.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertEquals("test", tagToken.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testCommentConstruction() {
        Token commentToken = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, commentToken.tokenType());
    }

    @Test
    public void testCommentSettingData() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().appendData("This is a comment");
        Assert.assertEquals("This is a comment", commentToken.asComment().getData());
    }

    @Test
    public void testCharacterConstruction() {
        Token characterToken = new Token.Character('A');
        Assert.assertEquals(TokenType.Character, characterToken.tokenType());
    }

    @Test
    public void testCharacterSettingData() {
        Token characterToken = new Token.Character('A');
        characterToken.asCharacter().appendData("B");
        Assert.assertEquals("AB", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFConstruction() {
        Token eofToken = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eofToken.tokenType());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token tagToken = new Token.StartTag();
        Assert.assertFalse(tagToken.isDoctype());
        Assert.assertFalse(tagToken.isStartTag());
        Assert.assertFalse(tagToken.isEndTag());
        Assert.assertFalse(tagToken.isComment());
        Assert.assertFalse(tagToken.isCharacter());
        Assert.assertFalse(tagToken.isEOF());
        Tag tag = tagToken.asStartTag();
        Assert.assertTrue(tagToken.isStartTag());
        Assert.assertTrue(tag.isStartTag());
    }

    @Test
    public void testTokenTypeIdentificationDoctype() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertFalse(doctypeToken.isDoctype());
        Assert.assertFalse(doctypeToken.isStartTag());
        Assert.assertFalse(doctypeToken.isEndTag());
        Assert.assertFalse(doctypeToken.isComment());
        Assert.assertFalse(doctypeToken.isCharacter());
        Assert.assertFalse(doctypeToken.isEOF());
        Doctype doctype = doctypeToken.asDoctype();
        Assert.assertTrue(doctypeToken.isDoctype());
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testTokenTypeIdentificationComment() {
        Token commentToken = new Token.Comment();
        Assert.assertFalse(commentToken.isDoctype());
        Assert.assertFalse(commentToken.isStartTag());
        Assert.assertFalse(commentToken.isEndTag());
        Assert.assertTrue(commentToken.isComment());
        Assert.assertFalse(commentToken.isCharacter());
        Assert.assertFalse(commentToken.isEOF());
        Comment comment = commentToken.asComment();
        Assert.assertTrue(commentToken.isComment());
        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testTokenTypeIdentificationCharacter() {
        Token characterToken = new Token.Character('A');
        Assert.assertFalse(characterToken.isDoctype());
        Assert.assertFalse(characterToken.isStartTag());
        Assert.assertFalse(characterToken.isEndTag());
        Assert.assertFalse(characterToken.isComment());
        Assert.assertTrue(characterToken.isCharacter());
        Assert.assertFalse(characterToken.isEOF());
        Character charac = characterToken.asCharacter();
        Assert.assertTrue(characterToken.isCharacter());
        Assert.assertTrue(charac.isCharacter());
    }

    @Test
    public void testTokenTypeIdentificationEndTag() {
        Token endTagToken = new Token.EndTag();
        Assert.assertFalse(endTagToken.isDoctype());
        Assert.assertFalse(endTagToken.isStartTag());
        Assert.assertTrue(endTagToken.isEndTag());
        Assert.assertFalse(endTagToken.isComment());
        Assert.assertFalse(endTagToken.isCharacter());
        Assert.assertFalse(endTagToken.isEOF());
        EndTag end = endTagToken.asEndTag();
        Assert.assertTrue(endTagToken.isEndTag());
        Assert.assertTrue(end.isEndTag());
    }

    @Test
    public void testTokenTypeIdentificationStartTag() {
        Token startTagToken = new Token.StartTag();
        Assert.assertFalse(startTagToken.isDoctype());
        Assert.assertTrue(startTagToken.isStartTag());
        Assert.assertFalse(startTagToken.isEndTag());
        Assert.assertFalse(startTagToken.isComment());
        Assert.assertFalse(startTagToken.isCharacter());
        Assert.assertFalse(startTagToken.isEOF());
        StartTag start = startTagToken.asStartTag();
        Assert.assertTrue(startTagToken.isStartTag());
        Assert.assertTrue(start.isStartTag());
    }

    @Test
    public void testTokenTypeIdentificationEOF() {
        Token eofToken = new Token.EOF();
        Assert.assertFalse(eofToken.isDoctype());
        Assert.assertFalse(eofToken.isStartTag());
        Assert.assertFalse(eofToken.isEndTag());
        Assert.assertFalse(eofToken.isComment());
        Assert.assertFalse(eofToken.isCharacter());
        Assert.assertTrue(eofToken.isEOF());
        EOF endOfFile = eofToken.asEOF();
        Assert.assertTrue(eofToken.isEOF());
        Assert.assertTrue(endOfFile.isEOF());
    }
}