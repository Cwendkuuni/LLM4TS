import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

public class LibraryTest {

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertNotNull(library.getDictionaries());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertNull(library.getDictionary("NonExistentVersion"));
    }

    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<File>();
        String version = "TestVersion";
        library.loadDirectory(files, version);
        assertNotNull(library.getDictionary(version));
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = {
                new File("Fields.xml"),
                new File("Enums.xml"),
                new File("Components.xml"),
                new File("MsgType.xml"),
                new File("MsgContents.xml")
        };
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
    }
}