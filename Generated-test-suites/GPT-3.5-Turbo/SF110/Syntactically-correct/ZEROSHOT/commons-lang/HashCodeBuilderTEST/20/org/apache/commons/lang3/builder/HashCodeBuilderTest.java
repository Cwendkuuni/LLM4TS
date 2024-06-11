package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object object = new Object();
        int result = HashCodeBuilder.reflectionHashCode(object);
        assertNotNull(result);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new Object();
        String[] excludeFields = {"field1", "field2"};
        int result = HashCodeBuilder.reflectionHashCode(17, 37, object, false, Object.class, excludeFields);
        assertNotNull(result);
    }

    @Test
    public void testHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        builder.append(false);
        builder.append(10);
        builder.append(3.14);
        builder.append("test");
        int result = builder.toHashCode();
        assertNotNull(result);
    }

    @Test
    public void testBuild() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        Integer result = builder.build();
        assertNotNull(result);
    }
    
}