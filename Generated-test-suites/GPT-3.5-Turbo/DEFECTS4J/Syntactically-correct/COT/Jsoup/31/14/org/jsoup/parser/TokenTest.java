
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testTokenType() {
    Token token = new Token() {
      // implementation is not necessary for this test
    };
    assertEquals("Token", token.tokenType());
  }
  
  @Test
  public void testDoctypeGetName() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.name.append("html");
    assertEquals("html", doctype.getName());
  }
  
  @Test
  public void testDoctypeGetPublicIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
    assertEquals("//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
  }
  
  @Test
  public void testDoctypeGetSystemIdentifier() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
    assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
  }
  
  @Test
  public void testDoctypeIsForceQuirks() {
    Token.Doctype doctype = new Token.Doctype();
    doctype.forceQuirks = true;
    assertTrue(doctype.isForceQuirks());
  }
  
  @Test
  public void testStartTagName() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.name("div");
    assertEquals("div", startTag.name());
  }
  
  @Test
  public void testStartTagIsSelfClosing() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.selfClosing = true;
    assertTrue(startTag.isSelfClosing());
  }
  
  @Test
  public void testStartTagGetAttributes() {
    Token.StartTag startTag = new Token.StartTag();
    startTag.attributes = new Attributes();
    assertNotNull(startTag.getAttributes());
  }
  
  @Test
  public void testEndTagName() {
    Token.EndTag endTag = new Token.EndTag();
    endTag.name("div");
    assertEquals("div", endTag.name());
  }
  
  @Test
  public void testCommentGetData() {
    Token.Comment comment = new Token.Comment();
    comment.data.append("This is a comment");
    assertEquals("This is a comment", comment.getData());
  }
  
  @Test
  public void testCharacterGetData() {
    Token.Character character = new Token.Character("A");
    assertEquals("A", character.getData());
  }
  
  @Test
  public void testEOF() {
    Token.EOF eof = new Token.EOF();
    assertTrue(eof.isEOF());
  }
  
  @Test
  public void testIsDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    assertTrue(doctype.isDoctype());
  }
  
  @Test
  public void testAsDoctype() {
    Token.Doctype doctype = new Token.Doctype();
    Token token = doctype;
    assertEquals(doctype, token.asDoctype());
  }
  
  @Test
  public void testIsStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    assertTrue(startTag.isStartTag());
  }
  
  @Test
  public void testAsStartTag() {
    Token.StartTag startTag = new Token.StartTag();
    Token token = startTag;
    assertEquals(startTag, token.asStartTag());
  }
  
  @Test
  public void testIsEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    assertTrue(endTag.isEndTag());
  }
  
  @Test
  public void testAsEndTag() {
    Token.EndTag endTag = new Token.EndTag();
    Token token = endTag;
    assertEquals(endTag, token.asEndTag());
  }
  
  @Test
  public void testIsComment() {
    Token.Comment comment = new Token.Comment();
    assertTrue(comment.isComment());
  }
  
  @Test
  public void testAsComment() {
    Token.Comment comment = new Token.Comment();
    Token token = comment;
    assertEquals(comment, token.asComment());
  }
  
  @Test
  public void testIsCharacter() {
    Token.Character character = new Token.Character("A");
    assertTrue(character.isCharacter());
  }
  
  @Test
  public void testAsCharacter() {
    Token.Character character = new Token.Character("A");
    Token token = character;
    assertEquals(character, token.asCharacter());
  }
  
  @Test
  public void testIsEOF() {
    Token.EOF eof = new Token.EOF();
    assertTrue(eof.isEOF());
  }
}

