import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_SuccessfulLoad() {
        // Arrange
        String libraryPath = "C:/testLibrary";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertTrue(result);
        assertEquals(1, library.getDictionaries().size());
        assertNotNull(library.getDictionary("1.0.0"));
    }
    
    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        // Arrange
        String libraryPath = "C:/emptyLibrary";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_InvalidPath() {
        // Arrange
        String libraryPath = "C:/invalidPath";
        
        // Act
        boolean result = library.loadFromDirectory(libraryPath);
        
        // Assert
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }
    
    @Test
    public void testGetDictionaries() {
        // Arrange
        String libraryPath = "C:/testLibrary";
        library.loadFromDirectory(libraryPath);
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertEquals(1, dictionaries.size());
    }
    
    @Test
    public void testGetDictionary_ExistingVersion() {
        // Arrange
        String libraryPath = "C:/testLibrary";
        library.loadFromDirectory(libraryPath);
        
        // Act
        DictionaryInfo dictionary = library.getDictionary("1.0.0");
        
        // Assert
        assertNotNull(dictionary);
        assertEquals("1.0.0", dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_NonExistingVersion() {
        // Arrange
        String libraryPath = "C:/testLibrary";
        library.loadFromDirectory(libraryPath);
        
        // Act
        DictionaryInfo dictionary = library.getDictionary("2.0.0");
        
        // Assert
        assertNull(dictionary);
    }
    
    @Test
    public void testLoadDirectory_SuccessfulLoad() {
        // Arrange
        List<File> files = new ArrayList<>();
        files.add(new File("C:/testLibrary/Fields.xml"));
        files.add(new File("C:/testLibrary/Enums.xml"));
        files.add(new File("C:/testLibrary/Components.xml"));
        files.add(new File("C:/testLibrary/MsgType.xml"));
        files.add(new File("C:/testLibrary/MsgContents.xml"));
        String version = "1.0.0";
        
        // Act
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }
    
    @Test
    public void testLoadDirectory_UnsuccessfulLoad() {
        // Arrange
        List<File> files = new ArrayList<>();
        files.add(new File("C:/testLibrary/Fields.xml"));
        files.add(new File("C:/testLibrary/Enums.xml"));
        files.add(new File("C:/testLibrary/Components.xml"));
        files.add(new File("C:/testLibrary/MsgType.xml"));
        String version = "1.0.0";
        
        // Act
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNotNull(dictionary);
        assertFalse(dictionary.isLoaded());
    }
    
    @Test
    public void testArrangeFiles_ValidFiles() {
        // Arrange
        File[] files = new File[5];
        files[0] = new File("C:/testLibrary/Fields.xml");
        files[1] = new File("C:/testLibrary/Enums.xml");
        files[2] = new File("C:/testLibrary/Components.xml");
        files[3] = new File("C:/testLibrary/MsgType.xml");
        files[4] = new File("C:/testLibrary/MsgContents.xml");
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }
    
    @Test
    public void testArrangeFiles_MissingFiles() {
        // Arrange
        File[] files = new File[3];
        files[0] = new File("C:/testLibrary/Fields.xml");
        files[1] = new File("C:/testLibrary/Components.xml");
        files[2] = new File("C:/testLibrary/MsgContents.xml");
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertEquals(3, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Components.xml", arrangedFiles.get(1).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(2).getName());
    }
   
}