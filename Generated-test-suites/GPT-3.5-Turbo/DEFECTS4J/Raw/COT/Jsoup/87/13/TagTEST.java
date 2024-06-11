import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class TagTest {
   private Tag tag;

   @Before
   public void setUp() {
      tag = new Tag("test");
   }

   @Test
   public void testGetName() {
      assertEquals("test", tag.getName());
   }

   @Test
   public void testIsBlock() {
      assertTrue(tag.isBlock());
   }

   @Test
   public void testFormatAsBlock() {
      assertTrue(tag.formatAsBlock());
   }

   @Test
   public void testCanContainBlock() {
      assertTrue(tag.canContainBlock());
   }

   @Test
   public void testIsInline() {
      assertFalse(tag.isInline());
   }

   @Test
   public void testIsData() {
      assertFalse(tag.isData());
   }

   @Test
   public void testIsEmpty() {
      assertFalse(tag.isEmpty());
   }

   @Test
   public void testIsSelfClosing() {
      assertFalse(tag.isSelfClosing());
   }

   @Test
   public void testIsKnownTag() {
      assertFalse(tag.isKnownTag());
   }

   @Test
   public void testIsKnownTagString() {
      assertFalse(Tag.isKnownTag("test"));
   }

   @Test
   public void testPreserveWhitespace() {
      assertFalse(tag.preserveWhitespace());
   }

   @Test
   public void testIsFormListed() {
      assertFalse(tag.isFormListed());
   }

   @Test
   public void testIsFormSubmittable() {
      assertFalse(tag.isFormSubmittable());
   }

   @Test
   public void testSetSelfClosing() {
      assertTrue(tag.setSelfClosing().isSelfClosing());
   }

   @Test
   public void testEquals() {
      Tag tag2 = new Tag("test");
      assertTrue(tag.equals(tag2));
   }

   @Test
   public void testHashCode() {
      assertEquals(tag.hashCode(), tag.hashCode());
   }

   @Test
   public void testToString() {
      assertEquals("test", tag.toString());
   }

   @Test
   public void testValueOf() {
      tag = Tag.valueOf("div", ParseSettings.preserveCase);
      assertTrue(tag.formatAsBlock());
   }
}