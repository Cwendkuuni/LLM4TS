import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest extends TokeniserTest {

    @Test
    public void testTokeniserConstructor() {
        CharacterReader reader = new CharacterReader("Some HTML input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("b", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("c", token.toString());

        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("<html>", token.toString());

        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadEndTag() {
        CharacterReader reader = new CharacterReader("<html><body></body></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        Assert.assertEquals("<html>", startTag.toString());

        Token endTag = tokeniser.read();
        Assert.assertNotNull(endTag);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("html", endTag.tagName);

        tokeniser.acknowledgeSelfClosingFlag();

        Assert.assertNull(tokeniser.read());

        token = tokeniser.read();
        Assert.assertEquals("<body>", token.toString());

        endTag = tokeniser.read();
        Assert.assertNotNull(endTag);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("body", endTag.tagName);

        Assert.assertNull(tokeniser.read());

        token = tokeniser.read();
        Assert.assertEquals("</html>", token.toString());
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- Some comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);

        token = tokeniser.read();
        Assert.assertEquals("<!--", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("Some", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("comment", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("-->", token.toString());

        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testReadDoctype() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertEquals("<!DOCTYPE", token.toString());

        token = tokeniser.read();
        Assert.assertEquals("html", token.toString());

        Assert.assertNull(tokeniser.read());
    }

    @Test(expected = ParseError.class)
    public void testMalformedCharacterReference() {
        CharacterReader reader = new CharacterReader("&abcd;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testValidCharacterReference() {
        CharacterReference charRef = tokeniser.consumeCharacterReference('\u00A9', false);
        Assert.assertEquals('\u00A9', charRef);
    }

    @Test
    public void testValidNamedCharacterReference() {
        CharacterReference charRef = tokeniser.consumeCharacterReference("trade", false);
        Assert.assertEquals('\u00A9', charRef.charValue());
    }

    @Test
    public void testCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader("<img src=\"image.png\" alt=\"Some &image; with &amp; quote\">");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.transition(TokeniserState.StartTag);

        Token startTag = tokeniser.read();
        Assert.assertEquals("<img", startTag.toString());

        tokeniser.consumeCharacterReference('i', true);

        tokeniser.transition(TokeniserState.TagName);
        tokeniser.consumeCharacterReference('m', true);
        tokeniser.consumeCharacterReference('g', true);

        tokeniser.transition(TokeniserState.Attributes);

        tokeniser.consume(" src=\"");
        tokeniser.consume("image.png\"");

        tokeniser.transition(TokeniserState.TagBody);

        tokeniser.transition(TokeniserState.Character);

        tokeniser.consumeCharacterReference('&', false);
        tokeniser.consumeCharacterReference("image", false);
        tokeniser.consumeCharacterReference(';', false);

        tokeniser.transition(TokeniserState.Character);

        tokeniser.consumeCharacterReference('&', false);
        tokeniser.consumeCharacterReference("amp", false);
        tokeniser.consumeCharacterReference(';', false);

        tokeniser.consumeCharacterReference(' ');
        tokeniser.consumeCharacterReference('q', false);
        tokeniser.consumeCharacterReference('u', false);
        tokeniser.consumeCharacterReference('o', false);
        tokeniser.consumeCharacterReference('t', false);
        tokeniser.consumeCharacterReference('e', false);

        Assert.assertNull(tokeniser.read());

        Token endTag = tokeniser.read();
        Assert.assertEquals(">", endTag.toString());
    }

    @Test(expected = ParseError.class)
    public void testMissingSemicolonInCharacterReference() {
        CharacterReader reader = new CharacterReader("&image;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.consumeCharacterReference("image", false);
    }

    @Test(expected = ParseError.class)
    public void testInvalidNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&invalidname;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.consumeCharacterReference("invalidname", false);
    }

    @Test(expected = ParseError.class)
    public void testInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&fffd;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
    }

    @Test
    public void testTransitionAndAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Character);
        tokeniser.read();

        Assert.assertEquals("a", tokeniser.read().toString());

        tokeniser.advanceTransition(TokeniserState.Character);

        Assert.assertEquals("b", tokeniser.read().toString());

        tokeniser.advanceTransition(TokeniserState.Character);

        Assert.assertEquals("c", tokeniser.read().toString());

        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("<html><body><script>alert('XSS');</script></body></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.transition(TokeniserState.StartTag);

        Token startTag = tokeniser.read();
        Assert.assertEquals("<html>", startTag.toString());

        tokeniser.transition(TokeniserState.TagName);
        tokeniser.consumeCharacterReference('h', true);
        tokeniser.consumeCharacterReference("t", true);
        tokeniser.consumeCharacterReference("m", true);
        tokeniser.consumeCharacterReference("l", true);

        tokeniser.transition(TokeniserState.Attributes);

        List<String> attributes = new ArrayList<>();
        tokeniser.consume(" xmlns:");
        tokeniser.consume("xml", false);
        tokeniser.consume("=");
        tokeniser.consume(" HTTPS");
        tokeniser.consume(" ");

        attributes.add("xmlns:xml=\"HTTPS\"");

        tokeniser.transition(TokeniserState.TagBody);

        tokeniser.transition(TokeniserState.Script);

        tokeniser.consume("script>");
        tokeniser.consume("alert('XSS');");
        tokeniser.consume("</script>");

        tokeniser.transition(TokeniserState.Character);

        tokeniser.error(TokeniserState.Script);

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character 's' in input state [SCRIPT]", errors.get(0).getMessage());
    }

    @Test
    public void testEOFErrorHandling() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.transition(TokeniserState.StartTag);

        Token startTag = tokeniser.read();
        Assert.assertEquals("<html>", startTag.toString());

        tokeniser.transition(TokeniserState.TagName);
        tokeniser.consumeCharacterReference('h', true);
        tokeniser.consumeCharacterReference("t", true);
        tokeniser.consumeCharacterReference("m", true);
        tokeniser.consumeCharacterReference("l", true);

        tokeniser.transition(TokeniserState.Attributes);

        List<String> attributes = new ArrayList<>();
        tokeniser.consume(" xmlns:");
        tokeniser.consume("xml", false);
        tokeniser.consume("=");
        tokeniser.consume(" HTTPS");
        tokeniser.consume(" ");

        attributes.add("xmlns:xml=\"HTTPS\"");

        tokeniser.transition(TokeniserState.TagBody);

        tokeniser.transition(TokeniserState.Character);

        tokeniser.eofError(TokeniserState.TagBody);

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [TAG_BODY]", errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceTable() {
        CharacterReader reader = new CharacterReader("&copy; &trade; & Male; &female; &reg; &deg; &sup1; &sup2;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        tokeniser.transition(TokeniserState.Character);

        Character c1 = tokeniser.consumeCharacterReference('\u00A9', false);
        Character c2 = tokeniser.consumeCharacterReference('\u2122', false);
        Character c3 = tokeniser.consumeCharacterReference('M', false);
        Character c4 = tokeniser.consumeCharacterReference('F', false);
        Character c5 = tokeniser.consumeCharacterReference('\u00AE', false);
        Character c6 = tokeniser.consumeCharacterReference('\u00B0', false);
        Character c7 = tokeniser.consumeCharacterReference('\u2031', false);
        Character c8 = tokeniser.consumeCharacterReference('\u2032', false);

        Assert.assertEquals('\u00A9', c1);
        Assert.assertEquals('\u2122', c2);
        Assert.assertEquals('M', c3);
        Assert.assertEquals('F', c4);
        Assert.assertEquals('\u00AE', c5);
        Assert.assertEquals('\u00B0', c6);
        Assert.assertEquals('\u2031', c7);
        Assert.assertEquals('\u2032', c8);

        Assert.assertNull(tokeniser.read());
    }
}