package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory_ValidDirectory_ReturnsTrue() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }

    @Test
    public void testLoadFromDirectory_InvalidDirectory_ReturnsFalse() {
        Library library = new Library();
        assertFalse(library.loadFromDirectory("invalid/directory"));
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
        Library library = new Library();
        assertFalse(library.loadFromDirectory("path/to/empty/directory"));
    }

    @Test
    public void testGetDictionaries_ReturnsListOfDictionaryInfo() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionaries());
        assertTrue(library.getDictionaries().size() > 0);
        assertTrue(library.getDictionaries().get(0) instanceof DictionaryInfo);
    }

    @Test
    public void testGetDictionary_ValidVersion_ReturnsDictionaryInfo() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertNotNull(library.getDictionary("1.0"));
        assertTrue(library.getDictionary("1.0") instanceof DictionaryInfo);
        assertEquals("1.0", library.getDictionary("1.0").getVersion());
    }

    @Test
    public void testGetDictionary_InvalidVersion_ReturnsNull() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertNull(library.getDictionary("invalid"));
    }

    @Test
    public void testLoadDirectory_ValidFiles_LoadsDictionary() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        files.add(new File("path/to/Components.xml"));
        files.add(new File("path/to/MsgType.xml"));
        files.add(new File("path/to/MsgContents.xml"));
        library.loadDirectory(files, "1.0");
        assertEquals(1, library.getDictionaries().size());
        assertEquals("1.0", library.getDictionaries().get(0).getVersion());
    }

    @Test
    public void testArrangeFiles_ValidFiles_ReturnsArrangedFiles() {
        Library library = new Library();
        File[] files = new File[5];
        files[0] = new File("path/to/Fields.xml");
        files[1] = new File("path/to/Enums.xml");
        files[2] = new File("path/to/Components.xml");
        files[3] = new File("path/to/MsgType.xml");
        files[4] = new File("path/to/MsgContents.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}