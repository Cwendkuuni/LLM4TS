import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("testDirectory"));
        assertFalse(library.loadFromDirectory("invalidDirectory"));
    }

    @Test
    public void testGetDictionaries() {
        assertNotNull(library.getDictionaries());
        assertEquals(0, library.getDictionaries().size());

        library.loadFromDirectory("testDirectory");
        assertEquals(2, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("2017"));
        assertNull(library.getDictionary("2018"));

        library.loadFromDirectory("testDirectory");
        assertNotNull(library.getDictionary("2017"));
        assertNotNull(library.getDictionary("2018"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        files.add(new File("testFields.xml"));
        files.add(new File("testEnums.xml"));
        files.add(new File("testComponents.xml"));
        files.add(new File("testMsgType.xml"));
        files.add(new File("testMsgContents.xml"));

        library.loadDirectory(files, "2017");
        assertNotNull(library.getDictionary("2017"));
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        files[0] = new File("MsgContents.xml");
        files[1] = new File("Enums.xml");
        files[2] = new File("Fields.xml");
        files[3] = new File("Components.xml");
        files[4] = new File("MsgType.xml");

        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
}