package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;
import org.fixsuite.message.info.*;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        String emptyDirectory = "path/to/emptyDirectory";
        assertFalse(library.loadFromDirectory(emptyDirectory));
        
        String nonDirectoryPath = "path/to/file.txt";
        assertFalse(library.loadFromDirectory(nonDirectoryPath));
        
        String validDirectory = "path/to/directory";
        assertTrue(library.loadFromDirectory(validDirectory));
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
        
        String validDirectory = "path/to/directory";
        library.loadFromDirectory(validDirectory);
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version1");
        assertNull(dictionary);
        
        String validDirectory = "path/to/directory";
        library.loadFromDirectory(validDirectory);
        dictionary = library.getDictionary("version1");
        assertNotNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        List<File> emptyFiles = new ArrayList<>();
        String version = "version1";
        library.loadDirectory(emptyFiles, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
        
        List<File> validFiles = new ArrayList<>();
        validFiles.add(new File("Fields.xml"));
        validFiles.add(new File("Enums.xml"));
        validFiles.add(new File("Components.xml"));
        validFiles.add(new File("MsgType.xml"));
        validFiles.add(new File("MsgContents.xml"));
        library.loadDirectory(validFiles, version);
        dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }
    
    @Test
    public void testArrangeFiles() {
        File[] emptyFiles = new File[0];
        List<File> arrangedFiles = library.arrangeFiles(emptyFiles);
        assertNotNull(arrangedFiles);
        assertEquals(0, arrangedFiles.size());
        
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
        assertEquals(files[0], arrangedFiles.get(0));
        assertEquals(files[1], arrangedFiles.get(1));
        assertEquals(files[2], arrangedFiles.get(2));
        assertEquals(files[3], arrangedFiles.get(3));
        assertEquals(files[4], arrangedFiles.get(4));
    }
}