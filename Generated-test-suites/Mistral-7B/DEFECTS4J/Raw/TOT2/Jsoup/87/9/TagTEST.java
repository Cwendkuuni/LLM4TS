import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 // Test for getName() method
 @Test
 public void testGetName() {
 Tag tag = Tag.valueOf("div");
 String expectedName = "div";
 String actualName = tag.getName();
 assertEquals(expectedName, actualName);
 }

 // Test for valueOf() method with known tag
 @Test
 public void testValueOfKnownTag() {
 Tag tag = Tag.valueOf("p");
 String expectedTagName = "p";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName);
 }

 // Test for valueOf() method with unknown tag
 @Test
 public void testValueOfUnknownTag() {
 Tag tag = Tag.valueOf("xyz");
 String expectedTagName = "xyz";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName); // since unknown tags are not registered, they only .equals()
 }

 // Test for isBlock() method
 @Test
 public void testIsBlock() {
 Tag tag = Tag.valueOf("p");
 boolean expectedIsBlock = true;
 boolean actualIsBlock = tag.isBlock();
 assertEquals(expectedIsBlock, actualIsBlock);
 }

 // Test for formatAsBlock() method
 @Test
 public void testFormatAsBlock() {
 Tag tag = Tag.valueOf("p");
 boolean expectedFormatAsBlock = true;
 boolean actualFormatAsBlock = tag.formatAsBlock();
 assertEquals(expectedFormatAsBlock, actualFormatAsBlock);
 }

 // Test for isData() method
 @Test
 public void testIsData() {
 Tag tag = Tag.valueOf("script");
 boolean expectedIsData = true;
 boolean actualIsData = tag.isData();
 assertEquals(expectedIsData, actualIsData);
 }

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 Tag tag = Tag.valueOf("img");
 boolean expectedIsEmpty = false;
 boolean actualIsEmpty = tag.isEmpty();
 assertEquals(expectedIsEmpty, actualIsEmpty);
 }

 // Test for isSelfClosing() method
 @Test
 public void testIsSelfClosing() {
 Tag tag = Tag.valueOf("img");
 boolean expectedIsSelfClosing = false;
 boolean actualIsSelfClosing = tag.isSelfClosing();
 assertEquals(expectedIsSelfClosing, actualIsSelfClosing);
 }

 // Test for isKnownTag() method
 @Test
 public void testIsKnownTag() {
 boolean expectedIsKnownTag = Tag.isKnownTag("p");
 boolean actualIsKnownTag = expectedIsKnownTag;
 assertTrue(expectedIsKnownTag);
 }

 // Test for preserveWhitespace() method
 @Test
 public void testPreserveWhitespace() {
 Tag tag = Tag.valueOf("pre");
 boolean expectedPreserveWhitespace = true;
 boolean actualPreserveWhitespace = tag.preserveWhitespace();
 assertEquals(expectedPreserveWhitespace, actualPreserveWhitespace);
 }

 // Test for isFormListed() method
 @Test
 public void testIsFormListed() {
 Tag tag = Tag.valueOf("input");
 boolean expectedIsFormListed = true;
 boolean actualIsFormListed = tag.isFormListed();
 assertEquals(expectedIsFormListed, actualIsFormListed);
 }

 // Test for isFormSubmittable() method
 @Test
 public void testIsFormSubmittable() {
 Tag tag = Tag.valueOf("button");
 boolean expectedIsFormSubmittable = false;
 boolean actualIsFormSubmittable = tag.isFormSubmittable();
 assertEquals(expectedIsFormSubmittable, actualIsFormSubmittable);
 }

 // Edge case testing
 // Test for getting a tag with an empty string
 @Test
 public void testGetTagWithEmptyString() {
 Tag tag = Tag.valueOf("");
 String expectedTagName = "";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName);
 }

 // Test for getting a tag with a null string
 @Test(expected = NullPointerException.class)
 public void testGetTagWithNullString() {
 Tag tag = Tag.valueOf(null);
 }

 // Test for getting a tag with an invalid tag name (contains a digit)
 @Test
 public void testGetTagWithInvalidTagName() {
 Tag tag = Tag.valueOf("tag1");
 String expectedTagName = "tag1";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName); // since unknown tags are not registered, they only .equals()
 }

 // Test for getting a tag with a case-sensitive tag name
 @Test
 public void testGetTagWithCaseSensitiveTagName() {
 Tag tag = Tag.valueOf("P");
 String expectedTagName = "P";
 String actualTagName = tag.getName();
 assertEquals(expectedTagName, actualTagName);
 }

 // Exception handling testing
 // Test for NullPointerException when passing null to valueOf() method
 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionWhenPassingNullToString() {
 Tag tag = Tag.valueOf(null);
 }
}