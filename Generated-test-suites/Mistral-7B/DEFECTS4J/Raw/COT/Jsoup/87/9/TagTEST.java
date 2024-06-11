import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag inputTag;

 @Before
 public void setUp() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 inputTag = Tag.valueOf("input");
 }

 /**
 * Test getName() method
 */
 @Test
 public void testGetName() {
 String pTagName = pTag.getName();
 String imgTagName = imgTag.getName();
 String inputTagName = inputTag.getName();

 assertEquals("p", pTagName);
 assertEquals("img", imgTagName);
 assertEquals("input", inputTagName);
 }

 /**
 * Test valueOf() method with known tag
 */
 @Test
 public void testValueOfKnownTag() {
 Tag knownTag = Tag.valueOf("p");

 assertTrue(knownTag.isKnownTag());
 assertEquals("p", knownTag.getName());
 }

 /**
 * Test valueOf() method with unknown tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testValueOfUnknownTag() {
 Tag unknownTag = Tag.valueOf("UnknownTag");

 assertFalse(unknownTag.isKnownTag());
 assertNull(unknownTag.getName());
 }

 /**
 * Test isBlock() method
 */
 @Test
 public void testIsBlock() {
 boolean pIsBlock = pTag.isBlock();
 boolean imgIsBlock = imgTag.isBlock();
 boolean inputIsBlock = inputTag.isBlock();

 assertTrue(pIsBlock);
 assertFalse(imgIsBlock);
 assertFalse(inputIsBlock);
 }

 /**
 * Test formatAsBlock() method
 */
 @Test
 public void testFormatAsBlock() {
 boolean pFormatAsBlock = pTag.formatAsBlock();
 boolean imgFormatAsBlock = imgTag.formatAsBlock();
 boolean inputFormatAsBlock = inputTag.formatAsBlock();

 assertTrue(pFormatAsBlock);
 assertFalse(imgFormatAsBlock);
 assertFalse(inputFormatAsBlock);
 }

 /**
 * Test isData() method
 */
 @Test
 public void testIsData() {
 boolean pIsData = pTag.isData();
 boolean imgIsData = imgTag.isData();
 boolean inputIsData = inputTag.isData();

 assertFalse(pIsData);
 assertFalse(imgIsData);
 assertFalse(inputIsData);
 }

 /**
 * Test isEmpty() method
 */
 @Test
 public void testIsEmpty() {
 boolean pIsEmpty = pTag.isEmpty();
 boolean imgIsEmpty = imgTag.isEmpty();
 boolean inputIsEmpty = inputTag.isEmpty();

 assertFalse(pIsEmpty);
 assertFalse(imgIsEmpty);
 assertFalse(inputIsEmpty);
 }

 /**
 * Test isSelfClosing() method
 */
 @Test
 public void testIsSelfClosing() {
 boolean imgIsSelfClosing = imgTag.isSelfClosing();

 assertTrue(imgIsSelfClosing);
 }

 /**
 * Test isKnownTag() method
 */
 @Test
 public void testIsKnownTagMethod() {
 boolean isPTagKnown = Tag.isKnownTag("p");

 assertTrue(isPTagKnown);
 }

 /**
 * Test preserveWhitespace() method
 */
 @Test
 public void testPreserveWhitespace() {
 boolean pPreserveWhitespace = pTag.preserveWhitespace();

 assertFalse(pPreserveWhitespace);
 }

 /**
 * Test isFormListed() method
 */
 @Test
 public void testIsFormListed() {
 boolean inputIsFormListed = inputTag.isFormListed();

 assertTrue(inputIsFormListed);
 }

 /**
 * Test isFormSubmittable() method
 */
 @Test
 public void testIsFormSubmittable() {
 boolean inputIsFormSubmittable = inputTag.isFormSubmittable();

 assertTrue(inputIsFormSubmittable);
 }

 /**
 * Test equals() method
 */
 @Test
 public void testEquals() {
 Tag pTagCopy = new Tag("p");

 assertTrue(pTag.equals(pTag));
 assertTrue(pTag.equals(pTagCopy));
 assertFalse(pTag.equals(imgTag));
 assertFalse(pTag.equals(null));
 }

 /**
 * Test equals() method with different tag names
 */
 @Test
 public void testEqualsWithDifferentTagNames() {
 Tag pTagCopyWithDifferentName = new Tag("p1");

 assertFalse(pTag.equals(pTagCopyWithDifferentName));
 }

 /**
 * Test hashCode() method
 */
 @Test
 public void testHashCode() {
 int pTagHashCode = pTag.hashCode();
 int pTagHashCodeCopy = pTag.hashCode();
 int imgTagHashCode = imgTag.hashCode();

 assertEquals(pTagHashCode, pTagHashCodeCopy);
 assertNotEquals(pTagHashCode, imgTagHashCode);
 }

 /**
 * Test toString() method
 */
 @Test
 public void testToString() {
 String pTagString = pTag.toString();

 assertEquals("p", pTagString);
 }

 /**
 * Test valueOf() method with case insensitive tag name
 */
 @Test
 public void testValueOfWithCaseInsensitiveTagName() {
 Tag pTagCaseInsensitive = Tag.valueOf("p");

 assertEquals(pTag, pTagCaseInsensitive);
 }

 /**
 * Test valueOf() method with normalizeTag()
 */
 @Test
 public void testValueOfWithNormalizeTag() {
 Tag pTagNormalized = Tag.valueOf("P");

 assertEquals(pTag, pTagNormalized);
 }

 /**
 * Test valueOf() method with unknown tag and settings
 */
 @Test
 public void testValueOfWithUnknownTagAndSettings() {
 Tag unknownTagWithSettings = Tag.valueOf("UnknownTag", new ParseSettings());

 assertFalse(unknownTagWithSettings.isKnownTag());
 assertNull(unknownTagWithSettings.getName());
 }

 /**
 * Test canContainBlock() method
 */
 @Test
 public void testCanContainBlock() {
 boolean pCanContainBlock = pTag.canContainBlock();

 assertTrue(pCanContainBlock);
 }

 /**
 * Test canContainInline() method
 */
 @Test
 public void testCanContainInline() {
 boolean pCanContainInline = pTag.canContainInline();

 assertTrue(pCanContainInline);
 }

 /**
 * Test empty tag method
 */
 @Test
 public void testEmptyTags() {
 boolean metaTagIsEmpty = Tag.valueOf("meta").isEmpty();

 assertTrue(metaTagIsEmpty);
 }

 /**
 * Test formatAsInline tag method
 */
 @Test
 public void testFormatAsInlineTags() {
 boolean titleTagIsFormatAsInline = Tag.valueOf("title").formatAsInline();

 assertTrue(titleTagIsFormatAsInline);
 }

 /**
 * Test preserveWhitespace tag method
 */
 @Test
 public void testPreserveWhitespaceTags() {
 boolean preTagPreserveWhitespace = Tag.valueOf("pre").preserveWhitespace();

 assertTrue(preTagPreserveWhitespace);
 }

 /**
 * Test formListed tag method
 */
 @Test
 public void testFormListedTags() {
 boolean buttonTagIsFormListed = Tag.valueOf("button").isFormListed();

 assertTrue(buttonTagIsFormListed);
 }

 /**
 * Test formSubmit tag method
 */
 @Test
 public void testFormSubmitTags() {
 boolean inputTagIsFormSubmit = Tag.valueOf("input").isFormSubmit();

 assertTrue(inputTagIsFormSubmit);
 }

}