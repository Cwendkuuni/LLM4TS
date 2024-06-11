package org.fixsuite.message;

import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {
    private static final String TEST_DIRECTORY = "testDirectory";
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        // Test case when dictionaries are already loaded
        library.getDictionaries().add(new DictionaryInfo("v1.0"));
        Assert.assertFalse(library.loadFromDirectory(TEST_DIRECTORY));

        // Test case when path is not a directory
        Assert.assertFalse(library.loadFromDirectory("notADirectory"));

        // Test case when directory is empty
        Assert.assertFalse(library.loadFromDirectory(TEST_DIRECTORY));

        // Test case when successfully loading dictionaries
        Assert.assertTrue(library.loadFromDirectory(TEST_DIRECTORY));
    }

    @Test
    public void testGetDictionaries() {
        // Test case when no dictionaries are loaded
        Assert.assertTrue(library.getDictionaries().isEmpty());

        // Test case when dictionaries are loaded
        library.getDictionaries().add(new DictionaryInfo("v1.0"));
        Assert.assertEquals(1, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary() {
        // Test case when dictionary does not exist
        Assert.assertNull(library.getDictionary("v1.0"));

        // Test case when dictionary exists
        DictionaryInfo dictionaryInfo = new DictionaryInfo("v1.0");
        library.getDictionaries().add(dictionaryInfo);
        Assert.assertEquals(dictionaryInfo, library.getDictionary("v1.0"));
    }

    @Test
    public void testLoadDirectory() {
        // Test case when dictionary is loaded successfully
        List<File> files = new ArrayList<>();
        files.add(new File("file1.xml"));
        files.add(new File("file2.xml"));
        library.loadDirectory(files, "v1.0");
        Assert.assertEquals(1, library.getDictionaries().size());

        // Test case when dictionary fails to load
        files = new ArrayList<>();
        files.add(new File("invalidFile.xml"));
        library.loadDirectory(files, "v1.0");
        Assert.assertEquals(1, library.getDictionaries().size());
    }

    @Test
    public void testArrangeFiles() {
        // Test case when files are arranged correctly
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        Assert.assertEquals(5, arrangedFiles.size());
        for (int i = 0; i < arrangedFiles.size(); i++) {
            Assert.assertEquals(files[i], arrangedFiles.get(i));
        }

        // Test case when files are not arranged correctly
        files = new File[2];
        files[0] = new File("Invalid1.xml");
        files[1] = new File("Invalid2.xml");
        arrangedFiles = library.arrangeFiles(files);
        Assert.assertNotEquals(5, arrangedFiles.size());
    }
}