import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

  // Test read() method
  @Test
  public void testRead() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    Token token = tokeniser.read();

    assertEquals(new Token.Character("input"), token);
  }

  // Test emit() method
  @Test
  public void testEmit() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.emit("output");

    assertEquals("output", tokeniser.emitPending);
    assertEquals(true, tokeniser.isEmitPending);
  }

  // Test emit(char) method
  @Test
  public void testEmitChar() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.emit('o');

    assertEquals("o", tokeniser.charBuffer.toString());
  }

  // Test getState() method
  @Test
  public void testGetState() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    TokeniserState state = tokeniser.getState();

    assertEquals(TokeniserState.Data, state);
  }

  // Test transition() method
  @Test
  public void testTransition() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.transition(TokeniserState.TagName);

    assertEquals(TokeniserState.TagName, tokeniser.getState());
  }

  // Test advanceTransition() method
  @Test
  public void testAdvanceTransition() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.advanceTransition(TokeniserState.TagName);

    assertEquals(TokeniserState.TagName, tokeniser.getState());
    assertEquals(1, tokeniser.reader.pos());
  }

  // Test acknowledgeSelfClosingFlag() method
  @Test
  public void testAcknowledgeSelfClosingFlag() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.acknowledgeSelfClosingFlag();

    assertEquals(true, tokeniser.selfClosingFlagAcknowledged);
  }

  // Test consumeCharacterReference() method
  @Test
  public void testConsumeCharacterReference() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    Character character = tokeniser.consumeCharacterReference(null, false);

    assertNull(character);
  }

  // Test createTagPending() method
  @Test
  public void testCreateTagPending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    Token.Tag tag = tokeniser.createTagPending(true);

    assertEquals(true, tokeniser.tagPending);
    assertEquals(Token.Tag, tag);
  }

  // Test emitTagPending() method
  @Test
  public void testEmitTagPending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.emitTagPending();

    assertEquals(tokeniser.tagPending.finaliseTag(), tokeniser.emitPending);
  }

  // Test createCommentPending() method
  @Test
  public void testCreateCommentPending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.createCommentPending();

    assertEquals(true, tokeniser.commentPending);
  }

  // Test emitCommentPending() method
  @Test
  public void testEmitCommentPending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.emitCommentPending();

    assertEquals(tokeniser.commentPending, tokeniser.emitPending);
  }

  // Test createDoctypePending() method
  @Test
  public void testCreateDoctypePending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.createDoctypePending();

    assertEquals(true, tokeniser.doctypePending);
  }

  // Test emitDoctypePending() method
  @Test
  public void testEmitDoctypePending() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.emitDoctypePending();

    assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
  }

  // Test createTempBuffer() method
  @Test
  public void testCreateTempBuffer() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.createTempBuffer();

    assertEquals(true, tokeniser.dataBuffer);
  }

  // Test isAppropriateEndTagToken() method
  @Test
  public void testIsAppropriateEndTagToken() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.tagPending = new Token.StartTag();
    tokeniser.lastStartTag = new Token.StartTag();
    tokeniser.tagPending.tagName = "tag";

    assertEquals(true, tokeniser.isAppropriateEndTagToken());
  }

  // Test isTrackErrors() method
  @Test
  public void testIsTrackErrors() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    assertEquals(true, tokeniser.isTrackErrors());
  }

  // Test setTrackErrors() method
  @Test
  public void testSetTrackErrors() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"));

    tokeniser.setTrackErrors(false);

    assertEquals(false, tokeniser.isTrackErrors());
  }
}