package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

public class TokeniserTest {
  private Tokeniser tokeniser;

  @Before
  public void setUp() {
    tokeniser = new Tokeniser(null, null);
  }

  @Test
  public void testRead() {
    assertNotNull(tokeniser.read());
  }

  @Test
  public void testEmit() {
    Token token = new Token();
    tokeniser.emit(token);
    assertTrue(tokeniser.isEmitPending);
    assertEquals(token, tokeniser.emitPending);
  }

  @Test
  public void testEmitString() {
    String str = "test";
    tokeniser.emit(str);
    assertNotNull(tokeniser.charsString);
    assertEquals(str, tokeniser.charsString);
  }

  @Test
  public void testEmitCharArray() {
    char[] chars = {'t', 'e', 's', 't'};
    tokeniser.emit(chars);
    assertNotNull(tokeniser.charsString);
    assertEquals("test", tokeniser.charsString);
  }

  @Test
  public void testEmitInt() {
    char c = 't';
    tokeniser.emit(c);
    assertNotNull(tokeniser.charsString);
    assertEquals("t", tokeniser.charsString);
  }

  @Test
  public void testGetState() {
    assertNotNull(tokeniser.getState());
  }

  @Test
  public void testTransition() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.transition(state);
    assertEquals(state, tokeniser.getState());
  }

  @Test
  public void testAdvanceTransition() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.advanceTransition(state);
    assertEquals(state, tokeniser.getState());
  }

  @Test
  public void testAcknowledgeSelfClosingFlag() {
    tokeniser.acknowledgeSelfClosingFlag();
    assertTrue(tokeniser.selfClosingFlagAcknowledged);
  }

  @Test
  public void testConsumeCharacterReference() {
    assertArrayEquals(null, tokeniser.consumeCharacterReference(null, false));
  }

  @Test
  public void testCreateTagPending() {
    boolean start = true;
    assertNotNull(tokeniser.createTagPending(start));
    assertTrue(tokeniser.tagPending instanceof Token.StartTag);
  }

  @Test
  public void testEmitTagPending() {
    tokeniser.tagPending = tokeniser.startPending;
    tokeniser.emitTagPending();
    assertTrue(tokeniser.isEmitPending);
    assertEquals(tokeniser.startPending, tokeniser.emitPending);
  }

  @Test
  public void testCreateCommentPending() {
    tokeniser.createCommentPending();
    assertTrue(tokeniser.commentPending instanceof Token.Comment);
  }

  @Test
  public void testEmitCommentPending() {
    tokeniser.emitCommentPending();
    assertTrue(tokeniser.isEmitPending);
    assertEquals(tokeniser.commentPending, tokeniser.emitPending);
  }

  @Test
  public void testCreateDoctypePending() {
    tokeniser.createDoctypePending();
    assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
  }

  @Test
  public void testEmitDoctypePending() {
    tokeniser.emitDoctypePending();
    assertTrue(tokeniser.isEmitPending);
    assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
  }

  @Test
  public void testCreateTempBuffer() {
    tokeniser.createTempBuffer();
    assertNotNull(tokeniser.dataBuffer);
  }

  @Test
  public void testIsAppropriateEndTagToken() {
    assertFalse(tokeniser.isAppropriateEndTagToken());
    tokeniser.lastStartTag = "test";
    assertTrue(tokeniser.isAppropriateEndTagToken());
  }

  @Test
  public void testAppropriateEndTagName() {
    assertNull(tokeniser.appropriateEndTagName());
    tokeniser.lastStartTag = "test";
    assertEquals("test", tokeniser.appropriateEndTagName());
  }

  @Test
  public void testError() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.error(state);
    assertTrue(tokeniser.errors.canAddError());
  }

  @Test
  public void testEofError() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.eofError(state);
    assertTrue(tokeniser.errors.canAddError());
  }

  @Test
  public void testCharacterReferenceError() {
    String message = "test";
    tokeniser.characterReferenceError(message);
    assertTrue(tokeniser.errors.canAddError());
  }

  @Test
  public void testErrorString() {
    String errorMsg = "test";
    tokeniser.error(errorMsg);
    assertTrue(tokeniser.errors.canAddError());
  }

  @Test
  public void testCurrentNodeInHtmlNS() {
    assertTrue(tokeniser.currentNodeInHtmlNS());
  }

  @Test
  public void testUnescapeEntities() {
    boolean inAttribute = false;
    String result = tokeniser.unescapeEntities(inAttribute);
    assertNotNull(result);
  }
}