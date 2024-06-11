
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

   @Test
   public void testRead() {
      // Test case to check Tokeniser's read method functionality
      
      // Create a Tokeniser object with a CharacterReader and ParseErrorList
      CharacterReader reader = new CharacterReader("test");
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      
      // Call the read method of Tokeniser
      Token token = tokeniser.read();
      
      // Check if the Token object is not null
      assertNotNull(token);
      
      // Check if the Token object has the same type as expected
      assertEquals(Token.TokenType.Data, token.type);
      
      // Check if the Token object has the same data as expected
      assertEquals("test", token.getData());
   }
   
   @Test
   public void testEmit() {
      // Test case to check Tokeniser's emit method functionality
      
      // Create a Tokeniser object with a CharacterReader and ParseErrorList
      CharacterReader reader = new CharacterReader("test");
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      
      // Create a Token object
      Token token = new Token(Token.TokenType.StartTag, "TAG");
      
      // Call the emit method of Tokeniser
      tokeniser.emit(token);
      
      // Call the read method of Tokeniser
      Token readToken = tokeniser.read();
      
      // Check if the emitted Token is the same as read Token
      assertEquals(token, readToken);
   }
   
   @Test
   public void testCharacterReferenceError() {
      // Test case to check Tokeniser's characterReferenceError method functionality
      
      // Create a Tokeniser object with a CharacterReader and ParseErrorList
      CharacterReader reader = new CharacterReader("test");
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      
      // Call the characterReferenceError method of Tokeniser
      tokeniser.characterReferenceError("error");
      
      // Check if the error list contains the expected error message
      assertTrue(errors.contains("Invalid character reference: error"));
   }
   
   @Test
   public void testIsAppropriateEndTagToken() {
      // Test case to check Tokeniser's isAppropriateEndTagToken method functionality
      
      // Create a Tokeniser object with a CharacterReader and ParseErrorList
      CharacterReader reader = new CharacterReader("test");
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      
      // Set the lastStartTag field of Tokeniser
      tokeniser.lastStartTag = "TAG";
      
      // Call the isAppropriateEndTagToken method of Tokeniser
      boolean result = tokeniser.isAppropriateEndTagToken();
      
      // Check if the result is true as expected
      assertTrue(result);
      
      // Set the lastStartTag field of Tokeniser to null
      tokeniser.lastStartTag = null;
      
      // Call the isAppropriateEndTagToken method of Tokeniser again
      result = tokeniser.isAppropriateEndTagToken();
      
      // Check if the result is false as expected
      assertFalse(result);
   }
   
   @Test
   public void testUnescapeEntities() {
      // Test case to check Tokeniser's unescapeEntities method functionality
      
      // Create a Tokeniser object with a CharacterReader and ParseErrorList
      CharacterReader reader = new CharacterReader("a&amp;b");
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      
      // Call the unescapeEntities method of Tokeniser
      String result = tokeniser.unescapeEntities(false);
      
      // Check if the result is the expected string with entities unescaped
      assertEquals("a&b", result);
   }
}
