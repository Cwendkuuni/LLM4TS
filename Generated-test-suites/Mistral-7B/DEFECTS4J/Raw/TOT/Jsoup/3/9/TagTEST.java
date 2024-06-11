import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testgetValueOf() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag unknownTag = Tag.valueOf("UnknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotEquals("p", divTag.getName());
 assertNotEquals("p", unknownTag.getName());

 // test case insensitivity
 assertEquals("p", Tag.valueOf("P").getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pTagInsideDiv = Tag.valueOf("p");
 Tag divTagInsideP = Tag.valueOf("div");
 Tag headTag = Tag.valueOf("head");
 Tag bodyTag = Tag.valueOf("body");
 Tag imgTag = Tag.valueOf("img");

 // p can contain div
 assertTrue(pTag.canContain(divTag));

 // div can contain p
 assertTrue(divTag.canContain(pTag));

 // p cannot contain p
 assertFalse(pTag.canContain(pTagInsideP));

 // div cannot contain div
 assertFalse(divTag.canContain(divTagInsideP));

 // head can contain body, but not itself
 assertTrue(headTag.canContain(bodyTag));
 assertFalse(headTag.canContain(headTag));

 // image tag can contain nothing
 assertFalse(imgTag.canContain(new Tag("")));

 // test invalid parent-child relationships
 assertFalse(pTag.canContain(headTag));
 assertFalse(pTag.canContain(imgTag));
 }

 @Test
 public void testIsValidParent() {
 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag pTag2 = Tag.valueOf("p");
 Tag divTag2 = Tag.valueOf("div");

 // p is valid parent for p
 assertTrue(pTag.isValidParent(pTag2));

 // div is valid parent for p
 assertTrue(divTag.isValidParent(pTag));

 // p is not a valid parent for div
 assertFalse(pTag.isValidParent(divTag));

 // div is not a valid parent for itself
 assertFalse(divTag.isValidParent(divTag2));
 }
}