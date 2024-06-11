import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing true cases
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("as"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("break"));
 // ... (add tests for all keywords)

 // Testing false cases
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("def"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("if_statement"));
 // ... (add tests for all non-keywords)
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing true cases
 assertTrue(TokenStream.isJSIdentifier("identifier1"));
 assertTrue(TokenStream.isJSIdentifier("_identifier"));
 assertTrue(TokenStream.isJSIdentifier("identifierWithNumber_123"));
 // Testing false cases
 assertFalse(TokenStream.isJSIdentifier("123identifier"));
 assertFalse(TokenStream.isJSIdentifier("identifier-with-hyphen"));
 assertFalse(TokenStream.isJSIdentifier("identifier with space"));
 // Add more test cases as needed
 }
}