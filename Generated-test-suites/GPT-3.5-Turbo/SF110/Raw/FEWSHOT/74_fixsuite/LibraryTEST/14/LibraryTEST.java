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
    public void testLoadFromDirectory_ValidDirectory_Success() {
        assertTrue(library.loadFromDirectory("path/to/valid/directory"));
    }
    
    @Test
    public void testLoadFromDirectory_InvalidDirectory_Failure() {
        assertFalse(library.loadFromDirectory("path/to/invalid/directory"));
    }
    
    @Test
    public void testGetDictionaries_EmptyLibrary_EmptyList() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_ValidVersion_Success() {
        library.loadFromDirectory("path/to/valid/directory");
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_InvalidVersion_ReturnsNull() {
        library.loadFromDirectory("path/to/valid/directory");
        DictionaryInfo dictionary = library.getDictionary("2.0");
        assertNull(dictionary);
    }
}