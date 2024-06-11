package org.fixsuite.message;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() throws IOException {
        // Test when dictionaries are loaded successfully
        assertTrue(library.loadFromDirectory("path/to/directory"));

        // Test when dictionaries are not loaded successfully
        assertFalse(library.loadFromDirectory("path/to/invalid/directory"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = new ArrayList<>();
        assertEquals(dictionaries, library.getDictionaries());
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);

        // Add a dictionary to the library
        DictionaryInfo newDictionary = new DictionaryInfo("version");
        library.dictionaries.put("version", newDictionary);

        DictionaryInfo retrievedDictionary = library.getDictionary("version");
        assertEquals(newDictionary, retrievedDictionary);
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));

        // Test with additional files
        files = new File[6];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        files[5] = new File("AdditionalFile.xml");

        arrangedFiles = library.arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));

        // Test when dictionary is loaded successfully
        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());

        // Test when dictionary is not loaded successfully
        files.remove(0);
        library.loadDirectory(files, "invalid_version");
        DictionaryInfo invalidDictionary = library.getDictionary("invalid_version");
        assertNull(invalidDictionary);
    }
}