import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

 private Tag pTag;
 private Tag h1Tag;
 private Tag divTag;
 private Tag ulTag;
 private Tag olTag;
 private Tag liTag;
 private Tag tableTag;
 private Tag trTag;
 private Tag thTag;
 private Tag tdTag;

 @Test
 public void testTagCreation() {
 pTag = Tag.valueOf("p");
 h1Tag = Tag.valueOf("h1");
 divTag = Tag.valueOf("div");
 ulTag = Tag.valueOf("ul");
 olTag = Tag.valueOf("ol");
 liTag = Tag.valueOf("li");
 tableTag = Tag.valueOf("table");
 trTag = Tag.valueOf("tr");
 thTag = Tag.valueOf("th");
 tdTag = Tag.valueOf("td");

 // Basic functionality tests
 Assert.assertNotNull(pTag);
 Assert.assertEquals("p", pTag.getName());
 Assert.assertTrue(pTag.isBlock());
 Assert.assertTrue(pTag.canContainBlock());

 // Edge cases and exception handling tests

 // Test canContain() method
 Assert.assertTrue(pTag.canContain(divTag));
 Assert.assertTrue(pTag.canContain(tdTag));
 Assert.assertFalse(pTag.canContain(pTag));
 Assert.assertFalse(pTag.canContain(h1Tag));

 // Test isData() method
 Assert.assertFalse(pTag.isData());

 // Test isEmpty() method
 Assert.assertFalse(pTag.isEmpty());

 // Test preserveWhitespace() method
 Assert.assertFalse(pTag.preserveWhitespace());

 // Test getImplicitParent() method
 Assert.assertNull(pTag.getImplicitParent());

 // Test isValidParent() method
 Assert.assertTrue(divTag.isValidParent(pTag));
 Assert.assertFalse(h1Tag.isValidParent(pTag));

 // Test equals() method
 Tag sameTag = Tag.valueOf("p");
 Assert.assertEquals(pTag, sameTag);

 // Test hashCode() method
 Tag anotherPTag = Tag.valueOf("p");
 Assert.assertEquals(pTag.hashCode(), anotherPTag.hashCode());
 }

 @Test
 public void testTagMethods() {
 // Test setContainInlineOnly() method
 pTag.setContainInlineOnly();
 Assert.assertFalse(pTag.canContainBlock());
 Assert.assertTrue(pTag.canContainInline());

 // Test setContainDataOnly() method
 pTag.setContainDataOnly();
 Assert.assertFalse(pTag.canContainBlock());
 Assert.assertFalse(pTag.canContainInline());
 Assert.assertTrue(pTag.preserveWhitespace);

 // Test setEmpty() method
 pTag.setEmpty();
 Assert.assertFalse(pTag.canContainBlock());
 Assert.assertFalse(pTag.canContainInline());
 Assert.assertTrue(pTag.empty);

 // Test setOptionalClosing() method
 pTag.setOptionalClosing();
 Assert.assertTrue(pTag.optionalClosing);

 // Test setAncestor() method
 pTag.setAncestor("div");
 List<Tag> ancestors = pTag.getAncestors();
 Assert.assertEquals(1, ancestors.size());
 Assert.assertEquals("div", ancestors.get(0).getName());
 }

 @Test
 public void testTagBlockingRelationships() {
 // Test p as ancestor of h1
 pTag.setAncestor("h1");
 Assert.assertTrue(h1Tag.isValidParent(pTag));

 // Test h1 as ancestor of p
 h1Tag.setAncestor("p");
 Assert.assertFalse(pTag.isValidParent(h1Tag));

 // Test p as ancestor of div
 pTag.setAncestor("div");
 Assert.assertTrue(divTag.isValidParent(pTag));

 // Test div as ancestor of p
 divTag.setAncestor("p");
 Assert.assertFalse(pTag.isValidParent(divTag));
 }

 @Test
 public void testTagNesting() {
 // Test p inside ul
 pTag.setAncestor("ul");
 Assert.assertTrue(ulTag.isValidParent(pTag));

 // Test li inside ul
 liTag.setAncestor("ul");
 Assert.assertTrue(ulTag.isValidParent(liTag));

 // Test p inside li
 pTag.setAncestor("li");
 Assert.assertTrue(liTag.isValidParent(pTag));
 }

 @Test
 public void testTagNestingWithSameTag() {
 // Test p inside p
 pTag.setAncestor("p");
 Assert.assertFalse(pTag.isValidParent(pTag));
 }
}