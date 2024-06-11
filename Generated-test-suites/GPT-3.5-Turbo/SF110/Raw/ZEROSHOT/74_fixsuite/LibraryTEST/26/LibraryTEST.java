package org.fixsuite.message;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Assert;
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
    public void testLoadFromDirectory() {
        boolean result = library.loadFromDirectory("testDirectory");
        
        Assert.assertTrue(result);
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        Assert.assertFalse(dictionaries.isEmpty());
        
        for (DictionaryInfo dictionary : dictionaries) {
            Assert.assertTrue(dictionary.isLoaded());
        }
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        Assert.assertNotNull(dictionaries);
        Assert.assertTrue(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        
        Assert.assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        
        library.loadDirectory(files, "version");
        
        DictionaryInfo dictionary = library.getDictionary("version");
        
        Assert.assertNotNull(dictionary);
        Assert.assertTrue(dictionary.isLoaded());     
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
        
        Assert.assertEquals(5, arrangedFiles.size());
        Assert.assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        Assert.assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        Assert.assertEquals("Components.xml", arrangedFiles.get(2).getName());
        Assert.assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        Assert.assertEquals("MsgContents.xml", arrangedFiles.get(4).getName()); 
    }
}