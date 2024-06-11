package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory(null));
        assertFalse(library.loadFromDirectory(""));
        File resourcesDirectory = new File("src/test/resources");
        assertTrue(library.loadFromDirectory(resourcesDirectory.getAbsolutePath()));
    }

    @Test
    public void testGetDictionaries() {
        assertNotNull(library.getDictionaries());
        assertTrue(library.getDictionaries().isEmpty());
        File resourcesDirectory = new File("src/test/resources");
        library.loadFromDirectory(resourcesDirectory.getAbsolutePath());

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary(null));
        assertNull(library.getDictionary(""));
        File resourcesDirectory = new File("src/test/resources");
        library.loadFromDirectory(resourcesDirectory.getAbsolutePath());
        assertNotNull(library.getDictionary("FIX.4.1"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("src/test/resources/Fields.xml"));
        files.add(new File("src/test/resources/Enums.xml"));
        files.add(new File("src/test/resources/Components.xml"));
        files.add(new File("src/test/resources/MsgType.xml"));
        files.add(new File("src/test/resources/MsgContents.xml"));
        library.loadDirectory(files, "SampleVersion");
        DictionaryInfo dictionary = library.getDictionary("SampleVersion");
        assertNotNull(dictionary);
        assertFalse(dictionary.getFields().isEmpty());
        assertFalse(dictionary.getEnums().isEmpty());
        assertFalse(dictionary.getComponents().isEmpty());
        assertFalse(dictionary.getMsgTypes().isEmpty());
        assertFalse(dictionary.getMsgContents().isEmpty());
    }

    @Test
    public void testArrangeFiles() {
        File resourcesDirectory = new File("src/test/resources");
        File[] files = resourcesDirectory.listFiles();
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
    }
}