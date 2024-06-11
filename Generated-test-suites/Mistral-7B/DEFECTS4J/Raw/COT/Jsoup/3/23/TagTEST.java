import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8, tag9;

 @Before
 public void setup() {
 tag1 = Tag.valueOf("p");
 tag2 = Tag.valueOf("div");
 tag3 = Tag.valueOf("ul");
 tag4 = Tag.valueOf("ol");
 tag5 = Tag.valueOf("li");
 tag6 = Tag.valueOf("table");
 tag7 = Tag.valueOf("tr");
 tag8 = Tag.valueOf("th");
 tag9 = Tag.valueOf("td");
 }

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag instanceof Tag);
 assertEquals("p", tag.getName());
 }

 @Test
 public void testCanContain() {
 // Test valid cases
 assertTrue(tag1.canContain(tag1));
 assertTrue(tag1.canContain(tag2));
 assertTrue(tag2.canContain(tag1));
 assertTrue(tag2.canContain(tag3));
 assertTrue(tag3.canContain(tag5));

 // Test invalid cases
 assertFalse(tag1.canContain(tag2));
 assertFalse(tag2.canContain(tag1));
 assertFalse(tag1.canContain(tag3));
 assertFalse(tag3.canContain(tag1));
 assertFalse(tag1.canContain(tag6));
 assertFalse(tag6.canContain(tag1));
 assertFalse(tag1.canContain(tag7));
 assertFalse(tag7.canContain(tag1));
 assertFalse(tag1.canContain(tag8));
 assertFalse(tag8.canContain(tag1));
 assertFalse(tag1.canContain(tag9));
 assertFalse(tag9.canContain(tag1));
 }

 @Test
 public void testIsBlock() {
 // Test valid cases
 assertTrue(tag1.isBlock());
 assertTrue(tag2.isBlock());

 // Test invalid cases
 assertFalse(tag3.isBlock());
 assertFalse(tag4.isBlock());
 assertFalse(tag5.isBlock());
 assertFalse(tag6.isBlock());
 assertFalse(tag7.isBlock());
 assertFalse(tag8.isBlock());
 assertFalse(tag9.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test valid cases
 assertTrue(tag1.canContainBlock());
 assertTrue(tag2.canContainBlock());

 // Test invalid cases
 assertFalse(tag3.canContainBlock());
 assertFalse(tag4.canContainBlock());
 assertFalse(tag5.canContainBlock());
 assertFalse(tag6.canContainBlock());
 assertFalse(tag7.canContainBlock());
 assertFalse(tag8.canContainBlock());
 assertFalse(tag9.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 // Test valid cases
 assertFalse(tag1.isEmpty());
 assertFalse(tag2.isEmpty());
 assertFalse(tag3.isEmpty());
 assertFalse(tag4.isEmpty());
 assertFalse(tag5.isEmpty());
 assertFalse(tag6.isEmpty());
 assertFalse(tag7.isEmpty());
 assertFalse(tag8.isEmpty());
 assertFalse(tag9.isEmpty());

 // Test empty tag
 Tag emptyTag = Tag.valueOf("br");
 assertTrue(emptyTag.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainNull() {
 Tag tag = new Tag("p");
 try {
 tag.canContain(null);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainSelf() {
 Tag tag = new Tag("p");
 try {
 tag.canContain(tag);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testToString() {
 // Test valid cases
 assertEquals("p", tag1.toString());
 assertEquals("div", tag2.toString());
 }

 @Test
 public void testEquals() {
 // Test equal objects
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = tag1;
 assertTrue(tag1.equals(tag2));
 assertTrue(tag1.equals(tag3));

 // Test not equal objects
 Tag tag4 = Tag.valueOf("div");
 assertFalse(tag1.equals(tag4));

 // Test equals with null
 assertFalse(tag1.equals(null));
 }

 @Test
 public void testHashCode() {
 // Test hashcode consistency
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = tag1;
 int hashcode1 = tag1.hashCode();
 int hashcode2 = tag2.hashCode();
 int hashcode3 = tag3.hashCode();
 assertEquals(hashcode1, hashcode2);
 assertEquals(hashcode1, hashcode3);
 }

}