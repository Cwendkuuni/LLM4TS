import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 mockCharacterReader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, errors);
 }

 @Test
 public void testReadEmptyStream() {
 // Given - an empty input stream
 when(mockCharacterReader.isEmpty()).thenReturn(true);
 // When - calling read()
 Token token = tokeniser.read();
 // Then - an empty string token is returned
 assertEquals("", token.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadSingleCharacter() {
 // Given - a single character input stream
 when(mockCharacterReader.current()).thenReturn('a');
 when(mockCharacterReader.read()).thenReturn(1);
 // When - calling read()
 Token token = tokeniser.read();
 // Then - a character token is returned with the character 'a'
 Token.Character charToken = (Token.Character) token;
 assertEquals('a', charToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadSelfClosingTag() {
 // Given - an input stream with a self-closing tag
 when(mockCharacterReader.current()).thenReturn('<').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn('/').thenReturn('i').thenReturn('m').thenReturn('>');
 // When - calling read() twice
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 // Then - the first token is an opening tag 'img' and the second token is an empty string tag 'img'
 Token.StartTag startTag = (Token.StartTag) firstToken;
 Token.EndTag endTag = (Token.EndTag) secondToken;
 assertEquals("img", startTag.tagName);
 assertEquals("img", endTag.tagName);
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadCharacterReference() {
 // Given - an input stream with a character reference
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a character reference '&amp;', the second token is an empty string token, and the third token is the character '&'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&amp;", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("&", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadInvalidCharacterReference() {
 // Given - an input stream with an invalid character reference
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('x').thenReturn('>').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is an invalid character reference error, the second token is an empty string token, and the third token is the character '>'
 Token.Error errorToken = (Token.Error) firstToken;
 assertEquals("invalid character reference: invalid named referenece 'x'", errorToken.errorMessage());
 assertEquals("", secondToken.data());
 assertEquals(">", secondToken.data());
 // And - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("Unexpected character 'x' in input state [Data]", error.getMessage());
 }

 @Test
 public void testReadEOF() {
 // Given - an input stream with an EOF
 when(mockCharacterReader.isEmpty()).thenReturn(true);
 // When - calling read()
 Token token = tokeniser.read();
 // Then - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
 // And - no token is returned
 assertNull(token);
 }

 @Test
 public void testReadInvalidState() {
 // Given - an input stream with an invalid state
 tokeniser.transition(TokeniserState.EndTag);
 // When - calling read()
 Token token = tokeniser.read();// Then - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("There is an unread token pending!", error.getMessage());
 // And - no token is returned
 assertNull(token);
 }

 @Test
 public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
 // Given - an input stream with a character reference with an additional allowed character
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn('#').thenReturn('x').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a character reference '&amp;#x', the second token is an empty string token, and the third token is the character 'x'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&amp;#x", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("x", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadNamedCharacterReference() {
 // Given - an input stream with a named character reference
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a named character reference '&nam;', the second token is an empty string token, and the third token is the character 'a'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&nam;", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("a", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadNamedCharacterReferenceWithSemicolon() {
 // Given - an input stream with a named character reference with a semicolon
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(';').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a named character reference '&nam;', the second token is an empty string token, and the third token is the character ';'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&nam;", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals(";", thirdToken.data());
 // And - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("missing semicolon", error.getMessage());
 }

 @Test
 public void testReadSelfClosingTagWithMissingSemicolon() {
 // Given - an input stream with a self-closing tag without a semicolon
 when(mockCharacterReader.current()).thenReturn('<').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn('/').thenReturn('i').thenReturn('m');
 // When - calling read() twice
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 // Then - the first token is an opening tag 'img', the second token is an error 'Self closing flag not acknowledged', and no third token is returned
 Token.StartTag startTag = (Token.StartTag) firstToken;
 Token.Error errorToken = (Token.Error) secondToken;
 assertEquals("img", startTag.tagName);
 assertEquals("Self closing flag not acknowledged", errorToken.errorMessage());
 // And - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("Self closing flag not acknowledged", error.getMessage());
 }

 @Test
 public void testReadCharacterReferenceWithInvalidCharacter() {
 // Given - an input stream with a character reference with an invalid character
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn('#').thenReturn('\u0100').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a character reference '&amp;#\u0100', the second token is an empty string token, and the third token is the character '\u0100'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&amp;#\u0100", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("\u0100", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadCharacterReferenceWithCharacterOutsideValidRange() {
 // Given - an input stream with a character reference with a character outside the valid range
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn('#').thenReturn('\uFFFFF').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a character reference '&amp;#\uFFFFF', the second token is an empty string token, and the third token is the character replacementChar
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&amp;#\uFFFFF", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals(Tokeniser.replacementChar, thirdToken.data());
 // And - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("character outside of valid range", error.getMessage());
 }

 @Test
 public void testReadNamedCharacterReferenceWithInvalidCharacter() {
 // Given - an input stream with a named character reference with an invalid character
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(';').thenReturn('\u0100');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a named character reference '&nam;', the second token is an error 'character outside of valid range', and no third token is returned
 Token.Error errorToken = (Token.Error) secondToken;
 assertEquals("&nam;", firstToken.data());
 assertEquals("character outside of valid range", errorToken.errorMessage());
 // And - an error is reported
 ParseError error = errors.getErrors().get(0);
 assertEquals(1, errors.getErrors().size());
 assertEquals("character outside of valid range", error.getMessage());
 }

 @Test
 public void testReadNamedCharacterReferenceWithMultipleCharacters() {
 // Given - an input stream with a named character reference with multiple characters
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';').thenReturn('e').thenReturn('m').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a named character reference '&nam;em', the second token is an empty string token, and the third token is the character 'e'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&nam;em", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("e", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }

 @Test
 public void testReadNamedCharacterReferenceWithTrailingSemicolon() {
 // Given - an input stream with a named character reference with a trailing semicolon
 when(mockCharacterReader.current()).thenReturn('&').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn(';').thenReturn(';');
 // When - calling read() three times
 Token firstToken = tokeniser.read();
 Token secondToken = tokeniser.read();
 Token thirdToken = tokeniser.read();
 // Then - the first token is a named character reference '&nam;', the second token is an empty string token, and the third token is the character ';'
 Token.Character charToken = (Token.Character) firstToken;
 assertEquals("&nam;", charToken.data());
 assertEquals("", secondToken.data());
 assertEquals("", thirdToken.data());
 // And - no errors are reported
 assertTrue(errors.isEmpty());
 }
}