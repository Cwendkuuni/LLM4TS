import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.info.*;
import java.io.*;
import java.util.*;
import org.slf4j.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_WhenDictionariesIsEmpty_ShouldReturnTrue() {
        String libraryPath = "test/path";
        boolean result = library.loadFromDirectory(libraryPath);
        assertTrue(result);
    }
    
    @Test
    public void testLoadFromDirectory_WhenDictionariesIsNotEmpty_ShouldClearDictionaries() {
        // Arrange
        String libraryPath = "test/path";
        library.loadFromDirectory(libraryPath);
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertTrue(result);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_WhenPathIsNotDirectory_ShouldReturnFalse() {
        String libraryPath = "test/file.txt";
        boolean result = library.loadFromDirectory(libraryPath);
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_WhenDirectoryIsEmpty_ShouldReturnFalse() {
        // Arrange
        String libraryPath = "test/empty-directory";
        File directory = new File(libraryPath);
        directory.mkdirs();
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testLoadFromDirectory_WhenValidDirectory_ShouldLoadDictionaries() {
        // Arrange
        String libraryPath = "test/valid-directory";
        File directory = new File(libraryPath);
        directory.mkdirs();
        File fieldsFile = new File(directory, "Fields.xml");
        File enumsFile = new File(directory, "Enums.xml");
        File componentsFile = new File(directory, "Components.xml");
        File msgTypeFile = new File(directory, "MsgType.xml");
        File msgContentsFile = new File(directory, "MsgContents.xml");
        List<File> files = Arrays.asList(fieldsFile, enumsFile, componentsFile, msgTypeFile, msgContentsFile);
        library.loadFromDirectory(libraryPath);
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertTrue(result);
        assertEquals(files.size(), library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionaries_ShouldReturnDictionaries() {
        // Arrange
        String libraryPath = "test/path";
        library.loadFromDirectory(libraryPath);
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertNotNull(dictionaries);
        assertEquals(library.getDictionaries().size(), dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_WhenDictionaryExists_ShouldReturnDictionaryInfo() {
        // Arrange
        String libraryPath = "test/path";
        library.loadFromDirectory(libraryPath);
        String version = library.getDictionaries().get(0).getVersion();
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNotNull(dictionary);
    }
    
    @Test
    public void testGetDictionary_WhenDictionaryDoesNotExist_ShouldReturnNull() {
        // Arrange
        String libraryPath = "test/path";
        library.loadFromDirectory(libraryPath);
        String version = "nonexistent";
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory_WhenDirectoryContainsFiles_ShouldLoadDictionary() {
        // Arrange
        String version = "test-version";
        File file1 = new File("test/path/file1.xml");
        File file2 = new File("test/path/file2.xml");
        List<File> files = Arrays.asList(file1, file2);
        
        // Act
        library.loadDirectory(files, version);
        
        // Assert
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        DictionaryInfo dictionary = dictionaries.get(0);
        assertTrue(dictionary.isLoaded());
        assertEquals(version, dictionary.getVersion());
    }
    
    @Test
    public void testArrangeFiles_WhenFilesAreInOrder_ShouldReturnOrderedFilesList() {
        // Arrange
        File file1 = new File("test/path/Fields.xml");
        File file2 = new File("test/path/Enums.xml");
        File file3 = new File("test/path/Components.xml");
        File file4 = new File("test/path/MsgType.xml");
        File file5 = new File("test/path/MsgContents.xml");
        File[] files = {file1, file2, file3, file4, file5};
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertEquals(Arrays.asList(file1, file2, file3, file4, file5), arrangedFiles);
    }
    
    @Test
    public void testArrangeFiles_WhenFilesAreNotInOrder_ShouldReturnOrderedFilesList() {
        // Arrange
        File file1 = new File("test/path/MsgType.xml");
        File file2 = new File("test/path/Components.xml");
        File file3 = new File("test/path/Fields.xml");
        File file4 = new File("test/path/MsgContents.xml");
        File file5 = new File("test/path/Enums.xml");
        File[] files = {file1, file2, file3, file4, file5};
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertEquals(Arrays.asList(file3, file5, file2, file1, file4), arrangedFiles);
    }
}