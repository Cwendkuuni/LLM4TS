import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class ValuedEnumTest {

	@Test
	public void testGetName() {
		JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
		assertEquals("Java 1.0", java1_0.getName());
	}

	@Test
	public void testGetValue() {
		JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
		assertEquals(100, java1_0.getValue());
	}

	@Test
	public void testGetEnumString() {
		JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
		assertEquals(100, java1_0.getValue());

		JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
		assertEquals(130, java1_3.getValue());
	}

	@Test
	public void testGetEnumInt() {
		JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
		assertEquals("Java 1.0", java1_0.getName());

		JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
		assertEquals("Java 1.3", java1_3.getName());
	}

	@Test
	public void testGetEnumMap() {
		Map enumMap = JavaVersionEnum.getEnumMap();
		assertEquals(4, enumMap.size());
		assertTrue(enumMap.containsKey("Java 1.0"));
		assertTrue(enumMap.containsKey("Java 1.1"));
		assertTrue(enumMap.containsKey("Java 1.2"));
		assertTrue(enumMap.containsKey("Java 1.3"));
	}

	@Test
	public void testGetEnumList() {
		List enumList = JavaVersionEnum.getEnumList();
		assertEquals(4, enumList.size());
		assertEquals("Java 1.0", ((JavaVersionEnum)enumList.get(0)).getName());
		assertEquals("Java 1.1", ((JavaVersionEnum)enumList.get(1)).getName());
		assertEquals("Java 1.2", ((JavaVersionEnum)enumList.get(2)).getName());
		assertEquals("Java 1.3", ((JavaVersionEnum)enumList.get(3)).getName());
	}

	@Test
	public void testIterator() {
		Iterator enumIterator = JavaVersionEnum.iterator();
		assertTrue(enumIterator.hasNext());
		assertEquals("Java 1.0", ((JavaVersionEnum)enumIterator.next()).getName());
		assertTrue(enumIterator.hasNext());
		assertEquals("Java 1.1", ((JavaVersionEnum)enumIterator.next()).getName());
		assertTrue(enumIterator.hasNext());
		assertEquals("Java 1.2", ((JavaVersionEnum)enumIterator.next()).getName());
		assertTrue(enumIterator.hasNext());
		assertEquals("Java 1.3", ((JavaVersionEnum)enumIterator.next()).getName());
		assertFalse(enumIterator.hasNext());
	}

	@Test
	public void testCompareTo() {
		JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
		JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;

		assertTrue(java1_0.compareTo(java1_1) < 0);
		assertTrue(java1_1.compareTo(java1_0) > 0);
		assertTrue(java1_0.compareTo(java1_0) == 0);
	}

	@Test(expected = ClassCastException.class)
	public void testCompareToDifferentEnumType() {
		JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
		SeasonEnum spring = SeasonEnum.SPRING;

		java1_0.compareTo(spring);
	}

	@Test
	public void testToString() {
		JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
		assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
	}

}