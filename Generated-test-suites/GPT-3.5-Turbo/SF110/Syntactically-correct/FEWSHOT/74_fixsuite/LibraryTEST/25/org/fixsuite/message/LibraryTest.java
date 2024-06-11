
package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LibraryTest {
    
    private Library library;
    private String testDirectoryPath;

    @Before
    public void setUp() {
        library = new Library();
        testDirectoryPath = "test/directory/path";
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory(testDirectoryPath));
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        assertFalse(library.loadFromDirectory(""));
    }

    @Test
    public void testLoadFromDirectory_notDirectory() {
        assertFalse(library.loadFromDirectory("not/a/directory"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("version"));
    }

}
