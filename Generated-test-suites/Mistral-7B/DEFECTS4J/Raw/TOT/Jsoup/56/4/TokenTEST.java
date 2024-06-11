import org.junit.Assert;
import org.junit.Test;
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
    public void testDoctypeToken() {
        Doctype doctypeToken = new Doctype();
        doctypeToken.name.append("html");
        doctypeToken.publicIdentifier.append("Public ID");
        doctypeToken.systemIdentifier.append("System ID");

        Doctype expectedDoctype = new Doctype();
        expectedDoctype.name.append("html");
        expectedDoctype.publicIdentifier.append("Public ID");
        expectedDoctype.systemIdentifier.append("System ID");
        expectedDoctype.forceQuirks = false;

        Assert.assertEquals(expectedDoctype, doctypeToken);

        // Test reset method
        Doctype resetDoctypeToken = doctypeToken.reset();
        Assert.assertNotEquals(doctypeToken, resetDoctypeToken);
        Assert.assertEquals(TokenType.Doctype, resetDoctypeToken.type);
        Assert.assertNull(resetDoctypeToken.name);
        Assert.assertNull(resetDoctypeToken.publicIdentifier);
        Assert.assertNull(resetDoctypeToken.systemIdentifier);
        Assert.assertFalse(resetDoctypeToken.forceQuirks);
    }

    @Test
    public void testStartTagToken() {
        Tag startTagToken = new StartTag();
        startTagToken.name("html");

        Tag expectedStartTag = new StartTag();
        expectedStartTag.name("html");
        expectedStartTag.normalName("html");

        Assert.assertEquals(expectedStartTag, startTagToken);

        // Test reset method
        Tag resetStartTagToken = startTagToken.reset();
        Assert.assertNotEquals(startTagToken, resetStartTagToken);
        Assert.assertEquals(TokenType.StartTag, resetStartTagToken.type);
        Assert.assertNull(resetStartTagToken.tagName);
        Assert.assertNull(resetStartTagToken.normalName);
        Assert.assertNull(resetStartTagToken.pendingAttributeName);
        Assert.assertNull(resetStartTagToken.pendingAttributeValue);
        Assert.assertNull(resetStartTagToken.pendingAttributeValueS);
        Assert.assertFalse(resetStartTagToken.hasEmptyAttributeValue);
        Assert.assertFalse(resetStartTagToken.hasPendingAttributeValue);
        Assert.assertFalse(resetStartTagToken.selfClosing);
        Assert.assertNull(resetStartTagToken.attributes);

        // Test new Attribute method
        startTagToken.newAttribute();
        Attribute attribute = new Attribute("class", "test");
        startTagToken.attributes.put(attribute);
        Attribute expectedAttribute = new Attribute("class", "test");
        Assert.assertEquals(expectedAttribute, startTagToken.attributes.get("class"));
    }

    @Test
    public void testEndTagToken() {
        Tag endTagToken = new EndTag();
        endTagToken.name("html");

        Tag expectedEndTag = new EndTag();
        expectedEndTag.name("html");
        expectedEndTag.normalName("html");

        Assert.assertEquals(expectedEndTag, endTagToken);

        // Test reset method
        Tag resetEndTagToken = endTagToken.reset();
        Assert.assertNotEquals(endTagToken, resetEndTagToken);
        Assert.assertEquals(TokenType.EndTag, resetEndTagToken.type);
        Assert.assertNull(resetEndTagToken.tagName);
        Assert.assertNull(resetEndTagToken.normalName);
        Assert.assertNull(resetEndTagToken.pendingAttributeName);
        Assert.assertNull(resetEndTagToken.pendingAttributeValue);
        Assert.assertNull(resetEndTagToken.pendingAttributeValueS);
        Assert.assertFalse(resetEndTagToken.hasEmptyAttributeValue);
        Assert.assertFalse(resetEndTagToken.hasPendingAttributeValue);
        Assert.assertFalse(resetEndTagToken.selfClosing);
        Assert.assertNull(resetEndTagToken.attributes);
    }

    @Test
    public void testCommentToken() {
        Comment commentToken = new Comment();
        commentToken.data.append("This is a comment");

        Comment expectedComment = new Comment();
        expectedComment.data.append("This is a comment");
        expectedComment.bogus = false;

        Assert.assertEquals(expectedComment, commentToken);

        // Test reset method
        Comment resetCommentToken = commentToken.reset();
        Assert.assertNotEquals(commentToken, resetCommentToken);
        Assert.assertEquals(TokenType.Comment, resetCommentToken.type);
        Assert.assertNull(resetCommentToken.data);
        Assert.assertFalse(resetCommentToken.bogus);
    }

    @Test
    public void testCharacterToken() {
        Character characterToken = new Character();
        characterToken.data("A");

        Character expectedCharacter = new Character();
        expectedCharacter.data("A");

        Assert.assertEquals(expectedCharacter, characterToken);

        // Test reset method
        Character resetCharacterToken = characterToken.reset();
        Assert.assertNotEquals(characterToken, resetCharacterToken);
        Assert.assertEquals(TokenType.Character, resetCharacterToken.type);
        Assert.assertNull(resetCharacterToken.data);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
    }

    @Test
    public void testEOFToken() {
        EOF eofToken = new EOF();

        EOF expectedEOF = new EOF();

        Assert.assertEquals(expectedEOF, eofToken);

        // Test reset method
        EOF resetEOFToken = eofToken.reset();
        Assert.assertNotEquals(eofToken, resetEOFToken);
        Assert.assertEquals(TokenType.EOF, resetEOFToken.type);
    }
}