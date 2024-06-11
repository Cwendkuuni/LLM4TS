import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    // Test the constructor
    @Test
    public void testConstructor() {
        ValuedEnum valuedEnum = new TestValuedEnum("Test", 1);
        assertNotNull(valuedEnum);
        assertEquals("Test", valuedEnum.getName());
        assertEquals(1, valuedEnum.getValue());
    }

    // Test the getEnum method with string argument
    @Test
    public void testGetEnumWithString() {
        TestValuedEnum testValuedEnum = (TestValuedEnum) TestValuedEnum.getEnum("Test");
        assertNotNull(testValuedEnum);
        assertEquals("Test", testValuedEnum.getName());
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the getEnum method with int argument
    @Test
    public void testGetEnumWithInt() {
        TestValuedEnum testValuedEnum = (TestValuedEnum) TestValuedEnum.getEnum(1);
        assertNotNull(testValuedEnum);
        assertEquals("Test", testValuedEnum.getName());
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the getEnumMap method
    @Test
    public void testGetEnumMap() {
        Map enumMap = TestValuedEnum.getEnumMap();
        assertNotNull(enumMap);
        assertEquals(1, enumMap.size());
        TestValuedEnum testValuedEnum = (TestValuedEnum) enumMap.get("Test");
        assertNotNull(testValuedEnum);
        assertEquals("Test", testValuedEnum.getName());
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the getEnumList method
    @Test
    public void testGetEnumList() {
        List enumList = TestValuedEnum.getEnumList();
        assertNotNull(enumList);
        assertEquals(1, enumList.size());
        TestValuedEnum testValuedEnum = (TestValuedEnum) enumList.get(0);
        assertNotNull(testValuedEnum);
        assertEquals("Test", testValuedEnum.getName());
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the iterator method
    @Test
    public void testIterator() {
        Iterator iterator = TestValuedEnum.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        TestValuedEnum testValuedEnum = (TestValuedEnum) iterator.next();
        assertNotNull(testValuedEnum);
        assertEquals("Test", testValuedEnum.getName());
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the getValue method
    @Test
    public void testGetValue() {
        TestValuedEnum testValuedEnum = new TestValuedEnum("Test", 1);
        assertEquals(1, testValuedEnum.getValue());
    }

    // Test the compareTo method
    @Test
    public void testCompareTo() {
        TestValuedEnum testValuedEnum1 = new TestValuedEnum("Test1", 1);
        TestValuedEnum testValuedEnum2 = new TestValuedEnum("Test2", 2);
        assertTrue(testValuedEnum1.compareTo(testValuedEnum2) < 0);
        assertTrue(testValuedEnum2.compareTo(testValuedEnum1) > 0);
        assertTrue(testValuedEnum1.compareTo(testValuedEnum1) == 0);
    }

    // Test the toString method
    @Test
    public void testToString() {
        TestValuedEnum testValuedEnum = new TestValuedEnum("Test", 1);
        assertEquals("TestValuedEnum[Test=1]", testValuedEnum.toString());
    }
}

class TestValuedEnum extends ValuedEnum {
    public static final TestValuedEnum TEST = new TestValuedEnum("Test", 1);

    protected TestValuedEnum(String name, int value) {
        super(name, value);
    }
}