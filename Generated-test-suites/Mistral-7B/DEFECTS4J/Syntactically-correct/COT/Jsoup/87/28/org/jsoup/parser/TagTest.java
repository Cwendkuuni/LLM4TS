import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag imgTag;
 private Tag scriptTag;
 private Tag inputTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 imgTag = Tag.valueOf("img");
 scriptTag = Tag.valueOf("script");
 inputTag = Tag.valueOf("input");
 }

 @Test
 public void testTagName() {
 // test getName() method
 assertEquals("p", pTag.getName());
 assertEquals("img", imgTag.getName());
 assertEquals("script", scriptTag.getName());
 }

 @Test
 public void testTagIsBlock() {
 // test isBlock() method
 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 assertFalse(scriptTag.isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 // test formatAsBlock() method
 assertTrue(pTag.formatAsBlock());
 assertFalse(imgTag.formatAsBlock());
 assertFalse(scriptTag.formatAsBlock());
 }

 @Test
 public void testTagIsData() {
 // test isData() method
 assertFalse(pTag.isData());
 assertFalse(imgTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 // test isEmpty() method
 assertFalse(pTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 assertFalse(scriptTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 // test isSelfClosing() method
 assertFalse(pTag.isSelfClosing());
 assertTrue(imgTag.isSelfClosing());
 assertFalse(scriptTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTag() {
 // test isKnownTag() method
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("img"));
 assertTrue(Tag.isKnownTag("script"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 // test preserveWhitespace() method
 assertFalse(pTag.preserveWhitespace());
 assertFalse(imgTag.preserveWhitespace());
 assertFalse(scriptTag.preserveWhitespace());
 }

 @Test
 public void testTagIsFormListed() {
 // test isFormListed() method
 assertFalse(pTag.isFormListed());
 assertFalse(imgTag.isFormListed());
 assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testTagIsFormSubmittable() {
 // test isFormSubmittable() method
 assertFalse(pTag.isFormSubmittable());
 assertFalse(imgTag.isFormSubmittable());
 assertTrue(inputTag.isFormSubmittable());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 // test valueOf() method with null tagName
 Tag tag = Tag.valueOf(null, ParseSettings.preserveCase);
 fail("Expected NullPointerException but got: " + tag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 // test valueOf() method with invalid tagName
 Tag tag = Tag.valueOf("TAG$%&/@", ParseSettings.preserveCase);
 fail("Expected IllegalArgumentException but got: " + tag);
 }

 @Test
 public void testCaseInsensitiveTagName() {
 // test valueOf() method with case-insensitive tagName
 Tag pTagUpper = Tag.valueOf("P", ParseSettings.preserveCase);
 assertEquals("p", pTagUpper.getName());
 }

 @Test
 public void testEmptyTagWithoutFormListedOrFormSubmittable() {
 // test empty tag without formListed or formSubmittable properties
 Tag tag = new Tag("emptyTag");
 tag.empty = true;
 tag.formList = false;
 tag.formSubmit = false;
 assertFalse(tag.isFormSubmittable());
 assertFalse(tag.isFormListed());
 }

 @Test
 public void testCanContainBlockTrue() {
 // test canContainBlock() method with true
 Tag divTag = new Tag("div");
 divTag.canContainBlock = true;
 assertTrue(divTag.canContainBlock());
 }

 @Test
 public void testCanContainBlockFalse() {
 // test canContainBlock() method with false
 Tag spanTag = new Tag("span");
 spanTag.canContainBlock = false;
 assertFalse(spanTag.canContainBlock());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainBlockNegativeValue() {
 // test canContainBlock() method with negative value
 Tag tag = new Tag("tag");
 tag.canContainBlock = -1;
 fail("Expected IllegalArgumentException but got: " + tag);
 }

 @Test
 public void testFormListedWithFormSubmittable() {
 // test formListed and formSubmittable properties together
 Tag formTag = new Tag("form");
 formTag.formList = true;
 formTag.formSubmit = true;
 assertTrue(formTag.isFormSubmittable());
 assertTrue(formTag.isFormListed());
 }

 @Test
 public void testFormListedWithoutFormSubmittable() {
 // test formListed property without formSubmittable
 Tag labelTag = new Tag("label");
 labelTag.formList = true;
 labelTag.formSubmit = false;
 assertTrue(labelTag.isFormListed());
 assertFalse(labelTag.isFormSubmittable());
 }

 @Test
 public void testFormSubmittableWithoutFormListed() {
 // test formSubmittable property without formListed
 Tag textareaTag = new Tag("textarea");
 textareaTag.formSubmit = true;
 textareaTag.formList = false;
 assertTrue(textareaTag.isFormSubmittable());
 assertFalse(textareaTag.isFormListed());
 }

}