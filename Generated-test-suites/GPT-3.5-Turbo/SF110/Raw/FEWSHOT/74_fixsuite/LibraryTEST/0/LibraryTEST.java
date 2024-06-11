package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

    private Library library;

    @Before
    public void setUp() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory("path/to/directory"));
        assertFalse(library.loadFromDirectory("invalid/path"));
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(0, dictionaries.size());

        library.loadFromDirectory("path/to/directory");
        dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(1, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("version"));

        library.loadFromDirectory("path/to/directory");
        DictionaryInfo dictionary = library.getDictionary("version");
        assertNotNull(dictionary);
        assertEquals("version", dictionary.getVersion());
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        String version = "version";

        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        files.add(new File("path/to/file.xml"));
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        File fieldsFile = new File("path/to/Fields.xml");
        files.add(fieldsFile);
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        File enumsFile = new File("path/to/Enums.xml");
        files.add(enumsFile);
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        File componentsFile = new File("path/to/Components.xml");
        files.add(componentsFile);
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        File msgTypeFile = new File("path/to/MsgType.xml");
        files.add(msgTypeFile);
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));

        File msgContentsFile = new File("path/to/MsgContents.xml");
        files.add(msgContentsFile);
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
    }

    @Test
    public void testArrangeFiles() {
        File fieldsFile = new File("path/to/Fields.xml");
        File enumsFile = new File("path/to/Enums.xml");
        File componentsFile = new File("path/to/Components.xml");
        File msgTypeFile = new File("path/to/MsgType.xml");
        File msgContentsFile = new File("path/to/MsgContents.xml");

        File[] files = {fieldsFile, enumsFile, componentsFile, msgTypeFile, msgContentsFile};
        List<File> arrangedFiles = library.arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals(fieldsFile, arrangedFiles.get(0));
        assertEquals(enumsFile, arrangedFiles.get(1));
        assertEquals(componentsFile, arrangedFiles.get(2));
        assertEquals(msgTypeFile, arrangedFiles.get(3));
        assertEquals(msgContentsFile, arrangedFiles.get(4));
    }
}