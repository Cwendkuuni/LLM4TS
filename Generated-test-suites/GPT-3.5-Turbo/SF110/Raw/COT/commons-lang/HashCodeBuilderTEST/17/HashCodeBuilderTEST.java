import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;

public class HashCodeBuilderTest {
   
    @Test
    public void testReflectionHashCode() {
        int result = HashCodeBuilder.reflectionHashCode(10, 20, "test");
        assert(result == 516);
    }
    
    @Test
    public void testReflectionHashCode_withNullObject() {
        try {
            int result = HashCodeBuilder.reflectionHashCode(10, 20, null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testReflectionHashCode_withTransients() {
        int result = HashCodeBuilder.reflectionHashCode(10, 20, "test", true);
        assert(result == 516);
    }
    
    @Test
    public void testReflectionHashCode_withExcludeFields() {
        int result = HashCodeBuilder.reflectionHashCode(10, 20, "test", false, String.class, "exclude");
        assert(result == 516);
    }
    
    @Test
    public void testReflectionHashCode_withCollectionExcludeFields() {
        Collection<String> excludeFields = new HashSet<>();
        excludeFields.add("exclude1");
        excludeFields.add("exclude2");
        int result = HashCodeBuilder.reflectionHashCode("test", excludeFields);
        assert(result == 516);
    }
    
    @Test
    public void testReflectionHashCode_withArrayExcludeFields() {
        String[] excludeFields = {"exclude1", "exclude2"};
        int result = HashCodeBuilder.reflectionHashCode("test", excludeFields);
        assert(result == 516);
    }
    
    @Test
    public void testHashCodeBuilder_defaultConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assert(builder.toHashCode() == 17);
    }
    
    @Test
    public void testHashCodeBuilder_constructor() {
        HashCodeBuilder builder = new HashCodeBuilder(10, 20);
        assert(builder.toHashCode() == 10);
    }
    
    @Test
    public void testHashCodeBuilder_constructor_withEvenInitialValue() {
        try {
            HashCodeBuilder builder = new HashCodeBuilder(8, 20);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testHashCodeBuilder_constructor_withEvenMultiplier() {
        try {
            HashCodeBuilder builder = new HashCodeBuilder(10, 8);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testHashCodeBuilder_append_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assert(builder.toHashCode() == 37);
    }
    
    @Test
    public void testHashCodeBuilder_append_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[] {true, false, true});
        assert(builder.toHashCode() == 14711);
    }
    
    @Test
    public void testHashCodeBuilder_append_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 10);
        assert(builder.toHashCode() == 12);
    }
    
    @Test
    public void testHashCodeBuilder_append_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[] {10, 20, 30});
        assert(builder.toHashCode() == 1666129);
    }
    
    @Test
    public void testHashCodeBuilder_append_char() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assert(builder.toHashCode() == 2065);
    }
    
    @Test
    public void testHashCodeBuilder_append_charArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new char[] {'a', 'b', 'c'});
        assert(builder.toHashCode() == 1080675);
    }
    
    @Test
    public void testHashCodeBuilder_append_double() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10.5);
        assert(builder.toHashCode() == 2129566228);
    }
    
    @Test
    public void testHashCodeBuilder_append_doubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new double[] {10.5, 20.5, 30.5});
        assert(builder.toHashCode() == 1247971636);
    }
    
    @Test
    public void testHashCodeBuilder_append_float() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10.5f);
        assert(builder.toHashCode() == 1465214680);
    }
    
    @Test
    public void testHashCodeBuilder_append_floatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new float[] {10.5f, 20.5f, 30.5f});
        assert(builder.toHashCode() == 1545352876);
    }
    
    @Test
    public void testHashCodeBuilder_append_int() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10);
        assert(builder.toHashCode() == 349);
    }
    
    @Test
    public void testHashCodeBuilder_append_intArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new int[] {10, 20, 30});
        assert(builder.toHashCode() == 1131439);
    }
    
    @Test
    public void testHashCodeBuilder_append_long() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10L);
        assert(builder.toHashCode() == 349);
    }
    
    @Test
    public void testHashCodeBuilder_append_longArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new long[] {10L, 20L, 30L});
        assert(builder.toHashCode() == 1131439);
    }
    
    @Test
    public void testHashCodeBuilder_append_object() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append("test");
        assert(builder.toHashCode() == 2112);
    }
    
    @Test
    public void testHashCodeBuilder_append_objectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new String[] {"test1", "test2", "test3"});
        assert(builder.toHashCode() == 869977680);
    }
    
    @Test
    public void testHashCodeBuilder_append_short() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 10);
        assert(builder.toHashCode() == 22);
    }
    
    @Test
    public void testHashCodeBuilder_append_shortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new short[] {10, 20, 30});
        assert(builder.toHashCode() == 1131439);
    }
    
    @Test
    public void testHashCodeBuilder_appendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(50);
        assert(builder.toHashCode() == 1899);
    }
    
    @Test
    public void testHashCodeBuilder_toHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int result = builder.toHashCode();
        assert(result == 17);
    }
    
    @Test
    public void testHashCodeBuilder_build() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Integer result = builder.build();
        assert(result == 17);
    }
    
    @Test
    public void testHashCodeBuilder_hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int result = builder.hashCode();
        assert(result == 17);
    }
}