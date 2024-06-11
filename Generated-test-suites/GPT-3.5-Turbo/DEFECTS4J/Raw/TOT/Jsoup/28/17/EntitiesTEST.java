package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

   @Test
   public void testIsNamedEntity() {
      assertTrue(Entities.isNamedEntity("lt"));
      assertTrue(Entities.isNamedEntity("gt"));
      assertTrue(Entities.isNamedEntity("quot"));
      assertTrue(Entities.isNamedEntity("amp"));
      assertTrue(Entities.isNamedEntity("apos"));
      assertFalse(Entities.isNamedEntity("unknownEntity"));
   }

   @Test
   public void testGetCharacterByName() {
      assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
      assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
      assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
      assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
      assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
      assertNull(Entities.getCharacterByName("unknownEntity"));
   }

   @Test
   public void testEscape() {
      String input = "<html>";
      String expectedOutput = "&lt;html&gt;";
      String output = Entities.escape(input, Document.OutputSettings.html());
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testUnescape() {
      String input = "&lt;html&gt;";
      String expectedOutput = "<html>";
      String output = Entities.unescape(input);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testEscape_ErrorScenario() {
      String input = "<html>";
      String expectedOutput = "&12345;html&gt;"; // invalid entity code
      String output = Entities.escape(input, Document.OutputSettings.html());
      assertNotEquals(expectedOutput, output);
   }

   @Test
   public void testUnescape_ErrorScenario() {
      String input = "&lt;html&gt;";
      String expectedOutput = "<html";
      String output = Entities.unescape(input);
      assertNotEquals(expectedOutput, output);
   }

   @Test
   public void testEscape_EdgeCase() {
      String input = "";
      String expectedOutput = "";
      String output = Entities.escape(input, Document.OutputSettings.html());
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testUnescape_EdgeCase() {
      String input = "";
      String expectedOutput = "";
      String output = Entities.unescape(input);
      assertEquals(expectedOutput, output);
   }
}