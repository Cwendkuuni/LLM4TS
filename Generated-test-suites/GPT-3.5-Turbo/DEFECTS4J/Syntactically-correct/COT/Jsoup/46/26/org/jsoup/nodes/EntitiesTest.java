
import org.jsoup.nodes.Entities;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

   // Test case for isNamedEntity method
   @Test
   public void testIsNamedEntity() {
      assertTrue(Entities.isNamedEntity("lt"));
      assertTrue(Entities.isNamedEntity("amp"));
      assertFalse(Entities.isNamedEntity("test"));
   }

   // Test case for isBaseNamedEntity method
   @Test
   public void testIsBaseNamedEntity() {
      assertTrue(Entities.isBaseNamedEntity("lt"));
      assertTrue(Entities.isBaseNamedEntity("amp"));
      assertFalse(Entities.isBaseNamedEntity("test"));
   }

   // Test case for getCharacterByName method
   @Test
   public void testGetCharacterByName() {
      assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
      assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
      assertNull(Entities.getCharacterByName("test"));
   }

   // Test case for escape method
   @Test
   public void testEscape() {
      assertEquals("lt", Entities.escape("<", null));
      assertEquals("gt", Entities.escape(">", null));
      assertEquals("amp", Entities.escape("&", null));
      assertEquals("quot", Entities.escape("\"", null));
   }

   // Test case for escape method with inAttribute set to true
   @Test
   public void testEscapeInAttribute() {
      assertEquals("&lt;", Entities.escape("<", null, true, false, false));
      assertEquals("&gt;", Entities.escape(">", null, true, false, false));
      assertEquals("&amp;", Entities.escape("&", null, true, false, false));
      assertEquals("&quot;", Entities.escape("\"", null, true, false, false));
   }

   // Test case for escape method with normaliseWhite set to true
   @Test
   public void testEscapeNormaliseWhite() {
      assertEquals(" ", Entities.escape(" ", null, false, true, false));
      assertEquals(" ", Entities.escape("\n", null, false, true, false));
      assertEquals(" ", Entities.escape("\t", null, false, true, false));
   }

   // Test case for escape method with stripLeadingWhite set to true
   @Test
   public void testEscapeStripLeadingWhite() {
      assertEquals("test", Entities.escape("   test", null, false, false, true));
      assertEquals("test", Entities.escape("\ttest", null, false, false, true));
      assertEquals("test", Entities.escape("\n\ntest", null, false, false, true));
   }

   // Test case for unescape method
   @Test
   public void testUnescape() {
      assertEquals("<", Entities.unescape("&lt;"));
      assertEquals(">", Entities.unescape("&gt;"));
      assertEquals("&", Entities.unescape("&amp;"));
      assertEquals("\"", Entities.unescape("&quot;"));
   }

   // Test case for unescape method with strict set to true
   @Test
   public void testUnescapeStrict() {
      assertEquals("<", Entities.unescape("&lt;", true));
      assertEquals(">", Entities.unescape("&gt;", true));
      assertEquals("&", Entities.unescape("&amp;", true));
      assertEquals("\"", Entities.unescape("&quot;", true));
   }

   // Test case for unescape method with strict set to false
   @Test
   public void testUnescapeNotStrict() {
      assertEquals("<", Entities.unescape("&lt;"));
      assertEquals(">", Entities.unescape("&gt;"));
      assertEquals("&", Entities.unescape("&amp;"));
      assertEquals("\"", Entities.unescape("&quot;"));
   }
}

