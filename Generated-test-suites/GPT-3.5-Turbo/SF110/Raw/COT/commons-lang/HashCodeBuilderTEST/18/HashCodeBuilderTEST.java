
import org.junit.*;
import org.apache.commons.lang3.builder.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // Test with non-transients and no excluded fields
        Object object = new Object();
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object);
        int actualHashCode = HashCodeBuilderTest.ReflectionHashCode(object, false, null);
        Assert.assertEquals(expectedHashCode, actualHashCode);
        
        // Test with transients and excluded fields
        object = new MyClass();
        expectedHashCode = HashCodeBuilder.reflectionHashCode(object, true, null, "excludedField");
        actualHashCode = HashCodeBuilderTest.ReflectionHashCode(object, true, null, "excludedField");
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        
        // Test with true value
        builder.append(true);
        int expectedHashCode = 37;
        int actualHashCode = builder.toHashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
        
        // Test with false value
        builder.append(false);
        expectedHashCode = 74;
        actualHashCode = builder.toHashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }
    
    // Test other append methods
    
    // ...
    // ...
    // ...

    public static int ReflectionHashCode(Object obj, boolean testTransients, Class<? super Object> reflectUpToClass, String... excludeFields) {
        if (obj == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        Class<?> clazz = obj.getClass();
        HashCodeBuilder.reflectionAppend(obj, clazz, builder, testTransients, excludeFields);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            HashCodeBuilder.reflectionAppend(obj, clazz, builder, testTransients, excludeFields);
        }
        return builder.toHashCode();
    }

    private static class MyClass {
        private int nonTransientField;
        private transient int transientField;
        private String excludedField;
    }
}

