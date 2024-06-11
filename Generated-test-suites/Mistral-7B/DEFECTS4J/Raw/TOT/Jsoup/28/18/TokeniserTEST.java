import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private List<Token> expectedTokens;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'd', 'a', 't', 'a', ' ', ' ', 's', 'c', 'r', 'i', 'p', 't', ' ', '>', '\n',
 "<script type='text/javascript'>", "function test() {", "}", "</script>", "\n",
 "<p id='test'>Hello, World!</p>", "</html>"}));
 tokeniser = new Tokeniser(reader, new ArrayList<>());
 errors = tokeniser.errors;
 expectedTokens = Arrays.asList(
 new Token.StartTag("html", null),
 new Token.StartTag("head", null),
 new Token.EndTag("head"),
 new Token.StartTag("body", null),
 new Token.StartTag("script", Arrays.asList("type", "text/javascript")),
 new Token.Character('('),
 new Token.Character('f'),
 new Token.Character('u'),
 new Token.Character('n'),
 new Token.Character('c'),
 new Token.Character('t'),
 new Token.Character('i'),
 new Token.Character('o'),
 new Token.Character('n'),
 new Token.Character(' '),
 new Token.Character('{'),
 new Token.EndTag("script"),
 new Token.Character('>'),
 new Token.Character('\n'),
 new Token.StartTag("p", Arrays.asList("id", "test")),
 new Token.Character('H'),
 new Token.Character('e'),
 new Token.Character('l', 160), // an accented 'l' character
 new Token.Character('l', 160),
 new Token.Character('o'),
 new Token.Character(','),
 new Token.Character(' '),
 new Token.Character('W'),
 new Token.Character('o'),
 new Token.Character('r', 161), // an accented 'o' character
 new Token.Character('l', 161),
 new Token.Character('d'),
 new Token.Character('!'),
 new Token.EndTag("p"),
 new Token.EndTag("body"),
 new Token.EndTag("html")
 );
 }

 @Test
 public void testReadAndEmitTokens() {
 Token token;
 List<Token> actualTokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 actualTokens.add(token);
 }
 Assert.assertEquals(expectedTokens, actualTokens);
 Assert.assertEquals(0, errors.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithError() {
 tokeniser.error("Unexpected character '!' in state [DATA]");
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithEOF() {
 tokeniser.eofError(TokeniserState.DATA);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character c = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(null, c);
 c = tokeniser.consumeCharacterReference('&', false);
 Assert.assertEquals(null, c);
 c = tokeniser.consumeCharacterReference('#', false);
 Assert.assertEquals('#', c);
 c = tokeniser.consumeCharacterReference("amp", false);
 Assert.assertEquals('&', c);
 c = tokeniser.consumeCharacterReference("quot", false);
 Assert.assertEquals('\"', c);
 }

 @Test
 public void testConsumeCharacterReferenceInAttribute() {
 Character c = tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals(null, c);
 c = tokeniser.consumeCharacterReference('&', true);
 Assert.assertEquals('&', c);
 c = tokeniser.consumeCharacterReference("#x27", true); // hex
 Assert.assertEquals('\'', c);
 c = tokeniser.consumeCharacterReference("amp", true);
 Assert.assertEquals('&', c);
 c = tokeniser.consumeCharacterReference("quot", true);
 Assert.assertEquals('\"', c);
 c = tokeniser.consumeCharacterReference("apos", true);
 Assert.assertEquals('\'\'', c);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithInvalidCharacter() {
 tokeniser.consumeCharacterReference('!', false);
 }

 @Test(expected = ParseError.class)
 public void testConsumeCharacterReferenceWithInvalidSequence() {
 tokeniser.consumeCharacterReference("lt", false);
 }

 @Test
 public void testCreateTags() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token tag = tokeniser.getLastToken();
 Assert.assertEquals(TokenType.StartTag, tag.type);
 Assert.assertEquals("html", tag.tagName);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tag = tokeniser.getLastToken();
 Assert.assertEquals(TokenType.EndTag, tag.type);
 Assert.assertEquals("html", tag.tagName);
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.SCRIPT_PI);
 Assert.assertEquals(TokeniserState.SCRIPT_PI, tokeniser.getState());
 tokeniser.transition(TokeniserState.DATA);
 Assert.assertEquals(TokeniserState.DATA, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.advanceTransition(TokeniserState.SCRIPT_PI);
 Assert.assertEquals(TokeniserState.DATA, tokeniser.getState());
 tokeniser.advanceTransition(TokeniserState.DATA);
 Assert.assertEquals(TokeniserState.DATA, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

}