import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.sql.*;
import bible.util.*;

public class VerseTest {
    private Verse verse;
    
    @Before
    public void setUp() {
        verse = new Verse(1);
    }
    
    @Test
    public void testGetId() {
        int expectedId = 1;
        int actualId = verse.getId();
        assertEquals(expectedId, actualId);
    }
    
    @Test
    public void testGetText() {
        String expectedText = "test text";
        verse.setText(expectedText);
        String actualText = verse.getText();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testGetValue() {
        String expectedValue = "test text";
        verse.setText(expectedValue);
        String actualValue = verse.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference expectedRef = new TranslationReference();
        verse.setTranslationReference(expectedRef);
        TranslationReference actualRef = verse.getTranslationReference();
        assertEquals(expectedRef, actualRef);
    }
    
    @Test
    public void testGetReference() {
        Reference expectedRef = new Reference();
        verse.setTranslationReference(new TranslationReference(new Translation(), expectedRef));
        Reference actualRef = verse.getReference();
        assertEquals(expectedRef, actualRef);
    }
    
    @Test
    public void testToString() {
        String expectedString = "Verse:[1 test reference test text]";
        String actualString = verse.toString();
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testGetId_withTranslationBookChapterVerse() {
        int expectedId = 1;
        int actualId = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(expectedId, actualId);
    }
    
    @Test
    public void testGet_withTranslationsBookChapterVerse() {
        ArrayList<Verse> expectedVerses = new ArrayList<Verse>();
        expectedVerses.add(new Verse(1));
        ArrayList<Verse> actualVerses = Verse.Get(new Translation[]{new Translation()}, 1, 1, 1);
        assertEquals(expectedVerses, actualVerses);
    }
    
    @Test
    public void testGet_withTranslationsBeginEnd() {
        ArrayList<Verse> expectedVerses = new ArrayList<Verse>();
        expectedVerses.add(new Verse(1));
        ArrayList<Verse> actualVerses = Verse.Get(new Translation[]{new Translation()}, new Reference(), new Reference());
        assertEquals(expectedVerses, actualVerses);
    }
    
    @Test
    public void testGetId_withTranslationIdBookChapterVerse() {
        int expectedId = 1;
        int actualId = Verse.GetId(1, 1, 1, 1);
        assertEquals(expectedId, actualId);
    }
    
}