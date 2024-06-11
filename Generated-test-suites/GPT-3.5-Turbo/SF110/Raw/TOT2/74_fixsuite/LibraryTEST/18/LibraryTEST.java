import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryTest {

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        assertNotNull(library);
    }
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        assertTrue(library.loadFromDirectory("example/path"));
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        assertNull(library.getDictionary("nonexistent"));
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        library.loadDirectory(new ArrayList<File>(), "version");
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        files.add(new File("Components.xml"));
        files.add(new File("MsgType.xml"));
        files.add(new File("MsgContents.xml"));
        
        List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        
        assertEquals(5, arrangedFiles.size());
    }
}