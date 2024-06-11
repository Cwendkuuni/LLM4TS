import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Test case 1: Empty directory
        assertFalse(library.loadFromDirectory("empty_directory"));
        
        // Test case 2: Non-existing directory
        assertFalse(library.loadFromDirectory("non_existing_directory"));
        
        // Test case 3: Valid directory with files
        assertTrue(library.loadFromDirectory("valid_directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Test case 1: No dictionaries loaded
        assertEquals(0, library.getDictionaries().size());
        
        // Test case 2: Dictionaries loaded
        library.loadFromDirectory("valid_directory");
        assertEquals(1, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Test case 1: Dictionary not loaded
        assertNull(library.getDictionary("1.0"));
        
        // Test case 2: Dictionary loaded
        library.loadFromDirectory("valid_directory");
        assertNotNull(library.getDictionary("1.0"));
    }
    
    // Additional test cases for edge cases and exception handling can be added here
    
}