import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag h1Tag;
 private Tag inputTag;
 private Tag formTag;
 private Tag ulTag;
 private Tag liTag;
 private Tag tableTag;
 private Tag trTag;

 @Before
 public void setup() {
 pTag = Tag.valueOf("p");
 h1Tag = Tag.valueOf("h1");
 inputTag = Tag.valueOf("input");
 formTag = Tag.valueOf("form");
 ulTag = Tag.valueOf("ul");
 liTag = Tag.valueOf("li");
 tableTag = Tag.valueOf("table");
 trTag = Tag.valueOf("tr");
 }

 @Test
 public void testTagName() {
 Tag tag = new Tag("testTag");
 assertEquals("testTag", tag.getName());
 }

 @Test
 public void testValueOfPredefinedTag() {
 assertEquals("p", pTag.getName());
 assertEquals("h1", h1Tag.getName());
 }

 @Test
 public void testValueOfUnknownTag() {
 Tag unknownTag = Tag.valueOf("unknownTag");
 assertFalse(unknownTag.equals(pTag));
 assertTrue(unknownTag.getName().equals("unknownTag"));
 }

 @Test
 public void testCanContainBlockWithBlockChild() {
 assertTrue(pTag.canContain(h1Tag));
 }

 @Test
 public void testCanContainBlockWithInlineChild() {
 assertTrue(pTag.canContain(inputTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainBlockWithSelfAsChild() {
 pTag.canContain(pTag);
 }

 @Test
 public void testCanContainInlineWithInlineChild() {
 assertTrue(h1Tag.canContain(inputTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCanContainInlineWithBlockChild() {
 h1Tag.canContain(pTag);
 }

 @Test
 public void testIsDataWithDataOnlyTag() {
 assertTrue(inputTag.isData());
 }

 @Test
 public void testIsEmptyWithEmptyTag() {
 assertTrue(inputTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespaceWithPreserveWhitespaceTag() {
 assertTrue(ulTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParentWithEmptyAncestors() {
 assertNull(pTag.getImplicitParent());
 }

 @Test
 public void testGetImplicitParentWithAncestors() {
 Tag bodyTag = new Tag("body");
 Tag htmlTag = new Tag("html");
 bodyTag.setAncestors(Collections.singletonList(htmlTag));
 assertEquals(htmlTag, pTag.getImplicitParent());
 }

 @Test
 public void testIsValidParentWithValidChild() {
 assertTrue(formTag.isValidParent(inputTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsValidParentWithInvalidChild() {
 formTag.isValidParent(pTag);
 }

 @Test
 public void testEqualsWithSameTag() {
 assertTrue(pTag.equals(pTag));
 }

 @Test
 public void testEqualsWithDifferentTag() {
 assertFalse(pTag.equals(h1Tag));
 }

 @Test
 public void testHashCodeWithSameTag() {
 int hashcode1 = pTag.hashCode();
 Tag tagWithSameProperties = new Tag("p");
 int hashcode2 = tagWithSameProperties.hashCode();
 assertEquals(hashcode1, hashcode2);
 }

 @Test
 public void testHashCodeWithDifferentTag() {
 int hashcode1 = pTag.hashCode();
 Tag tagWithDifferentProperties = new Tag("h1");
 int hashcode2 = tagWithDifferentProperties.hashCode();
 assertNotEquals(hashcode1, hashcode2);
 }

 @Test
 public void testToString() {
 assertEquals("p", pTag.toString());
 }

 @Test
 public void testCanContainWithHeadTag() {
 // TODO: Implement this test properly.
 // head can only contain a few tags. if more than head in here, modify to have a list of valids
 // or use a different approach.
 assertFalse(pTag.canContain(tableTag));
 }

 @Test
 public void testCanContainWithDTAndDD() {
 // don't allow children to contain their parent (directly)
 // this test assumes that DT and DD are treated as the same tag for simplicity
 assertFalse(liTag.canContain(liTag));
 }

 @Test
 public void testCanContainWithEmptyTag() {
 // empty tags cannot contain anything
 assertFalse(pTag.canContain(inputTag));
 }

}