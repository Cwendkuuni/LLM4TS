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

 private CharacterReader mockCharacterReader;
 private ParseErrorList mockParseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 mockCharacterReader = new MockCharacterReader();
 mockParseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, mockParseErrorList);
 }

 @Test
 public void testRead_NormalCharacter() {
 char expectedChar = 'a';
 mockCharacterReader.feed('a');
 Token actualToken = tokeniser.read();

 Character expectedCharacterToken = new Character(String.valueOf(expectedChar));
 Character actualCharacterToken = (Character) actualToken;

 Assert.assertEquals(expectedCharacterToken, actualCharacterToken);
 }

 @Test
 public void testRead_EmptyBuffer() {
 Token actualToken = tokeniser.read();

 Assert.assertNull(actualToken);
 }

 @Test
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser.selfClosingFlagAcknowledged = false;
 mockCharacterReader.feed('a');
 Token actualToken = tokeniser.read();

 Assert.assertEquals("Self closing flag not acknowledged", "Error", actualToken.type.toString());
 }

 @Test
 public void testEmit_Character() {
 Character expectedCharacterToken = new Character("a");
 tokeniser.emit(expectedCharacterToken);
 Token actualToken = tokeniser.read();

 Assert.assertEquals(expectedCharacterToken, actualToken);
 }

 @Test
 public void testEmit_StartTag() {
 StartTag expectedStartTag = new StartTag("div", new ArrayList<>());
 tokeniser.emit(expectedStartTag);
 Token actualToken = tokeniser.read();

 Assert.assertEquals(expectedStartTag, actualToken);
 }

 @Test
 public void testEmit_EndTag() {
 EndTag expectedEndTag = new EndTag("div");
 tokeniser.emit(expectedEndTag);
 Token actualToken = tokeniser.read();

 Assert.assertEquals(expectedEndTag, actualToken);
 }

 @Test
 public void testEmit_String() {
 String expectedString = "hello";
 tokeniser.emit(expectedString);
 char expectedChar;
 StringBuilder charBuffer = new StringBuilder();
 while ((expectedChar = tokeniser.read().tokenCharacter()) != null) {
 charBuffer.append(expectedChar);
 }

 Assert.assertEquals(expectedString, charBuffer.toString());
 }

 @Test
 public void testRead_CharacterReference() {
 Character expectedCharacterReference = 'á';
 mockCharacterReader.feed('#');
 mockCharacterReader.feed('x');
 mockCharacterReader.feed('3');
 mockCharacterReader.feed('b');
 mockCharacterReader.feed(';');

 Character actualCharacterReference = tokeniser.consumeCharacterReference(null, false);

 Assert.assertEquals(expectedCharacterReference, actualCharacterReference);
 }

 @Test
 public void testRead_NamedCharacterReference() {
 mockCharacterReader.feed('&');
 mockCharacterReader.feed('a');
 mockCharacterReader.feed('m');
 mockCharacterReader.feed('p');
 mockCharacterReader.feed(';');

 Character actualCharacterReference = tokeniser.consumeCharacterReference(null, false);

 Assert.assertEquals(Character.AMPERSAND, actualCharacterReference);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_CharacterReferenceWithAttributes() {
 mockCharacterReader.feed('&');
 mockCharacterReader.feed('a');
 mockCharacterReader.feed('m');
 mockCharacterReader.feed('p');
 mockCharacterReader.feed(' ');
 mockCharacterReader.feed('=');
 mockCharacterReader.feed('a');
 mockCharacterReader.feed('=');
 mockCharacterReader.feed('"');
 mockCharacterReader.feed(';');

 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testError_UnexpectedCharacter() {
 mockCharacterReader.feed('%');
 tokeniser.error(TokeniserState.Character);
 }

 @Test
 public void testError_UnexpectedEndOfFile() {
 mockCharacterReader.feed('a');
 mockCharacterReader.feed('b');
 mockCharacterReader.feed('c');
 mockCharacterReader.mark();
 mockCharacterReader.consumeCurrent();
 mockCharacterReader.reset();
 mockCharacterReader.consumeCurrent(); // consume c
 mockCharacterReader.consumeCurrent(); // consume b
 mockCharacterReader.consumeCurrent(); // consume a
 mockCharacterReader.consumeCurrent(); // consume %

 tokeniser.eofError(TokeniserState.Character);
 }

 @Test
 public void testCharacterReferenceError_InvalidNamedReference() {
 mockCharacterReader.feed('&');
 mockCharacterReader.feed('x');
 mockCharacterReader.feed('y');
 mockCharacterReader.feed(';');
 tokeniser.characterReferenceError("invalid named reference xy");
 }

 @Test
 public void testCharacterReferenceError_MissingSemicolon() {
 mockCharacterReader.feed('&');
 mockCharacterReader.feed('a');
 mockCharacterReader.feed('m');
 mockCharacterReader.feed('p');
 mockCharacterReader.consumeCurrent(); // consume p
 tokeniser.characterReferenceError("missing semicolon");
 }
}