import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        // typical use case: directory exists and contains files
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/directory"));

        // edge case: directory does not exist
        Library library2 = new Library();
        assertFalse(library2.loadFromDirectory("non/existent/directory"));

        // edge case: directory is empty
        Library library3 = new Library();
        assertFalse(library3.loadFromDirectory("empty/directory"));

        // error scenario: supplied path is not a directory
        Library library4 = new Library();
        assertFalse(library4.loadFromDirectory("path/to/file.txt"));
    }

    @Test
    public void testGetDictionaries() {
        // typical use case: dictionaries exist
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");
        assertEquals(5, library.getDictionaries().size());

        // edge case: no dictionaries loaded
        Library library2 = new Library();
        assertTrue(library2.getDictionaries().isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("path/to/directory");

        // typical use case: dictionary exists
        assertNotNull(library.getDictionary("version1"));

        // edge case: invalid version
        assertNull(library.getDictionary("invalid_version"));
    }

    // Test private methods separately

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = {new File("Fields.xml"),
                        new File("Enums.xml"),
                        new File("Components.xml"),
                        new File("MsgType.xml"),
                        new File("MsgContents.xml")};
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // typical use case: files are arranged correctly
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());

        // edge case: files not arranged correctly
        File[] files2 = {new File("Enums.xml"),
                        new File("MsgType.xml"),
                        new File("Fields.xml"),
                        new File("Components.xml"),
                        new File("MsgContents.xml")};
        List<File> arrangedFiles2 = library.arrangeFiles(files2);
        assertEquals(5, arrangedFiles2.size());
        assertEquals("Fields.xml", arrangedFiles2.get(2).getName());
        assertEquals("Enums.xml", arrangedFiles2.get(0).getName());
        assertEquals("Components.xml", arrangedFiles2.get(3).getName());
        assertEquals("MsgType.xml", arrangedFiles2.get(1).getName());
        assertEquals("MsgContents.xml", arrangedFiles2.get(4).getName());
    }
}