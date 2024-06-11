import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 private static final Character[] knownNamedEntities = {
 'lt', 'gt', 'amp', 'apos', 'quot'
 };

 @Test
 public void testIsNamedEntity() {
 for (Character namedEntity : knownNamedEntities) {
 Assert.assertTrue(Entities.isNamedEntity(NamedEntityToString(namedEntity)));
 }
 }

 @Test
 public void testIsNamedEntityWithInvalidName() {
 Assert.assertFalse(Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 for (Character namedEntity : knownNamedEntities) {
 Character charValue = Entities.getCharacterByName(NamedEntityToString(namedEntity));
 Assert.assertEquals(namedEntity, charValue);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameWithInvalidName() {
 Entities.getCharacterByName("InvalidName");
 }

 @Test
 public void testEscapeMethodWithDefaultEncoderAndBaseEscapeMode() {
 String inputString = "test < & >";
 String expectedOutput = "test &lt; &amp; &gt;";
 String output = Entities.escape(inputString, null, Entities.EscapeMode.base);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeMethodWithCustomEncoderAndBaseEscapeMode() {
 String inputString = "test < & >";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String expectedOutput = "test &#60; &#38; &#62;";
 String output = Entities.escape(inputString, encoder, Entities.EscapeMode.base);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeMethodWithDefaultEncoderAndExtendedEscapeMode() {
 String inputString = "test < & > 🌹";
 String expectedOutput = "test &lt; &amp; &gt; &#128525;";
 String output = Entities.escape(inputString, null, Entities.EscapeMode.extended);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeMethodWithCustomEncoderAndExtendedEscapeMode() {
 String inputString = "test < & > 🌹";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String expectedOutput = "test &#60; &#38; &#62; &#128525;";
 String output = Entities.escape(inputString, encoder, Entities.EscapeMode.extended);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeMethodWithDefaultEscapeMode() {
 String inputString = "test &lt; &amp; &gt; &#128525;";
 String expectedOutput = "test < & > 🌹";
 String output = Entities.unescape(inputString);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeMethodWithStrictMode() {
 String inputString = "test &lt; &amp; &gt; &#128525;";
 String expectedOutput = "test < & > 🌹";
 String output = Entities.unescape(inputString, true);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithInvalidInput() {
 Entities.unescape("Invalid Input");
 }

 private String NamedEntityToString(Character namedEntity) {
 return namedEntity.toString().toLowerCase();
 }

}