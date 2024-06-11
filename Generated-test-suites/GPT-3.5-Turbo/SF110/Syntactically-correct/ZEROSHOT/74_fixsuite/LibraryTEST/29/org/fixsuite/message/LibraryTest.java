import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.Library;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_true() {
        assertTrue(library.loadFromDirectory("path/to/directory"));
    }
    
    @Test
    public void testLoadFromDirectory_false() {
        assertFalse(library.loadFromDirectory("invalid/path/to/directory"));
    }
    
    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
    }
    
    @Test
    public void testGetDictionary() {
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNull(dictionary);
    }
    
    @Test
    public void testGetDictionary_existingVersion() {
        library.loadFromDirectory("path/to/directory");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = {new File("fields.xml"), new File("enums.xml"), 
                        new File("components.xml"), new File("msgType.xml"), new File("msgContents.xml")};
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
    }
    
    // Add more test methods to cover all the methods in the Library class
    
}