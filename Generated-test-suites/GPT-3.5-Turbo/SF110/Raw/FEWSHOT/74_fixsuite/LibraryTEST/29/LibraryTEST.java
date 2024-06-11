
package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_Success() {
        String libraryPath = "path/to/directory";
        boolean result = library.loadFromDirectory(libraryPath);
        assertTrue(result);
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        String libraryPath = "path/to/empty/directory";
        boolean result = library.loadFromDirectory(libraryPath);
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectory_NotDirectory() {
        String libraryPath = "path/to/file";
        boolean result = library.loadFromDirectory(libraryPath);
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());

        // Add dictionaries to library
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);

        dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
        assertTrue(dictionaries.contains(dictionary1));
        assertTrue(dictionaries.contains(dictionary2));
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("version1"));

        // Add dictionary to library
        DictionaryInfo dictionary = new DictionaryInfo("version1");
        library.getDictionaries().add(dictionary);

        DictionaryInfo retrievedDictionary = library.getDictionary("version1");
        assertNotNull(retrievedDictionary);
        assertEquals(dictionary, retrievedDictionary);
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        String version = "1.0";
        library.loadDirectory(files, version);

        Map<String, DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        DictionaryInfo dictionary = dictionaries.get(version);
        assertNotNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("MsgContents.xml");
        files[1] = new File("Fields.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("Enums.xml");
        files[4] = new File("MsgType.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        assertEquals(new File("Fields.xml"), arrangedFiles.get(0));
        assertEquals(new File("Enums.xml"), arrangedFiles.get(1));
        assertEquals(new File("Components.xml"), arrangedFiles.get(2));
        assertEquals(new File("MsgType.xml"), arrangedFiles.get(3));
        assertEquals(new File("MsgContents.xml"), arrangedFiles.get(4));
    }

}
