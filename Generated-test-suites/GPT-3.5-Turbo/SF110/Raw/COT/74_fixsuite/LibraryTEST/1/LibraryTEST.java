import org.junit.*;
import java.io.*;
import java.util.*;
import org.slf4j.*;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setup() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        // Test loading from a valid directory
        boolean result = library.loadFromDirectory("path/to/valid/directory");
        Assert.assertTrue(result);
        
        // Test loading from an empty directory
        result = library.loadFromDirectory("path/to/empty/directory");
        Assert.assertFalse(result);
        
        // Test loading from a non-existent directory
        result = library.loadFromDirectory("path/to/nonexistent/directory");
        Assert.assertFalse(result);
        
        // Test loading from a file instead of a directory
        result = library.loadFromDirectory("path/to/file.txt");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetDictionaries() {
        // Prepare test data
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);
        
        // Test getting all dictionaries
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertEquals(2, dictionaries.size());
        Assert.assertEquals(dictionary1, dictionaries.get(0));
        Assert.assertEquals(dictionary2, dictionaries.get(1));
    }
    
    @Test
    public void testGetDictionary() {
        // Prepare test data
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);
        
        // Test getting a specific dictionary by version
        DictionaryInfo result = library.getDictionary("version1");
        Assert.assertEquals(dictionary1, result);
        
        result = library.getDictionary("version2");
        Assert.assertEquals(dictionary2, result);
        
        result = library.getDictionary("version3");
        Assert.assertNull(result);
    }
}