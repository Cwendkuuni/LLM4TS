import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        Assert.assertNull(doctype.asDoctype().getName());
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name.append("html");
        Assert.assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithNameAndForceQuirks = new Token.Doctype();
        doctypeWithNameAndForceQuirks.asDoctype().name.append("html");
        doctypeWithNameAndForceQuirks.asDoctype().forceQuirks = true;
        Assert.assertEquals("html", doctypeWithNameAndForceQuirks.asDoctype().getName());
        Assert.assertTrue(doctypeWithNameAndForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag("p");
        Assert.assertEquals(TokenType.StartTag, startTag.type);
        Assert.assertEquals("p", startTag.asStartTag().name());
        Assert.assertNull(startTag.asStartTag().getAttributes());

        Token startTagWithAttributes = new Token.StartTag("p");
        startTagWithAttributes.newAttribute();
        startTagWithAttributes.appendAttributeName("class");
        startTagWithAttributes.appendAttributeValue("red");
        Assert.assertEquals("class", startTagWithAttributes.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertEquals("red", startTagWithAttributes.asStartTag().getAttributes().get("class"));

        Token startTagWithSelfClosing = new Token.StartTag("img");
        startTagWithSelfClosing.selfClosing = true;
        Assert.assertTrue(startTagWithSelfClosing.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag("p");
        Assert.assertEquals(TokenType.EndTag, endTag.type);
        Assert.assertEquals("p", endTag.asEndTag().name());

        Token endTagWithName = new Token.EndTag("div");
        Assert.assertEquals("div", endTagWithName.asEndTag().name());

        Token startTag = new Token.StartTag("div");
        Token endTagWithSameName = new Token.EndTag();
        endTagWithSameName.name.append(startTag.asStartTag().name());
        Assert.assertEquals(startTag.asStartTag().name(), endTagWithSameName.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.type);
        Assert.assertNull(comment.asComment().getData());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data.append("This is a comment.");
        Assert.assertEquals("This is a comment.", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        Assert.assertEquals(TokenType.Character, character.type);
        Assert.assertEquals("A", character.asCharacter().getData());

        Token characterWithMultipleChars = new Token.Character("ABC");
        Assert.assertEquals("ABC", characterWithMultipleChars.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.type);
        Assert.assertFalse(eof.isDoctype());
        Assert.assertFalse(eof.isStartTag());
        Assert.assertFalse(eof.isEndTag());
        Assert.assertFalse(eof.isComment());
        Assert.assertFalse(eof.isCharacter());
    }

}