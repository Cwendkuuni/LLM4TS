import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testReadNormalCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals("a", token.data());
        Assert.assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();
        Assert.assertEquals("<img", startTag.tagName());
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals(">", startTag.data());
        Assert.assertEquals("/img", endTag.tagName());
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testReadString() {
        CharacterReader reader = new CharacterReader("hello world");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals("hello world", token.data());
        Assert.assertEquals(Token.TokenType.String, token.type);
    }

    @Test
    public void testReadComment() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        Assert.assertEquals("<!-- comment -->", token.data());
        Assert.assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testReadError() {
        CharacterReader reader = new CharacterReader("</script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Validate.assertTrue(errors.size() > 0);
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testReadEof() {
        CharacterReader reader = new CharacterReader("hello");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        reader.consumeAll();
        Validate.assertTrue(errors.size() > 0);
        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("#x61");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('a', (char) codepoints[0]);
        Assert.assertArrayEquals(new int[]{'a'}, codepoints);
    }

    @Test
    public void testConsumeCharacterReferenceWithInvalidCharacter() {
        CharacterReader reader = new CharacterReader("#x10000");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals(Tokeniser.replacementChar, (char) codepoints[0]);
        Assert.assertArrayEquals(new int[]{Tokeniser.replacementChar}, codepoints);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.StartTag tag = tokeniser.createTagPending(true);
        tag.setAttribute("class", "test");
        Assert.assertEquals("test", tag.getAttribute("class"));
        Assert.assertEquals("", tag.tagName());
        Assert.assertEquals(Token.TokenType.StartTag, tag.type);
    }

    
}