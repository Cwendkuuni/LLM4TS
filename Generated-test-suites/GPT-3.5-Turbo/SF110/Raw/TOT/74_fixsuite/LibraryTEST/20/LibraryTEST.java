import static org.junit.Assert.*;
import org.junit.*;
import org.fixsuite.message.*;

public class LibraryTest {

    Library library;

    @Before
    public void setup() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory() {
        // Arrange
        String libraryPath = "./emptyDirectory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectory_notDirectory() {
        // Arrange
        String libraryPath = "./notDirectory.xml";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectory_validDirectory() {
        // Arrange
        String libraryPath = "./validDirectory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertTrue(result);

        // Additional assertions can be added to check if dictionaries are correctly loaded
    }

    @Test
    public void testGetDictionaries() {
        // Arrange
        // Load dictionaries before testing

        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        assertNotNull(dictionaries);
        // Additional assertions can be added to check if the correct number of dictionaries are returned
    }

    @Test
    public void testGetDictionary_existingVersion() {
        // Arrange
        // Load dictionaries before testing
        String version = "FIX.4.4";

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNotNull(dictionary);
    }

    @Test
    public void testGetDictionary_nonExistingVersion() {
        // Arrange
        // Load dictionaries before testing
        String version = "NON_EXISTING_VERSION";

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNull(dictionary);
    }

    // Additional test methods can be added to test the remaining methods of Library class

}