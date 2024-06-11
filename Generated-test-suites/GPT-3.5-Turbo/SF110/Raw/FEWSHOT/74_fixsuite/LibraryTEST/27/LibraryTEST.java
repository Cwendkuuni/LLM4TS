import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.Library;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
        // Arrange
        String libraryPath = "testData/emptyDirectory";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_DirectoryNotExists_ReturnsFalse() {
        // Arrange
        String libraryPath = "testData/nonExistentDirectory";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_ValidDirectory_ReturnsTrue() {
        // Arrange
        String libraryPath = "testData/validDirectory";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertTrue(result);
        assertEquals(2, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionaries_NoDictionaries_ReturnsEmptyList() {
        // Arrange
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());
    }
    
    @Test
    public void testGetDictionaries_WithDictionaries_ReturnsCorrectListSize() {
        // Arrange
        library.loadFromDirectory("testData/validDirectory");
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertNotNull(dictionaries);
        assertEquals(2, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_ExistingVersion_ReturnsCorrectDictionary() {
        // Arrange
        library.loadFromDirectory("testData/validDirectory");
        String version = "FIX.4.4";
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_NonExistingVersion_ReturnsNull() {
        // Arrange
        library.loadFromDirectory("testData/validDirectory");
        String version = "FIX.5.0";
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNull(dictionary);
    }
    
}