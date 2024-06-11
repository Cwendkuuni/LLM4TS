import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mock;

public class TokeniserTest {

  @Mock
  private CharacterReader mockReader;
  @Mock
  private ParseErrorList mockErrors;

  private Tokeniser tokeniser;

  @Before
  public void setUp() {
    mockReader = mock(CharacterReader.class);
    mockErrors = mock(ParseErrorList.class);
    tokeniser = new Tokeniser(mockReader, mockErrors);
  }

  @Test
  public void testRead() {
    Token token = new Token();
    when(mockReader.isEmpty()).thenReturn(true);
    when(mockReader.current()).thenReturn('\uFFFD');

    Token result = tokeniser.read();

    assertEquals(token, result);
    verify(mockReader).isEmpty();
    verify(mockReader).current();
  }

  @Test
  public void testEmitToken() {
    Token token = new Token();
    tokeniser.emit(token);

    assertTrue(tokeniser.isEmitPending);
    assertEquals(token, tokeniser.emitPending);
  }

  @Test
  public void testEmitString() {
    String str = "test";
    tokeniser.emit(str);

    assertEquals(str, tokeniser.charsString);
  }

  @Test
  public void testEmitCharArray() {
    char[] chars = {'t', 'e', 's', 't'};
    tokeniser.emit(chars);

    String str = new String(chars);
    assertEquals(str, tokeniser.charsString);
  }

  @Test
  public void testEmitIntArry() {
    int[] codepoints = {116, 101, 115, 116};
    tokeniser.emit(codepoints);

    String str = new String(codepoints, 0, codepoints.length);
    assertEquals(str, tokeniser.charsString);
  }

  @Test
  public void testEmitChar() {
    char c = 't';
    tokeniser.emit(c);

    String str = String.valueOf(c);
    assertEquals(str, tokeniser.charsString);
  }

  @Test
  public void testGetState() {
    TokeniserState state = TokeniserState.Comment;
    tokeniser.transition(state);

    TokeniserState result = tokeniser.getState();

    assertEquals(state, result);
  }

  @Test
  public void testTransition() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.transition(state);

    TokeniserState result = tokeniser.getState();

    assertEquals(state, result);
  }

  @Test
  public void testAdvanceTransition() {
    TokeniserState state = TokeniserState.TagName;
    tokeniser.advanceTransition(state);

    TokeniserState result = tokeniser.getState();

    verify(mockReader).advance();
    assertEquals(state, result);
  }

  @Test
  public void testAcknowledgeSelfClosingFlag() {
    tokeniser.acknowledgeSelfClosingFlag();

    assertTrue(tokeniser.selfClosingFlagAcknowledged);
  }

  @Test
  public void testConsumeCharacterReference() {
    Character additionalAllowedCharacter = ' ';
    boolean inAttribute = true;
    int[] codeRef = {116, 101, 115, 116};

    when(mockReader.isEmpty()).thenReturn(false);
    when(mockReader.current()).thenReturn('t');
    when(mockReader.matchesAnySorted(any(char[].class))).thenReturn(false);
    when(mockReader.mark()).thenReturn(0);
    when(mockReader.matchConsume("#")).thenReturn(false);
    when(mockReader.matchConsumeIgnoreCase("X")).thenReturn(false);
    when(mockReader.consumeHexSequence()).thenReturn(null);
    when(mockReader.consumeDigitSequence()).thenReturn(null);
    when(mockReader.matchConsume(";")).thenReturn(false);
    when(mockReader.rewindToMark()).thenReturn(0);
    when(mockReader.matchesLetter()).thenReturn(false);
    when(mockReader.matchesDigit()).thenReturn(false);
    when(mockReader.matchesAny(any(char[].class))).thenReturn(false);
    when(mockReader.rewindToMark()).thenReturn(0);
    when(mockErrors.canAddError()).thenReturn(true);

    int[] result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

    verify(mockReader).isEmpty();
    verify(mockReader).current();
    verify(mockReader).matchesAnySorted(any(char[].class));
    verify(mockReader).mark();
    verify(mockReader).matchConsume("#");
    verify(mockReader).consumeHexSequence();
    verify(mockReader).consumeDigitSequence();
    verify(mockReader).matchConsumeIgnoreCase("X");
    verify(mockReader).matchConsume(";");
    verify(mockReader).rewindToMark();
    verify(mockReader).matchesLetter();
    verify(mockReader).matchesDigit();
    verify(mockReader).matchesAny(any(char[].class));
    verify(mockErrors).canAddError();
    assertArrayEquals(codeRef, result);
  }

  @Test
  public void testCreateTagPending() {
    boolean start = true;
    Token.Tag tag = new Token.Tag();

    Token.Tag result = tokeniser.createTagPending(start);

    assertTrue(start ? tokeniser.startPending : tokeniser.endPending == result);
    verify(mockReader, atLeast(0)).advance();
  }

  @Test
  public void testEmitTagPending() {
    tokeniser.tagPending = new Token.Tag();
    tokeniser.emitTagPending();

    assertNotEquals(tokeniser.tagPending, tokeniser.startPending);
  }

  @Test
  public void testCreateCommentPending() {
    tokeniser.createCommentPending();

    assertNotNull(tokeniser.commentPending);
  }

  @Test
  public void testEmitCommentPending() {
    tokeniser.commentPending = new Token.Comment();
    tokeniser.emitCommentPending();

    assertNotEquals(tokeniser.commentPending, tokeniser.commentPending.reset());
  }

  @Test
  public void testCreateDoctypePending() {
    tokeniser.createDoctypePending();

    assertNotNull(tokeniser.doctypePending);
  }

  @Test
  public void testEmitDoctypePending() {
    tokeniser.doctypePending = new Token.Doctype();
    tokeniser.emitDoctypePending();

    assertNotEquals(tokeniser.doctypePending, tokeniser.doctypePending.reset());
  }

  @Test
  public void testCreateTempBuffer() {
    tokeniser.createTempBuffer();

    assertNotNull(tokeniser.dataBuffer);
  }

  @Test
  public void testIsAppropriateEndTagToken() {
    tokeniser.lastStartTag = "start";
    tokeniser.tagPending = Token.Tag();

    boolean result = tokeniser.isAppropriateEndTagToken();

    assertTrue(result);
  }

  @Test
  public void testAppropriateEndTagName() {
    tokeniser.lastStartTag = "start";

    String result = tokeniser.appropriateEndTagName();

    assertEquals(tokeniser.lastStartTag, result);
  }

  @Test
  public void testErrorState() {
    TokeniserState state = TokeniserState.Comment;
    tokeniser.error(state);

    verify(mockErrors).canAddError();
  }

  @Test
  public void testEofError() {
    TokeniserState state = TokeniserState.Comment;
    tokeniser.eofError(state);

    verify(mockErrors).canAddError();
  }

  @Test
  public void testCharacterReferenceError() {
    String message = "Invalid character reference";
    tokeniser.characterReferenceError(message);

    verify(mockErrors).canAddError();
  }

  @Test
  public void testError() {
    String errorMsg = "Error message";
    tokeniser.error(errorMsg);

    verify(mockErrors).canAddError();
  }

  @Test
  public void testGetCurrentNodeInHtmlNS() {
    boolean result = tokeniser.currentNodeInHtmlNS();

    assertTrue(result);
  }

  @Test
  public void testUnescapeEntities() {
    boolean inAttribute = true;
    String expected = "test";
    when(mockReader.isEmpty()).thenReturn(false);
    when(mockReader.consumeTo('&')).thenReturn("test");
    when(mockReader.matches('&')).thenReturn(true);
    when(mockReader.consume()).thenReturn('&');
    when(mockReader.current()).thenReturn('&');
    when(mockReader.current()).thenReturn('t');
    when(mockReader.matches('&')).thenReturn(false);

    String result = tokeniser.unescapeEntities(inAttribute);

    verify(mockReader).isEmpty();
    verify(mockReader, times(2)).consumeTo('&');
    verify(mockReader, times(2)).matches('&');
    verify(mockReader, times(2)).current();
    assertEquals(expected, result);
  }
}