import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testValueOfNull() {
 Assert.assertNull(Tag.valueOf(null));
 }

 @Test
 public void testValueOfEmpty() {
 Tag tag = Tag.valueOf(" ");
 assertEquals("", tag.getName());
 }

 @Test
 public void testValueOfUpperCase() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pInsideDiv = Tag.valueOf("p");
 Tag divInsideP = Tag.valueOf("div");

 assertTrue(pTag.canContain(pInsideP));
 assertTrue(divTag.canContain(pInsideP));
 assertTrue(divTag.canContain(divInsideP));
 assertFalse(pTag.canContain(divTag));
 }

 @Test
 public void testCanContainSelf() {
 Tag tag = Tag.valueOf("p");
 assertFalse(tag.canContain(tag));
 }

 @Test
 public void testCanContainEmptyTag() {
 Tag tag = Tag.valueOf("img");
 assertTrue(tag.canContain(Tag.valueOf("img")));
 }

 @Test
 public void testIsBlock() {
 Tag pTag = Tag.valueOf("p");
 assertTrue(pTag.isBlock());

 Tag emTag = Tag.valueOf("em");
 assertFalse(emTag.isBlock());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 assertTrue(scriptTag.isData());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("img");
 assertTrue(imgTag.isEmpty());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 assertTrue(preTag.preserveWhitespace());

 Tag pTag = Tag.valueOf("p");
 assertFalse(pTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag divTag = Tag.valueOf("div");
 assertTrue(divTag.getImplicitParent() == null);

 Tag pTag = Tag.valueOf("p");
 Tag bodyTag = Tag.valueOf("body");
 pTag.setAncestors(Collections.singletonList(bodyTag));
 assertEquals(bodyTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 Tag divTag = Tag.valueOf("div");
 Tag pTag = Tag.valueOf("p");
 assertFalse(divTag.isValidParent(pTag));

 Tag pParent = Tag.valueOf("p");
 pParent.setAncestors(Collections.singletonList(divTag));
 assertTrue(divTag.isValidParent(pTag));
 }

 @Test
 public void testEquals() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 assertTrue(tag1.equals(tag2));
 assertFalse(tag1.equals(tag3));
 }

 @Test
 public void testHashCode() {
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 int code1 = tag1.hashCode();
 int code2 = tag2.hashCode();
 int code3 = tag3.hashCode();

 assertEquals(code1, code2); //equals should give the same hashcode
 assertNotEquals(code1, code3);
 }

 @Test
 public void testToString() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.toString());
 }
}