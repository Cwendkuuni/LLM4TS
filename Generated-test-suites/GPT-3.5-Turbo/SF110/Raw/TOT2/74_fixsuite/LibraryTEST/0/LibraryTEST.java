package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        // Test loading from a valid directory
        Library library = new Library();
        assertTrue(library.loadFromDirectory("test_directory"));
        
        // Test loading from an empty directory
        assertFalse(library.loadFromDirectory("empty_directory"));
        
        // Test loading from a non-existent directory
        assertFalse(library.loadFromDirectory("non_existent_directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        // Test getting dictionaries when dictionaries are present
        Library library = new Library();
        library.loadFromDirectory("test_directory");
        assertEquals(5, library.getDictionaries().size());
        
        // Test getting dictionaries when no dictionaries have been loaded
        Library emptyLibrary = new Library();
        assertEquals(0, emptyLibrary.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionary() {
        // Test getting a dictionary for a valid version
        Library library = new Library();
        library.loadFromDirectory("test_directory");
        assertNotNull(library.getDictionary("1.0"));
        
        // Test getting a dictionary for an invalid version
        assertNull(library.getDictionary("2.0"));
    }

    // Additional test cases for edge cases and error scenarios
    // Add more test cases here as needed
}