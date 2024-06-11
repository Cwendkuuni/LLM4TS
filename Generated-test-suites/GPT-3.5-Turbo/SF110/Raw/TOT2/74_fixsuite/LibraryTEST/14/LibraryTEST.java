package org.fixsuite.message;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertNotNull(library.getDictionaries());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("version"));
    }

    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        assertNotNull(library.loadDirectory(new ArrayList<File>(), "version"));
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        assertNotNull(library.arrangeFiles(new File[]{new File("Fields.xml")}));
    }
}