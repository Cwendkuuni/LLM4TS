import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag htmlTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag dtTag;
 private Tag ddTag;
 private Tag imgTag;

 @Before
 public void setUp() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 dtTag = Tag.valueOf("DT");
 ddTag = Tag.valueOf("DD");
 imgTag = Tag.valueOf("IMG");
 }

 @Test
 public void testTAGvalueOf() {
 // Test getting a Tag by name
 Tag tag1 = Tag.valueOf("TAG1");
 Tag tag2 = Tag.valueOf("TAG1");
 // Test tag equality
 assertEquals(tag1, tag2);
 // Test getting a new generic tag when tag is not defined
 Tag tag3 = Tag.valueOf("UNKNOWN_TAG");
 assertFalse(tag3.equals(htmlTag));
 // Test tag hashcode
 int hashcode1 = tag1.hashCode();
 int hashcode2 = tag1.hashCode();
 assertEquals(hashcode1, hashcode2);
 }

 @Test
 public void testCanContain() {
 // Test valid child
 assertTrue(pTag.canContain(imgTag));
 // Test invalid child - block tag cannot contain another block tag
 assertFalse(bodyTag.canContain(pTag));
 // Test invalid child - inline tag cannot contain another inline tag
 assertFalse(pTag.canContain(pTag));
 // Test invalid child - parent and child are the same
 assertFalse(pTag.canContain(pTag));
 // Test invalid child - empty tag cannot contain anything
 assertFalse(imgTag.canContain(pTag));
 // Test invalid child - data only tag cannot contain anything
 assertFalse(headTag.canContain(pTag));
 // Test invalid child - head tag can only contain specific children
 assertFalse(headTag.canContain(bodyTag));
 // Test invalid child - dt and dd in dl cannot contain each other
 assertFalse(dtTag.canContain(ddTag));
 assertFalse(ddTag.canContain(dtTag));
 }

 @Test
 public void testIsBlock() {
 // Test block tag
 assertTrue(bodyTag.isBlock());
 // Test inline tag
 assertFalse(pTag.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test block tag that can contain block tags
 assertTrue(bodyTag.canContainBlock());
 // Test inline tag that cannot contain block tags
 assertFalse(pTag.canContainBlock());
 }

 @Test
 public void testIsData() {
 // Test data only tag
 assertTrue(headTag.isData());
 // Test tag that is not data only
 assertFalse(bodyTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test empty tag
 assertTrue(imgTag.isEmpty());
 // Test non-empty tag
 assertFalse(pTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 // Test tag that does not preserve whitespace
 assertFalse(pTag.preserveWhitespace());
 // Test tag that preserves whitespace
 assertTrue(headTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainNull() {
 // Test IllegalArgumentException when trying to test canContain with a null tag
 Tag nullTag = null;
 pTag.canContain(nullTag);
 }

 @Test
 public void testGetImplicitParent() {
 // Test getting the implicit parent of a tag
 Tag parentTag = pTag.getImplicitParent();
 assertEquals(bodyTag, parentTag);
 }

 @Test
 public void testIsValidParent() {
 // Test valid parent
 assertTrue(pTag.isValidParent(bodyTag));
 // Test invalid parent
 assertFalse(headTag.isValidParent(bodyTag));
 }

 @Test
 public void testEquals() {
 // Test tag equality
 Tag tag1 = new Tag("TAG1");
 Tag tag2 = new Tag("TAG1");
 assertEquals(tag1, tag2);
 // Test non-equal tags
 Tag tag3 = new Tag("TAG2");
 assertNotEquals(tag1, tag3);
 }

 @Test
 public void testHashCode() {
 // Test tag hashcode
 Tag tag1 = new Tag("TAG1");
 Tag tag2 = new Tag("TAG1");
 int hashcode1 = tag1.hashCode();
 int hashcode2 = tag2.hashCode();
 assertEquals(hashcode1, hashcode2);
 }

 @Test
 public void testToString() {
 // Test tag toString
 assertEquals("TAG1", tag1.toString());
 }

}