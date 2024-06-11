import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

    private Library library;
    private Logger logger;
    private Map<String, DictionaryInfo> dictionaries;

    @Before
    public void setUp() {
        library = new Library();
        logger = mock(Logger.class);
        dictionaries = new TreeMap<>();
    }

    @Test
    public void testLoadFromDirectory_directoryNotEmpty() {
        String libraryPath = "/path/to/library";
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(new File[] { mock(File.class) });

        assertTrue(library.loadFromDirectory(libraryPath));
        verify(logger).debug("Unloading previous dictionaries");
        verify(dictionaries).clear();
        verify(logger).info(anyString());
    }

    @Test
    public void testLoadFromDirectory_directoryEmpty() {
        String libraryPath = "/path/to/library";
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(new File[0]);

        assertFalse(library.loadFromDirectory(libraryPath));
        verify(logger).error("Supplied directory is empty.");
    }

    @Test
    public void testLoadFromDirectory_pathNotDirectory() {
        String libraryPath = "/path/to/library";
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(false);

        assertFalse(library.loadFromDirectory(libraryPath));
        verify(logger).error("Supplied path is not a directory.");
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> expected = new ArrayList<>();
        expected.add(mock(DictionaryInfo.class));

        dictionaries.put("1.0", expected.get(0));
        when(library.getDictionaries()).thenReturn(expected);

        List<DictionaryInfo> actual = library.getDictionaries();
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testGetDictionary() {
        String version = "1.0";
        DictionaryInfo expected = mock(DictionaryInfo.class);

        dictionaries.put(version, expected);
        when(library.getDictionary(version)).thenReturn(expected);

        DictionaryInfo actual = library.getDictionary(version);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadDirectory_dictionaryLoaded() {
        String version = "1.0";
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        List<File> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);

        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);

        when(file1.getName()).thenReturn("Fields.xml");
        when(file2.getName()).thenReturn("Enums.xml");

        when(dictionary.getVersion()).thenReturn(version);
        when(dictionary.isLoaded()).thenReturn(true);

        when(library.getDictionary(anyString())).thenReturn(null);
        when(library.arrangeFiles(any(File[].class))).thenReturn(files);
        when(dictionary.getVersion()).thenReturn(version);

        library.loadDirectory(files, version);
        verify(dictionary).isLoaded();
        verify(library.getDictionaries()).put(version, dictionary);
    }

    @Test
    public void testLoadDirectory_dictionaryNotLoaded() {
        String version = "1.0";
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        List<File> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);

        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(false);

        when(file1.getName()).thenReturn("Fields.xml");
        when(file2.getName()).thenReturn("Enums.xml");

        when(dictionary.getVersion()).thenReturn(version);
        when(dictionary.isLoaded()).thenReturn(false);

        when(library.getDictionary(anyString())).thenReturn(null);
        when(library.arrangeFiles(any(File[].class))).thenReturn(files);
        when(dictionary.getVersion()).thenReturn(version);

        library.loadDirectory(files, version);
        verify(dictionary).isLoaded();
        verify(logger).debug("Unable to load dictionary from " + version + ".");
    }

    @Test
    public void testArrangeFiles() {
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        File file3 = mock(File.class);
        File file4 = mock(File.class);
        File file5 = mock(File.class);

        when(file1.getName()).thenReturn("Fields.xml");
        when(file2.getName()).thenReturn("Enums.xml");
        when(file3.getName()).thenReturn("Components.xml");
        when(file4.getName()).thenReturn("MsgType.xml");
        when(file5.getName()).thenReturn("MsgContents.xml");

        File[] files = {file1, file2, file3, file4, file5};
        List<File> expected = new ArrayList<>();
        expected.add(file1);
        expected.add(file2);
        expected.add(file3);
        expected.add(file4);
        expected.add(file5);

        List<File> actual = library.arrangeFiles(files);
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }
}