import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("input.html"); // assuming input.html is a file containing the test input
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 Token token;
 do {
 token = tokeniser.read();
 System.out.println("Token: " + token);
 } while (token.type != Token.TokenType.EOF);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Token.Character("A"));
 tokeniser.emit(new Token.Character("B"));
 Token tag = tokeniser.createTagPending(true);
 tag.tagName = "div";
 tokeniser.emitTagPending();
 tokeniser.emit(new Token.Character("C"));

 List<Token> expected = Arrays.asList(
 new Token.Character("A"),
 new Token.Character("B"),
 new Token.StartTag(new Token.Name("div"))
 );
 List<Token> actual = tokeniser.getTokens();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character chr = tokeniser.consumeCharacterReference(null, false);
 assertNull(chr);

 Character chr1 = tokeniser.consumeCharacterReference('&', false);
 assertNull(chr1);

 Character chr2 = tokeniser.consumeCharacterReference('#', false);
 assertNull(chr2);

 Character chr3 = tokeniser.consumeCharacterReference("#A", false);
 assertEquals(Entities.getCharacterByName("#A"), chr3);

 Character chr4 = tokeniser.consumeCharacterReference("#x41", false); // W
 assertEquals((char) 0x41, chr4.getValue());

 Character chr5 = tokeniser.consumeCharacterReference("&#x41;", false); // W
 assertEquals((char) 0x41, chr5.getValue());

 Character chr6 = tokeniser.consumeCharacterReference("#x{41}", false); // W
 assertEquals((char) 0x41, chr6.getValue());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReferenceInvalidNamedReference() {
 tokeniser.consumeCharacterReference("invalid", false);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReferenceMissingSemicolon() {
 tokeniser.consumeCharacterReference("#", false);
 }

 @Test
 public void testErrorHandling() {
 tokeniser.consumeCharacterReference("&", false); // valid character reference
 tokeniser.consumeCharacterReference("&a", false); // invalid named character reference
 tokeniser.consumeCharacterReference("#", true); // self-closing tag, missing semicolon
 tokeniser.consumeCharacterReference("#", false); // self-closing tag, missing semicolon (shouldn't be here)
 tokeniser.consumeCharacterReference("&x", false); // invalid character reference

 assertEquals(4, errors.size());
 }

 @Test
 public void testTransitions() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.advanceTransition(TokeniserState.ScriptData);
 tokeniser.advanceTransition(TokeniserState.Script);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.createTagPending(true).tagName = "div";
 tokeniser.createTagPending(false).tagName = "div";
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.createTagPending(true).tagName = "div";
 tokeniser.createTagPending(false).tagName = "p";
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }
}