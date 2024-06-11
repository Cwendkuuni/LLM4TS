import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

 private static final String TAG_NAME_A = "a";
 private static final String TAG_NAME_P = "p";
 private static final String TAG_NAME_DIV = "div";
 private static final String TAG_NAME_UNKNOWN = "unknown";

 @Test
 public void testTag_getName() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertEquals("a", tagA.getName());
 assertEquals("p", tagP.getName());
 assertEquals("div", tagDiv.getName());
 }

 @Test
 public void testTag_isBlock() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertTrue(tagP.isBlock());
 assertTrue(tagDiv.isBlock());
 assertFalse(tagA.isBlock());
 }

 @Test
 public void testTag_formatAsBlock() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertTrue(tagP.formatAsBlock());
 assertTrue(tagDiv.formatAsBlock());
 // by default, all tags format as block
 }

 @Test
 public void testTag_canContainBlock() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertTrue(tagP.canContainBlock());
 assertTrue(tagDiv.canContainBlock());
 // by default, all tags can contain block level tags
 }

 @Test
 public void testTag_isInline() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertFalse(tagA.isInline());
 assertTrue(tagP.isInline());
 assertFalse(tagDiv.isInline());
 }

 @Test
 public void testTag_isData() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertFalse(tagA.isData());
 assertFalse(tagP.isData());
 assertTrue(tagDiv.isData());
 }

 @Test
 public void testTag_isEmpty() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagImg = Tag.valueOf("img");
 Tag tagBr = Tag.valueOf("br");

 assertFalse(tagA.isEmpty());
 assertFalse(tagP.isEmpty());
 assertTrue(tagImg.isEmpty());
 assertTrue(tagBr.isEmpty());
 }

 @Test
 public void testTag_isSelfClosing() {
 Tag tagImg = Tag.valueOf("img");
 Tag tagBr = Tag.valueOf("br");

 assertTrue(tagImg.isSelfClosing());
 assertTrue(tagBr.isSelfClosing());
 }

 @Test
 public void testTag_isKnownTag() {
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagUnknown = Tag.valueOf(TAG_NAME_UNKNOWN);

 assertTrue(Tag.isKnownTag(TAG_NAME_P));
 assertFalse(Tag.isKnownTag(TAG_NAME_UNKNOWN));
 }

 @Test(expected = NullPointerException.class)
 public void testTag_valueOf_NullTagName() {
 Tag tag = Tag.valueOf(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_valueOf_EmptyTagName() {
 Tag tag = Tag.valueOf("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTag_valueOf_InvalidTagName() {
 Tag tag = Tag.valueOf("INVALID_TAG");
 }

 @Test
 public void testTag_equals() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagA2 = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 assertEquals(tagA, tagA2);
 assertNotEquals(tagA, tagP);
 assertNotEquals(tagA, tagDiv);
 }

 @Test
 public void testTag_hashCode() {
 Tag tagA = Tag.valueOf(TAG_NAME_A);
 Tag tagP = Tag.valueOf(TAG_NAME_P);
 Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

 int hashCodeA = tagA.hashCode();
 int hashCodeP = tagP.hashCode();
 int hashCodeDiv = tagDiv.hashCode();

 // different fields should result in different hash codes
 assertNotEquals(hashCodeA, hashCodeP);
 assertNotEquals(hashCodeA, hashCodeDiv);
 }

 @Test
 public void testTag_toString() {
Tag tagA = Tag.valueOf(TAG_NAME_A);
Tag tagP = Tag.valueOf(TAG_NAME_P);
Tag tagDiv = Tag.valueOf(TAG_NAME_DIV);

assertEquals("a", tagA.toString());
assertEquals("p", tagP.toString());
assertEquals("div", tagDiv.toString());
}

// Edge cases and exception handling test cases

@Test(expected = IllegalArgumentException.class)
public void testTag_valueOf_NullTagName_Exception() {
Tag.valueOf(null);
}

@Test(expected = IllegalArgumentException.class)
public void testTag_valueOf_EmptyTagName_Exception() {
Tag.valueOf("");
}

@Test
public void testTag_getValueOf_UnknownTagName() {
Tag tagUnknown = Tag.valueOf(TAG_NAME_UNKNOWN);
// Unknown tag should be created and behave as generic tag
assertFalse(tagUnknown.isBlock);
assertTrue(tagUnknown.canContainBlock);
}

@Test(expected = IllegalArgumentException.class)
public void testTag_register_NullTag() {
Tag tagNull = null;
try {
Tag.register(tagNull);
fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Expected
}
}
}