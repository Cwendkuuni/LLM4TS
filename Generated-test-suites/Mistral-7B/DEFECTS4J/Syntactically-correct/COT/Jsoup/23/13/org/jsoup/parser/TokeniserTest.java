import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 assertNull(token);

 reader.append("a");
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('a', ((Character) token).chars[0]);

 reader.append("bc");
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('b', ((Character) token).chars[0]);
 assertEquals('c', ((Character) token).chars[1]);

 reader.append(" </script>");
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Character("a"));
 tokeniser.emit(new Character("b"));
 tokeniser.emit(new Character("c"));
 tokeniser.emit(new EndTag("script"));

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('a', ((Character) token).chars[0]);

 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('b', ((Character) token).chars[0]);

 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('c', ((Character) token).chars[0]);

 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 @Test
 public void testErrorHandling() {
 reader.append("<script");
 tokeniser.read();
 tokeniser.read();
 reader.append(">"); // self closing tag without /
 reader.append("</script>"); // mismatched end tag

 tokeniser.read(); // should error
 tokeniser.read(); // should error

 Token token = tokeniser.read();
 assertNull(token);

 // check for errors
 assertEquals(2, errors.size());
 }

 @Test
 public void testCharacterReference() {
 reader.append("&a;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character expected = new Character("a");
 Character actual = (Character) tokeniser.consumeCharacterReference(null, false);
 assertEquals(expected, actual);

 reader.append("&amp;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character ampersand = new Character('&');
 Character actualAmpersand = (Character) tokeniser.consumeCharacterReference(ampsander, false);
 assertEquals(ampsander, actualAmpersand);

 reader.append("&gt;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character greaterThan = new Character('>');
 Character actualGreaterThan = (Character) tokeniser.consumeCharacterReference(greaterThan, false);
 assertEquals(greaterThan, actualGreaterThan);

 reader.append("&lt;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character lessThan = new Character('<');
 Character actualLessThan = (Character) tokeniser.consumeCharacterReference(lessThan, false);
 assertEquals(lessThan, actualLessThan);

 reader.append("&nbsp;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character nbsp = Character.toChars(0x00A0)[0];
 Character actualNbsp = (Character) tokeniser.consumeCharacterReference(null, true); // in attribute
 assertEquals(nbsp, actualNbsp);

 reader.append("&iexcl;");
 tokeniser.read();
 tokeniser.read(); // skip &
 Character iexcl = Character.toChars(0x00A1)[0];
 Character actualIexcl = (Character) tokeniser.consumeCharacterReference(iexcl, false);
 assertEquals(iexcl, actualIexcl);
 }

 @Test
 public void testSelfClosingFlags() {
 reader.append("<script></script>");
 tokeniser.read();
 tokeniser.read(); // skip <
 tokeniser.read(); // script
 tokeniser.read(); // /script
 Token expected = new EndTag("script");
 Token actual = tokeniser.read();
 assertEquals(expected.tagName, actual.tagName);
 assertEquals(expected.type, actual.type);
 }

 @Test
 public void testStartTagWithAttributes() {
 reader.append("<div id='test' class='testClass'/>");
 tokeniser.read(); // <div
 Token tag = tokeniser.createTagPending(true);
 tag.tagName = "div";
 tag.attributes = new ArrayList<>();
 tag.attributes.add(new Token.Attribute("id", "test"));
 tag.attributes.add(new Token.Attribute("class", "testClass"));
 tokeniser.emitTagPending();
 tokeniser.read(); // /div
 Token expectedEndTag = new EndTag("div");
 Token actualEndTag = tokeniser.read();
 assertEquals(expectedEndTag.tagName, actualEndTag.tagName);
 }
}