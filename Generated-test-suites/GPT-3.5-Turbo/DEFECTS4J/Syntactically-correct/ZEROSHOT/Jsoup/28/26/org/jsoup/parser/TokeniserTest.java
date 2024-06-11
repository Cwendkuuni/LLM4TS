package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

  private Tokeniser tokeniser;

  @Before
  public void setUp() {
    tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
  }

  @Test
  public void testRead() {
    Token token = tokeniser.read();
    assertNull(token);
  }

  @Test
  public void testEmit() {
    Token tagToken = new Token.Tag();
    tokeniser.emit(tagToken);
    Token emittedToken = tokeniser.read();
    assertEquals(tagToken, emittedToken);
  }

  @Test
  public void testEmitString() {
    tokeniser.emit("test");
    Token emittedToken = tokeniser.read();
    assertEquals("test", ((Token.Character)emittedToken).getData());
  }

  @Test
  public void testEmitChar() {
    tokeniser.emit('t');
    Token emittedToken = tokeniser.read();
    assertEquals("t", ((Token.Character)emittedToken).getData());
  }

  @Test
  public void testGetState() {
    TokeniserState state = tokeniser.getState();
    assertEquals(TokeniserState.Data, state);
  }

  @Test
  public void testTransition() {
    tokeniser.transition(TokeniserState.TagName);
    TokeniserState newState = tokeniser.getState();
    assertEquals(TokeniserState.TagName, newState);
  }

  @Test
  public void testAdvanceTransition() {
    tokeniser.advanceTransition(TokeniserState.TagName);
    TokeniserState newState = tokeniser.getState();
    assertEquals(TokeniserState.TagName, newState);
  }

  @Test
  public void testAcknowledgeSelfClosingFlag() {
    tokeniser.acknowledgeSelfClosingFlag();
    assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
  }

  @Test
  public void testConsumeCharacterReference() {
    CharacterReader reader = new CharacterReader("&#65;");
    tokeniser.setReader(reader);
    Character character = tokeniser.consumeCharacterReference(null, false);
    assertEquals('A', character.charValue());
  }

  @Test
  public void testCreateTagPending() {
    tokeniser.createTagPending(true);
    Token.Tag tagPending = tokeniser.getTagPending();
    assertTrue(tagPending instanceof Token.StartTag);
  }

  @Test
  public void testEmitTagPending() {
    tokeniser.createTagPending(true);
    tokeniser.emitTagPending();
    Token emittedToken = tokeniser.read();
    assertTrue(emittedToken instanceof Token.StartTag);
  }

  @Test
  public void testCreateCommentPending() {
    tokeniser.createCommentPending();
    Token.Comment commentPending = tokeniser.getCommentPending();
    assertNotNull(commentPending);
  }

  @Test
  public void testEmitCommentPending() {
    tokeniser.createCommentPending();
    tokeniser.emitCommentPending();
    Token emittedToken = tokeniser.read();
    assertTrue(emittedToken instanceof Token.Comment);
  }

  @Test
  public void testCreateDoctypePending() {
    tokeniser.createDoctypePending();
    Token.Doctype doctypePending = tokeniser.getDoctypePending();
    assertNotNull(doctypePending);
  }

  @Test
  public void testEmitDoctypePending() {
    tokeniser.createDoctypePending();
    tokeniser.emitDoctypePending();
    Token emittedToken = tokeniser.read();
    assertTrue(emittedToken instanceof Token.Doctype);
  }

  @Test
  public void testCreateTempBuffer() {
    tokeniser.createTempBuffer();
    StringBuilder dataBuffer = tokeniser.getDataBuffer();
    assertNotNull(dataBuffer);
  }

  @Test
  public void testIsAppropriateEndTagToken() {
    assertFalse(tokeniser.isAppropriateEndTagToken());
    tokeniser.createTagPending(true);
    tokeniser.getTagPending().setTagName("div");
    tokeniser.setLastStartTag(new Token.StartTag());
    tokeniser.getLastStartTag().setTagName("div");
    assertTrue(tokeniser.isAppropriateEndTagToken());
  }

  @Test
  public void testAppropriateEndTagName() {
    tokeniser.setLastStartTag(new Token.StartTag());
    tokeniser.getLastStartTag().setTagName("div");
    String tagName = tokeniser.appropriateEndTagName();
    assertEquals("div", tagName);
  }

  @Test
  public void testError() {
    // TODO: Implement test
  }

  @Test
  public void testEofError() {
    // TODO: Implement test
  }

  @Test
  public void testCharacterReferenceError() {
    // TODO: Implement test
  }

  @Test
  public void testError() {
    // TODO: Implement test
  }

  @Test
  public void testCurrentNodeInHtmlNS() {
    assertTrue(tokeniser.currentNodeInHtmlNS());
  }
}