import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('st');

 Token token = tokeniser.read();
 assertEquals("Character", token.type.name());
 assertEquals("test", token.data);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('p');
 tokeniser.emit('>');

 tokeniser.read();
 tokeniser.read();
 tokeniser.read();

 Token tagStart = tokeniser.read();
 assertEquals("StartTag", tagStart.type.name());
 assertEquals("p", tagStart.tagName);
 assertNull(tagStart.data);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('p');
 tokeniser.emit('>');

 Token tagEnd = tokeniser.read();
 assertEquals("EndTag", tagEnd.type.name());
 assertEquals("p", tagEnd.tagName);
 assertNull(tagEnd.data);
 }

 @Test
 public void testReadEmptyElement() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit('>');

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit('/>');

 Token tag = tokeniser.read();
 assertEquals("StartTag", tag.type.name());
 assertEquals("img", tag.tagName);
 assertTrue(tag.selfClosing);
 }

 @Test
 public void testReadError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('&');

 tokeniser.read();
 tokeniser.read();

 assertEquals(1, errors.size());
 assertEquals("Unexpected character '&' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testReadComment() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('!');
 tokeniser.emit('--');

 List<Token> comments = new ArrayList<>();
 tokeniser.emit('>');

 while (!tokeniser.getState().isCommentEnd()) {
 Token token = tokeniser.read();
 comments.add(token);
 }

 tokeniser.read();
 tokeniser.read();

 Token comment = comments.get(0);
 assertEquals("Comment", comment.type.name());
 assertEquals("", comment.data);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('!');
 tokeniser.emit('D');
 tokeniser.emit('O');
 tokeniser.emit('C');
 tokeniser.emit('T');
 tokeniser.emit('Y');
 tokeniser.emit('P');
 tokeniser.emit('E');
 tokeniser.emit('>');
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();

 Token doctype = tokeniser.read();
 assertEquals("Doctype", doctype.type.name());
 assertNull(doctype.data);
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r' );
 tokeniser.emit('i' );
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 tokeniser.createTempBuffer();

 List<Token> script = new ArrayList<>();
 while (!tokeniser.getState().isScriptTagEnd()) {
 Token token = tokeniser.read();
 script.add(token);
 }

 tokeniser.emitScriptTagEnd();
 tokeniser.read();
 tokeniser.read();

 Token tagStart = script.get(0);
 assertEquals("StartTag", tagStart.type.name());
 assertEquals("script", tagStart.tagName);
 assertNull(tagStart.data);

 Map<String, String> expectedAttributes = new HashMap<>();
 expectedAttributes.put("type", "text/javascript");
 expectedAttributes.put("src", "about:blank");

 for (int i = 1; i < script.size(); i++) {
 Token token = script.get(i);
 assertSame(TokenType.Attribute, token.type);
 assertEquals("src", token.tag.attributeName);
 assertEquals("about:blank", token.tag.attributeValue);
 }
 }

 @Test
 public void testReadErrorFromScriptTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('<');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r' );
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 tokeniser.createTempBuffer();
 tokeniser.emit('<');
 tokeniser.emit('/');
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.emit('e');
 tokeniser.emit('r');
 tokeniser.emit('r');
 tokeniser.emit('o');
 tokeniser.emit('r');
 tokeniser.emit('>');
 tokeniser.read();
 tokeniser.read();

 assertEquals(2, errors.size());
 assertEquals("Unexpected character 'e' in input state [ScriptTag]", errors.get(0).getMessage());
 assertEquals("Attributes incorrectly present on end tag", errors.get(1).getMessage());
 }
}