import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class LibraryTest {

	// Test case for the loadFromDirectory method
	@Test
	public void testLoadFromDirectory() {
		Library library = new Library();
		
		// Typical use case with a valid directory path
		boolean result1 = library.loadFromDirectory("path/to/valid/directory");
		assertTrue(result1);
		
		// Edge case with an empty directory
		boolean result2 = library.loadFromDirectory("path/to/empty/directory");
		assertFalse(result2);
		
		// Edge case with a non-existing directory
		boolean result3 = library.loadFromDirectory("path/to/non/existing/directory");
		assertFalse(result3);
		
		// Error scenario with a file instead of a directory
        boolean result4 = library.loadFromDirectory("path/to/file.txt");
        assertFalse(result4);
	}
	
	// Test case for the getDictionaries method
	@Test
	public void testGetDictionaries() {
		Library library = new Library();
		
		// Typical use case with loaded dictionaries
		library.loadFromDirectory("path/to/valid/directory");
		List<DictionaryInfo> dictionaries = library.getDictionaries();
		assertNotNull(dictionaries);
		assertTrue(dictionaries.size() > 0);
		
		// Edge case with no loaded dictionaries
		Library library2 = new Library();
		List<DictionaryInfo> dictionaries2 = library2.getDictionaries();
		assertNotNull(dictionaries2);
		assertEquals(0, dictionaries2.size());
	}
	
	// Test case for the getDictionary method
	@Test
	public void testGetDictionary() {
		Library library = new Library();
		
		// Typical use case with an existing dictionary version
		library.loadFromDirectory("path/to/valid/directory");
		DictionaryInfo dictionary1 = library.getDictionary("1.0");
		assertNotNull(dictionary1);
		assertEquals("1.0", dictionary1.getVersion());
		
		// Edge case with a non-existing dictionary version
		DictionaryInfo dictionary2 = library.getDictionary("2.0");
		assertNull(dictionary2);
	}
	
	// Test case for the loadDirectory method
	@Test
	public void testLoadDirectory() {
		Library library = new Library();
		List<File> files = new ArrayList<File>();
		files.add(new File("path/to/valid/file1.xml"));
		files.add(new File("path/to/valid/file2.xml"));
		
		// Typical use case with valid files
		library.loadDirectory(files, "1.0");
		DictionaryInfo dictionary1 = library.getDictionary("1.0");
		assertNotNull(dictionary1);
		assertEquals("1.0", dictionary1.getVersion());
		
		// Error scenario with invalid files
		library.loadDirectory(files, "2.0");
		DictionaryInfo dictionary2 = library.getDictionary("2.0");
		assertNull(dictionary2);
	}
	
	// Test case for the arrangeFiles method
	@Test
	public void testArrangeFiles() {
		Library library = new Library();
		File[] files = new File[5];
		files[0] = new File("path/to/Fields.xml");
		files[1] = new File("path/to/Enums.xml");
		files[2] = new File("path/to/Components.xml");
		files[3] = new File("path/to/MsgType.xml");
		files[4] = new File("path/to/MsgContents.xml");
		
		List<File> arrangedFiles = library.arrangeFiles(files);
		assertNotNull(arrangedFiles);
		assertEquals(5, arrangedFiles.size());
		assertEquals("path/to/Fields.xml", arrangedFiles.get(0).getName());
		assertEquals("path/to/Enums.xml", arrangedFiles.get(1).getName());
		assertEquals("path/to/Components.xml", arrangedFiles.get(2).getName());
		assertEquals("path/to/MsgType.xml", arrangedFiles.get(3).getName());
		assertEquals("path/to/MsgContents.xml", arrangedFiles.get(4).getName());
	}
}