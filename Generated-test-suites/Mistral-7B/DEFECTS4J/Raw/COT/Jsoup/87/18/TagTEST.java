import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagName() {
 Tag tag = new Tag("div");
 String name = tag.getName();
 assertEquals("div", name);
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("div");
 String name = tag.getName();
 assertEquals("div", name);
 }

 @Test
 public void testIsBlock() {
 Tag tag = new Tag("div");
 boolean isBlock = tag.isBlock();
 assertTrue(isBlock);

 Tag tagInline = new Tag("span");
 boolean isBlockInline = tagInline.isBlock();
 assertFalse(isBlockInline);
 }

 @Test
 public void testFormatAsBlock() {
 Tag tag = new Tag("p");
 boolean formatAsBlock = tag.formatAsBlock();
 assertTrue(formatAsBlock);

 Tag tagInline = new Tag("span");
 boolean formatAsBlockInline = tagInline.formatAsBlock();
 assertFalse(formatAsBlockInline);
 }

 @Test
 public void testEmptyTag() {
 Tag tag = new Tag("img");
 boolean empty = tag.isEmpty();
 assertFalse(empty);

 Tag imgTag = new Tag("img");
 imgTag.empty = true;
 boolean emptyImg = imgTag.isEmpty();
 assertTrue(emptyImg);
 }

 @Test
 public void testSelfClosingTag() {
 Tag tag = new Tag("img");
 boolean selfClosing = tag.isSelfClosing();
 assertFalse(selfClosing);

 Tag imgSelfClosing = new Tag("img").setSelfClosing();
 boolean selfClosingImg = imgSelfClosing.isSelfClosing();
 assertTrue(selfClosingImg);
 }

 @Test
 public void testIsKnownTag() {
 Tag tag = new Tag("p");
 boolean isKnown = Tag.isKnownTag("p");
 assertTrue(isKnown);

 Tag unknownTag = new Tag("unknown");
 boolean isUnknown = Tag.isKnownTag("unknown");
 assertFalse(isUnknown);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Tag tag = new Tag(null);
 // test for IllegalArgumentException when tagName is null
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Tag tag = new Tag("");
 // test for IllegalArgumentException when tagName is empty
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 assertTrue(tag1.equals(tag2));

 Tag tag3 = new Tag("div");
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("p");
 int hashCode1 = tag1.hashCode();

 Tag tag2 = new Tag("p");
 int hashCode2 = tag2.hashCode();

 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("p");
 String toString = tag.toString();
 assertEquals("p", toString);
 }
}