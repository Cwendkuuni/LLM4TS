package org.fixsuite.message;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("TestDirectory");
        assertTrue(result);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionaryInfo = library.getDictionary("TestVersion");
        assertNull(dictionaryInfo);
    }
}