
package org.fixsuite.message;

import static org.junit.Assert.*;

import java.util.List;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("path/to/library"));
        assertFalse(library.loadFromDirectory("invalid/path"));
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(0, dictionaries.size());
        
        // Add test dictionaries
        
        // Test after adding dictionaries
        dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size()); // Assuming 2 dictionaries added
        
        // Test dictionary contents
        
        // Test empty dictionary
        library = new Library();
        dictionaries = library.getDictionaries();
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version1");
        assertNull(dictionary);
        
        // Add test dictionaries
        
        dictionary = library.getDictionary("version1");
        assertNotNull(dictionary); // Assuming dictionary with version1 exists
        
        dictionary = library.getDictionary("version2");
        assertNotNull(dictionary); // Assuming dictionary with version2 exists
        
        dictionary = library.getDictionary("invalidVersion");
        assertNull(dictionary);
    }
    
}
