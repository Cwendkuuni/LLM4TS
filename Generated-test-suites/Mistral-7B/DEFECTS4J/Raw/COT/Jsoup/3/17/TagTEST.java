import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TagTest {

 private Tag defaultAncestor;
 private Tag pTag;
 private Tag divTag;
 private Tag headTag;
 private Tag bodyTag;
 private Tag htmlTag;

 @Before
 public void setUp() {
 defaultAncestor = Tag.valueOf("BODY");
 htmlTag = Tag.valueOf("HTML");
 bodyTag = Tag.valueOf("BODY");
 headTag = Tag.valueOf("HEAD");
 pTag = Tag.valueOf("P");
 divTag = Tag.valueOf("DIV");
 }

 @Test
 public void testTagNameGetters() {
 // Test getName() method
 Tag tag = Tag.valueOf("TAG");
 Assert.assertEquals("tag", tag.getName());

 // Test getName() method with capitalized tag name
 Tag capitalizedTag = Tag.valueOf("Tag");
 Assert.assertEquals("tag", capitalizedTag.getName());
 }

 @Test
 public void testTagCanContainMethod() {
 // Test canContain() method with valid child
 Assert.assertTrue(pTag.canContain(bodyTag));

 // Test canContain() method with invalid child (block tag in inline tag)
 Assert.assertFalse(pTag.canContain(divTag));

 // Test canContain() method with invalid child (inline tag in block tag)
 Assert.assertFalse(divTag.canContain(pTag));

 // Test canContain() method with parent and child being the same tag
 Assert.assertFalse(pTag.canContain(pTag));

 // Test canContain() method with empty tag
 Assert.assertTrue(pTag.canContain(Tag.valueOf("BR")));

 // Test canContain() method with data-only tag
 Tag scriptTag = Tag.valueOf("SCRIPT");
 Assert.assertFalse(bodyTag.canContain(scriptTag));

 // Test canContain() method with head tag
 Assert.assertTrue(headTag.canContain(scriptTag));
 }

 @Test
 public void testTagIsBlockAndInlineMethods() {
 // Test isBlock() method
 Assert.assertTrue(divTag.isBlock());

 // Test isInline() method
 Assert.assertFalse(pTag.isInline());

 // Test isData() method
 Assert.assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmptyMethod() {
 // Test isEmpty() method with empty tag
 Assert.assertTrue(Tag.valueOf("IMG").isEmpty());

 // Test isEmpty() method with non-empty tag
 Assert.assertFalse(pTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespaceMethod() {
 // Test preserveWhitespace() method with non-preserving tag
 Assert.assertFalse(pTag.preserveWhitespace());

 // Test preserveWhitespace() method with preserving tag
 Tag preTag = Tag.valueOf("PRE");
 Assert.assertTrue(preTag.preserveWhitespace());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfEmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test
 public void testTagValueOfInvalidTagName() {
 // Test valueOf() method with invalid tag name (uppercase letter)
 Tag invalidTag = Tag.valueOf("TAG");
 Assert.assertFalse(invalidTag.equals(Tag.valueOf("tag")));

 // Test valueOf() method with unknown tag name
 Tag unknownTag = Tag.valueOf("UNKNOWN_TAG");
 Assert.assertFalse(unknownTag.isBlock());
 Assert.assertFalse(unknownTag.isData());
 Assert.assertFalse(unknownTag.isEmpty());
 Assert.assertEquals("unknown_tag", unknownTag.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testTagCanContainSelf() {
 // Test canContain() method with self as child
 pTag.canContain(pTag);
 }

 @Test
 public void testTagIsValidParent() {
 // Test isValidParent() method with valid parent-child relationship
 Assert.assertTrue(htmlTag.isValidParent(bodyTag));

 // Test isValidParent() method with invalid parent-child relationship
 Assert.assertFalse(bodyTag.isValidParent(htmlTag));
 }

 @Test
 public void testTagEqualsMethod() {
 // Test equals() method with identical tags
 Tag identicalTag = Tag.valueOf("P");
 Assert.assertEquals(identicalTag.hashCode(), pTag.hashCode());
 Assert.assertTrue(identicalTag.equals(pTag));

 // Test equals() method with different tags
 Tag differentTag = Tag.valueOf("DIV");
 Assert.assertFalse(differentTag.equals(pTag));

 // Test equals() method with same tag but different properties
 Tag sameTagDifferentProperties = new Tag("P");
 sameTagDifferentProperties.isEmpty();
 Assert.assertFalse(sameTagDifferentProperties.equals(pTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagValueOfNullObject() {
 Tag tag = Tag.valueOf(null);
 }

}