import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 @Test
 public void testValueOf() {
 Tag pTag = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");
 Tag unknownTag = Tag.valueOf("UnknownTag");

 assertEquals("p", pTag.getName());
 assertEquals("div", divTag.getName());
 assertNotEquals("p", divTag.getName());
 assertNotEquals("UnknownTag", pTag.getName());
 assertNotEquals("p", unknownTag.getName());
 }

 @Test
 public void testCanContain() {
 Tag pTag = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");
 Tag imgTag = Tag.valueOf("IMG");
 Tag brTag = Tag.valueOf("BR");
 Tag headTag = Tag.valueOf("HEAD");
 Tag bodyTag = Tag.valueOf("BODY");

 // p can contain div, img, br, head, body and itself (nested)
 assertTrue(pTag.canContain(divTag));
 assertTrue(pTag.canContain(imgTag));
 assertTrue(pTag.canContain(brTag));
 assertTrue(pTag.canContain(headTag));
 assertTrue(pTag.canContain(bodyTag));
 assertTrue(pTag.canContain(pTag));

 // head cannot contain p, img, br, head, body or itself (directly)
 assertFalse(headTag.canContain(pTag));
 assertFalse(headTag.canContain(imgTag));
 assertFalse(headTag.canContain(brTag));
 assertFalse(headTag.canContain(headTag));
 assertFalse(headTag.canContain(bodyTag));
 assertFalse(headTag.canContain(pTag));

 // img cannot contain p, div, img, br, head, body or itself
 assertFalse(imgTag.canContain(pTag));
 assertFalse(imgTag.canContain(divTag));
 assertFalse(imgTag.canContain(imgTag));
 assertFalse(imgTag.canContain(brTag));
 assertFalse(imgTag.canContain(headTag));
 assertFalse(imgTag.canContain(bodyTag));
 assertFalse(imgTag.canContain(pTag));
 }

 @Test
 public void testIsBlockAndIsInline() {
 Tag pTag = Tag.valueOf("P");
 Tag brTag = Tag.valueOf("BR");

 assertTrue(pTag.isBlock());
 assertFalse(brTag.isBlock());
 assertFalse(pTag.isInline());
 assertTrue(brTag.isInline());
 }

 @Test
 public void testIsData() {
 Tag scriptTag = Tag.valueOf("SCRIPT");
 Tag textareaTag = Tag.valueOf("TEXTAREA");

 assertFalse(scriptTag.isData());
 assertFalse(textareaTag.isData());
 textareaTag.setEmpty();
 assertTrue(textareaTag.isData());
 }

 @Test
 public void testIsEmpty() {
 Tag imgTag = Tag.valueOf("IMG");
 Tag insTag = Tag.valueOf("INS");
 Tag delTag = Tag.valueOf("DEL");

 assertFalse(imgTag.isEmpty());
 assertFalse(insTag.isEmpty());
 assertFalse(delTag.isEmpty());
 imgTag.setEmpty();
 insTag.setEmpty();
 delTag.setEmpty();
 assertTrue(imgTag.isEmpty());
 assertTrue(insTag.isEmpty());
 assertTrue(delTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 Tag preTag = Tag.valueOf("PRE");

 assertFalse(preTag.preserveWhitespace());
 preTag.setPreserveWhitespace();
 assertTrue(preTag.preserveWhitespace());
 }

 @Test
 public void testGetImplicitParent() {
 Tag pTag = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");

 // p implicitly has body as parent
 assertEquals("body", pTag.getImplicitParent().getName());
 // div has no implicit parent
 assertNull(divTag.getImplicitParent());

 // set parents explicitly
 pTag.setAncestor("body");
 divTag.setAncestor("div", "p");

 // p still has body as implicit parent
 assertEquals("body", pTag.getImplicitParent().getName());
 // div has div as implicit parent
 assertEquals("div", divTag.getImplicitParent().getName());
 }

 @Test
 public void testIsValidParent() {
 Tag divTag = Tag.valueOf("DIV");
 Tag pTag = Tag.valueOf("P");
 Tag bodyTag = Tag.valueOf("BODY");
 Tag headTag = Tag.valueOf("HEAD");

 // div can be parent of p
 assertTrue(divTag.isValidParent(pTag));
 // div cannot be parent of body
 assertFalse(divTag.isValidParent(bodyTag));
 // body can be parent of div
 assertTrue(bodyTag.isValidParent(divTag));
 // body cannot be parent of head
 assertFalse(bodyTag.isValidParent(headTag));
 // head cannot be parent of div
 assertFalse(headTag.isValidParent(divTag));
 }

 @Test
 public void testEqualsAndHashcode() {
 Tag pTag1 = Tag.valueOf("P");
 Tag pTag2 = Tag.valueOf("P");
 Tag divTag = Tag.valueOf("DIV");

 // two equal p tags
 assertEquals(pTag1, pTag2);
 // two hashcodes should be equal
 int hashcode1 = pTag1.hashCode();
 int hashcode2 = pTag2.hashCode();
 assertEquals(hashcode1, hashcode2);
 // p and div are not equal
 assertNotEquals(pTag1, divTag);
 // two hashcodes should be different
 int hashcodeP = pTag1.hashCode();
 int hashcodeD = divTag.hashCode();
 assertNotEquals(hashcodeP, hashcodeD);
 }
}