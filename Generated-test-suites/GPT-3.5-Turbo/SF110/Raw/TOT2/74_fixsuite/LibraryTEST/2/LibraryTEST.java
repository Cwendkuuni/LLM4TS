import static org.junit.Assert.*;
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
        assertTrue(library.loadFromDirectory("testDirectory"));
    }

    @Test
    public void testGetDictionaries() {
        assertNotNull(library.getDictionaries());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("NonExistentVersion"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> testFiles = new ArrayList<>();
        testFiles.add(new File("Fields.xml"));
        library.loadDirectory(testFiles, "TestVersion");
        assertNotNull(library.getDictionary("TestVersion"));
    }

    @Test
    public void testArrangeFiles() {
        List<File> testFiles = new ArrayList<>();
        testFiles.add(new File("Fields.xml"));
        testFiles.add(new File("MsgType.xml"));
        testFiles.add(new File("WrongFile.xml"));
        List<File> arrangedFiles = library.arrangeFiles(testFiles.toArray(new File[testFiles.size()]));
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(1).getName());
        assertEquals(2, arrangedFiles.size());
    }
}