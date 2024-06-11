import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Test empty string
    assertFalse(TokenStream.isKeyword(""));
    
    // Test single letter keywords
    assertTrue(TokenStream.isKeyword("f"));
    assertTrue(TokenStream.isKeyword("i"));
    assertTrue(TokenStream.isKeyword("n"));
    assertTrue(TokenStream.isKeyword("t"));
    assertTrue(TokenStream.isKeyword("v"));
    
    // Test two letter keywords
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("in"));
    assertTrue(TokenStream.isKeyword("do"));
    assertTrue(TokenStream.isKeyword("of"));
    
    // Test three letter keywords
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("try"));
    assertTrue(TokenStream.isKeyword("var"));
    
    // Test four letter keywords
    assertTrue(TokenStream.isKeyword("byte"));
    assertTrue(TokenStream.isKeyword("case"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("goto"));
    assertTrue(TokenStream.isKeyword("long"));
    assertTrue(TokenStream.isKeyword("null"));
    assertTrue(TokenStream.isKeyword("void"));
    assertTrue(TokenStream.isKeyword("with"));
    
    // Test five letter keywords
    assertTrue(TokenStream.isKeyword("class"));
    assertTrue(TokenStream.isKeyword("break"));
    assertTrue(TokenStream.isKeyword("while"));
    assertTrue(TokenStream.isKeyword("false"));
    assertTrue(TokenStream.isKeyword("const"));
    assertTrue(TokenStream.isKeyword("final"));
    assertTrue(TokenStream.isKeyword("float"));
    assertTrue(TokenStream.isKeyword("short"));
    assertTrue(TokenStream.isKeyword("super"));
    assertTrue(TokenStream.isKeyword("throw"));
    assertTrue(TokenStream.isKeyword("catch"));
    
    // Test six letter keywords
    assertTrue(TokenStream.isKeyword("native"));
    assertTrue(TokenStream.isKeyword("delete"));
    assertTrue(TokenStream.isKeyword("return"));
    assertTrue(TokenStream.isKeyword("throws"));
    assertTrue(TokenStream.isKeyword("import"));
    assertTrue(TokenStream.isKeyword("double"));
    assertTrue(TokenStream.isKeyword("static"));
    assertTrue(TokenStream.isKeyword("public"));
    assertTrue(TokenStream.isKeyword("switch"));
    assertTrue(TokenStream.isKeyword("export"));
    assertTrue(TokenStream.isKeyword("typeof"));
    
    // Test seven letter keywords
    assertTrue(TokenStream.isKeyword("package"));
    assertTrue(TokenStream.isKeyword("default"));
    assertTrue(TokenStream.isKeyword("finally"));
    assertTrue(TokenStream.isKeyword("boolean"));
    assertTrue(TokenStream.isKeyword("private"));
    assertTrue(TokenStream.isKeyword("extends"));
    
    // Test eight letter keywords
    assertTrue(TokenStream.isKeyword("abstract"));
    assertTrue(TokenStream.isKeyword("continue"));
    assertTrue(TokenStream.isKeyword("debugger"));
    assertTrue(TokenStream.isKeyword("function"));
    assertTrue(TokenStream.isKeyword("volatile"));
    
    // Test nine letter keywords
    assertTrue(TokenStream.isKeyword("interface"));
    assertTrue(TokenStream.isKeyword("protected"));
    assertTrue(TokenStream.isKeyword("transient"));
    
    // Test ten letter keywords
    assertTrue(TokenStream.isKeyword("implements"));
    assertTrue(TokenStream.isKeyword("instanceof"));
    
    // Test twelve letter keywords
    assertTrue(TokenStream.isKeyword("synchronized"));
    
    // Test invalid keywords
    assertFalse(TokenStream.isKeyword("abcd"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    // Test empty string
    assertFalse(TokenStream.isJSIdentifier(""));
    
    // Test valid JS identifiers
    assertTrue(TokenStream.isJSIdentifier("test"));
    assertTrue(TokenStream.isJSIdentifier("myVariable"));
    assertTrue(TokenStream.isJSIdentifier("_private"));
    assertTrue(TokenStream.isJSIdentifier("camelCase"));
    assertTrue(TokenStream.isJSIdentifier("$id"));
  }

}