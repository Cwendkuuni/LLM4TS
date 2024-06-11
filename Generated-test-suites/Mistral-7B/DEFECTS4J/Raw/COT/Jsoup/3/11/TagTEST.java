import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag htmlTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag h1Tag;
 private Tag ulTag;
 private Tag liTag;
 private Tag dtTag;
 private Tag ddTag;

 @Before
 public void setUp() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 h1Tag = Tag.valueOf("H1");
 ulTag = Tag.valueOf("UL");
 liTag = Tag.valueOf("LI");
 dtTag = Tag.valueOf("DT");
 ddTag = Tag.valueOf("DD");
 }

 @Test
 public void testTagValueOf() {
 Tag tag = Tag.valueOf("P");
 assertEquals("p", tag.getName());
 }

 @Test
 public void testCanContain() {
 // Test that p tag can contain other inline tags
 assertTrue(pTag.canContain(new Tag("SPAN")));

 // Test that p tag cannot contain block tags
 assertFalse(pTag.canContain(new Tag("P")));

 // Test that head tag can contain specific tags
 assertTrue(headTag.canContain(new Tag("SCRIPT")));
 assertTrue(headTag.canContain(new Tag("LINK")));
 assertTrue(headTag.canContain(new Tag("META")));
 assertTrue(headTag.canContain(new Tag("TITLE")));

 // Test that head tag cannot contain other head tags
 assertFalse(headTag.canContain(new Tag("HEAD")));
 }

 @Test
 public void testIsBlock() {
 // Test that p tag is an inline tag
 assertFalse(pTag.isBlock());

 // Test that h1 tag is a block tag
 assertTrue(h1Tag.isBlock());
 }

 @Test
 public void testIsData() {
 // Test that p tag is not a data only tag
 assertFalse(pTag.isData());

 // Test that script tag is a data only tag
 assertTrue(new Tag("SCRIPT").isData());
 }

 @Test
 public void testIsEmpty() {
 // Test that img tag is an empty tag
 assertTrue(new Tag("IMG").isEmpty());

 // Test that p tag is not an empty tag
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 // Test that pre tag should preserve whitespace
 assertTrue(new Tag("PRE").preserveWhitespace());

 // Test that p tag should not preserve whitespace
 assertFalse(pTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagValueOf() {
 Tag tag = Tag.valueOf("InvalidTag");
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testCanContainParent() {
 // Test that p tag is a valid parent for an inline tag (span)
 assertTrue(pTag.isValidParent(new Tag("SPAN")));

 // Test that p tag is not a valid parent for a block tag (div)
 assertFalse(pTag.isValidParent(new Tag("DIV")));
 }

 @Test
 public void testGetImplicitParent() {
 // Test that an empty list means no implicit parent
 assertNull(new Tag("EMPTY").getImplicitParent());

 // Test that the first ancestor is the implicit parent
 assertEquals(bodyTag, pTag.getImplicitParent());
 }

 @Test
 public void testEquals() {
 Tag tag1 = new Tag("P");
 Tag tag2 = new Tag("P");
 Tag tag3 = new Tag("DIV");

 // Test that two identical tags are equal
 assertEquals(tag1, tag2);

 // Test that two different tags are not equal
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 Tag tag1 = new Tag("P");
 Tag tag2 = new Tag("P");
 Tag tag3 = new Tag("DIV");

 // Test that identical tags have the same hashcode
 assertEquals(tag1.hashCode(), tag2.hashCode());

 // Test that different tags have different hashcodes
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("p", pTag.toString());
 }

}