import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("p");
 Tag expectedTag = new Tag("p");
 assertEquals(expectedTag, tag);
 }

 @Test
 public void testTagCanContainBlock() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 assertTrue(pTag.canContainBlock());
 assertTrue(divTag.canContainBlock());

 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.canContainBlock());
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNull() {
 Tag tag = Tag.valueOf(null);
 // this test should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmpty() {
 Tag tag = Tag.valueOf("");
 // this test should throw an IllegalArgumentException
 }

 @Test
 public void testTagIsKnownTag() {
 assertTrue(Tag.isKnownTag("p"));
 assertTrue(Tag.isKnownTag("div"));
 assertFalse(Tag.isKnownTag("unknownTag"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 preTag.preserveWhitespace(true);
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagFormatAsBlock() {
 Tag pTag = Tag.valueOf("p");
 pTag.formatAsBlock(false);
 assertFalse(pTag.formatAsBlock());
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isEmpty());

 Tag brTag = Tag.valueOf("br");
 assertTrue(brTag.isEmpty());
 }

 @Test
 public void testTagIsSelfClosing() {
 Tag imgTag = Tag.valueOf("img");
 assertFalse(imgTag.isSelfClosing());

 Tag imgSelfClosingTag = imgTag.setSelfClosing();
 assertTrue(imgSelfClosingTag.isSelfClosing());
 }

 @Test
 public void testTagIsKnownTagArray() {
 String[] knownTags = { "p", "div" };
 for (String tagName : knownTags) {
 assertTrue(Tag.isKnownTag(tagName));
 }
 }

 @Test
 public void testTagIsNotKnownTagArray() {
 String[] unknownTags = { "unknownTag1", "unknownTag2" };
 for (String tagName : unknownTags) {
 assertFalse(Tag.isKnownTag(tagName));
 }
 }
}