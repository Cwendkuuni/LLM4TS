import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testTagValueOf() {
 Tag htmlTag = Tag.valueOf("HTML");
 Tag bodyTag = Tag.valueOf("BODY");
 Tag pTag = Tag.valueOf("P");

 assertEquals("HTML", htmlTag.getName());
 assertEquals("BODY", bodyTag.getName());
 assertEquals("P", pTag.getName());
 }

 @Test
 public void testTagCanContain() {
 Tag pTag = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");
 Tag imgTag = Tag.valueOf("IMG");

 // Case 1: p can contain div
 assertTrue(pTag.canContain(divTag));

 // Case 2: div can contain p
 assertTrue(divTag.canContain(pTag));

 // Case 3: p cannot contain img
 assertFalse(pTag.canContain(imgTag));
 }

 @Test
 public void testTagIsBlock() {
 Tag pTag = Tag.valueOf("P");
 Tag imgTag = Tag.valueOf("IMG");

 assertTrue(pTag.isBlock());
 assertFalse(imgTag.isBlock());
 }

 @Test
 public void testTagCanContainBlock() {
 Tag divTag = Tag.valueOf("DIV");
 Tag pTag = Tag.valueOf("P");

 assertTrue(divTag.canContainBlock());
 assertTrue(divTag.canContain(pTag));
 }

 @Test
 public void testTagIsData() {
 Tag scriptTag = Tag.valueOf("SCRIPT");

 assertTrue(scriptTag.isData());
 }

 @Test
 public void testTagIsEmpty() {
 Tag imgTag = Tag.valueOf("IMG");

 assertTrue(imgTag.isEmpty());
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("PRE");

 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testTagGetImplicitParent() {
 Tag divTag = Tag.valueOf("DIV");
 Tag pTag = Tag.valueOf("P");
 Tag bodyTag = Tag.valueOf("BODY");

 pTag.setAncestor(divTag);
 divTag.setAncestor(bodyTag);

 assertEquals(bodyTag.getName(), pTag.getImplicitParent().getName());
 }

 @Test
 public void testTagIsValidParent() {
 Tag pTag = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");

 assertTrue(pTag.isValidParent(divTag));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTagEqualsNull() {
 Tag pTag = Tag.valueOf("P");

 assertEquals(pTag, null); // This should throw an exception
 }

 @Test
 public void testTagHashCode() {
 Tag pTag1 = Tag.valueOf("P");
 Tag pTag2 = Tag.valueOf("P");

 int hashCode1 = pTag1.hashCode();
 int hashCode2 = pTag2.hashCode();

 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testTagToString() {
 Tag pTag = Tag.valueOf("P");

 assertEquals("P", pTag.toString());
 }

 @Test
 public void testTagCanContainSelf() {
 Tag pTag = Tag.valueOf("P");

 assertFalse(pTag.canContain(pTag));
 }

 @Test
 public void testTagEmptyAncestors() {
 Tag imgTag = Tag.valueOf("IMG");

 assertTrue(imgTag.isValidParent(Tag.valueOf("HEAD")));
 }
}