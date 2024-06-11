import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader reader;
 private ParseErrorList errors;

 @Before
 public void setup() {
 reader = new CharacterReader("");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testConstructor() {
 Tokeniser tokeniser = new Tokeniser(null, null);
 Assert.assertNull(tokeniser);
 tokeniser = new Tokeniser(new CharacterReader(""), null);
 Assert.assertNotNull(tokeniser);
 }

 @Test
 public void testRead() {
 CharSequence input = "Hello World";
 reader.setInput(input);
 tokeniser = new Tokeniser(reader, errors);

 Token token = tokeniser.read();
 Assert.assertNotNull(token);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("H", token.data[0]);

 token = tokeniser.read();
 Assert.assertNotNull(token);
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("e", token.data[0]);

 token = tokeniser.read();
 //... continue testing all characters in the input

 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadWhenErrorsPresent() {
 reader.setInput("Hello World");
 errors.add(new ParseError(0, "Error 1"));
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.read();
 }

 @Test
 public void testEmit() {
 Token token = new Token.Character('H');
 tokeniser.emit(token);
 Token emittedToken = tokeniser.read();
 Assert.assertNotNull(emittedToken);
 Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
 Assert.assertEquals('H', emittedToken.data[0]);
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("Hello");
 Token emittedToken = tokeniser.read();
 Assert.assertNotNull(emittedToken);
 Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
 Assert.assertEquals("H", emittedToken.data[0]);
 Assert.assertEquals("e", tokeniser.read().data[0]);
 Assert.assertEquals("l", tokeniser.read().data[0]);
 Assert.assertEquals("l", tokeniser.read().data[0]);
 Assert.assertEquals("o", tokeniser.read().data[0]);
 Assert.assertEquals(0, tokeniser.read().type);
 }

 @Test
 public void testEmitCharacterReference() throws Exception {
 tokeniser.emit("&#123;");
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
 Assert.assertEquals('\u007B', emittedToken.data[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmitCharacterReferenceInvalid() throws Exception {
 tokeniser.emit("&#X"); // missing number
 }

 @Test(expected = NumberFormatException.class)
 public void testEmitCharacterReferenceInvalidNumber() throws Exception {
 tokeniser.emit("&#abcd"); // invalid number
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmitCharacterReferenceOutsideRange() throws Exception {
 tokeniser.emit("&#1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890");
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 Assert.assertNotNull(tag);
 Assert.assertTrue(tag instanceof Token.StartTag);
 tag = tokeniser.createTagPending(false);
 Assert.assertNotNull(tag);
 Assert.assertTrue(tag instanceof Token.EndTag);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token emittedToken = tokeniser.read();
 Assert.assertNotNull(emittedToken);
 Assert.assertEquals(Token.TokenType.StartTag, emittedToken.type);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token emittedToken = tokeniser.read();
 Assert.assertNotNull(emittedToken);
 Assert.assertEquals(Token.TokenType.Comment, emittedToken.type);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token emittedToken = tokeniser.read();
 Assert.assertNotNull(emittedToken);
 Assert.assertEquals(Token.TokenType.Doctype, emittedToken.type);
 }
}