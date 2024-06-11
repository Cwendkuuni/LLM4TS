package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 characterReader = Mockito.mock(CharacterReader.class);
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testRead_ReturnsExpectedToken() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("abcdefg<tag/>".getBytes());
 characterReader = Mockito.spy(new CharacterReader(inputStream));
 when(characterReader.current()).thenReturn('a');
 tokeniser.read();
 Token expectedToken = new Token.Character('a');
 verify(characterReader).consume();
 assertEquals(expectedToken, tokeniser.read());

 when(characterReader.current()).thenReturn('b');
 expectedToken = new Token.Character('b');
 verify(characterReader, times(2)).consume();
 assertEquals(expectedToken, tokeniser.read());

 when(characterReader.current()).thenReturn('<');
 Token tagToken = tokeniser.createTagPending(true);
 tagToken.tagName = "tag";
 tokeniser.emitTagPending();
 Tag tag = (Tag) tokeniser.read();
 assertEquals(tag.tagName(), "tag");
 assertEquals(tag.tagType(), Tag.TagType.START_TAG);

 when(characterReader.current()).thenReturn('/');
 Token endTagToken = tokeniser.read();
 assertEquals(endTagToken.tagName(), "tag");
 assertEquals(endTagToken.tagType(), Tag.TagType.END_TAG);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_ThrowsIllegalStateExceptionWhenEmitPending() {
 tokeniser.emit(new Token.Character('a'));
 tokeniser.isEmitPending = true;
 tokeniser.read();
 }

 @Test
 public void testRead_ReturnsCharacterTokenWhenPendingCharsExist() {
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit("bcdefg");
 tokeniser.charsString = "bcdefg";
 tokeniser.read();
 Token expectedToken = new Token.Character('b');
 assertEquals(expectedToken.data(), "bcdefg");
 verify(characterReader, times(3)).consume();
 }

 @Test
 public void testRead_ReturnsCharacterTokenWhenPendingStringExists() {
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new StringBuilder("bcdefg"));
 tokeniser.read();
 Token expectedToken = new Token.Character('b');
 assertEquals(expectedToken.data(), "bcdefg");
 verify(characterReader, times(3)).consume();
 }

 @Test
 public void testRead_ReturnsPendingTokenWhenPendingTagExists() {
 tokeniser.emit(new Token.StartTag("tag"));
 tokeniser.read();
 Tag expectedTag = new Token.StartTag("tag");
 assertEquals(expectedTag, tokeniser.read());
 }

 @Test
 public void testRead_ReturnsPendingTagEndWhenPendingEndTagExists() {
 tokeniser.emit(new Token.StartTag("tag"));
 tokeniser.emit(new Token.EndTag("tag"));
 tokeniser.read();
 Token expectedEndTag = new Token.EndTag("tag");
 assertEquals(expectedEndTag, tokeniser.read());
 }

 @Test
 public void testRead_ReturnsPendingCommentWhenPendingCommentExists() {
 tokeniser.emit(new Token.Comment("comment"));
 tokeniser.read();
 Token expectedComment = new Token.Comment("comment");
 assertEquals(expectedComment, tokeniser.read());
 }

 @Test
 public void testRead_ReturnsPendingDoctypeWhenPendingDoctypeExists() {
 tokeniser.emit(new Token.Doctype("name", null));
 tokeniser.read();
 Token expectedDoctype = new Token.Doctype("name", null);
 assertEquals(expectedDoctype, tokeniser.read());
 }

 @Test
 public void testRead_ThrowsParseErrorWhenCharacterReferenceError() {
 characterReader = Mockito.spy(new CharacterReader("&#x12345;"));
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 tokeniser.read();
 assertEquals(1, parseErrorList.size());
 ParseError error = parseErrorList.get(0);
 assertEquals("Invalid character reference: unexpected character '#' in state [DATA]", error.getMessage());
 }

 @Test
 public void testRead_ThrowsParseErrorWhenInvalidNamedEntityReference() {
 characterReader = Mockito.spy(new CharacterReader("&#xABCD; &abcd;"));
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 tokeniser.read();
 assertEquals(2, parseErrorList.size());
 ParseError error1 = parseErrorList.get(0);
 assertEquals("Invalid character reference: unexpected character 'A' in state [DATA]", error1.getMessage());
 ParseError error2 = parseErrorList.get(1);
 assertEquals("Invalid character reference: unexpected character 'b' in state [DATA]", error2.getMessage());
 }

 @Test
 public void testRead_ThrowsParseErrorWhenEofError() throws IOException {
 characterReader = Mockito.mock(CharacterReader.class);
 when(characterReader.isEmpty()).thenReturn(true);
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 tokeniser.read();
 assertEquals(1, parseErrorList.size());
 ParseError error = parseErrorList.get(0);
 assertEquals("Unexpectedly reached end of file (EOF) in input state [DATA]", error.getMessage());
 }

 @Test
 public void testRead_ThrowsParseErrorWhenUnexpectedCharacter() throws IOException {
 characterReader = Mockito.mock(CharacterReader.class);
 when(characterReader.current()).thenReturn('>');
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 tokeniser.read();
 assertEquals(1, parseErrorList.size());
 ParseError error = parseErrorList.get(0);
 assertEquals("Unexpected character '>' in input state [DATA]", error.getMessage());
 }

 @Test
 public void testGetState() {
 TokeniserState expectedState = TokeniserState.DATA;
 TokeniserState actualState = tokeniser.getState();
 assertEquals(expectedState, actualState);
 }

 @Test
 public void testTransition() {
 TokeniserState expectedState = TokeniserState.SCRIPT;
 tokeniser.transition(expectedState);
 TokeniserState actualState = tokeniser.getState();
 assertEquals(expectedState, actualState);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertNotNull(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testUnescapeEntities() {
 tokeniser.emit(new Token.StartTag("script"));
 String entities = tokeniser.unescapeEntities(false);
 assertEquals("script\n<![CDATA[", entities);

 tokeniser.emit(new Token.EndTag("script"));
 entities = tokeniser.unescapeEntities(false);
 assertEquals("]]>", entities);

 tokeniser.emit(new Token.Character('&'));
 entities = tokeniser.unescapeEntities(true);
 assertEquals("&amp;", entities);

 tokeniser.emit(new Token.Character('\u00A9'));
 entities = tokeniser.unescapeEntities(true);
 assertEquals("&#xA9;", entities);

 tokeniser.emit(new Token.Character('<'));
 entities = tokeniser.unescapeEntities(true);
 assertEquals("&lt;", entities);

 tokeniser.emit(new Token.Character('>'));
 entities = tokeniser.unescapeEntities(true);
 assertEquals("&gt;", entities);
 }
}