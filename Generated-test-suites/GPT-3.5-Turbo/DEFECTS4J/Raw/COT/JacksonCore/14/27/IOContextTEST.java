
package com.fasterxml.jackson.core.io;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF8);
        assert context.getEncoding() == JsonEncoding.UTF8;
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, false);
        IOContext newContext = context.withEncoding(JsonEncoding.UTF8);
        assert newContext != context;
        assert newContext.getEncoding() == JsonEncoding.UTF8;
    }

    @Test
    public void testGetSourceReference() {
        IOContext context = new IOContext(null, "source", false);
        assert context.getSourceReference().equals("source");
    }

    @Test
    public void testIsResourceManaged() {
        IOContext context = new IOContext(null, null, true);
        assert context.isResourceManaged();
    }

    // ... add test cases for other public methods ...

}
