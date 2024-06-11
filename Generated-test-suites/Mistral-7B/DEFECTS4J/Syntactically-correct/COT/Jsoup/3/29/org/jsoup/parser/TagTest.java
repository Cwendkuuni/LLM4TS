import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag htmlTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag pTag;
 private Tag imgTag;
 private Tag scriptTag;
 private Tag divTag;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 htmlTag = Tag.valueOf("HTML");
 headTag = Tag.valueOf("HEAD");
 bodyTag = Tag.valueOf("BODY");
 pTag = Tag.valueOf("P");
 imgTag = Tag.valueOf("IMG");
 scriptTag = Tag.valueOf("SCRIPT");
 divTag = Tag.valueOf("DIV");
 }

 @Test
 public void testTagNameGetters() {
 // Test getting tag name for different tags
 Assert.assertEquals("HTML", htmlTag.getName());
 Assert.assertEquals("HEAD", headTag.getName());
 Assert.assertEquals("BODY", bodyTag.getName());
 Assert.assertEquals("P", pTag.getName());
 Assert.assertEquals("IMG", imgTag.getName());
 Assert.assertEquals("SCRIPT", scriptTag.getName());
 Assert.assertEquals("DIV", divTag.getName());
 }

 @Test
 public void testTagCanContain() {
 // Test tag can contain another tag based on its type
 Assert.assertTrue(bodyTag.canContain(pTag));
 Assert.assertTrue(pTag.canContain(imgTag));
 Assert.assertFalse(pTag.canContain(scriptTag));
 }

 @Test
 public void testTagIsBlock() {
 // Test if tag is block or inline
 Assert.assertTrue(bodyTag.isBlock());
 Assert.assertTrue(pTag.isBlock());
 Assert.assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagIsData() {
 // Test if tag is data only tag
 Assert.assertFalse(pTag.isData());
 Assert.assertFalse(bodyTag.isData());
 Assert.assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 // Test if tag is empty tag
 Assert.assertFalse(pTag.isEmpty());
 Assert.assertFalse(bodyTag.isEmpty());
 Assert.assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 // Test if tag preserves whitespace
 Assert.assertFalse(pTag.preserveWhitespace());
 Assert.assertFalse(bodyTag.preserveWhitespace());
 Assert.assertTrue(scriptTag.preserveWhitespace());
 }

 @Test
 public void testTagEquals() {
 // Test tag equals itself and other tags
 Tag tag1 = new Tag("tag1");
 Tag tag2 = new Tag("tag1");
 Tag tag3 = new Tag("tag2");

 Assert.assertEquals(tag1, tag1);
 Assert.assertEquals(tag1, tag2);
 Assert.assertNotEquals(tag1, tag3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 // Test with an empty tag name
 Tag emptyTag = new Tag("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 // Test with a null tag name
 Tag nullTag = new Tag(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateTagName() {
 // Test with a duplicate tag name
 Tag tag = new Tag("HTML");
 Tag existingTag = Tag.valueOf("HTML");
 tag.setAncestor(existingTag.tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTagName() {
 // Test with an unknown tag name
 Tag unknownTag = Tag.valueOf("unknownTag");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullChildTag() {
 // Test with a null child tag
 Tag parentTag = new Tag("parent");
 Assert.assertFalse(parentTag.canContain(null));
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidParentChildRelation() {
 // Test with an invalid parent-child relation
 Tag parentTag = new Tag("parent");
 Tag childTag = new Tag("child");
 childTag.setAncestor(parentTag);
 parentTag.setAncestor(childTag);
 }

 @Test
 public void testTagCanContainSelf() {
 // Test if a tag can contain itself
 Tag tag = new Tag("tag");
 Assert.assertFalse(tag.canContain(tag));
 }

 @Test
 public void testTagHasAncestors() {
 // Test if a tag has ancestors
 Tag tag = new Tag("tag");
 tag.setAncestor("ancestor1", "ancestor2");
 Assert.assertEquals("ancestor1", tag.ancestors.get(0).getName());
 Assert.assertEquals("ancestor2", tag.ancestors.get(1).getName());
 }

 @Test
 public void testTagIsValidParent() {
 // Test if a tag is a valid parent for another tag
 Tag parentTag = new Tag("parent");
 Tag childTag = new Tag("child");
 childTag.ancestors.add(parentTag);

 Assert.assertFalse(parentTag.isValidParent(childTag));
 Assert.assertTrue(parentTag.isValidParent(divTag));
 }

}