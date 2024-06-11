package com.google.javascript.rhino.jstype;

import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(null, "reference", null, 0, 0);
        namedType.getReferencedType();
    }

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "reference", null, 0, 0);
        namedType.getReferenceName();
    }
}
