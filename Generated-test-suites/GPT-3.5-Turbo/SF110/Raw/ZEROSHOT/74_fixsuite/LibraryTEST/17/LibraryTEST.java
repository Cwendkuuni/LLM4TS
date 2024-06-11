import org.junit.*;
import java.io.*;
import java.util.*;
import org.slf4j.*;

public class LibraryTest {    

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory("testPath");
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        Assert.assertNotNull(dictionaries);
        Assert.assertEquals(0, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("version");
        Assert.assertNull(dictionary);
    }

}