import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("_"));
 assertFalse(TokenStream.isKeyword("ab_cd"));
 assertFalse(TokenStream.isKeyword("abCD"));
 assertFalse(TokenStream.isKeyword("ab_Cd"));
 assertFalse(TokenStream.isKeyword("ab_123"));
 assertFalse(TokenStream.isKeyword("ab123"));
 assertFalse(TokenStream.isKeyword("ab_123_"));
 assertFalse(TokenStream.isKeyword("ABC"));
 assertFalse(TokenStream.isKeyword("AB_CD"));
 assertFalse(TokenStream.isKeyword("AB_123"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("a"));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("ab"));
 assertTrue(TokenStream.isJSIdentifier("abcd"));
 assertTrue(TokenStream.isJSIdentifier("ab_cd"));
 assertTrue(TokenStream.isJSIdentifier("abCd"));
 assertTrue(TokenStream.isJSIdentifier("ab_123"));
 assertTrue(TokenStream.isJSIdentifier("ab123"));
 assertTrue(TokenStream.isJSIdentifier("ab_123_"));
 assertTrue(TokenStream.isJSIdentifier("ABC"));
 assertTrue(TokenStream.isJSIdentifier("AB_CD"));
 assertTrue(TokenStream.isJSIdentifier("AB_123"));
 assertTrue(TokenStream.isJSIdentifier("A_B"));
 assertTrue(TokenStream.isJSIdentifier("_A"));
 assertTrue(TokenStream.isJSIdentifier("__A"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("abc123"));
 assertFalse(TokenStream.isJSIdentifier("!"));
 assertFalse(TokenStream.isJSIdentifier("@"));
 assertFalse(TokenStream.isJSIdentifier("#"));
 assertFalse(TokenStream.isJSIdentifier("%"));
 assertFalse(TokenStream.isJSIdentifier("^"));
 assertFalse(TokenStream.isJSIdentifier("&"));
 assertFalse(TokenStream.isJSIdentifier("*"));
 assertFalse(TokenStream.isJSIdentifier("("));
 assertFalse(TokenStream.isJSIdentifier(")"));
 assertFalse(TokenStream.isJSIdentifier("-"));
 assertFalse(TokenStream.isJSIdentifier("+"));
 assertFalse(TokenStream.isJSIdentifier("="));
 assertFalse(TokenStream.isJSIdentifier("{"));
 assertFalse(TokenStream.isJSIdentifier("}"));
 assertFalse(TokenStream.isJSIdentifier("["));
 assertFalse(TokenStream.isJSIdentifier("]"));
 assertFalse(TokenStream.isJSIdentifier("|"));
 assertFalse(TokenStream.isJSIdentifier(":"));
 assertFalse(TokenStream.isJSIdentifier(";"));
 assertFalse(TokenStream.isJSIdentifier("<"));
 assertFalse(TokenStream.isJSIdentifier(">"));
 assertFalse(TokenStream.isJSIdentifier("/"));
 assertFalse(TokenStream.isJSIdentifier("\\"));
 assertFalse(TokenStream.isJSIdentifier("?"));
 assertFalse(TokenStream.isJSIdentifier("."));
 }
}