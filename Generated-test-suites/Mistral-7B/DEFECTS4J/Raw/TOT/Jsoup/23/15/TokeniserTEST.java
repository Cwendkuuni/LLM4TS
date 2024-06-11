import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader("Test Data");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emit("<html>");
 Token expected = new StartTag("html");
 Token actual = tokeniser.read();
 assertEquals(expected, actual);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emit("<html>");
 tokeniser.emit("<html/>");
 Token expected = new EndTag("html");
 Token actual = tokeniser.read();
 assertEquals(expected, actual);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("A");
 Token expected = new Character("A");
 Token actual = tokeniser.read();
 assertEquals(expected, actual);
 }

 @Test
 public void testReadComment() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createCommentPending();
 tokeniser.emit("<!-- Test Comment -->");
 tokeniser.emitCommentPending();
 Token expected = new Comment("Test Comment");
 Token actual = tokeniser.read();
 assertEquals(expected, actual);
 }

 @Test
 public void testReadError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error("Unexpected character '%s' in input state [%s]", reader.current(), TokeniserState.Data);
 ParseError error = errors.get(0);
 assertEquals("Unexpected character '.' in input state [Data]", error.getMessage());
 }

 @Test
 public void testReadEOF() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);
 ParseError error = errors.get(0);
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emit("<img src='image.jpg' />");
 Token expected = new StartTag("img", null, null, true);
 Token actual = tokeniser.read();
 assertEquals(expected, actual);
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&#10;");
 Character actual = tokeniser.read().asCharacter();
 assertEquals('\n', actual);
 }

 @Test
 public void testReadNamedCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&#x2014;");
 Character actual = tokeniser.read().asCharacter();
 assertEquals('\u2014', actual.intValue());
 }

 @Test
 public void testReadSelfClosingTagWithAttribute() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.createTagPending(false);
 tokeniser.emit("<img src='image.jpg' alt='Test Image' />");
 StartTag expectedStartTag = new StartTag("img", new ArrayList<Attribute>() {{
 add(new Attribute("src", "image.jpg"));
 add(new Attribute("alt", "Test Image"));
 }}, null, true);
 StartTag actualStartTag = (StartTag) tokeniser.read();
 assertEquals(expectedStartTag, actualStartTag);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 tokeniser.emit("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 Doctype expectedDoctype = new Doctype("html", "HTML");
 Doctype actualDoctype = tokeniser.readDoctypePending();
 assertEquals(expectedDoctype, actualDoctype);
 }

 @Test
 public void testReadAppropriateEndTag() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.emit("<html><body><p>Test</p></p></body></html>");
 assertTrue(tokeniser.isAppropriateEndTagToken());
 String expectedEndTagName = "p";
 String actualEndTagName = tokeniser.appropriateEndTagName();
 assertEquals(expectedEndTagName, actualEndTagName);
 }

 @Test
 public void testReadInappropriateEndTag() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.emit("<html><body><p>Test</p></head></html>");
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testReadCharactersInString() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("Test & \"'< >");
 StringBuilder expected = new StringBuilder("Test & \"'< >");
 StringBuilder actual = new StringBuilder();
 while (tokeniser.charBuffer.length() > 0) {
 actual.append(tokeniser.read().asCharacter());
 }
 assertEquals(expected, actual);
 }
}