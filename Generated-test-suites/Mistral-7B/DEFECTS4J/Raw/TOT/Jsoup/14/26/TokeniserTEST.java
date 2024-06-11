import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.DataUtil;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 reader = new CharacterReader(DataUtil.loadFile("test.html").getBytes());
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testReadStartTag() {
 Token startTag = tokeniser.read();
 assertEquals("html", startTag.tagName);
 assertEquals(TokenType.StartTag, startTag.type);
 }

 @Test
 public void testReadEndTag() {
 Token endTag = tokeniser.read();
 assertEquals("html", endTag.tagName);
 assertEquals(TokenType.EndTag, endTag.type);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadUnmatchedEndTag() {
 tokeniser.read(); // read <html>
 tokeniser.read(); // read </body>
 tokeniser.read(); // should throw exception
 }

 @Test
 public void testReadCharacter() {
 tokeniser.emit("a");
 Token character = tokeniser.read();
 assertEquals("a", character.data);
 assertEquals(TokenType.Character, character.type);
 }

 @Test
 public void testReadWhiteSpace() {
 tokeniser.emit(" ");
 tokeniser.emit("\t");
 tokeniser.emit("\r");
 tokeniser.emit("\n");
 Token whiteSpace = tokeniser.read();
 assertEquals(" ", whiteSpace.data);
 assertEquals(TokenType.Character, whiteSpace.type);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character ref = tokeniser.consumeCharacterReference('#', true);
 assertNull(ref); // semicolon expected
 ref = tokeniser.consumeCharacterReference("amp", true);
 assertEquals('\u0026', ref.charValue());
 ref = tokeniser.consumeCharacterReference("apos", false);
 assertEquals('\'', ref.charValue());
 ref = tokeniser.consumeCharacterReference("quot", false);
 assertEquals('"', ref.charValue());
 }

 @Test
 public void testEmitTag() {
 tokeniser.emit(tokeniser.createTagPending(true));
 tokeniser.emitTagPending();
 Token tag = tokeniser.read();
 assertEquals("html", tag.tagName);
 assertEquals(TokenType.StartTag, tag.type);
 }

 @Test
 public void testEmitComment() {
 tokeniser.emit(tokeniser.createCommentPending());
 tokeniser.emitCommentPending();
 Token comment = tokeniser.read();
 assertEquals("<!--", comment.data);
 assertEquals(TokenType.Comment, comment.type);
 }

 @Test
 public void testEmitDoctype() {
 tokeniser.emit(tokeniser.createDoctypePending());
 tokeniser.emitDoctypePending();
 Token doctype = tokeniser.read();
 assertEquals("<!DOCTYPE", doctype.data);
 assertEquals(TokenType.Doctype, doctype.type);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitAfterEmitPending() {
 tokeniser.emit(tokeniser.createTagPending(true));
 tokeniser.emitTagPending();
 tokeniser.emit('a');
 }

 @Test
 public void testReadWithErrorsDisabled() {
 Tokeniser tokeniserNoErrors = new Tokeniser(reader);
 tokeniserNoErrors.setTrackErrors(false);
 tokeniserNoErrors.read(); // should not throw exception
 }

 @Test
 public void testErrorHandling() {
 assertEquals(0, errors.size());
 tokeniser.transition(TokeniserState.StartTag); // should not acknowledge flag
 tokeniser.error("Unexpected character in input");
 assertEquals(1, errors.size());
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.StartTag); // should acknowledge flag
 assertFalse(tokeniser.isTrackErrors());
 tokeniser.error("Self closing flag not acknowledged");
 assertEquals(2, errors.size());
 tokeniser.setTrackErrors(true);
 tokeniser.error("Self closing flag not acknowledged");
 assertEquals(3, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCharacterReader() {
 new Tokeniser(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalStateException() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitTagPending();
 tokeniser.emitTagPending();
 tokeniser.read(); // should throw exception
 }
}