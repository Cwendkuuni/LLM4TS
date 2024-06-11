import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testTagValueOfUnknown() {
 Tag tag = Tag.valueOf("unknownTag");
 assertEquals("unknownTag", tag.getName());
 assertFalse(tag.isKnownTag());
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.formatAsBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.canContainBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag hrTag = Tag.valueOf("hr");
 assertTrue(hrTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameValueOf() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagNameValueOf() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhiteSpaceTagNameValueOf() {
 Tag tag = Tag.valueOf("   ");
 }

 @Test
 public void testRegisterTags() {
 Tag htmlTag = Tag.valueOf("html");
 Tag headTag = Tag.valueOf("head");
 Tag bodyTag = Tag.valueOf("body");

 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("head"));
 assertTrue(Tag.isKnownTag("body"));
 }
}