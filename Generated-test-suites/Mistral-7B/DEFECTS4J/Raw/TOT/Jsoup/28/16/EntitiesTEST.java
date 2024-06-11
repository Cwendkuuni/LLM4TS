package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full set
 assertTrue(Entities.isNamedEntity("lt"));
 // Test for unknown named entity
 assertFalse(Entities.isNamedEntity("tt"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities in the full set
 assertEquals('\u003C', Entities.getCharacterByName("lt"));
 // Test for unknown named entity
 assertNull(Entities.getCharacterByName("tt"));
 }

 @Test
 public void testEscapeWithDefaultEscapeMode() {
 String input = "This is a test string with <special> characters: &amp; < > ' \"";
 String expectedOutput = "This is a test string with &amp; &lt; &gt; ' &apos; &quot;";
 String output = Entities.escape(input, null, Entities.EscapeMode.base);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeWithXHTMLEscapeMode() {
 String input = "This is a test string with <special> characters: &amp; < > ' \"";
 String expectedOutput = "This is a test string with &amp; &lt; &gt; ' &apos; &quot;";
 String output = Entities.escape(input, null, Entities.EscapeMode.xhtml);
 assertEquals(Entities.getCharacterByName("lt").toString(), output.substring(0, 1));
 assertEquals(Entities.getCharacterByName("gt").toString(), output.substring(output.length() - 1, output.length()));
 assertEquals(expectedOutput.substring(1, expectedOutput.length() - 1), output.substring(1, output.length() - 1));
 }

 @Test
 public void testUnescape() {
 String input = "This is a test string with &lt;special&gt; characters: &amp;lt; &lt; &gt; &apos; &quot;";
 String expectedOutput = "This is a test string with <special> characters: &amp; < & > ' ' \"";
 String output = Entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadEntitiesThrowsException() {
 Entities.loadEntities("non-existent.properties");
 }
}