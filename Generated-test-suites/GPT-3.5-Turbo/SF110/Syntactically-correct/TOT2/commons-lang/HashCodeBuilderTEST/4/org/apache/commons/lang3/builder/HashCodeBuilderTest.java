package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCodeWithNullObject() {
        int hashCode = HashCodeBuilder.reflectionHashCode(1, 2, null);
        assertEquals(17, hashCode);
    }

    @Test
    public void testAppendBooleanTrue() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(54, hashCode);
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 10;
        builder.appendSuper(superHashCode);
        int hashCode = builder.toHashCode();
        assertEquals(47, hashCode);
    }
}