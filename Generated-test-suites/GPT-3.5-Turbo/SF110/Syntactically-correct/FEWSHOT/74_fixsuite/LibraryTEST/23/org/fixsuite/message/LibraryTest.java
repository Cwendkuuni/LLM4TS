package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("path/to/directory"));
        assertFalse(library.loadFromDirectory(""));
    }

    @Test
    public void testGetDictionaries() {
        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("version"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/file1.xml"));
        files.add(new File("path/to/file2.xml"));
        
        library.loadDirectory(files, "version");
        
        assertEquals(1, library.getDictionaries().size());
        assertEquals("version", library.getDictionaries().get(0).getVersion());
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
        
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}