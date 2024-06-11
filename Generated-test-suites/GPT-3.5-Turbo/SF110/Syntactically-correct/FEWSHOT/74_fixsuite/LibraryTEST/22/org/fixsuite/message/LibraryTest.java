import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory(null));
        assertFalse(library.loadFromDirectory("invalid/path"));
    }

    @Test
    public void testGetDictionaries() {
        assertNotNull(library.getDictionaries());
        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("1.0"));
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = library.arrangeFiles(null);
        assertNotNull(files);
        assertTrue(files.isEmpty());

        files = library.arrangeFiles(new File[0]);
        assertNotNull(files);
        assertTrue(files.isEmpty());

        File[] inputFiles = new File[] {new File("Enums.xml"), new File("Fields.xml")};
        files = library.arrangeFiles(inputFiles);
        assertNotNull(files);
        assertEquals(2, files.size());
        assertEquals("Fields.xml", files.get(0).getName());
        assertEquals("Enums.xml", files.get(1).getName());
    }
}