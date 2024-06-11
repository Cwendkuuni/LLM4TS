package org.fixsuite.message;

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
    public void testLoadFromDirectory_ValidDirectory_ReturnsTrue() {
        // Arrange
        String libraryPath = "path/to/valid/directory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
        // Arrange
        String libraryPath = "path/to/empty/directory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectory_InvalidDirectory_ReturnsFalse() {
        // Arrange
        String libraryPath = "path/to/invalid/directory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries_ReturnsListOfDictionaries() {
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        assertNotNull(dictionaries);
    }

    @Test
    public void testGetDictionary_ExistingVersion_ReturnsDictionary() {
        // Arrange
        String version = "1.0";

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }

    @Test
    public void testGetDictionary_NonExistingVersion_ReturnsNull() {
        // Arrange
        String version = "2.0";

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory_ValidFiles_LoadsDictionary() {
        // Arrange
        String version = "1.0";
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/Fields.xml"));
        files.add(new File("path/to/Enums.xml"));
        files.add(new File("path/to/Components.xml"));
        files.add(new File("path/to/MsgType.xml"));
        files.add(new File("path/to/MsgContents.xml"));

        // Act
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testLoadDirectory_InvalidFiles_DoesNotLoadDictionary() {
        // Arrange
        String version = "1.0";
        List<File> files = new ArrayList<>();
        files.add(new File("path/to/Fields.xml")); // Valid file
        files.add(new File("path/to/Invalid.xml")); // Invalid file

        // Act
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNull(dictionary);
    }

    @Test
    public void testArrangeFiles_ValidFiles_ReturnsArrangedFiles() {
        // Arrange
        File[] files = {
            new File("path/to/MsgType.xml"),
            new File("path/to/Enums.xml"),
            new File("path/to/Components.xml"),
            new File("path/to/Fields.xml"),
            new File("path/to/MsgContents.xml")
        };

        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);

        // Assert
        assertNotNull(arrangedFiles);
        assertEquals(5, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Enums.xml", arrangedFiles.get(1).getName());
        assertEquals("Components.xml", arrangedFiles.get(2).getName());
        assertEquals("MsgType.xml", arrangedFiles.get(3).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(4).getName());
    }

    @Test
    public void testArrangeFiles_MissingFiles_ReturnsPartialArrangement() {
        // Arrange
        File[] files = {
            new File("path/to/Components.xml"),
            new File("path/to/Fields.xml"),
            new File("path/to/MsgContents.xml")
        };

        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);

        // Assert
        assertNotNull(arrangedFiles);
        assertEquals(3, arrangedFiles.size());
        assertEquals("Fields.xml", arrangedFiles.get(0).getName());
        assertEquals("Components.xml", arrangedFiles.get(1).getName());
        assertEquals("MsgContents.xml", arrangedFiles.get(2).getName());
    }

}