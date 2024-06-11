package org.fixsuite.message;

import org.junit.Test;

import static org.junit.Assert.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        
        // Test case for loading from valid directory
        assertTrue(library.loadFromDirectory("path/to/valid/directory"));
        
        // Test case for loading from empty directory
        assertFalse(library.loadFromDirectory("path/to/empty/directory"));
        
        // Test case for loading from non-directory path
        assertFalse(library.loadFromDirectory("path/to/non-directory/file"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        
        // Test case for getting dictionaries when dictionaries is empty
        assertTrue(library.getDictionaries().isEmpty());
        
        // Test case for getting dictionaries when dictionaries is not empty
        library.loadFromDirectory("path/to/directory");
        assertFalse(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        
        // Test case for getting dictionary with existing version
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("version001"));
        
        // Test case for getting dictionary with non-existing version
        assertNull(library.getDictionary("version999"));
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        
        // Test case for loading directory with valid files
        library.loadFromDirectory("path/to/directory");
        assertEquals(1, library.getDictionaries().size());
        assertNotNull(library.getDictionary("version001"));
        
        // Test case for loading directory with invalid files
        library.loadFromDirectory("path/to/invalid/directory");
        assertEquals(0, library.getDictionaries().size());
        assertNull(library.getDictionary("version001"));
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File[] {
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml")
        };
        
        // Test case for arranging files in correct order
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
        
        // Test case for arranging files with missing file
        files = new File[] {
            new File("Fields.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml")
        };
        arrangedFiles = library.arrangeFiles(files);
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertNull(arrangedFiles.get(1));
        assertNull(arrangedFiles.get(2));
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}