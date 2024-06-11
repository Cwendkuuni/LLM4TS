import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ValuedEnumTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test
    public void testGetEnumWithValidClassAndValue() {
        ValuedEnum testEnum = ValuedEnum.JAVA1_0;
        ValuedEnum enumWithValue = ValuedEnum.getEnum(ValuedEnum.class, testEnum.getValue());
        Assert.assertEquals(testEnum, enumWithValue);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullEnum() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test
    public void testGetEnumWithInvalidValue() {
        Assert.assertNull(ValuedEnum.getEnum(ValuedEnum.class, 1000));
    }

    @Test
    public void testGetEnumMap() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Map map = ValuedEnum.getEnumMap(ValuedEnum.class);
        Assert.assertEquals(enumList.size(), map.size());
        for (Object enumObj : enumList) {
            ValuedEnum enumValue = (ValuedEnum) enumObj;
            Assert.assertEquals(enumValue, map.get(enumValue.getName()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumMapWithNullClass() {
        ValuedEnum.getEnumMap(null);
    }

    @Test
    public void testGetEnumList() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Assert.assertEquals(4, enumList.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListWithNullClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testIterator() {
        List enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Iterator iterator = ValuedEnum.iterator(ValuedEnum.class);
        Assert.assertEquals(enumList.size(), 4);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), enumList.get(0));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(iterator.next(), enumList.get(1));
        // ... continue for all elements
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullClass() {
        ValuedEnum.iterator(null);
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        String expected = "JavaVersion[Java 1.0=100]";
        String actual = javaVersion1_0.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNullObject() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        javaVersion1_0.compareTo(null);
    }

    @Test
    public void testCompareToWithSameValue() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_0Copy = new ValuedEnum("Java 1.0", javaVersion1_0.getValue()) {
        };
        int compareResult = javaVersion1_0.compareTo(javaVersion1_0Copy);
        Assert.assertEquals(0, compareResult);
    }

    @Test
    public void testCompareToWithDifferentValues() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_1 = ValuedEnum.JAVA1_1;
        int compareResult = javaVersion1_0.compareTo(javaVersion1_1);
        Assert.assertLessThan(0, compareResult);
    }
}