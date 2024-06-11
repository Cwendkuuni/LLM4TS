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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 @Mock
 private ParseErrorList errors;

 @Mock
 private CharacterReader reader;

 @InjectMocks
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testNormalFlow() throws Exception {
 String html = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
 tokeniser = new Tokeniser(new StringReader(html), errors);

 List<Token> expectedTokens = new ArrayList<>();
 expectedTokens.add(new StartTag("html", null, null, null));
 expectedTokens.add(new Doctype("HTML 4.01 Transitional", null, null, null));
 expectedTokens.add(new StartTag("head", null, null, null));
 expectedTokens.add(new StartTag("title", null, null, null));
 expectedTokens.add(new Character("Test"));
 expectedTokens.add(new EndTag("title"));
 expectedTokens.add(new StartTag("body", null, null, null));
 expectedTokens.add(new Character("Test"));
 expectedTokens.add(new EndTag("body"));
 expectedTokens.add(new EndTag("html"));

 Token token;
 for (int i = 0; i < expectedTokens.size(); i++) {
 token = tokeniser.read();
 verify(errors).canAddError();
 verify(errors, never()).add(new ParseError(anyInt(), anyString()));
 assertEquals(expectedTokens.get(i), token);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSelfClosingFlagNotAcknowledged() throws Exception {
 tokeniser = new Tokeniser(new StringReader(""), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read(); // this one should throw an exception
 }

 @Test
 public void testCharacterReference() throws Exception {
 tokeniser = new Tokeniser(new StringReader("<p>&#x26;<p>"), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.CharacterReference);
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertEquals(CharacterReference.AMPERSAND, character);
 tokeniser.transition(TokeniserState.Data);
 }

 @Test
 public void testNamedCharacterReference() throws Exception {
 tokeniser = new Tokeniser(new StringReader("<p>&copy;</p>"), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.CharacterReference);
 Character character = tokeniser.consumeCharacterReference(';', false);
 assertEquals(CharacterReference.COPYRIGHT_SYMB, character);
 tokeniser.transition(TokeniserState.Data);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceInAttribute() throws Exception {
 tokeniser = new Tokeniser(new StringReader("<p class=\"foo&bar\">"), errors);
 tokeniser.read(); // <p
 tokeniser.read(); // class="foo
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.AttributeValue);
 Character character = tokeniser.consumeCharacterReference(null, true); // this one should throw an exception
 }

 @Test
 public void testCDataSection() throws Exception {
 tokeniser = new Tokeniser(new StringReader("<![CDATA[foo<bar>baz]]>"), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.CDataSection);tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.Data);
 }

 @Test
 public void testScriptTag() throws Exception {
 tokeniser = new Tokeniser(new StringReader("<script src=\"script.js\"></script>"), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.ScriptTag);
 tokeniser.read(); // <script
 tokeniser.read(); // src="script.js"
 tokeniser.emitPending();
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.Data);
 }
}