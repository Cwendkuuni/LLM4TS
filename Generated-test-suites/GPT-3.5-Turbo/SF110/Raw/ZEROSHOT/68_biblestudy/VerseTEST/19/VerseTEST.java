
package bible.obj;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import bible.util.Reference;
import bible.util.Util;
import bible.util.Translation;
import bible.util.TranslationReference;

public class VerseTest {
  
  @Test
  public void testNew() {
    Verse verse = Verse.New(1);
    assertNotNull(verse);
    assertEquals(1, verse.getId());
  }
  
  @Test
  public void testGetId() {
    int id = Verse.GetId(1, 2, 3, 4);
    assertEquals(0, id);
  }
  
  @Test
  public void testGetText() {
    Verse verse = new Verse(1);
    assertEquals("verse text", verse.getText());
  }
  
  @Test
  public void testGetTranslationReference() {
    Verse verse = new Verse(1);
    Translation reference = verse.getTranslationReference();
    assertNotNull(reference);
  }
  
  @Test
  public void testGetReference() {
    Verse verse = new Verse(1);
    Reference reference = verse.getReference();
    assertNotNull(reference);
  }
  
  @Test
  public void testGetValue() {
    Verse verse = new Verse(1);
    assertEquals("verse text", verse.getValue());
  }
  
  @Test
  public void testToString() {
    Verse verse = new Verse(1);
    String expected = "bible.obj.Verse:[1 translation reference verse text]";
    assertEquals(expected, verse.toString());
  }
  
  @Test
  public void testGet() {
    Translation[] translations = new Translation[]{new Translation(1)};
    ArrayList<Verse> verses = Verse.Get(translations, 2, 3, 4);
    assertNotNull(verses);
    assertEquals(0, verses.size());
  }
  
  @Test
  public void testGetWithReferences() {
    Translation[] translations = new Translation[]{new Translation(1)};
    Reference begin = new Reference(2, 3, 4);
    Reference end = new Reference(5, 6, 7);
    ArrayList<Verse> verses = Verse.Get(translations, begin, end);
    assertNotNull(verses);
    assertEquals(0, verses.size());
  }
  
}
