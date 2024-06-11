import org.junit.*;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test basic functionality
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object());
        assertEquals(17, hashCode);

        // Test object with transients
        TestObject obj = new TestObject();
        obj.setValue(10);
        obj.setTransientValue(5);
        hashCode = HashCodeBuilder.reflectionHashCode(obj, true);
        assertEquals(42, hashCode);

        // Test object with excluded fields
        hashCode = HashCodeBuilder.reflectionHashCode(obj, TestObject.class, "value");
        assertEquals(5, hashCode);

        // Test object with null array
        int[] array = null;
        hashCode = new HashCodeBuilder().append(array).toHashCode();
        assertEquals(0, hashCode);

        // Test object with non-null array
        array = new int[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(array).toHashCode();
        assertEquals(6, hashCode);
    }

    @Test
    public void testAppend() {
        // Test append boolean
        int hashCode = new HashCodeBuilder().append(true).toHashCode();
        assertEquals(37, hashCode);
        
        // Test append boolean array (null)
        boolean[] array = null;
        hashCode = new HashCodeBuilder().append(array).toHashCode();
        assertEquals(0, hashCode);

        // Test append boolean array (non-null)
        array = new boolean[]{true, false};
        hashCode = new HashCodeBuilder().append(array).toHashCode();
        assertEquals(74, hashCode);

        // Test append byte
        hashCode = new HashCodeBuilder().append((byte) 5).toHashCode();
        assertEquals(197, hashCode);

        // Test append byte array (null)
        byte[] byteArray = null;
        hashCode = new HashCodeBuilder().append(byteArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append byte array (non-null)
        byteArray = new byte[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(byteArray).toHashCode();
        assertEquals(115, hashCode);

        // Test append char
        hashCode = new HashCodeBuilder().append('c').toHashCode();
        assertEquals(70499, hashCode);

        // Test append char array (null)
        char[] charArray = null;
        hashCode = new HashCodeBuilder().append(charArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append char array (non-null)
        charArray = new char[]{'a', 'b', 'c'};
        hashCode = new HashCodeBuilder().append(charArray).toHashCode();
        assertEquals(420173, hashCode);

        // Test append double
        hashCode = new HashCodeBuilder().append(3.14).toHashCode();
        assertEquals(1078530011, hashCode);

        // Test append double array (null)
        double[] doubleArray = null;
        hashCode = new HashCodeBuilder().append(doubleArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append double array (non-null)
        doubleArray = new double[]{1.0, 2.0, 3.0};
        hashCode = new HashCodeBuilder().append(doubleArray).toHashCode();
        assertEquals(2040849609, hashCode);

        // Test append float
        hashCode = new HashCodeBuilder().append(3.14f).toHashCode();
        assertEquals(1078530011, hashCode);

        // Test append float array (null)
        float[] floatArray = null;
        hashCode = new HashCodeBuilder().append(floatArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append float array (non-null)
        floatArray = new float[]{1.0f, 2.0f, 3.0f};
        hashCode = new HashCodeBuilder().append(floatArray).toHashCode();
        assertEquals(2040849609, hashCode);

        // Test append int
        hashCode = new HashCodeBuilder().append(42).toHashCode();
        assertEquals(59, hashCode);

        // Test append int array (null)
        int[] intArray = null;
        hashCode = new HashCodeBuilder().append(intArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append int array (non-null)
        intArray = new int[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(intArray).toHashCode();
        assertEquals(6, hashCode);

        // Test append long
        hashCode = new HashCodeBuilder().append(1234567890L).toHashCode();
        assertEquals(1963706317, hashCode);

        // Test append long array (null)
        long[] longArray = null;
        hashCode = new HashCodeBuilder().append(longArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append long array (non-null)
        longArray = new long[]{1L, 2L, 3L};
        hashCode = new HashCodeBuilder().append(longArray).toHashCode();
        assertEquals(115, hashCode);

        // Test append object (null)
        Object obj = null;
        hashCode = new HashCodeBuilder().append(obj).toHashCode();
        assertEquals(0, hashCode);

        // Test append object (array)
        obj = new int[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(obj).toHashCode();
        assertEquals(22, hashCode);

        // Test append object (non-array)
        obj = "test";
        hashCode = new HashCodeBuilder().append(obj).toHashCode();
        assertEquals(3556498, hashCode);

        // Test append object array (null)
        Object[] objArray = null;
        hashCode = new HashCodeBuilder().append(objArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append object array (non-null)
        objArray = new Object[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(objArray).toHashCode();
        assertEquals(115, hashCode);

        // Test append short
        hashCode = new HashCodeBuilder().append((short) 5).toHashCode();
        assertEquals(197, hashCode);

        // Test append short array (null)
        short[] shortArray = null;
        hashCode = new HashCodeBuilder().append(shortArray).toHashCode();
        assertEquals(0, hashCode);

        // Test append short array (non-null)
        shortArray = new short[]{1, 2, 3};
        hashCode = new HashCodeBuilder().append(shortArray).toHashCode();
        assertEquals(115, hashCode);

        // Test append super
        int superHashCode = 123456;
        hashCode = new HashCodeBuilder().appendSuper(superHashCode).toHashCode();
        assertEquals(2498817, hashCode);
    }

    private class TestObject {
        private int value;
        private transient int transientValue;

        public void setValue(int value) {
            this.value = value;
        }

        public void setTransientValue(int transientValue) {
            this.transientValue = transientValue;
        }
    }

}