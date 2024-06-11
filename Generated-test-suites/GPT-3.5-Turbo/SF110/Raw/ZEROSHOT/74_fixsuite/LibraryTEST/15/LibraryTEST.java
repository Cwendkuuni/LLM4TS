package org.fixsuite.message;

import org.fixsuite.message.info.*;
import java.io.*;
import java.text.*;
import org.fixsuite.message.parsers.fpl.*;
import java.util.*;
import org.slf4j.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("path/to/directory");
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
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        files.add(new File("file1.xml"));
        files.add(new File("file2.xml"));
        library.loadDirectory(files, "version");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(arrangedFiles.size(), 5);
    }
}