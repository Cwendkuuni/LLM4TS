
package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryTest {
    private Library library;
    private List<File> testFiles;
    private String testPath;

    @Before
    public void setUp() {
        library = new Library();
        testFiles = new ArrayList<>();
        testPath = "./testFiles";
    }

    @Test
    public void testLoadFromDirectory() {
        // Test loading directory with no files
        assertTrue(library.loadFromDirectory(testPath));
        
        // Test loading directory with files
        testFiles.add(new File(testPath + "/Fields.xml"));
        testFiles.add(new File(testPath + "/Enums.xml"));
        testFiles.add(new File(testPath + "/Components.xml"));
        testFiles.add(new File(testPath + "/MsgType.xml"));
        testFiles.add(new File(testPath + "/MsgContents.xml"));
        
        MainParser.parse(library, testFiles.get(0));
        assertTrue(library.loadFromDirectory(testPath));
        
        // Test loading invalid directory
        assertFalse(library.loadFromDirectory("./invalidPath"));
    }
    
    @Test
    public void testGetDictionaries() {
        assertTrue(library.loadFromDirectory(testPath));
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        assertEquals("1.0", dictionaries.get(0).getVersion());
    }
    
    @Test
    public void testGetDictionary() {
        assertTrue(library.loadFromDirectory(testPath));
        
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
        
        dictionary = library.getDictionary("2.0");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> files = Arrays.asList(new File(testPath + "/Fields.xml"),
                                         new File(testPath + "/Enums.xml"),
                                         new File(testPath + "/Components.xml"),
                                         new File(testPath + "/MsgType.xml"),
                                         new File(testPath + "/MsgContents.xml"));
        
        library.loadDirectory(files, "1.0");
        
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
        assertEquals("1.0", dictionary.getVersion());
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File(testPath + "/MsgType.xml");
        files[1] = new File(testPath + "/Components.xml");
        files[2] = new File(testPath + "/Enums.xml");
        files[3] = new File(testPath + "/Fields.xml");
        files[4] = new File(testPath + "/MsgContents.xml");
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(files[3], arrangedFiles.get(0));
        assertEquals(files[2], arrangedFiles.get(1));
        assertEquals(files[1], arrangedFiles.get(2));
        assertEquals(files[0], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }
    
}

