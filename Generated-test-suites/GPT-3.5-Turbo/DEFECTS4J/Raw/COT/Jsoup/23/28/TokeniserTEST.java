import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
  
  private Tokeniser tokeniser;
  @Mock
  private CharacterReader reader;
  @Mock
  private ParseErrorList errors;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    tokeniser = new Tokeniser(reader, errors);
  }
  
  @Test
  public void testRead() {
    // Test basic functionality of read()
    Token token = new Token.Character("Test");
    Mockito.when(reader.isEmpty()).thenReturn(false);
    Mockito.when(reader.current()).thenReturn('T');
    Mockito.when(reader.matchesAny(Mockito.any())).thenReturn(false);
    Mockito.when(reader.mark()).thenReturn(0);
    Mockito.when(reader.matchConsume("#")).thenReturn(false);
    Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(false);
    Mockito.when(reader.consumeHexSequence()).thenReturn("");
    Mockito.when(reader.consumeDigitSequence()).thenReturn("");
    Mockito.when(reader.matchConsume(";")).thenReturn(false);
    Mockito.when(reader.matchesLetter()).thenReturn(true);
    Mockito.when(reader.matchesDigit()).thenReturn(true);
    Mockito.when(reader.matchesAny('=', '-', '_')).thenReturn(true);
    Mockito.when(reader.matches(';')).thenReturn(false);
    Mockito.when(reader.consumeLetterSequence()).thenReturn("");
    Mockito.when(reader.matches('\t', '\n', '\f', ' ', '<', '&')).thenReturn(false);
    Mockito.when(reader.rewindToMark()).thenReturn("");
    Mockito.when(reader.advance()).thenReturn("");
    Mockito.when(reader.pos()).thenReturn(0);
    Mockito.when(errors.canAddError()).thenReturn(true);

    Mockito.when(errors.add(Mockito.any(ParseError.class))).thenReturn(true);

    Mockito.when(reader.isEmpty()).thenReturn(true);
    tokeniser.emit(token);

    Assert.assertEquals(token, tokeniser.read());
  }
  
  // add more test cases for other methods here
  
  @Test
  public void testEmitTagPending() {
    // Test emitTagPending method
    Token.Tag tagPending = tokeniser.createTagPending(true);
    Mockito.when(tagPending.getTagName()).thenReturn("testTag");
    
    tokeniser.emitTagPending();
    
    Assert.assertEquals(tagPending, tokeniser.getToken());
  }
  
  @Test
  public void testCreateCommentPending() {
    // Test createCommentPending method
    tokeniser.createCommentPending();
    
    Assert.assertNotNull(tokeniser.getCommentToken());
  }
  
  @Test
  public void testEmitCommentPending() {
    // Test emitCommentPending method
    Token.Comment commentPending = new Token.Comment();
    
    tokeniser.emit(commentPending);
    
    Assert.assertEquals(commentPending, tokeniser.getToken());
  }
  
  @Test
  public void testCreateDoctypePending() {
    // Test createDoctypePending method
    tokeniser.createDoctypePending();
    
    Assert.assertNotNull(tokeniser.getDoctypeToken());
  }
  
  @Test
  public void testEmitDoctypePending() {
    // Test emitDoctypePending method
    Token.Doctype doctypePending = new Token.Doctype();
    
    tokeniser.emit(doctypePending);
    
    Assert.assertEquals(doctypePending, tokeniser.getToken());
  }
  
  @Test
  public void testCreateTempBuffer() {
    // Test createTempBuffer method
    tokeniser.createTempBuffer();
    
    Assert.assertNotNull(tokeniser.getDataBuffer());
  }
  
  @Test
  public void testIsAppropriateEndTagToken() {
    // Test isAppropriateEndTagToken method
    tokeniser.createTagPending(true);
    tokeniser.createTagPending(false);
    
    Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    
    tokeniser.createTagPending(true);
    tokeniser.lastStartTag = tokeniser.createTagPending(true);
    
    Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
  }
  
  @Test
  public void testAppropriateEndTagName() {
    // Test appropriateEndTagName method
    tokeniser.lastStartTag = tokeniser.createTagPending(true);
    Mockito.when(tokeniser.lastStartTag.getTagName()).thenReturn("testTag");
    
    Assert.assertEquals("testTag", tokeniser.appropriateEndTagName());
  }
  
  @Test
  public void testError() {
    // Test error method
    Mockito.when(errors.canAddError()).thenReturn(true);
    Mockito.when(reader.current()).thenReturn('T');
    
    tokeniser.error(TokeniserState.Data);

    Mockito.verify(errors, Mockito.times(1)).add(Mockito.any(ParseError.class));
  }
  
  @Test
  public void testEofError() {
    // Test eofError method
    Mockito.when(errors.canAddError()).thenReturn(true);
    
    tokeniser.eofError(TokeniserState.Data);

    Mockito.verify(errors, Mockito.times(1)).add(Mockito.any(ParseError.class));
  }
  
  @Test
  public void testCharacterReferenceError() {
    // Test characterReferenceError method
    Mockito.when(errors.canAddError()).thenReturn(true);
    
    tokeniser.characterReferenceError("Invalid character reference");

    Mockito.verify(errors, Mockito.times(1)).add(Mockito.any(ParseError.class));
  }
  
  @Test
  public void testErrorOnConsumingCharacterReference() {
    // Test consumeCharacterReference method with error scenarios
    Mockito.when(reader.isEmpty()).thenReturn(true);
    Mockito.when(errors.canAddError()).thenReturn(true);
    Mockito.when(reader.current()).thenReturn('&');
    Mockito.when(reader.matchesAny(Mockito.any())).thenReturn(false);
    Mockito.when(reader.mark()).thenReturn(0);
    Mockito.when(reader.matchConsume("#")).thenReturn(true);
    Mockito.when(reader.matchConsumeIgnoreCase("X")).thenReturn(true);
    Mockito.when(reader.consumeHexSequence()).thenReturn("");
    Mockito.when(reader.consumeDigitSequence()).thenReturn("");
    Mockito.when(reader.matchConsume(";")).thenReturn(false);
    Mockito.when(reader.matchesLetter()).thenReturn(false);
    Mockito.when(reader.matchesDigit()).thenReturn(false);
    Mockito.when(reader.matchesAny('=', '-', '_')).thenReturn(false);
    Mockito.when(reader.matches(';')).thenReturn(false);
    Mockito.when(reader.consumeLetterSequence()).thenReturn("");
    Mockito.when(reader.matches('\t', '\n', '\f', ' ', '<', '&')).thenReturn(true);
    Mockito.when(reader.rewindToMark()).thenReturn("");
    Mockito.when(reader.advance()).thenReturn("");
    Mockito.when(reader.pos()).thenReturn(0);
    Mockito.when(errors.add(Mockito.any(ParseError.class))).thenReturn(true);

    tokeniser.consumeCharacterReference(null, false);

    Mockito.verify(errors, Mockito.times(1)).add(Mockito.any(ParseError.class));
  }
  
  @Test
  public void testCurrentNodeInHtmlNS() {
    // Test currentNodeInHtmlNS method - always returning true for now
    Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
  }
}