
package org.fixsuite.message;

import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Test loading from valid directory
        assertTrue(library.loadFromDirectory("path/to/valid/directory"));
        
        // Test loading from invalid directory
        assertFalse(library.loadFromDirectory("path/to/invalid/directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Test getting dictionaries from empty library
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
        
        // Test getting dictionaries from populated library
        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Test getting dictionary with version that does not exist
        assertNull(library.getDictionary("1.0"));
        
        // Test getting dictionary with version that exists
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("1.0"));
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        
        // Test arranging files in correct order
        File[] files = new File[5];
        files[0] = new File("Fields.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Components.xml");
        files[3] = new File("MsgType.xml");
        files[4] = new File("MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(arrangedFiles.size(), 5);
        assertEquals(arrangedFiles.get(0).getName(), "Fields.xml");
        assertEquals(arrangedFiles.get(1).getName(), "Enums.xml");
        assertEquals(arrangedFiles.get(2).getName(), "Components.xml");
        assertEquals(arrangedFiles.get(3).getName(), "MsgType.xml");
        assertEquals(arrangedFiles.get(4).getName(), "MsgContents.xml");
        
        // Test arranging files with missing files
        files[0] = new File("Enums.xml");
        files[1] = new File("MsgType.xml");
        arrangedFiles = library.arrangeFiles(files);
        assertEquals(arrangedFiles.size(), 2);
        assertEquals(arrangedFiles.get(0).getName(), "Enums.xml");
        assertEquals(arrangedFiles.get(1).getName(), "MsgType.xml");
    }
}
