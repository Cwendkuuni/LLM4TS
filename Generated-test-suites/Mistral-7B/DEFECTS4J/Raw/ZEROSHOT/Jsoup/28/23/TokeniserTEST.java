import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("testing <html><head></head><body><script>alert('hello')</script></body></html>");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacter() {
 tokeniser.emit("t");
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("t", token.toString());
 }

 @Test
 public void testReadWhiteSpace() {
 tokeniser.emit(" ");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals(" ", token.toString());
 }

 @Test
 public void testReadTagName() {
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 Token tag = tokeniser.tagPending;
 assertEquals("html", tag.tagName);
 tokeniser.emitTagPending();
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.emit('s');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.transition(TokeniserState.ScriptTag);
 tokeniser.advanceTransition(TokeniserState.ScriptTag);
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 Token tag = tokeniser.tagPending;
 assertEquals("script", tag.tagName);
 tokeniser.emitTagPending();
 }

 @Test
 public void testReadComment() {
 tokeniser.emit('/');
 tokeniser.emit('/');
 tokeniser.createCommentPending();
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Data);
 Token comment = tokeniser.commentPending;
 assertEquals("<!--", comment.toString());
 tokeniser.emitCommentPending();
 }

 @Test
 public void testReadDoctype() {
 tokeniser.emit('<');
 tokeniser.emit('d');
 tokeniser.emit('o');tokeniser.emit('c');
 tokeniser.emit('t');
 tokeniser.emit('y');
 tokeniser.emit('p');
 tokeniser.emit('e');
 tokeniser.emit('>');
 tokeniser.createDoctypePending();
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Data);
 Doctype doctype = tokeniser.doctypePending;
 assertEquals("DOCTYPE", doctype.doctype);
 tokeniser.emitDoctypePending();
 }

 @Test
 public void testReadEndTag() {
 tokeniser.emit('/');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('m');
 tokeniser.emit('l');
 tokeniser.transition(TokeniserState.EndTagName);
 tokeniser.advanceTransition(TokeniserState.EndTagName);
 tokeniser.advanceTransition(TokeniserState.EndTagName);
 tokeniser.advanceTransition(TokeniserState.EndTagName);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.TagEnd);
 EndTag endTag = tokeniser.tagPending;
 assertEquals("html", endTag.tagName);
 tokeniser.emitTagPending();
 }

 @Test
 public void testReadAttributes() {
 tokeniser.emit('<');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('s');
 tokeniser.emit('u');
 tokeniser.emit('r');
 tokeniser.emit(' ');
 tokeniser.emit('c');
 tokeniser.emit('l');
 tokeniser.emit('a');
 tokeniser.emit('s');
 tokeniser.emit('s');
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.transition(TokeniserState.Attributes);
 tokeniser.advanceTransition(TokeniserState.Attribute);
 tokeniser.advanceTransition(TokeniserState.AttributeName);
 tokeniser.advanceTransition(TokeniserState.AttributeName);
 tokeniser.advanceTransition(TokeniserState.AttributeValueQuoted);
 tokeniser.advanceTransition(TokeniserState.AttributeValueQuoted);
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.advanceTransition(TokeniserState.AttributeValue);
 tokeniser.advanceTransition(TokeniserState.AttributeValue);
 tokeniser.transition(TokeniserState.AttributeEnd);
 StartTag startTag = tokeniser.tagPending;
 List<Attribute> attributes = startTag.attributes;
 assertEquals(2, attributes.size());
 Attribute attr1 = attributes.get(0);
 assertEquals("src", attr1.name);
 Attribute attr2 = attributes.get(1);
 assertEquals("class", attr2.name);
 tokeniser.emitTagPending();
 }

 @Test(expected = AssertionError.class)
 public void testInvalidCharacterReference() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference((char) 0xFFFE, false);
 }

 @Test
 public void testValidCharacterReference() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('A', false);
 tokeniser.emit(';');
 assertEquals('\u0041', tokeniser.read().getValue());
 }

 @Test
 public void testValidNamedCharacterReference() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference("ampersand", false);
 tokeniser.emit(';');
 assertEquals("&", tokeniser.read().getValue());
 }

 @Test
 public void testCharacterReferenceInAttribute() {
 tokeniser.emit('<');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('s');
 tokeniser.emit('u');
 tokeniser.emit('r');
 tokeniser.emit(' ');
 tokeniser.emit('c');
 tokeniser.emit('l');
 tokeniser.emit('a');
 tokeniser.emit('s');
 tokeniser.emit('s');
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.transition(TokeniserState.Attributes);
 tokeniser.transition(TokeniserState.Attribute);
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.advanceTransition(TokeniserState.AttributeName);
 tokeniser.transition(TokeniserState.AttributeValueQuoted);
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference("nbsp", true);
 tokeniser.emit(';');
 tokeniser.transition(TokeniserState.AttributeValue);
 Character character = tokeniser.read();
 assertEquals('\u00A0', character.getValue());
 tokeniser.transition(TokeniserState.AttributeEnd);
 StartTag startTag = tokeniser.tagPending;
 List<Attribute> attributes = startTag.attributes;
 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("class", attribute.name);
 tokeniser.emitTagPending();
 }

}