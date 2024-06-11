package org.fixsuite.message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_Success() {
        boolean result = library.loadFromDirectory("path/to/directory");
        assertTrue(result);
    }
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        boolean result = library.loadFromDirectory("path/to/empty/directory");
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_NotADirectory() {
        boolean result = library.loadFromDirectory("path/to/file.txt");
        assertFalse(result);
    }
    
    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionaries());
        assertEquals(1, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionary() {
        library.loadFromDirectory("path/to/directory");
        String version = "1.0";
        assertNotNull(library.getDictionary(version));
    }
}