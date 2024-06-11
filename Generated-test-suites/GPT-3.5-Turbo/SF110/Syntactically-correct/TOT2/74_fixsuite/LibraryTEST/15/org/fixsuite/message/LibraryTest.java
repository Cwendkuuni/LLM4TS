package org.fixsuite.message;
import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        // Typical Use Case
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/directory"));
        
        // Edge Case: Directory is empty
        assertFalse(library.loadFromDirectory("path/to/empty_directory"));
        
        // Error Scenario: Path is not a directory
        assertFalse(library.loadFromDirectory("path/to/file.txt"));
    }
    
    @Test
    public void testGetDictionaries() {
        // Typical Use Case
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertFalse(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        // Typical Use Case
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("1.0"));
        
        // Error Scenario: Dictionary is not found
        assertNull(library.getDictionary("2.0"));
    }
    
    // Add test cases for remaining methods
    
}