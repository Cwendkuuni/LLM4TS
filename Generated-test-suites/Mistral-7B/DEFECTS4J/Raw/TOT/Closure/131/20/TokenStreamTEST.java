import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Test valid keywords
    assertTrue(isKeyword("if"));
    assertTrue(isKeyword("else"));
    assertTrue(isKeyword("for"));
    assertTrue(isKeyword("while"));
    assertTrue(isKeyword("switch"));
    assertTrue(isKeyword("case"));
    assertTrue(isKeyword("default"));
    assertTrue(isKeyword("try"));
    assertTrue(isKeyword("catch"));
    assertTrue(isKeyword("throw"));
    assertTrue(isKeyword("function"));
    assertTrue(isKeyword("var"));
    assertTrue(isKeyword("false"));
    assertTrue(isKeyword("null"));
    assertTrue(isKeyword("true"));
    assertTrue(isKeyword("break"));
    assertTrue(isKeyword("continue"));
    assertTrue(isKeyword("return"));
    assertTrue(isKeyword("this"));
    assertTrue(isKeyword("void"));
    assertTrue(isKeyword("with"));
    assertTrue(isKeyword("class"));
    assertTrue(isKeyword("const"));
    assertTrue(isKeyword("final"));
    assertTrue(isKeyword("int"));
    assertTrue(isKeyword("float"));
    assertTrue(isKeyword("double"));
    assertTrue(isKeyword("long"));
    assertTrue(isKeyword("short"));
    assertTrue(isKeyword("byte"));
    assertTrue(isKeyword("boolean"));
    assertTrue(isKeyword("public"));
    assertTrue(isKeyword("private"));
    assertTrue(isKeyword("protected"));
    assertTrue(isKeyword("static"));
    assertTrue(isKeyword("transient"));
    assertTrue(isKeyword("abstract"));
    assertTrue(isKeyword("interface"));
    assertTrue(isKeyword("package"));
    assertTrue(isKeyword("import"));
    assertTrue(isKeyword("extends"));
    assertTrue(isKeyword("implements"));
    assertTrue(isKeyword("instanceof"));
    assertTrue(isKeyword("new"));
    assertTrue(isKeyword("super"));
    assertTrue(isKeyword(" goto"));
    assertTrue(isKeyword(" throws"));
    assertTrue(isKeyword("native"));
    assertTrue(isKeyword("delete"));
    assertTrue(isKeyword("export"));
    assertTrue(isKeyword("yield"));
    assertTrue(isKeyword("import")); // duplicate for 'import' as a statement
    assertTrue(isKeyword("as"));

    // Test invalid keywords
    assertFalse(isKeyword("")); // empty string
    assertFalse(isKeyword("invalid"));
  }

  @Test
  public void testIsJSIdentifier() {
    // Test valid identifiers
    assertTrue(isJSIdentifier("identifier1"));
    assertTrue(isJSIdentifier("identifier_with_underscore"));
    assertTrue(isJSIdentifier("identifierWithDigit0"));
    assertTrue(isJSIdentifier("identifierWithDigit123"));
    assertTrue(isJSIdentifier("identifierWithDollarSign$"));

    // Test invalid identifiers
    assertFalse(isJSIdentifier("identifier-with-dash"));
    assertFalse(isJSIdentifier("identifierWithSpace "));
    assertFalse(isJSIdentifier("identifierWithTab"));
    assertFalse(isJSIdentifier("identifierWithNewline\n"));
  }
}