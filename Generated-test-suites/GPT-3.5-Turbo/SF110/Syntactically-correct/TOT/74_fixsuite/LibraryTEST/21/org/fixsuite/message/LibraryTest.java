
package org.fixsuite.message;


import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {
    private static final String FIELDS_FILE_NAME = "Fields.xml";
    private static final String ENUMS_FILE_NAME = "Enums.xml";
    private static final String COMPONENTS_FILE_NAME = "Components.xml";
    private static final String MSG_TYPE_FILE_NAME = "MsgType.xml";
    private static final String MSG_CONTENTS_FILE_NAME = "MsgContents.xml";
    private static final int FIELDS_FILE_KEY = 1;
    private static final int ENUMS_FILE_KEY = 2;
    private static final int COMPONENTS_FILE_KEY = 3;
    private static final int MSG_TYPE_FILE_KEY = 4;
    private static final int MSG_CONTENTS_FILE_KEY = 5;
    private static Logger logger;
    private Library library;

    @Before
    public void setup() {
        this.library = new Library();
    }

    @Test
    public void testLoadFromDirectory_loadsDictionaries() {
        // Arrange
        File testDirectory = new File("testDirectory");
        testDirectory.mkdir();
        File[] testFiles = {
                new File(testDirectory, "Fields.xml"),
                new File(testDirectory, "Enums.xml"),
                new File(testDirectory, "Components.xml"),
                new File(testDirectory, "MsgType.xml"),
                new File(testDirectory, "MsgContents.xml")
        };
        for (File file : testFiles) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Act
        boolean result = library.loadFromDirectory(testDirectory.getPath());

        // Assert
        Assert.assertTrue(result);
        Assert.assertEquals(5, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectory_doesNotLoadDictionaries_whenNoFilesPresent() {
        // Arrange
        File testDirectory = new File("testDirectory");
        testDirectory.mkdir();

        // Act
        boolean result = library.loadFromDirectory(testDirectory.getPath());

        // Assert
        Assert.assertFalse(result);
        Assert.assertEquals(0, library.getDictionaries().size());

        testDirectory.delete();
    }

    @Test
    public void testLoadFromDirectory_doesNotLoadDictionaries_whenPathNotADirectory() {
        // Arrange
        File testFile = new File("testFile.xml");
        try {
            testFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Act
        boolean result = library.loadFromDirectory(testFile.getPath());

        // Assert
        Assert.assertFalse(result);
        Assert.assertEquals(0, library.getDictionaries().size());

        testFile.delete();
    }

    @Test
    public void testGetDictionaries_returnsListOfDictionaryInfo() {
        // Arrange
        DictionaryInfo dictionary1 = new DictionaryInfo("Test1");
        DictionaryInfo dictionary2 = new DictionaryInfo("Test2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);

        // Act
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Assert
        Assert.assertEquals(2, dictionaries.size());
        Assert.assertTrue(dictionaries.contains(dictionary1));
        Assert.assertTrue(dictionaries.contains(dictionary2));
    }

    @Test
    public void testGetDictionary_returnsDictionaryWithMatchingVersion() {
        // Arrange
        DictionaryInfo dictionary1 = new DictionaryInfo("Test1");
        DictionaryInfo dictionary2 = new DictionaryInfo("Test2");
        library.getDictionaries().add(dictionary1);
        library.getDictionaries().add(dictionary2);

        // Act
        DictionaryInfo result1 = library.getDictionary("Test1");
        DictionaryInfo result2 = library.getDictionary("Test2");
        DictionaryInfo result3 = library.getDictionary("Test3");

        // Assert
        Assert.assertEquals(dictionary1, result1);
        Assert.assertEquals(dictionary2, result2);
        Assert.assertNull(result3);
    }

    @Test
    public void testLoadDirectory_addsLoadedDictionaryToDictionaries() {
        // Arrange
        File testFile = new File("testFile.xml");
        try {
            testFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LibraryInfo dictionary = new LibraryInfo("Test");
        List<File> files = new ArrayList<>();
        files.add(testFile);

        // Act
        library.loadDirectory(files, "Test");

        // Assert
        Assert.assertEquals(dictionary, library.getDictionary("Test"));

        testFile.delete();
    }

    @Test
    public void testLoadDirectory_doesNotAddDictionaryToDictionaries_whenNotLoaded() {
        // Arrange
        File testFile = new File("testFile.xml");
        try {
            testFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LibraryInfo dictionary = new LibraryInfo("Test");

        // Act
        library.loadDirectory(new ArrayList<>(), "Test");

        // Assert
        Assert.assertNotEquals(dictionary, library.getDictionary("Test"));

        testFile.delete();
    }

    @Test
    public void testArrangeFiles_returnsArrangedList() {
        // Arrange
        File fieldsFile = new File("Fields.xml");
        File enumsFile = new File("Enums.xml");
        File componentsFile = new File("Components.xml");
        File msgTypeFile = new File("MsgType.xml");
        File msgContentsFile = new File("MsgContents.xml");
        File unexpectedFile = new File("Unexpected.xml");
        List<File> files = new ArrayList<>();
        files.add(unexpectedFile);
        files.add(fieldsFile);
        files.add(enumsFile);
        files.add(componentsFile);
        files.add(msgTypeFile);
        files.add(msgContentsFile);

        // Act
        List<File> arrangedFiles = library.arrangeFiles(files);

        // Assert
        Assert.assertEquals(fieldsFile, arrangedFiles.get(0));
        Assert.assertEquals(enumsFile, arrangedFiles.get(1));
        Assert.assertEquals(componentsFile, arrangedFiles.get(2));
        Assert.assertEquals(msgTypeFile, arrangedFiles.get(3));
        Assert.assertEquals(msgContentsFile, arrangedFiles.get(4));
        Assert.assertEquals(5, arrangedFiles.size());
    }

}

