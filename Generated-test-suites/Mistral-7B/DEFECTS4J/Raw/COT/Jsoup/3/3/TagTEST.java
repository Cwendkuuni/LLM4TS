import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag htmlTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag ulTag;
 private Tag liTag;
 private Tag dtTag;
 private Tag ddTag;

 @Before
 public void setup() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 ulTag = Tag.valueOf("UL");
 liTag = Tag.valueOf("LI");
 dtTag = Tag.valueOf("DT");
 ddTag = Tag.valueOf("DD");
 }

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.getName());
 }

 @Test
 public void testValueOfPredefinedTag() {
 assertEquals(htmlTag, Tag.valueOf("HTML"));
 assertEquals(headTag, Tag.getValueOf("HEAD"));
 assertEquals(bodyTag, Tag.getValueOf("BODY"));
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertNotEquals(htmlTag, unknownTag);
 assertEquals("unknownTag", unknownTag.getName());
 }

 @Test
 public void testCanContain() {
 // positive test case
 assertTrue(bodyTag.canContain(pTag));

 // negative test case
 assertFalse(pTag.canContain(bodyTag));
 assertFalse(bodyTag.canContain(bodyTag));
 assertFalse(pTag.canContain(ulTag));
 assertFalse(ulTag.canContain(pTag));
 assertFalse(liTag.canContain(pTag));
 assertFalse(pTag.canContain(liTag));
 }

 @Test
 public void testIsBlock() {
 assertTrue(bodyTag.isBlock());
 assertFalse(pTag.isBlock());
 }

 @Test
 public void testCanContainBlock() {
 assertTrue(bodyTag.canContainBlock());
 assertTrue(ulTag.canContainBlock());
 assertFalse(pTag.canContainBlock());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(bodyTag.isEmpty());
 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testIsData() {
 assertFalse(bodyTag.isData());
 assertTrue(scriptTag.isData());
 }

 @Test
 public void testPreserveWhitespace() {
 assertFalse(bodyTag.preserveWhitespace());
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 assertEquals(bodyTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParent() {
 assertTrue(bodyTag.isValidParent(pTag));
 assertFalse(pTag.isValidParent(bodyTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNull() {
 Tag tag = new Tag("testTag");
 assertNotEquals(null, tag);
 }

 @Test
 public void testEqualsSameObject() {
 Tag tag = new Tag("testTag");
 assertEquals(tag, tag);
 }

 @Test
 public void testEqualsDifferentObjects() {
 Tag tag1 = new Tag("testTag1");
 Tag tag2 = new Tag("testTag2");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testEqualsEqualTags() {
 Tag tag1 = new Tag("testTag");
 Tag tag2 = new Tag("testTag");
 assertEquals(tag1, tag2);
 }

 @Test
 public void testEqualsDifferentTags() {
 Tag tag1 = new Tag("testTag1");
 Tag tag2 = new Tag("testTag2");
 assertNotEquals(tag1, tag2);
 }

 @Test
 public void testHashCodeEqualTags() {
 Tag tag1 = new Tag("testTag");
 Tag tag2 = new Tag("testTag");
 assertEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testHashCodeDifferentTags() {
 Tag tag1 = new Tag("testTag1");
 Tag tag2 = new Tag("testTag2");
 assertNotEquals(tag1.hashCode(), tag2.hashCode());
 }

 @Test
 public void testToString() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.toString());
 }

 @Test
 public void testToStringEmptyTag() {
 Tag tag = new Tag("img");
 assertEquals("img", tag.toString());
 }

 //Edge cases and exception handling tests

 @Test(expected = IllegalArgumentException.class)
 public void testValidateNullTagName() {
 Tag tag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateEmptyTagName() {
 Tag tag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateMultipleTagNameSpaces() {
 Tag tag = new Tag(" Tag ");
 }

 @Test
 public void testCanContainNullTag() {
 Tag tag = new Tag("testTag");
 try {
 tag.canContain(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainSameTag() {
 Tag tag = new Tag("testTag");
 try {
 tag.canContain(tag);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainTagWithEmptyAncestors() {
 Tag emptyAncestorTag = new Tag("testTag");
 emptyAncestorTag.setAncestor(new String[0]);
 Tag childTag = new Tag("testChildTag");
 try {
 emptyAncestorTag.canContain(childTag);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainTagWithInvalidAncestors() {
 Tag invalidAncestorTag = new Tag("testTag");
 String[] invalidAncestors = {"testChildTag"};
 invalidAncestorTag.setAncestor(invalidAncestors);
 Tag childTag = new Tag("testChildTag");
 try {
 invalidAncestorTag.canContain(childTag);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorNull() {
 Tag tag = new Tag("testTag");
 tag.setAncestor(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorEmptyArray() {
 Tag tag = new Tag("testTag");
 tag.setAncestor(new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAncestorInvalidTagName() {
 Tag tag = new Tag("testTag");
 String[] ancestorTagNames = {"testChildTag", "invalidTagName"};
 tag.setAncestor(ancestorTagNames);
 }

}