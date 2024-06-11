
package org.fixsuite.message;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setup() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_validDirectory() {
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }
    
    @Test
    public void testLoadFromDirectory_invalidDirectory() {
        assertFalse(library.loadFromDirectory("invalid/directory"));
    }
    
    @Test
    public void testGetDictionaries_emptyDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionaries_nonEmptyDictionaries() {
        library.loadFromDirectory("path/to/directory");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_existingVersion() {
        library.loadFromDirectory("path/to/directory");
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertEquals("1.0", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_nonExistingVersion() {
        library.loadFromDirectory("path/to/directory");
        DictionaryInfo dictionary = library.getDictionary("2.0");
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory_validFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/file1.xml"));
        files.add(new File("path/to/file2.xml"));
        files.add(new File("path/to/file3.xml"));
        files.add(new File("path/to/file4.xml"));
        files.add(new File("path/to/file5.xml"));
        
        library.loadDirectory(files, "1.0");
        
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }
    
    @Test
    public void testLoadDirectory_invalidFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/file1.xml"));
        files.add(new File("path/to/file2.xml"));
        files.add(new File("path/to/file3.xml"));
        
        library.loadDirectory(files, "1.0");
        
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertFalse(dictionary.isLoaded());
    }
    
    @Test
    public void testArrangeFiles_validFiles() {
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
    }
    
    @Test
    public void testArrangeFiles_invalidFiles() {
        File[] files = new File[3];
        files[0] = new File("Fields.xml");
        files[1] = new File("NotEnums.xml");
        files[2] = new File("Components.xml");
        
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertNotNull(arrangedFiles);
        assertEquals(2, arrangedFiles.size());
    }
}
