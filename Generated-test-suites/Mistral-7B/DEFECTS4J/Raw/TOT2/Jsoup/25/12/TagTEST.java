import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = Tag.valueOf("div");
 String expected = "div";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagIsBlock() {
 Tag tag = Tag.valueOf("div");
 boolean expected = true;
 boolean actual = tag.isBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag tag = Tag.valueOf("p");
 boolean expected = true;
 boolean actual = tag.formatAsBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagCanContainBlock() {
 Tag tag = Tag.valueOf("div");
 boolean expected = true;
 boolean actual = tag.canContainBlock();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagCanContainInline() {
 Tag tag = Tag.valueOf("span");
 boolean expected = true;
 boolean actual = tag.canContainInline();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagIsEmpty() {
 Tag tag = Tag.valueOf("img");
 boolean expected = false;
 boolean actual = tag.isEmpty();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag tag = Tag.valueOf("img");
 boolean expected = false;
 boolean actual = tag.isSelfClosing();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagIsKnownTag() {
 Tag tag = Tag.valueOf("div");
 boolean expected = true;
 boolean actual = tag.isKnownTag();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 assertTrue(tag1.equals(tag2));
 }

 @Test
 public void testTagDifferentValuesAreNotEqual() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("div");
 assertFalse(tag1.equals(tag2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameThrowsException() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagNameThrowsException() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrimmedTagNameIsSameAsOriginal() {
 Tag tag = new Tag(" p ");
 String expected = "p";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpperCaseTagNameIsTransformedToLowerCase() {
 Tag tag = new Tag("DIV");
 String expected = "div";
 String actual = tag.getName();
 assertEquals(expected, actual);
 }

 @Test
 public void testTagRegistration() {
 Tag tag = Tag.valueOf("testTag");
 Tag registeredTag = Tag.valueOf("testTag");
 assertEquals(tag.hashCode(), registeredTag.hashCode());
 }
}