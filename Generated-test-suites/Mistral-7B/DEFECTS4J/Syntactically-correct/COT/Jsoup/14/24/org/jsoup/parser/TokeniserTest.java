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
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setUp() throws Exception {
 characterReader = new MockCharacterReader();
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testConstructor() {
 Tokeniser tokeniser = new Tokeniser(characterReader);
 assertNotNull(tokeniser);
 }

 @Test
 public void testReadCharacter() {
 Character character = tokeniser.read();
 assertNotNull(character);
 characterReader.expectAndConsume('a');
 assertEquals("a", character.getData());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitPendingOnInitialRead() {
 tokeniser.emit(new Character('a'));
 tokeniser.read();
 fail("Should not be able to emit a token before reading one");
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.read(); // consume initial character
 tokeniser.emit(new Character('a'));
 Character character = tokeniser.read();
 assertNotNull(character);
 assertEquals("a", character.getData());
 }

 @Test
 public void testEmitString() {
 tokeniser.read(); // consume initial character
 tokeniser.emit("hello");
 String string = tokeniser.read().getData();
 assertEquals("hello", string);
 }

 @Test
 public void testEmitChar() {
 tokeniser.read(); // consume initial character
 tokeniser.emit('a');
 Character character = tokeniser.read();
 assertNotNull(character);
 assertEquals('a', character.getData()[0]);
 }

 @Test
 public void testGetState() {
 TokeniserState state = tokeniser.getState();
 assertEquals(TokeniserState.Data, state);
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.ProcessingInstruction);
 assertEquals(TokeniserState.ProcessingInstruction, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() {
 characterReader.expectSequence("<?xml");
 tokeniser.advanceTransition(TokeniserState.ProcessingInstruction);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character character = tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
 assertNull(character);
 characterReader.expectSequence("#x1234");
 character = tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
 assertEquals((char) 0x1234, character.getData()[0]);
 characterReader.expectSequence("#X1234");
 character = tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
 assertEquals((char) 0x1234, character.getData()[0]);
 characterReader.expectSequence("#x1f1234");
 character = tokeniser.consumeCharacterReference(Character.MAX_VALUE, false);
 assertEquals(Character.REPLACEMENT_CHAR, character.getData()[0]);
 characterReader.expectSequence("#name");
 character = tokeniser.consumeCharacterReference(Character.MIN_VALUE, false);
 assertEquals(Entities.getCharacterByName("name"), character.getData()[0]);
 characterReader.expectSequence("#NAME");
 character = tokeniser.consumeCharacterReference(Character.MIN_Value, false); // intentional typo
 assertEquals(Entities.getCharacterByName("NAME"), character.getData()[0]);
 characterReader.expectSequence("#xname");
 character = tokeniser.consumeCharacterReference(Character.MIN_VALUE, true); // in attribute
 assertNull(character);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 assertTrue(tokeniser.tagPending instanceof StartTag);
 tokeniser.createTagPending(false);
 assertTrue(tokeniser.tagPending instanceof EndTag);
 }

 @Test
 public void testEmitTagPending() {
 StartTag startTag = new StartTag("tag", new ArrayList<>());
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.tagPending.equals(startTag));
 EndTag endTag = new EndTag("tag");
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.tagPending.equals(endTag));
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 assertTrue(tokeniser.commentPending instanceof Comment);
 }

 @Test
 public void testEmitCommentPending() {
 Comment comment = new Comment("comment");
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertTrue(tokeniser.commentPending.equals(comment));
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 assertTrue(tokeniser.doctypePending instanceof Doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 Doctype doctype = new Doctype("html", new ArrayList<>(), new ArrayList<>());
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 assertTrue(tokeniser.doctypePending.equals(doctype));
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 StringBuilder dataBuffer = tokeniser.dataBuffer;
 assertNotNull(dataBuffer);
 assertEquals(0, dataBuffer.length());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 StartTag startTag = new StartTag("div");
 EndTag endTag = new EndTag("div");
 assertTrue(tokeniser.isAppropriateEndTagToken(startTag, endTag));
 }

 @Test
 public void testIsAppropriateEndTagTokenWithIncorrectTagName() {
 StartTag startTag = new StartTag("div");
 EndTag endTag = new EndTag("p");
 assertFalse(tokeniser.isAppropriateEndTagToken(startTag, endTag));
 }

 @Test(expected = NullPointerException.class)
 public void testErrorWithNullState() {
 tokeniser.error(null);
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test(expected = IllegalStateException.class)
 public void testErrorWithUnexpectedCharacterState() {
 tokeniser.error(TokeniserState.ProcessingInstruction);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError();
 }

 @Test
 public void testCurrentNodeInHtmlNS() {
 // This test relies on the currentNode() method being implemented
 // and returning a valid node. Since it is not available in the
 // provided class, this test cannot be run.
 fail("Cannot test currentNodeInHtmlNS without a valid currentNode() method");
 }

 @After
 public void tearDown() throws Exception {
 characterReader.clear();
 tokeniser.clearErrors();
 }

}