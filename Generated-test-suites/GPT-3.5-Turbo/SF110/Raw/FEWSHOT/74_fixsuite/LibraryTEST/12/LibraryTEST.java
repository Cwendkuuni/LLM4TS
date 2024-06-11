
package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

    private Library library;
    private Map<String, DictionaryInfo> dictionaries;

    @Before
    public void setUp() {
        library = new Library();
        dictionaries = new TreeMap<>();
    }

    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory(null));
        assertFalse(library.loadFromDirectory(""));
        assertFalse(library.loadFromDirectory("invalid_path"));
        assertTrue(library.loadFromDirectory("valid_path"));

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).debug(stringArgumentCaptor.capture());
        assertEquals("Unloading previous dictionaries", stringArgumentCaptor.getValue());

        ArgumentCaptor<String> stringArgumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).error(stringArgumentCaptor2.capture());
        assertEquals("Supplied directory is empty.", stringArgumentCaptor2.getValue());

        ArgumentCaptor<String> stringArgumentCaptor3 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).error(stringArgumentCaptor3.capture());
        assertEquals("Supplied path is not a directory.", stringArgumentCaptor3.getValue());

        ArgumentCaptor<String> stringArgumentCaptor4 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).error(stringArgumentCaptor4.capture());
        assertEquals("Unable to load any dictionaries", stringArgumentCaptor4.getValue());

        ArgumentCaptor<String> stringArgumentCaptor5 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).debug(stringArgumentCaptor5.capture());
        assertEquals("Loaded " + dictionaries.size() + " FIX dictionaries.", stringArgumentCaptor5.getValue());

        ArgumentCaptor<String> stringArgumentCaptor6 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).error(stringArgumentCaptor6.capture());
        assertEquals("Unable to load dictionary from " + anyVersion + ".", stringArgumentCaptor6.getValue());

        ArgumentCaptor<String> stringArgumentCaptor7 = ArgumentCaptor.forClass(String.class);
        verify(Library.logger).info(stringArgumentCaptor7.capture());
        assertEquals("Loading time: " + anyLoadingTime + " milliseconds.", stringArgumentCaptor7.getValue());
    }

    @Test
    public void testGetDictionaries() {
        List<DictionaryInfo> expectedDictionaries = new ArrayList<>(dictionaries.values());
        List<DictionaryInfo> actualDictionaries = library.getDictionaries();
        assertEquals(expectedDictionaries, actualDictionaries);
    }

    @Test
    public void testGetDictionary() {
        String version = "1.0";
        dictionaries.put(version, new DictionaryInfo(version));
        DictionaryInfo expectedDictionary = dictionaries.get(version);
        DictionaryInfo actualDictionary = library.getDictionary(version);
        assertEquals(expectedDictionary, actualDictionary);
    }

    @Test
    public void testLoadDirectory() {
        List<File> files = new ArrayList<>();
        String version = "1.0";
        DictionaryInfo dictionaryInfoMock = mock(DictionaryInfo.class);
        MainParser mainParserMock = mock(MainParser.class);

        when(dictionaryInfoMock.isLoaded()).thenReturn(true);
        when(dictionaryInfoMock.getVersion()).thenReturn(version);
        when(mainParserMock.parse(dictionaryInfoMock, files.get(0))).thenReturn(true);

        library.loadDirectory(files, version);

        verify(dictionaries).put(version, dictionaryInfoMock);

        verify(Library.logger).debug("Unable to load dictionary from " + version + ".");
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        for (int i = 0; i < 5; i++) {
            files[i] = mock(File.class);
        }

        String fieldsFileName = "Fields.xml";
        String enumsFileName = "Enums.xml";
        String componentsFileName = "Components.xml";
        String msgTypeFileName = "MsgType.xml";
        String msgContentsFileName = "MsgContents.xml";

        when(files[0].getName()).thenReturn(fieldsFileName);
        when(files[1].getName()).thenReturn(enumsFileName);
        when(files[2].getName()).thenReturn(componentsFileName);
        when(files[3].getName()).thenReturn(msgTypeFileName);
        when(files[4].getName()).thenReturn(msgContentsFileName);

        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(files[0]);
        expectedFiles.add(files[1]);
        expectedFiles.add(files[2]);
        expectedFiles.add(files[3]);
        expectedFiles.add(files[4]);

        List<File> actualFiles = library.arrangeFiles(files);
        assertEquals(expectedFiles, actualFiles);
    }
}
