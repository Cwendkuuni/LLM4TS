package bible.obj;

import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {
    
    Verse verse;
    
    @Test
    public void testNew() {
        verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        int id = 1;
        verse = Verse.New(id);
        assertEquals(id, verse.getId());
    }
    
    @Test
    public void testGetText() {
        verse = Verse.New(1);
        assertNotNull(verse.getText());
    }
    
    @Test
    public void testGetValue() {
        verse = Verse.New(1);
        assertNotNull(verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        verse = Verse.New(1);
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        verse = Verse.New(1);
        assertNotNull(verse.getReference());
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}