import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        // Test loading from a valid directory
        Library library = new Library();
        assertTrue(library.loadFromDirectory("test/resources"));
        
        // Test loading from an empty directory
        Library emptyLibrary = new Library();
        assertFalse(emptyLibrary.loadFromDirectory("test/empty"));
        
        // Test loading from a non-existent directory
        Library nonExistentLibrary = new Library();
        assertFalse(nonExistentLibrary.loadFromDirectory("test/nonexistent"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertEquals(0, library.getDictionaries().size());
        
        // Add dictionaries and check if size matches
        library.loadFromDirectory("test/resources");
        assertTrue(library.getDictionaries().size() > 0);
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertNull(library.getDictionary("nonexistent"));
        
        // Add dictionaries and check if retrieved dictionary matches
        library.loadFromDirectory("test/resources");
        assertNotNull(library.getDictionary("FIX.4.4"));
        
        // Test getting dictionary with invalid version
        assertNull(library.getDictionary("invalid"));
    }
}