import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
  
  private Tokeniser tokeniser;
  private CharacterReader reader;
  private ParseErrorList errors;

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
  }

  @Test
  public void testEmit() {
    Token.Tag tag = tokeniser.createTagPending(true);
    tag.addAttribute("attribute", "value");
    tokeniser.emitTagPending();
    
    Token emittedToken = tokeniser.read();
    assertNotNull(emittedToken);
  }
  
  @Test
  public void testCreateCommentPending() {
    tokeniser.createCommentPending();
    assertTrue(tokeniser.commentPending.isPending);
  }
  
  @Test
  public void testEmitCommentPending() {
    tokeniser.createCommentPending();
    tokeniser.commentPending.addData("This is a comment");
    tokeniser.emitCommentPending();
    
    Token emittedToken = tokeniser.read();
    assertNotNull(emittedToken);
  }
  
  @Test
  public void testCreateDoctypePending() {
    tokeniser.createDoctypePending();
    assertTrue(tokeniser.doctypePending.isPending);
  }
  
  @Test
  public void testEmitDoctypePending() {
    tokeniser.createDoctypePending();
    tokeniser.doctypePending.setName("html");
    tokeniser.emitDoctypePending();
    
    Token emittedToken = tokeniser.read();
    assertNotNull(emittedToken);
  }
  
  @Test
  public void testCreateTempBuffer() {
    tokeniser.createTempBuffer();
    assertNotNull(tokeniser.dataBuffer);
  }
  
  @Test
  public void testIsAppropriateEndTagToken() {
    tokeniser.lastStartTag = "html";
    assertTrue(tokeniser.isAppropriateEndTagToken());
    
    tokeniser.lastStartTag = null;
    assertFalse(tokeniser.isAppropriateEndTagToken());
  }
  
  @Test
  public void testAppropriateEndTagName() {
    tokeniser.lastStartTag = "html";
    assertEquals("html", tokeniser.appropriateEndTagName());
    
    tokeniser.lastStartTag = null;
    assertNull(tokeniser.appropriateEndTagName());
  }
  
  @Test
  public void testError() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.error(state);
    
    assertFalse(errors.isEmpty());
  }
  
  @Test
  public void testEofError() {
    TokeniserState state = TokeniserState.Data;
    tokeniser.eofError(state);
    
    assertFalse(errors.isEmpty());
  }
  
  @Test
  public void testCharacterReferenceError() {
    String message = "Invalid character reference";
    tokeniser.characterReferenceError(message);
    
    assertFalse(errors.isEmpty());
  }
  
  @Test
  public void testErrorWithString() {
    String errorMsg = "Error in tokeniser";
    tokeniser.error(errorMsg);
    
    assertFalse(errors.isEmpty());
  }
  
  @Test
  public void testCurrentNodeInHtmlNS() {
    assertTrue(tokeniser.currentNodeInHtmlNS());
  }
  
  @Test
  public void testUnescapeEntities() {
    String unescaped = tokeniser.unescapeEntities(true);
    assertNotNull(unescaped);
  }
}