
package org.fixsuite.message;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        boolean result = library.loadFromDirectory("testDirectory");
        
        assertTrue(result);
        assertEquals(1, library.getDictionaries().size());
        assertNotNull(library.getDictionary("version"));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        DictionaryInfo dictionary = library.getDictionary("version");
        
        assertNull(dictionary);
    }
}
