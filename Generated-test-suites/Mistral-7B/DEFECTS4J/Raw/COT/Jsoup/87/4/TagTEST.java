import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag scriptTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 scriptTag = Tag.valueOf("script");
 }

 /**
 * Test method for {@link Tag#getName()}.
 */
 @Test
 public void testGetName() {
 String pName = pTag.getName();
 String imgName = imgTag.getName();
 String scriptName = scriptTag.getName();

 assertEquals("p", pName);
 assertEquals("img", imgName);
 assertEquals("script", scriptName);
 }

 /**
 * Test method for {@link Tag#isBlock()}.
 */
 @Test
 public void testIsBlock() {
 boolean pIsBlock = pTag.isBlock();
 boolean imgIsBlock = imgTag.isBlock();
 boolean scriptIsBlock = scriptTag.isBlock();

 assertTrue(pIsBlock);
 assertFalse(imgIsBlock);
 assertFalse(scriptIsBlock);
 }

 /**
 * Test method for {@link Tag#formatAsBlock()}.
 */
 @Test
 public void testFormatAsBlock() {
 boolean pFormatAsBlock = pTag.formatAsBlock();
 boolean imgFormatAsBlock = imgTag.formatAsBlock();
 boolean scriptFormatAsBlock = scriptTag.formatAsBlock();

 assertTrue(pFormatAsBlock);
 assertFalse(imgFormatAsBlock);
 assertFalse(scriptFormatAsBlock);
 }

 /**
 * Test method for {@link Tag#isEmpty()}.
 */
 @Test
 public void testIsEmpty() {
 boolean pIsEmpty = pTag.isEmpty();
 boolean imgIsEmpty = imgTag.isEmpty();
 boolean scriptIsEmpty = scriptTag.isEmpty();

 assertFalse(pIsEmpty);
 assertFalse(imgIsEmpty);
 assertFalse(scriptIsEmpty);
 }

 /**
 * Test method for {@link Tag#isSelfClosing()}.
 */
 @Test
 public void testIsSelfClosing() {
 boolean pIsSelfClosing = pTag.isSelfClosing();
 boolean imgIsSelfClosing = imgTag.isSelfClosing();
 boolean scriptIsSelfClosing = scriptTag.isSelfClosing();

 assertFalse(pIsSelfClosing);
 assertTrue(imgIsSelfClosing);
 assertFalse(scriptIsSelfClosing);
 }

 /**
 * Test method for {@link Tag#isKnownTag()}.
 */
 @Test
 public void testIsKnownTag() {
 String unknownTagName = "unknown";
 Tag unknownTag = Tag.valueOf(unknownTagName);

 assertFalse(Tag.isKnownTag(unknownTagName));
 assertFalse(unknownTag.isKnownTag());

 assertTrue(Tag.isKnownTag("p"));
 assertTrue(pTag.isKnownTag());
 }

 /**
 * Test method for {@link Tag#preserveWhitespace()}.
 */
 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 assertTrue(preTag.preserveWhitespace());
 }

 /**
 * Test method for {@link Tag#isFormListed()}.
 */
 @Test
 public void testIsFormListed() {
 Tag inputTag = Tag.valueOf("input");

 assertTrue(inputTag.isFormListed());
 }

 /**
 * Test method for {@link Tag#isFormSubmittable()}.
 */
 @Test
 public void testIsFormSubmittable() {
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(textareaTag.isFormSubmittable());
 }

 /**
 * Test edge cases and exceptions.
 */
 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 Tag nullTag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag emptyTag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Tag invalidTag = new Tag("invalidTagName");
 }
}