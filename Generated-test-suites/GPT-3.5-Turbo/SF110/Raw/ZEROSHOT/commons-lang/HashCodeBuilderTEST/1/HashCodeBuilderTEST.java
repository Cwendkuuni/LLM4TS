import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    HashCodeBuilder hashCodeBuilder;

    @Before
    public void setUp() {
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder result = hashCodeBuilder.append(true);
        assertEquals(37, result.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new boolean[]{true, false});
        assertEquals(75, result.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder result = hashCodeBuilder.append((byte) 1);
        assertEquals(38, result.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new byte[]{1, 2});
        assertEquals(112, result.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder result = hashCodeBuilder.append('a');
        assertEquals(5186, result.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new char[]{'a', 'b'});
        assertEquals(107950, result.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder result = hashCodeBuilder.append(1.1);
        assertEquals(-761275170, result.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new double[]{1.1, 2.2});
        assertEquals(1016158112, result.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder result = hashCodeBuilder.append(1.1f);
        assertEquals(1077847795, result.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new float[]{1.1f, 2.2f});
        assertEquals(-1090816968, result.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder result = hashCodeBuilder.append(1);
        assertEquals(38, result.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new int[]{1, 2});
        assertEquals(112, result.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder result = hashCodeBuilder.append(1L);
        assertEquals(-169576766, result.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new long[]{1L, 2L});
        assertEquals(-1924713969, result.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder result = hashCodeBuilder.append("test");
        assertEquals(-1703760027, result.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new Object[]{"test", 1});
        assertEquals(-1337773058, result.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder result = hashCodeBuilder.append((short) 1);
        assertEquals(39, result.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder result = hashCodeBuilder.append(new short[]{1, 2});
        assertEquals(113, result.toHashCode());
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder result = hashCodeBuilder.appendSuper(100);
        assertEquals(3737, result.toHashCode());
    }

    @Test
    public void testBuild() {
        Integer result = hashCodeBuilder.build();
        assertNotNull(result);
        assertEquals(17, result.intValue());
    }

    @Test
    public void testHashCode() {
        int result = hashCodeBuilder.hashCode();
        assertEquals(17, result);
    }
}