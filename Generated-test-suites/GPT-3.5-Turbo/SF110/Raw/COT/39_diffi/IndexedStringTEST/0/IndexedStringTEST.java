import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {
	
	private IndexedString indexedString;
	
	@Before
	public void setUp() {
		indexedString = new IndexedString("Hello World");
	}
	
	@Test
	public void testConstructor() {
		IndexedString indexedString = new IndexedString("Hello World");
		assertNotNull(indexedString);
	}
	
	@Test
	public void testIndexOfValidMatch() {
		char[] target = {'o', ' ', 'W'};
		int expectedIndex = 4;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}
	
	@Test
	public void testIndexOfInvalidMatch() {
		char[] target = {'y', 'H'};
		int expectedIndex = -1;
		int actualIndex = indexedString.indexOf(target);
		assertEquals(expectedIndex, actualIndex);
	}
	
	@Test
	public void testCount() {
		int expectedCount = 3;
		int actualCount = indexedString.count('o');
		assertEquals(expectedCount, actualCount);
	}
	
	@Test
	public void testCountZero() {
		int expectedCount = 0;
		int actualCount = indexedString.count('z');
		assertEquals(expectedCount, actualCount);
	}
	
	@Test
	public void testCountAll() {
		int expectedCount = 3;
		int actualCount = indexedString.count('l');
		assertEquals(expectedCount, actualCount);
	}

}