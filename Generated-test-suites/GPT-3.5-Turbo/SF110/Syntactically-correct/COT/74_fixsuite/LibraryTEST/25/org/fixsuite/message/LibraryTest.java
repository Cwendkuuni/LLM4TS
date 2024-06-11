
package org.fixsuite.message;

import org.fixsuite.message.info.*;
import java.io.*;
import java.text.*;
import org.fixsuite.message.parsers.fpl.*;
import java.util.*;
import org.slf4j.*;
import org.junit.*;
import static org.junit.Assert.*;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory_WhenLibraryPathIsValid_ReturnsTrue() {
        // Arrange
        Library library = new Library();
        String libraryPath = "path/to/library";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testLoadFromDirectory_WhenLibraryPathIsInvalid_ReturnsFalse() {
        // Arrange
        Library library = new Library();
        String libraryPath = "invalid/path/to/library";

        // Act
        boolean result = library.loadFromDirectory(libraryPath);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries_WhenDictionariesExist_ReturnsNonEmptyList() {
        // Arrange
        Library library = new Library();
        // Load dictionaries
        library.loadFromDirectory("path/to/library");

        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaries_WhenNoDictionariesExist_ReturnsEmptyList() {
        // Arrange
        Library library = new Library();

        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        assertTrue(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary_WhenDictionaryExists_ReturnsDictionary() {
        // Arrange
        Library library = new Library();
        // Load dictionaries
        library.loadFromDirectory("path/to/library");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        String version = dictionaries.get(0).getVersion();

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }

    @Test
    public void testGetDictionary_WhenDictionaryDoesNotExist_ReturnsNull() {
        // Arrange
        Library library = new Library();
        String version = "nonexistent-version";

        // Act
        DictionaryInfo dictionary = library.getDictionary(version);

        // Assert
        assertNull(dictionary);
    }

}
