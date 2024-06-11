
import org.fixsuite.message.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_ValidDirectory_ReturnTrueAndLoadDictionaries() {
        String libraryPath = "path/to/valid/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertTrue(result);
        assertFalse(dictionaries.isEmpty());
        assertEquals(dictionaries.size(), 1);
        
        DictionaryInfo dictionary = dictionaries.get(0);
        assertNotNull(dictionary);
        assertEquals(dictionary.getVersion(), "version");
    }
    
    @Test
    public void testLoadFromDirectory_DirectoryIsEmpty_ReturnFalse() {
        String libraryPath = "path/to/empty/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_InvalidDirectory_ReturnFalse() {
        String libraryPath = "path/to/nonexistent/directory";
        
        boolean result = library.loadFromDirectory(libraryPath);
        
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionaries_NoDictionaries_ReturnEmptyList() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionaries_DictionariesLoaded_ReturnListOfDictionaries() {
        List<DictionaryInfo> expectedDictionaries = createDummyDictionaries();
        library = createLibraryWithDictionaries(expectedDictionaries);
        
        List<DictionaryInfo> actualDictionaries = library.getDictionaries();
        
        assertEquals(expectedDictionaries.size(), actualDictionaries.size());
        assertTrue(actualDictionaries.containsAll(expectedDictionaries));
    }
    
    @Test
    public void testGetDictionary_DictionaryExists_ReturnDictionary() {
        List<DictionaryInfo> expectedDictionaries = createDummyDictionaries();
        library = createLibraryWithDictionaries(expectedDictionaries);
        String version = "version2";
        DictionaryInfo expectedDictionary = expectedDictionaries.get(1);
        
        DictionaryInfo actualDictionary = library.getDictionary(version);
        
        assertNotNull(actualDictionary);
        assertEquals(expectedDictionary, actualDictionary);
    }
    
    @Test
    public void testGetDictionary_DictionaryDoesNotExist_ReturnNull() {
        List<DictionaryInfo> expectedDictionaries = createDummyDictionaries();
        library = createLibraryWithDictionaries(expectedDictionaries);
        String version = "nonexistentVersion";
        
        DictionaryInfo actualDictionary = library.getDictionary(version);
        
        assertNull(actualDictionary);
    }
    
    @Test
    public void testLoadDirectory_ValidFiles_LoadDictionary() {
        List<File> files = createDummyFiles();
        String version = "version";
        
        library.loadDirectory(files, version);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertEquals(dictionaries.size(), 1);
        DictionaryInfo dictionary = dictionaries.get(0);
        assertNotNull(dictionary);
        assertEquals(dictionary.getVersion(), version);
    }
    
    @Test
    public void testLoadDirectory_InvalidFiles_DoNotLoadDictionary() {
        List<File> files = createInvalidFiles();
        String version = "version";
        
        library.loadDirectory(files, version);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        assertTrue(dictionaries.isEmpty());
    }
    
    private List<DictionaryInfo> createDummyDictionaries() {
        List<DictionaryInfo> dictionaries = new ArrayList<>();
        
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        
        dictionaries.add(dictionary1);
        dictionaries.add(dictionary2);
        
        return dictionaries;
    }
    
    private Library createLibraryWithDictionaries(List<DictionaryInfo> dictionaries) {
        Library library = new Library();
        
        for (DictionaryInfo dictionary : dictionaries) {
            library.dictionaries.put(dictionary.getVersion(), dictionary);
        }
        
        return library;
    }
    
    private List<File> createDummyFiles() {
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
        
        return files;
    }
    
    private List<File> createInvalidFiles() {
        List<File> files = new ArrayList<>();
        
        File invalidFile = new File("InvalidFile.xml");
        files.add(invalidFile);
        
        return files;
    }
}
