import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeConstruction() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.getType());
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("publicIdentifier");
        doctype.getSystemIdentifier().append("systemIdentifier");
        doctype.setForceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.asDoctype().getName());
        assertEquals("publicIdentifier", doctype.asDoctype().getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.asDoctype().getSystemIdentifier());
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeGettersWithInvalidData() {
        Token doctype = new Token.Doctype();
        doctype.getName().append(null);
        doctype.getPublicIdentifier().append("");
        doctype.getSystemIdentifier().append(null);
        doctype.setForceQuirks(-1);
    }

    // Add similar test cases for other Token subclasses

    @Test
    public void testStartTagConstruction() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.getType());
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testStartTagGettersAndSetters() {
        Token startTag = new Token.StartTag();
        startTag.name("div");
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");
        startTag.newAttribute();
        startTag.appendAttributeName("style");
        startTag.appendAttributeValue("color:red;");

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.asStartTag().name());
        assertTrue(startTag.asStartTag().isSelfClosing());
        assertEquals(2, startTag.asStartTag().getAttributes().size());
        assertEquals("class", startTag.asStartTag().getAttributes().keySet().toArray()[0].toString());
        assertEquals("red", startTag.asStartTag().getAttributes().get("class").toString());
        assertEquals("style", startTag.asStartTag().getAttributes().keySet().toArray()[1].toString());
        assertEquals("color:red;", startTag.asStartTag().getAttributes().get("style").toString());
    }

    @Test(expected = AssertionError.class)
    public void testStartTagGettersAndSettersWithInvalidData() {
        Token startTag = new Token.StartTag();
        startTag.name(null);
        startTag.newAttribute();
        startTag.appendAttributeName("");
        startTag.appendAttributeValue("red");
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue(null);
    }

    @Test
    public void testEndTagConstruction() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.getType());
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testEndTagGetters() {
        Token endTag = new Token.EndTag();
        endTag.name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("div", endTag.asEndTag().name());
    }

    @Test(expected = AssertionError.class)
    public void testEndTagGettersWithInvalidData() {
        Token endTag = new Token.EndTag();
        endTag.name(null);
    }

    @Test
    public void testCommentConstruction() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.getType());
        assertTrue(comment.isComment());
    }

    @Test
    public void testCommentGetter() {
        Token comment = new Token.Comment();
        comment.getData().append("This is a comment.");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment.", comment.asComment().getData());
    }

    @Test(expected = AssertionError.class)
    public void testCommentGetterWithInvalidData() {
        Token comment = new Token.Comment();
        comment.getData().append(null);
    }

    @Test
    public void testCharacterConstruction() {
        Token character = new Token.Character("A");
        assertEquals(TokenType.Character, character.getType());
        assertTrue(character.isCharacter());
    }

    @Test
    public void testCharacterGetter() {
        Token character = new Token.Character("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.asCharacter().getData());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterGetterWithInvalidData() {
        Token character = new Token.Character(null);
    }

    @Test
    public void testEOFConstruction() {
        Token eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.getType());
        assertTrue(eof.isEOF());
    }

    // Add test cases for other methods and edge cases

}