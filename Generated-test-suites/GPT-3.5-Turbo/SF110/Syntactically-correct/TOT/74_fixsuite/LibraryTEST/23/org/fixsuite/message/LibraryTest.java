import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory("nonexistent/directory"));
        
        assertTrue(library.loadFromDirectory("valid/directory"));
        
        // Test with an empty directory
        assertTrue(library.loadFromDirectory("empty/directory"));
        
        // Test with a directory containing only subdirectories
        assertTrue(library.loadFromDirectory("subdirectories/directory"));
    }

    @Test
    public void testGetDictionaries() {
        assertEquals(0, library.getDictionaries().size());
        
        // Test with loaded dictionaries
        library.loadFromDirectory("valid/directory");
        assertEquals(5, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("invalid/version"));
        
        // Test with existing dictionary
        library.loadFromDirectory("valid/directory");
        assertNotNull(library.getDictionary("1.0"));
    }

    @Test
    public void testLoadDirectory() {
        // Test with empty file list
        library.loadDirectory(new ArrayList<File>(), "1.0");
        assertNull(library.getDictionary("1.0"));
        
        // Test with loaded files
        List<File> files = new ArrayList<>();
        files.add(new File("valid/File1.xml"));
        files.add(new File("valid/File2.xml"));
        library.loadDirectory(files, "1.0");
        assertNotNull(library.getDictionary("1.0"));
        assertEquals(2, library.getDictionary("1.0").getFiles().size());
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
        
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}