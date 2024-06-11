import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 private Token createToken(TokenType tokenType) {
 switch (tokenType) {
 case Doctype:
 return new Token.Doctype();
 case StartTag:
 return new Token.StartTag();
 case EndTag:
 return new Token.EndTag();
 case Comment:
 return new Token.Comment();
 case Character:
 return new Token.Character();
 case EOF:
 return new Token.EOF();
 default:
 throw new IllegalArgumentException("Unknown token type: " + tokenType);
 }
 }

 @org.junit.Test
 public void testTokenType() {
 Token token = createToken(TokenType.StartTag);
 assertEquals("StartTag", token.tokenType());
 }

 @org.junit.Test
 public void testDoctypeReset() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().reset();
 assertTrue(doctypeToken.isDoctype());
 }

 @org.junit.Test
 public void testStartTagReset() {
 Token startTagToken = new Token.StartTag();
 startTagToken.asStartTag().reset();
 assertTrue(startTagToken.isStartTag());
 }

 @org.junit.Test
 public void testEndTagReset() {
 Token endTagToken = new Token.EndTag();
 endTagToken.asEndTag().reset();
 assertTrue(endTagToken.isEndTag());
 }

 @org.junit.Test
 public void testCommentReset() {
 Token commentToken = new Token.Comment();
 commentToken.asComment().reset();
 assertTrue(commentToken.isComment());
 }

 @org.junit.Test
 public void testCharacterReset() {
 Token characterToken = new Token.Character();
 characterToken.asCharacter().reset();
 assertTrue(characterToken.isCharacter());
 }

 @org.junit.Test
 public void testEOFReset() {
 Token eofToken = new Token.EOF();
 eofToken.asEOF().reset();
 assertTrue(eofToken.isEOF());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testUnknownTokenType() {
 Token unknownToken = createToken(null);
 }

 // Doctype tests

 @org.junit.Test
 public void testDoctypeGetName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().name("html");
 assertEquals("html", doctypeToken.asDoctype().getName());
 }

 @org.junit.Test
 public void testDoctypeGetPublicIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().publicIdentifier("public id");
 assertEquals("public id", doctypeToken.asDoctype().getPublicIdentifier());
 }

 @org.junit.Test
 public void testDoctypeGetSystemIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().systemIdentifier("system id");
 assertEquals("system id", doctypeToken.asDoctype().getSystemIdentifier());
 }

 @org.junit.Test
 public void testDoctypeIsForceQuirks() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().isForceQuirks();
 doctypeToken.asDoctype().isForceQuirks(true);
 assertTrue(doctypeToken.asDoctype().isForceQuirks());
 }

 // StartTag tests

 @org.junit.Test
 public void testStartTagName() {
 Token startTagToken = new Token.StartTag();
 startTagToken.asStartTag().name("div");
 assertEquals("div", startTagToken.asStartTag().name());
 }

 @org.junit.Test
 public void testStartTagNormalName() {
 Token startTagToken = new Token.StartTag();
 startTagToken.asStartTag().name("DIV");
 assertEquals("div", startTagToken.asStartTag().normalName());
 }

 @org.junit.Test
 public void testStartTagSelfClosing() {
 Token startTagToken = new Token.StartTag();
 startTagToken.asStartTag().selfClosing(true);
 assertTrue(startTagToken.asStartTag().isSelfClosing());
 }

 @org.junit.Test
 public void testStartTagAttributes() {
 Token startTagToken = new Token.StartTag();
 startTagToken.asStartTag().newAttribute();
 startTagToken.asStartTag().newAttribute("class", "class1 class2");
 startTagToken.asStartTag().newAttribute("id", "id1");
 startTagToken.asStartTag().finaliseTag();
 assertEquals(2, startTagToken.asStartTag().getAttributes().size());
 }

 // EndTag tests

 @org.junit.Test
 public void testEndTagName() {
 Token endTagToken = new Token.EndTag();
 endTagToken.asEndTag().name("div");
 assertEquals("div", endTagToken.asEndTag().name());
 }

 // Comment tests

 @org.junit.Test
 public void testCommentAppendData() {
 Token commentToken = new Token.Comment();
 commentToken.asComment().appendAttributeValue("<!-- Test -->");
 assertEquals("Test", commentToken.asComment().getData());
 }

 @org.junit.Test
 public void testCommentAppendDataMultipleTimes() {
 Token commentToken = new Token.Comment();
 commentToken.asComment().appendAttributeValue("<!-- Test ");
 commentToken.asComment().appendAttributeValue("Test -->");
 assertEquals("Test", commentToken.asComment().getData());
 }

 @org.junit.Test
 public void testCommentBogus() {
 Token commentToken = new Token.Comment();
 commentToken.asComment().bogus(true);
 assertTrue(commentToken.asComment().isBogus());
 }

 // Character tests

 @org.junit.Test
 public void testCharacterAppendData() {
 Token characterToken = new Token.Character();
 characterToken.asCharacter().data("A");
 assertEquals("A", characterToken.asCharacter().getData());
 }

 @org.junit.Test
 public void testCharacterAppendDataMultipleTimes() {
 Token characterToken = new Token.Character();
 characterToken.asCharacter().data("A");
 characterToken.asCharacter().data("B");
 assertEquals("AB", characterToken.asCharacter().getData());
 }

 @org.junit.Test
 public void testCharacterEmptyValue() {
 Token characterToken = new Token.Character();
 characterToken.asCharacter().setEmptyAttributeValue();
 assertEquals("", characterToken.asCharacter().getData());
 }
}