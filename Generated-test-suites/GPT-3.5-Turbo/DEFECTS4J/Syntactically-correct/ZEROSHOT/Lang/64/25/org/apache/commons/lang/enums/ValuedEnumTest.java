import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        // Test cases using static values from ValuedEnum class
        Assert.assertEquals(ValuedEnum.getEnum(JavaVersionEnum.class, 100), JavaVersionEnum.JAVA1_0);
        Assert.assertEquals(ValuedEnum.getEnum(JavaVersionEnum.class, 110), JavaVersionEnum.JAVA1_1);
        Assert.assertEquals(ValuedEnum.getEnum(JavaVersionEnum.class, 120), JavaVersionEnum.JAVA1_2);
        Assert.assertEquals(ValuedEnum.getEnum(JavaVersionEnum.class, 130), JavaVersionEnum.JAVA1_3);

        // Test case where enum does not exist
        Assert.assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 140));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(100, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_1;
        Assert.assertEquals(110, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_2;
        Assert.assertEquals(120, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        Assert.assertEquals(130, javaVersionEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        // Test cases for less than
        Assert.assertTrue(JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1) < 0);
        Assert.assertTrue(JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_2) < 0);
        Assert.assertTrue(JavaVersionEnum.JAVA1_2.compareTo(JavaVersionEnum.JAVA1_3) < 0);

        // Test cases for greater than
        Assert.assertTrue(JavaVersionEnum.JAVA1_3.compareTo(JavaVersionEnum.JAVA1_2) > 0);
        Assert.assertTrue(JavaVersionEnum.JAVA1_2.compareTo(JavaVersionEnum.JAVA1_1) > 0);
        Assert.assertTrue(JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_0) > 0);

        // Test case for equal
        Assert.assertEquals(0, JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_1));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentEnumTypes() {
        JavaVersionEnum.JAVA1_0.compareTo(AnotherEnum.ANOTHER_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNullEnum() {
        JavaVersionEnum.JAVA1_0.compareTo(null);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersionEnum.toString());

        javaVersionEnum = JavaVersionEnum.JAVA1_1;
        Assert.assertEquals("JavaVersionEnum[Java 1.1=110]", javaVersionEnum.toString());

        javaVersionEnum = JavaVersionEnum.JAVA1_2;
        Assert.assertEquals("JavaVersionEnum[Java 1.2=120]", javaVersionEnum.toString());

        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        Assert.assertEquals("JavaVersionEnum[Java 1.3=130]", javaVersionEnum.toString());
    }
}

class JavaVersionEnum extends ValuedEnum {
    public static final int JAVA1_0_VALUE = 100;
    public static final int JAVA1_1_VALUE = 110;
    public static final int JAVA1_2_VALUE = 120;
    public static final int JAVA1_3_VALUE = 130;
    public static final JavaVersionEnum JAVA1_0 = new JavaVersionEnum("Java 1.0", JAVA1_0_VALUE);
    public static final JavaVersionEnum JAVA1_1 = new JavaVersionEnum("Java 1.1", JAVA1_1_VALUE);
    public static final JavaVersionEnum JAVA1_2 = new JavaVersionEnum("Java 1.2", JAVA1_2_VALUE);
    public static final JavaVersionEnum JAVA1_3 = new JavaVersionEnum("Java 1.3", JAVA1_3_VALUE);

    private JavaVersionEnum(String name, int value) {
        super(name, value);
    }

    public static JavaVersionEnum getEnum(String javaVersion) {
        return (JavaVersionEnum) getEnum(JavaVersionEnum.class, javaVersion);
    }

    public static JavaVersionEnum getEnum(int javaVersion) {
        return (JavaVersionEnum) getEnum(JavaVersionEnum.class, javaVersion);
    }

    public static List getEnumList() {
        return getEnumList(JavaVersionEnum.class);
    }

    public static Iterator iterator() {
        return iterator(JavaVersionEnum.class);
    }
}

class AnotherEnum extends ValuedEnum {
    public static final int ANOTHER_VALUE = 500;
    public static final AnotherEnum ANOTHER = new AnotherEnum("Another", ANOTHER_VALUE);

    private AnotherEnum(String name, int value) {
        super(name, value);
    }

    public static AnotherEnum getEnum(String javaVersion) {
        return (AnotherEnum) getEnum(AnotherEnum.class, javaVersion);
    }

    public static AnotherEnum getEnum(int javaVersion) {
        return (AnotherEnum) getEnum(AnotherEnum.class, javaVersion);
    }

    public static List getEnumList() {
        return getEnumList(AnotherEnum.class);
    }

    public static Iterator iterator() {
        return iterator(AnotherEnum.class);
    }
}