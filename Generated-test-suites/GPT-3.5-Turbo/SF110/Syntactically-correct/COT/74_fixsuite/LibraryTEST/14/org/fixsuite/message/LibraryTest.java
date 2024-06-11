
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class LibraryTest {
    
    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        String libraryPath = "/path/to/library";
        
        // Test when the dictionaries map is empty and the supplied directory is not empty
        boolean result = library.loadFromDirectory(libraryPath);
        assertTrue(result);
        assertTrue(library.getDictionaries().isEmpty());
        
        // Test when the dictionaries map is not empty and the supplied directory is not empty
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.getDictionaries().add(dictionary);
        result = library.loadFromDirectory(libraryPath);
        assertTrue(result);
        assertTrue(library.getDictionaries().isEmpty());
        
        // Test when the supplied directory is empty
        libraryPath = "/path/to/empty/directory";
        result = library.loadFromDirectory(libraryPath);
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
        
        // Test when the supplied path is not a directory
        libraryPath = "/path/to/not/a/directory";
        result = library.loadFromDirectory(libraryPath);
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        DictionaryInfo dictionary1 = new DictionaryInfo("FIX.4.4");
        DictionaryInfo dictionary2 = new DictionaryInfo("FIX.5.0");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Test the size of the returned list
        assertEquals(2, dictionaries.size());
        
        // Test the contents of the returned list
        assertEquals(dictionary1, dictionaries.get(0));
        assertEquals(dictionary2, dictionaries.get(1));
        
        // Test that modifying the returned list doesn't modify the original list in the Library class
        dictionaries.clear();
        assertEquals(2, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary1 = new DictionaryInfo("FIX.4.4");
        DictionaryInfo dictionary2 = new DictionaryInfo("FIX.5.0");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);
        
        // Test retrieving an existing dictionary by version
        DictionaryInfo retrievedDictionary = library.getDictionary("FIX.4.4");
        assertEquals(dictionary1, retrievedDictionary);
        
        // Test retrieving a non-existing dictionary
        retrievedDictionary = library.getDictionary("FIX.4.2");
        assertNull(retrievedDictionary);
    }
    
    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        String version = "FIX.4.4";
        List<File> files = new ArrayList<>();
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        File file3 = new File("Components.xml");
        File file4 = new File("MsgType.xml");
        File file5 = new File("MsgContents.xml");
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
        
        // Test loading a valid directory with all the required files
        library.loadDirectory(files, version);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        DictionaryInfo dictionary = dictionaries.get(0);
        assertEquals(version, dictionary.getVersion());
        // Test that the files are correctly parsed
        assertTrue(dictionary.isLoaded());
        
        // Test loading a directory with missing files
        files.remove(file1);
        library.loadDirectory(files, version);
        dictionaries = library.getDictionaries();
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File file1 = new File("Fields.xml");
        File file2 = new File("Enums.xml");
        File file3 = new File("Components.xml");
        File file4 = new File("MsgType.xml");
        File file5 = new File("MsgContents.xml");
        File[] files = {file1, file2, file3, file4, file5};
        
        // Test arranging the files in the correct order
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(file1, arrangedFiles.get(0));
        assertEquals(file2, arrangedFiles.get(1));
        assertEquals(file3, arrangedFiles.get(2));
        assertEquals(file4, arrangedFiles.get(3));
        assertEquals(file5, arrangedFiles.get(4));
        
        // Test arranging the files when they are not in the correct order
        File[] shuffledFiles = {file5, file4, file3, file2, file1};
        arrangedFiles = library.arrangeFiles(shuffledFiles);
        assertEquals(file1, arrangedFiles.get(0));
        assertEquals(file2, arrangedFiles.get(1));
        assertEquals(file3, arrangedFiles.get(2));
        assertEquals(file4, arrangedFiles.get(3));
        assertEquals(file5, arrangedFiles.get(4));
    }
}
