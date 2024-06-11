import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 5");
 doctypeToken.publicIdentifier("HTML5");
 doctypeToken.systemIdentifier("/path/to/dtd/html5.dtd");
 doctypeToken.forceQuirks(true);

 Token expectedDoctypeToken = new Doctype()
 .name("html")
 .pubSysKey("DTD HTML 5")
 .publicIdentifier("HTML5")
 .systemIdentifier("/path/to/dtd/html5.dtd")
 .forceQuirks(true);

 Assert.assertEquals(expectedDoctypeToken.tokenType(), doctypeToken.tokenType());
 Assert.assertEquals(expectedDoctypeToken.getClass(), doctypeToken.getClass());
 Assert.assertEquals(expectedDoctypeToken.getName(), doctypeToken.getName());
 Assert.assertEquals(expectedDoctypeToken.getPubSysKey(), doctypeToken.getPubSysKey());
 Assert.assertEquals(expectedDoctypeToken.getPublicIdentifier(), doctypeToken.getPublicIdentifier());
 Assert.assertEquals(expectedDoctypeToken.getSystemIdentifier(), doctypeToken.getSystemIdentifier());
 Assert.assertEquals(expectedDoctypeToken.isForceQuirks(), doctypeToken.isForceQuirks());
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 5");
 doctypeToken.publicIdentifier("HTML5");
 doctypeToken.systemIdentifier("/path/to/dtd/html5.dtd");
 doctypeToken.forceQuirks(true);

 doctypeToken.reset();

 Token expectedResetDoctypeToken = new Doctype();

 Assert.assertEquals(expectedResetDoctypeToken.tokenType(), doctypeToken.tokenType());
 Assert.assertEquals(expectedResetDoctypeToken.getClass(), doctypeToken.getClass());
 Assert.assertNull(doctypeToken.getName());
 Assert.assertNull(doctypeToken.getPubSysKey());
 Assert.assertNull(doctypeToken.getPublicIdentifier());
 Assert.assertNull(doctypeToken.getSystemIdentifier());
 Assert.assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html");
 startTagToken.attributes(new Attributes().put("class", "app"));

 Token expectedStartTagToken = new StartTag()
 .name("html")
 .attributes(new Attributes().put("class", "app"));

 Assert.assertEquals(expectedStartTagToken.tokenType(), startTagToken.tokenType());
 Assert.assertEquals(expectedStartTagToken.getClass(), startTagToken.getClass());
 Assert.assertEquals(expectedStartTagToken.getName(), startTagToken.getName());
 Assert.assertEquals(expectedStartTagToken.getAttributes(), startTagToken.getAttributes());
 }

 @Test
 public void testResetTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").attributes(new Attributes().put("class", "app"));

 startTagToken.reset();

 Token expectedResetTagToken = new StartTag();

 Assert.assertEquals(expectedResetTagToken.tokenType(), startTagToken.tokenType());
 Assert.assertEquals(expectedResetTagToken.getClass(), startTagToken.getClass());
 Assert.assertNull(startTagToken.getName());
 Assert.assertNull(startTagToken.getAttributes());
 }

 @Test
 public void testInvalidToken() {
 Object invalidToken = new Object();

 Assert.assertFalse(invalidToken instanceof Token);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagTokenName() {
 StartTag startTagToken = new StartTag();
 startTagToken.name(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagTokenAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.attributes = null;
 }

 @Test(expected = NullPointerException.class)
 public void testNullDataForCharacterToken() {
 Character characterToken = new Character();
 characterToken.data(null);
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("Hello World!");

 Token expectedCharacterToken = new Character().data("Hello World!");

 Assert.assertEquals(expectedCharacterToken.tokenType(), characterToken.tokenType());
 Assert.assertEquals(expectedCharacterToken.getClass(), characterToken.getClass());
 Assert.assertEquals(expectedCharacterToken.getData(), characterToken.getData());
 }

 @Test
 public void testResetCharacterToken() {
 Character characterToken = new Character().data("Hello World!");

 characterToken.reset();

 Token expectedResetCharacterToken = new Character();

 Assert.assertEquals(expectedResetCharacterToken.tokenType(), characterToken.tokenType());
 Assert.assertEquals(expectedResetCharacterToken.getClass(), characterToken.getClass());
 Assert.assertNull(characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedEOFToken = new EOF();

 Assert.assertEquals(expectedEOFToken.tokenType(), eofToken.tokenType());
 Assert.assertEquals(expectedEOFToken.getClass(), eofToken.getClass());
 }

 @Test
 public void testResetEOFToken() {
 EOF eofToken = new EOF();

 eofToken.reset();

 Token expectedResetEOFToken = new EOF();

 Assert.assertEquals(expectedResetEOFToken.tokenType(), eofToken.tokenType());
 Assert.assertEquals(expectedResetEOFToken.getClass(), eofToken.getClass());
 }
}