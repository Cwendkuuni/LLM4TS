import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag tag = Tag.valueOf("p");
 assertTrue(tag.isBlock());
 Tag unknownTag = Tag.valueOf("UNKNOWN_TAG");
 assertFalse(unknownTag.isBlock());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag imgTag = Tag.valueOf("img");

 assertTrue(pTag.canContain(divTag));
 assertTrue(pTag.canContain(imgTag));
 assertFalse(divTag.canContain(imgTag));
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("script");
 Tag textAreaTag = Tag.valueOf("textarea");

 assertTrue(scriptTag.isData());
 assertTrue(textAreaTag.isData());
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
 public void testEquals() {
 Tag pTag1 = Tag.valueOf("p");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");

 assertEquals(pTag1, pTag2);
 assertNotEquals(pTag1, divTag);
 }

 @Test
 public void testHashCode() {
 Tag pTag = Tag.valueOf("p");
 Tag qTag = Tag.valueOf("q");

 int pHashCode = pTag.hashCode();
 int qHashCode = qTag.hashCode();

 assertNotEquals(pHashCode, qHashCode);
 }

 @Test
 public void testIsValidParent() {
 Tag parentTag = Tag.valueOf("div");
 Tag childTag1 = Tag.valueOf("p");
 Tag childTag2 = Tag.valueOf("div");

 assertTrue(parentTag.isValidParent(childTag1));
 assertFalse(parentTag.isValidParent(childTag2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullParent() {
 Tag childTag = Tag.valueOf("p");
 Tag nullParentTag = null;

 childTag.isValidParent(nullParentTag);
 }

}