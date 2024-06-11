import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.getName().equals("DOCTYPE");
 doctypeToken.reset();
 }

 @Test
 public void testDoctypeTokenWithPubSysKeyAndSystemIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.getName().equals("DOCTYPE");
 doctypeToken.setName("html");
 doctypeToken.getPublicIdentifier().equals("public");
 doctypeToken.getSystemIdentifier().equals("/test.html");
 doctypeToken.isForceQuirks().equals(false);
 doctypeToken.reset();
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeTokenWithNullName() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name(null);
 }

 @Test
 public void testTagToken() {
 Tag tagToken = new StartTag();
 tagToken.name().equals("tag");
 tagToken.reset();
 }

 @Test
 public void testTagTokenWithNameAndAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("tag").name("tagName", "value");
 startTagToken.name("anotherAttribute", "anotherValue");
 startTagToken.reset();
 }

 @Test
 public void testTagTokenWithSelfClosing() {
 StartTag startTagToken = new StartTag();
 startTagToken.isSelfClosing().equals(true);
 startTagToken.name("img").reset();
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.getData().equals("--comment");
 commentToken.reset();
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.getData().equals("char");
 characterToken.data("char").reset();
 }

 @Test(expected = AssertionError.class)
 public void testCharacterTokenWithNullData() {
 Character characterToken = new Character();
 characterToken.data(null);
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 eofToken.isEOF().equals(true);
 eofToken.reset();
 }
}