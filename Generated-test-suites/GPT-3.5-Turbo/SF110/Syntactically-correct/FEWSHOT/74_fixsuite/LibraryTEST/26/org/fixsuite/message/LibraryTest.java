package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
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
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }
    
    @Test
    public void testLoadFromEmptyDirectory() {
        assertFalse(library.loadFromDirectory("path/to/empty/directory"));
    }
    
    @Test
    public void testLoadFromNonDirectory() {
        assertFalse(library.loadFromDirectory("path/to/non/directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory("path/to/directory");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertNotNull(dictionaries);
        assertEquals(3, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        library.loadFromDirectory("path/to/directory");
        DictionaryInfo dictionary = library.getDictionary("version");
        
        assertNotNull(dictionary);
        assertEquals("version", dictionary.getVersion());
    }

}