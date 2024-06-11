package org.fixsuite.message;

import org.fixsuite.message.info.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @After
    public void tearDown() {
        library = null;
    }

    @Test
    public void testLoadFromDirectory() {
        // Test when directory is empty
        assertFalse(library.loadFromDirectory(""));
        
        // Test when directory is not a directory
        assertFalse(library.loadFromDirectory("path/to/file"));
        
        // Test when dictionary is loaded successfully
        assertTrue(library.loadFromDirectory("path/to/directory"));

        // Additional test cases can be added to verify different scenarios
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());

        // Test when dictionaries list is not empty
        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());

        // Additional test cases can be added for different scenarios
    }

    @Test
    public void testGetDictionary() {
        // Test when version is not present in dictionaries
        assertNull(library.getDictionary("version"));
        
        // Test when version is present in dictionaries
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("version"));

        // Additional test cases can be added for different scenarios
    }

    @Test
    public void testLoadDirectory() {
        // Create temporary directory for testing

        // Test when files list is empty
        List<File> files = new ArrayList<>();
        library.loadDirectory(files, "version");
        assertEquals(0, library.getDictionaries().size());

        // Test when files list is not empty
        files.add(new File("path/to/file"));
        library.loadDirectory(files, "version");
        assertEquals(1, library.getDictionaries().size());

        // Additional test cases can be added for different scenarios
    }

    @Test
    public void testArrangeFiles() {
        // Test when files array is empty
        File[] files = new File[0];
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(0, arrangedFiles.size());

        // Test when files array contains files in random order
        files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("MsgContents.xml");
        files[2] = new File("Enums.xml");
        files[3] = new File("Components.xml");
        files[4] = new File("MsgType.xml");
        arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());

        // Additional test cases can be added for different scenarios
    }
}