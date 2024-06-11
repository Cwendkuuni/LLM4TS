import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag htmlTag = Tag.valueOf("html");
 Tag bodyTag = Tag.valueOf("body");
 Tag pTag = Tag.valueOf("p");

 assertEquals("html", htmlTag.getName());
 assertEquals("body", bodyTag.getName());
 assertEquals("p", pTag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // p can contain div
 assertTrue(pTag.canContain(divTag));

 // div can contain p
 assertTrue(divTag.canContain(pTag));
 }

 @Test
 public void testTagInvalidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");

 // p cannot contain itself
 assertFalse(pTag.canContain(pTag2));
 }

 @Test
 public void testTagIsData() {
 Tag spanTag = Tag.valueOf("span");
 Tag scriptTag = Tag.valueOf("script");

 // span is not a data only tag
 assertFalse(spanTag.isData());

 // script is a data only tag
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("img");

 // img is not an empty tag
 assertFalse(imgTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");

 // pre tag should preserve whitespace
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagInvalidParentAncestry() {
 Tag pTag = Tag.valueOf("p");
 Tag bodyTag = Tag.valueOf("body");
 Tag headTag = Tag.valueOf("head");

 // p cannot be a child of head
 assertFalse(pTag.isValidParent(headTag));
 }

 @Test
 public void testTagEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // p tags are equal
 assertEquals(pTag1, pTag2);

 // p is not equal to div
 assertNotEquals(pTag1, divTag);
 }

 @Test
 public void testTagHashCode() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 // hashcodes for equal objects should be the same
 assertEquals(pTag1.hashCode(), pTag2.hashCode());

 // hashcodes for non-equal objects should be different
 assertNotEquals(pTag1.hashCode(), divTag.hashCode());
 }

 @Test
 public void testTagToString() {
 Tag pTag = Tag.valueOf("p");

 assertEquals("p", pTag.toString());
 }
}