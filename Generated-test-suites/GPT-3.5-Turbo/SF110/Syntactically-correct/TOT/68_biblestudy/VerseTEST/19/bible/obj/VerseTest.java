
package bible.obj;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;
        
        int actualId = Verse.GetId(translationId, bookId, chapterId, verseId);
        
        assertEquals(expectedId, actualId);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String expectedText = "This is the text of verse 1";
        
        String actualText = verse.getText();
        
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        String expectedValue = "This is the text of verse 1";
        
        String actualValue = verse.getValue();
        
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference expectedReference = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
        
        TranslationReference actualReference = verse.getTranslationReference();
        
        assertEquals(expectedReference, actualReference);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference expectedReference = new Reference(1, 1, 1);
        
        Reference actualReference = verse.getReference();
        
        assertEquals(expectedReference, actualReference);
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expectedString = "bible.obj.Verse:[1 TranslationReference:[Translation:1 Reference:[1 1 1]] This is the text of verse 1]";
        
        String actualString = verse.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(new Verse(1));
        expectedVerses.add(new Verse(2));
        
        ArrayList<Verse> actualVerses = Verse.Get(translations, bookId, chapterId, verseId);
        
        assertEquals(expectedVerses, actualVerses);
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 3);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(new Verse(1));
        expectedVerses.add(new Verse(2));
        expectedVerses.add(new Verse(3));
        
        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        
        assertEquals(expectedVerses, actualVerses);
    }
    
    @Test
    public void testGetWithEmptyTranslations() {
        Translation[] translations = {};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(3, 1, 3);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        
        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        
        assertEquals(expectedVerses, actualVerses);
    }
    
    @Test
    public void testGetWithNullTranslations() {
        Translation[] translations = null;
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(3, 1, 3);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        
        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        
        assertEquals(expectedVerses, actualVerses);
    }
}
