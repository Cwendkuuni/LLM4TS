import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.*;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("testDirectory"));
        assertFalse(library.loadFromDirectory("emptyDirectory"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        DictionaryInfo info = new DictionaryInfo("Version1");
        library.loadDirectory(Collections.singletonList(new File("testFile.xml")), "Version1");
        assertEquals(info, library.getDictionary("Version1"));
        assertNull(library.getDictionary("NonExistentVersion"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));
        library.loadDirectory(files, "Version2");
        assertNotNull(library.getDictionary("Version2"));
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("MsgType.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("MsgContents.xml");
        files[3] = new File("Fields.xml");
        files[4] = new File("Components.xml");
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}