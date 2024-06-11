import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 // Test for getName() method
 @Test
 public void testGetName() {
 String expectedTagName = "div";
 Tag tag = new Tag("div");
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName);
 }

 // Test for valueOf() method with known tag
 @Test
 public void testValueOfKnownTag() {
 String tagName = "p";
 Tag tag = Tag.valueOf(tagName);
 assertTrue(tag.isKnownTag());
 assertEquals(tagName, tag.getName());
 }

 // Test for valueOf() method with unknown tag
 @Test
 public void testValueOfUnknownTag() {
 String tagName = "abc123";
 Tag tag = Tag.valueOf(tagName);
 assertFalse(tag.isKnownTag());
 assertEquals(tagName, tag.getName());
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isBlock());
 }

 // Test for formatAsBlock() method
 @Test
 public void testFormatAsBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.formatAsBlock());

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.formatAsBlock());
 }

 // Test for canContainBlock() method (deprecated)
 @Test
 public void testCanContainBlock() {
 Tag pTag = new Tag("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isBlock());
 assertFalse(imgTag.canContainBlock());
 }

 // Test for isInline() method
 @Test
 public void testIsInline() {
 Tag pTag = new Tag("p");
 assertFalse(pTag.isInline());

 Tag imgTag = new Tag("img");
 assertTrue(imgTag.isInline());
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag divTag = new Tag("div");
 assertFalse(divTag.isData());

 Tag scriptTag = new Tag("script");
 assertTrue(scriptTag.isData());
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isEmpty());

 Tag brTag = new Tag("br");
 assertTrue(brTag.isEmpty());
 }

 // Test for isSelfClosing() method
 @Test
 public void testIsSelfClosing() {
 Tag imgTag = new Tag("img");
 assertFalse(imgTag.isSelfClosing());

 Tag brTag = new Tag("br");
 assertTrue(brTag.isSelfClosing());
 }

 // Test for isKnownTag() method
 @Test
 public void testIsKnownTagMethod() {
 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("xyz"));
 }

 // Test for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag preTag = new Tag("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = new Tag("p");
 assertFalse(pTag.preserveWhitespace());
 }

 // Test for isFormListed() method
 @Test
 public void testIsFormListed() {
 Tag buttonTag = new Tag("button");
 assertTrue(buttonTag.isFormListed());

 Tag divTag = new Tag("div");
 assertFalse(divTag.isFormListed());
 }

 // Test for isFormSubmittable() method
 @Test
 public void testIsFormSubmittable() {
 Tag inputTag = new Tag("input");
 assertTrue(inputTag.isFormSubmittable());

 Tag divTag = new Tag("div");
 assertFalse(divTag.isFormSubmittable());
 }

 // Test for equals() method
 @Test
 public void testEquals() {
 String tagName = "p";
 Tag tag1 = new Tag(tagName);
 Tag tag2 = new Tag(tagName);
 assertTrue(tag1.equals(tag2));
 }

 // Test for hashCode() method
 @Test
 public void testHashCode() {
 String tagName = "p";
 Tag tag1 = new Tag(tagName);
 Tag tag2 = new Tag(tagName);
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }
}