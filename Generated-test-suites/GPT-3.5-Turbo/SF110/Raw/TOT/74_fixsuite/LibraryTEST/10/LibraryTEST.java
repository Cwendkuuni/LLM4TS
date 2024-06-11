package org.fixsuite.message;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory_WithValidDirectory_ReturnsTrue() {
        // Arrange
        String libraryPath = "path/to/valid/directory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testLoadFromDirectory_WithEmptyDirectory_ReturnsFalse() {
        // Arrange
        String libraryPath = "path/to/empty/directory";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testLoadFromDirectory_WithNonDirectoryPath_ReturnsFalse() {
        // Arrange
        String libraryPath = "path/to/non/directory/file.xml";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testGetDictionaries_ReturnsListWithCorrectSize() {
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        Assert.assertEquals(0, dictionaries.size());

        // Arrange
        library.loadFromDirectory("path/to/valid/directory");

        // Act
        dictionaries = library.getDictionaries();

        // Assert
        Assert.assertEquals(1, dictionaries.size());
    }

    @Test
    public void testGetDictionary_WithValidVersion_ReturnsCorrectDictionary() {
        // Arrange
        String version = "1.0";
        library.loadFromDirectory("path/to/valid/directory");

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(version, dictionary.getVersion());
    }

    @Test
    public void testGetDictionary_WithInvalidVersion_ReturnsNull() {
        // Arrange
        String version = "2.0";
        library.loadFromDirectory("path/to/valid/directory");

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        Assert.assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory_WithValidFiles_LoadsDictionary() {
        // Arrange
        String version = "1.0";
        List<File> files = createValidFilesList();

        // Act
        library.loadDirectory(files, version);

        // Assert
        DictionaryInfo dictionary = library.getDictionary(version);
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(files.size(), dictionary.getFiles().size());
    }

    @Test
    public void testLoadDirectory_WithInvalidFiles_DoesNotLoadDictionary() {
        // Arrange
        String version = "1.0";
        List<File> files = createInvalidFilesList();

        // Act
        library.loadDirectory(files, version);

        // Assert
        DictionaryInfo dictionary = library.getDictionary(version);
        Assert.assertNull(dictionary);
    }

    @Test
    public void testArrangeFiles_WithValidFiles_ReturnsArrangedFiles() {
        // Arrange
        File[] files = createValidFilesArray();

        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);

        // Assert
        Assert.assertEquals(files.length, arrangedFiles.size());

        for (int i = 0; i < files.length; i++) {
            File file = arrangedFiles.get(i);
            Assert.assertEquals(i + 1, getKeyFromFileName(file.getName()));
        }
    }

    @Test
    public void testArrangeFiles_WithInvalidFiles_ReturnsEmptyList() {
        // Arrange
        File[] files = createInvalidFilesArray();

        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);

        // Assert
        Assert.assertEquals(0, arrangedFiles.size());
    }

    private int getKeyFromFileName(String fileName) {
        switch (fileName) {
            case "Fields.xml":
                return 1;
            case "Enums.xml":
                return 2;
            case "Components.xml":
                return 3;
            case "MsgType.xml":
                return 4;
            case "MsgContents.xml":
                return 5;
            default:
                return 0;
        }
    }

    private List<File> createValidFilesList() {
        // create valid files list
    }

    private List<File> createInvalidFilesList() {
        // create invalid files list
    }

    private File[] createValidFilesArray() {
        // create valid files array
    }

    private File[] createInvalidFilesArray() {
        // create invalid files array
    }
}