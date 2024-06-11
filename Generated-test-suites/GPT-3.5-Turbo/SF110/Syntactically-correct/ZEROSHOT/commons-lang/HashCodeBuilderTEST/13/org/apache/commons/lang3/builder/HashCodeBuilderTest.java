import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test with Integer object
        Integer obj1 = 100;
        Integer obj2 = 200;
        int expected1 = obj1.hashCode();
        int expected2 = obj2.hashCode();

        int actual1 = HashCodeBuilder.reflectionHashCode(obj1);
        int actual2 = HashCodeBuilder.reflectionHashCode(obj2);

        assert expected1 == actual1;
        assert expected2 == actual2;

        // Test with String object
        String str1 = "Hello";
        String str2 = "World";
        int expected3 = str1.hashCode();
        int expected4 = str2.hashCode();

        int actual3 = HashCodeBuilder.reflectionHashCode(str1);
        int actual4 = HashCodeBuilder.reflectionHashCode(str2);

        assert expected3 == actual3;
        assert expected4 == actual4;
    }

    @Test
    public void testAppend() {
        HashCodeBuilder builder = new HashCodeBuilder();

        // Test with boolean values
        boolean bool1 = true;
        boolean bool2 = false;

        builder.append(bool1);
        builder.append(bool2);

        int expected1 = bool1 ? 0 : 1;
        int expected2 = bool2 ? 0 : 1;

        int actual1 = builder.toHashCode();

        assert expected1 == actual1;

        // Test with byte values
        byte byte1 = 10;
        byte byte2 = 20;

        builder.append(byte1);
        builder.append(byte2);

        int expected3 = byte1;
        int expected4 = byte2;

        int actual2 = builder.toHashCode();

        assert expected3 == actual2;

        // Test with char values
        char char1 = 'A';
        char char2 = 'B';

        builder.append(char1);
        builder.append(char2);

        int expected5 = char1;
        int expected6 = char2;

        int actual3 = builder.toHashCode();

        assert expected5 == actual3;

        // Test with double values
        double double1 = 3.14;
        double double2 = 2.71;

        builder.append(double1);
        builder.append(double2);

        long expected7 = Double.doubleToLongBits(double1);
        long expected8 = Double.doubleToLongBits(double2);

        int actual4 = builder.toHashCode();

        assert expected7 == actual4;

        // Test with float values
        float float1 = 3.14f;
        float float2 = 2.71f;

        builder.append(float1);
        builder.append(float2);

        int expected9 = Float.floatToIntBits(float1);
        int expected10 = Float.floatToIntBits(float2);

        int actual5 = builder.toHashCode();

        assert expected9 == actual5;

        // Test with int values
        int int1 = 100;
        int int2 = 200;

        builder.append(int1);
        builder.append(int2);

        int expected11 = int1;
        int expected12 = int2;

        int actual6 = builder.toHashCode();

        assert expected11 == actual6;

        // Test with long values
        long long1 = 1000L;
        long long2 = 2000L;

        builder.append(long1);
        builder.append(long2);

        int expected13 = (int) (long1 ^ (long1 >> 32));
        int expected14 = (int) (long2 ^ (long2 >> 32));

        int actual7 = builder.toHashCode();

        assert expected13 == actual7;

        // Test with Object values
        Object obj1 = new Object();
        Object obj2 = new Object();

        builder.append(obj1);
        builder.append(obj2);

        int expected15 = obj1.hashCode();
        int expected16 = obj2.hashCode();

        int actual8 = builder.toHashCode();

        assert expected15 == actual8;
    }

    @Test
    public void testAppendSuper() {
        // Test with super hash code
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);

        int superHashCode = 123;
        builder.appendSuper(superHashCode);

        int expected = 17 * 37 + superHashCode;

        int actual = builder.toHashCode();

        assert expected == actual;
    }
}