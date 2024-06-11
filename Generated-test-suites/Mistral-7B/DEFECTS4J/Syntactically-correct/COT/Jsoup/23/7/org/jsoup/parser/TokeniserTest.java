import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader createCharacterReader(String input) {
 // Create a mock CharacterReader for testing
 // You may need to implement this class based on the actual CharacterReader implementation in your codebase
 CharacterReader characterReader = mock(CharacterReader.class);
 when(characterReader.current()).thenAnswer(i -> input.charAt(i.getArgument(0)));
 when(characterReader.consume(anyChar())).thenAnswer(i -> {
 char c = input.charAt((int) i.getArgument(0));
 input = input.substring((int) i.getArgument(0) + 1);
 return c;
 });
 when(characterReader.match(anyChar())).thenAnswer(i -> input.charAt((int) i.getArgument(0)) == i.getArgument(1));
 when(characterReader.matchConsume(anyString())).thenAnswer(i -> {
 String s = i.getArgument(0);
 input = input.substring(input.indexOf(s) + s.length());
 return s;
 });
 when(characterReader.isEmpty()).thenAnswer(i -> input.isEmpty());
 when(characterReader.advance()).thenAnswer(i -> {
 input = input.substring(1);
 return input.charAt(0);
 });
 when(characterReader.pos()).thenReturn(0);
 return characterReader;
 }

 @Test
 public void testTokeniser_DefaultConstructor() {
 Tokeniser tokeniser = new Tokeniser(null, null);
 Assert.assertNull(tokeniser.reader);
 Assert.assertNull(tokeniser.errors);
 }

 @Test
 public void testTokeniser_Constructor() {
 CharacterReader reader = createCharacterReader("input");
 ParseErrorList errors = new ParseErrorList();
 Tokeniser tokeniser = new Tokeniser(reader, errors);
 Assert.assertEquals(reader, tokeniser.reader);
 Assert.assertEquals(errors, tokeniser.errors);
 }

 @Test(expected = NullPointerException.class)
 public void testTokeniser_NullReader() {
 new Tokeniser(null, new ParseErrorList());
 }

 @Test(expected = NullPointerException.class)
 public void testTokeniser_NullErrorList() {
 new Tokeniser(new CharacterReader() {
 @Override
 public char current() {
 return 'a';
 }

 @Override
 public char consume(int offset) {
 return 'b';
 }

 @Override
 public boolean match(int expected) {
 return true;
 }

 @Override
 public String consumeLetterSequence() {
 return "ab";
 }

 @Override
 public boolean matchesLetter() {
 return true;
 }

 @Override
 public boolean matchesDigit() {
 return true;
 }

 @Override
 public boolean matchesAny(String... chars) {
 return true;
 }

 @Override
 public int pos() {
 return 0;
 }

 @Override
 public void advance() {
 // empty
 }
 }, null);
 }

 @Test
 public void testRead_NormalCharacter() {
 CharacterReader reader = createCharacterReader("abc");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Token token = tokeniser.read();
 Assert.assertEquals('a', token.getCharacter());
 }

 @Test
 public void testRead_EmptyInput() {
 CharacterReader reader = createCharacterReader("");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testRead_ErrorState() {
 CharacterReader reader = createCharacterReader("error");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.transition(TokeniserState.Error);
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testEmit_Character() {
 CharacterReader reader = createCharacterReader("ab");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.emit(new Character("a"));
 tokeniser.emit('b');
 tokeniser.emit('c');
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(3, tokens.size());
 Assert.assertEquals('a', tokens.get(0).getCharacter());
 Assert.assertEquals('b', tokens.get(1).getCharacter());
 Assert.assertEquals('c', tokens.get(2).getCharacter());
 }

 @Test
 public void testEmit_Tag() {
 CharacterReader reader = createCharacterReader("<tag>");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Token.StartTag tag = new Token.StartTag("tag");
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(1, tokens.size());
 Assert.assertEquals(tag, tokens.get(0));
 }

 @Test
 public void testEmit_Comment() {
 CharacterReader reader = createCharacterReader("<!--comment-->");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(1, tokens.size());
 Assert.assertEquals(new Comment("comment"), tokens.get(0));
 }

 @Test
 public void testEmit_Doctype() {
 CharacterReader reader = createCharacterReader("<!DOCTYPE html>");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(1, tokens.size());
 Assert.assertEquals(new Doctype("html"), tokens.get(0));
 }

 @Test
 public void testTransition_ErrorState() {
 CharacterReader reader = createCharacterReader("error");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.transition(TokeniserState.Data);
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition_ErrorState() {
 CharacterReader reader = createCharacterReader("error");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Error);
 Assert.assertEquals(TokeniserState.Error, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 CharacterReader reader = createCharacterReader("selfclosing");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 CharacterReader reader = createCharacterReader("&#123;");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('\u007B', character.charValue());
 }

 @Test
 public void testConsumeCharacterReference_Invalid() {
 CharacterReader reader = createCharacterReader("&#x123;ab");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(Tokeniser.replacementChar, character);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamed() {
 CharacterReader reader = createCharacterReferenceReader("foobar");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(Tokeniser.replacementChar, character);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamedWithAttribute() {
 CharacterReader reader = createCharacterReferenceReader("foobar\" class=\"foo\"");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 Character character = tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals(Tokeniser.replacementChar, character);
 }

 private CharacterReader createCharacterReferenceReader(String input) {
 CharacterReader characterReader = mock(CharacterReader.class);
 when(characterReader.current()).thenAnswer(i -> input.charAt(i.getArgument(0)));
 when(characterReader.consume(anyInt())).thenAnswer(i -> {
 char c = input.charAt((int) i.getArgument(0));
 input = input.substring((int) i.getArgument(0) + 1);
 return c;
 });
 when(characterReader.match(anyChar())).thenAnswer(i -> input.charAt((int) i.getArgument(0)) == i.getArgument(1));
 when(characterReader.consumeLetterSequence()).thenReturn("ab");
 when(characterReader.matchesLetter()).thenReturn(true);
 when(characterReader.matchesDigit()).thenReturn(true);
 when(characterReader.matchesAny(anyArray())).thenAnswer(i -> true);
 when(characterReader.pos()).thenReturn(0);
 when(characterReader.advance()).thenAnswer(i -> {
 input = input.substring(1);
 return input.charAt(0);
 });
 return characterReader;
 }

 @Test
 public void testGetTokens_ValidInput() {
 CharacterReader reader = createCharacterReader("abc<tag></tag>def");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(7, tokens.size());
 Assert.assertEquals('a', tokens.get(0).getCharacter());
 Assert.assertEquals('b', tokens.get(1).getCharacter());
 Assert.assertEquals(Token.StartTag.class, tokens.get(2).getClass());
 Assert.assertEquals("tag", tokens.get(2).tagName);
 Assert.assertEquals(Token.EndTag.class, tokens.get(3).getClass());
 Assert.assertEquals("tag", tokens.get(3).tagName);
 Assert.assertEquals('d', tokens.get(4).getCharacter());
 Assert.assertEquals('e', tokens.get(5).getCharacter());
 Assert.assertEquals('f', tokens.get(6).getCharacter());
 }

 @Test
 public void testGetTokens_ErrorInput() {
 CharacterReader reader = createCharacterReader("error");
 Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
 List<Token> tokens = tokeniser.getTokens();
 Assert.assertEquals(0, tokens.size());
 }

}