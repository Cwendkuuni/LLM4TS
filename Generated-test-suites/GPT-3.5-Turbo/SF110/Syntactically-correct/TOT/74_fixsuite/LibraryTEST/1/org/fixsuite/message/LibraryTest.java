
package org.fixsuite.message;

import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LibraryTest {
    
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory_whenPathContainsValidFiles_shouldLoadDictionaries() {
        // Arrange
        String directoryPath = "some_valid_directory_path";
        File directory = new File(directoryPath);
        
        // Act
        boolean result = library.loadFromDirectory(directoryPath);
        
        // Assert
        assertTrue(result);
        assertFalse(library.getDictionaries().isEmpty());
        assertEquals(directory.list().length, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_whenPathContainsDirectories_shouldLoadDictionariesFromAllDirectories() {
        // Arrange
        String directoryPath = "some_valid_directory_path";
        File directory = new File(directoryPath);
        File[] subDirectories = new File[]{
                new File("sub_directory_1"),
                new File("sub_directory_2"),
                new File("sub_directory_3")
        };
        directory.mkdir();
        
        for (File subDirectory : subDirectories) {
            subDirectory.mkdir();
            File[] files = new File[]{
                    new File(subDirectory, "Fields.xml"),
                    new File(subDirectory, "Enums.xml"),
                    new File(subDirectory, "Components.xml"),
                    new File(subDirectory, "MsgType.xml"),
                    new File(subDirectory, "MsgContents.xml")
            };
            
            for (File file : files) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    fail("Failed to create file: " + file.getAbsolutePath());
                }
            }
        }
        
        // Act
        boolean result = library.loadFromDirectory(directoryPath);
        
        // Assert
        assertTrue(result);
        assertFalse(library.getDictionaries().isEmpty());
        assertEquals(subDirectories.length, library.getDictionaries().size());
    }
    
    @Test
    public void testLoadFromDirectory_whenPathIsEmpty_shouldReturnFalse() {
        // Arrange
        String directoryPath = "empty_directory";
        File directory = new File(directoryPath);
        directory.mkdir();
        
        // Act
        boolean result = library.loadFromDirectory(directoryPath);
        
        // Assert
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testLoadFromDirectory_whenPathIsNotDirectory_shouldReturnFalse() {
        // Arrange
        String filePath = "some_file.txt";
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail("Failed to create file: " + file.getAbsolutePath());
        }
        
        // Act
        boolean result = library.loadFromDirectory(filePath);
        
        // Assert
        assertFalse(result);
        assertTrue(library.getDictionaries().isEmpty());
    }
    
    @Test
    public void testGetDictionaries_whenDictionariesLoaded_shouldReturnListOfDictionaryInfos() {
        // Arrange
        String directoryPath = "some_valid_directory_path";
        library.loadFromDirectory(directoryPath);
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertFalse(dictionaries.isEmpty());
        assertEquals(library.getDictionaries().size(), dictionaries.size());
    }
    
    @Test
    public void testGetDictionaries_whenNoDictionariesLoaded_shouldReturnEmptyList() {
        // Arrange
        
        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        
        // Assert
        assertTrue(dictionaries.isEmpty());
    }
    
    @Test
    public void testGetDictionary_whenVersionExists_shouldReturnDictionaryInfo() {
        // Arrange
        String directoryPath = "some_valid_directory_path";
        library.loadFromDirectory(directoryPath);
        String version = library.getDictionaries().get(0).getVersion();
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }
    
    @Test
    public void testGetDictionary_whenVersionDoesNotExist_shouldReturnNull() {
        // Arrange
        String version = "non_existing_version";
        
        // Act
        DictionaryInfo dictionary = library.getDictionary(version);
        
        // Assert
        assertNull(dictionary);
    }
    
    @Test
    public void testArrangeFiles_whenFilesExistWithCorrectNames_shouldReturnArrangedFiles() {
        // Arrange
        File[] files = new File[]{
                new File("Fields.xml"),
                new File("Enums.xml"),
                new File("Components.xml"),
                new File("MsgType.xml"),
                new File("MsgContents.xml")
        };
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertNotNull(arrangedFiles);
        assertEquals(files.length, arrangedFiles.size());
        for (int i = 0; i < arrangedFiles.size(); i++) {
            String expectedFileName = getExpectedFileName(i + 1);
            assertEquals(expectedFileName, arrangedFiles.get(i).getName());
        }
    }
    
    @Test
    public void testArrangeFiles_whenFilesExistWithIncorrectNames_shouldReturnEmptyList() {
        // Arrange
        File[] files = new File[]{
                new File("Invalid1.xml"),
                new File("Invalid2.xml"),
                new File("Invalid3.xml"),
                new File("Invalid4.xml"),
                new File("Invalid5.xml")
        };
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertNotNull(arrangedFiles);
        assertTrue(arrangedFiles.isEmpty());
    }
    
    @Test
    public void testArrangeFiles_whenFilesNotExist_shouldReturnEmptyList() {
        // Arrange
        File[] files = new File[0];
        
        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);
        
        // Assert
        assertNotNull(arrangedFiles);
        assertTrue(arrangedFiles.isEmpty());
    }
    
    private String getExpectedFileName(int fileKey) {
        switch (fileKey) {
            case 1:
                return "Fields.xml";
            case 2:
                return "Enums.xml";
            case 3:
                return "Components.xml";
            case 4:
                return "MsgType.xml";
            case 5:
                return "MsgContents.xml";
            default:
                return "";
        }
    }
}

