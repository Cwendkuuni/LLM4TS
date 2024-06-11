import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;

public class LibraryTest {
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
    }
    
    @Test
    public void testLoadFromDirectory() {
        assertFalse(library.loadFromDirectory("invalid/path")); // Test for invalid path
        assertFalse(library.loadFromDirectory("")); // Test for empty path
        assertFalse(library.loadFromDirectory(null)); // Test for null path
    }
    
    @Test
    public void testGetDictionaries() {
        assertTrue(library.getDictionaries().isEmpty()); // Test when no dictionaries are loaded
    }
    
    @Test
    public void testGetDictionary() {
        assertNull(library.getDictionary("non-existent version")); // Test for non-existent version
    }
    
    @Test
    public void testLoadDirectory() {
        library.loadDirectory(null, "1.0.0"); // Test for null files
        assertTrue(library.dictionaries.isEmpty()); // Test when dictionary is not loaded
        
        // Mocking files and parsing to test successful loading
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("Fields.xml");
        List<File> mockFiles = new ArrayList<>();
        mockFiles.add(mockFile);
        
        assertEquals(1, mockFiles.size());
        library.loadDirectory(mockFiles, "1.0.0");
        assertFalse(library.dictionaries.isEmpty());
    }
    
    @Test
    public void testArrangeFiles() {
        File[] files = new File[5];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File("TestFile" + i + ".xml");
        }
        
        assertNotNull(library.arrangeFiles(files)); // Test with random file names
        
        //Mocking specific files
        File mockFieldsFile = mock(File.class);
        when(mockFieldsFile.getName()).thenReturn("Fields.xml");
        File mockEnumsFile = mock(File.class);
        when(mockEnumsFile.getName()).thenReturn("Enums.xml");
        File mockComponentsFile = mock(File.class);
        when(mockComponentsFile.getName()).thenReturn("Components.xml");
        File mockMsgTypeFile = mock(File.class);
        when(mockMsgTypeFile.getName()).thenReturn("MsgType.xml");
        File mockMsgContentsFile = mock(File.class);
        when(mockMsgContentsFile.getName()).thenReturn("MsgContents.xml");
        
        File[] mockFiles = new File[]{mockFieldsFile, mockEnumsFile, mockComponentsFile, mockMsgTypeFile, mockMsgContentsFile};
        assertEquals(5, mockFiles.length);
        assertEquals(5, library.arrangeFiles(mockFiles).size());
    }
}