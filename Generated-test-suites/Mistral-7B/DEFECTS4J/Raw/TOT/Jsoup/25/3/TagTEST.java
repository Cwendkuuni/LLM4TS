import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

 @Test
 public void testTagValueOf() {
 String tagName = "div";
 Tag tag = Tag.valueOf(tagName);

 assertEquals(tagName, tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagValueOfUnknown() {
 String tagName = "mytag";
 Tag tag = Tag.valueOf(tagName);

 assertEquals(tagName, tag.getName());
 assertFalse(tag.isBlock());
 assertTrue(tag.canContainBlock());
 assertTrue(tag.canContainInline());
 assertFalse(tag.isEmpty());
 assertFalse(tag.isSelfClosing());
 assertFalse(tag.preserveWhitespace());
 }

 @Test
 public void testTagIsKnownTag() {
 String tagName = "div";
 assertTrue(Tag.isKnownTag(tagName));

 tagName = "mytag";
 assertFalse(Tag.isKnownTag(tagName));
 }

 @Test
 public void testTagIsBlock() {
 String tagName = "div";
 assertTrue(Tag.valueOf(tagName).isBlock());

 tagName = "span";
 assertFalse(Tag.valueOf(tagName).isBlock());
 }

 @Test
 public void testTagFormatAsBlock() {
 String tagName = "p";
 assertTrue(Tag.valueOf(tagName).formatAsBlock());

 tagName = "span";
 assertFalse(Tag.valueOf(tagName).formatAsBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 String tagName = "div";
 assertTrue(Tag.valueOf(tagName).canContainBlock());

 tagName = "img";
 assertFalse(Tag.valueOf(tagName).canContainBlock());
 }

 @Test
 public void testTagCanContainInline() {
 String tagName = "p";
 assertTrue(Tag.valueOf(tagName).canContainInline());

 tagName = "script";
 assertFalse(Tag.valueOf(tagName).canContainInline());
 }

 @Test
 public void testTagIsEmpty() {
 String tagName = "img";
 assertFalse(Tag.valueOf(tagName).isEmpty());

 tagName = "input";
 assertFalse(Tag.valueOf(tagName).isEmpty());

 tagName = "hr";
 assertTrue(Tag.valueOf(tagName).isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 String tagName = "img";
 assertFalse(Tag.valueOf(tagName).isSelfClosing());

 tagName = "img";
 tagName = tagName + "/";
 assertTrue(Tag.valueOf(tagName).isSelfClosing());
 }

 @Test
 public void testTagPreserveWhitespace() {
 String tagName = "pre";
 assertTrue(Tag.valueOf(tagName).preserveWhitespace());

 tagName = "p";
 assertFalse(Tag.valueOf(tagName).preserveWhitespace());
 }
}