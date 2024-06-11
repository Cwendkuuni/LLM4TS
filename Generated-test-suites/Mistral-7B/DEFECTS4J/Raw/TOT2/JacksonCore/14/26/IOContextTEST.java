package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    IOContext context;
    BufferRecycler bufferRecycler;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        context = new IOContext(bufferRecycler, null, false);
    }

    

    @Test
    public void IOContextTest_Constructor() {
        // Test constructor with valid arguments
        IOContext testContext = new IOContext(bufferRecycler, null, false);
        assertNotNull(testContext);

        // Test constructor with null BufferRecycler
        try {
            new IOContext(null, null, false);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test constructor with null sourceRef
        try {
            new IOContext(bufferRecycler, null, false);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test constructor with null managedResource
        try {
            new IOContext(bufferRecycler, null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void IOContextTest_setEncoding() {
        // Test with null JsonEncoding
        try {
            context.setEncoding(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid JsonEncoding
        try {
            context.setEncoding(new JsonEncoding() {
                @Override
                public String name() {
                    return "InvalidEncoding";
                }
            });
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void IOContextTest_withEncoding() {
        // Test with null JsonEncoding
        IOContext testContext = context.withEncoding(null);
        assertSame(context, testContext);

        // Test with invalid JsonEncoding
        IOContext testContext2 = context.withEncoding(new JsonEncoding() {
            @Override
            public String name() {
                return "InvalidEncoding";
            }
        });
        assertSame(context, testContext2);
    }

    @Test
    public void IOContextTest_Getters() {
        Object sourceRef = new Object();
        JsonEncoding encoding = new JsonEncoding() {
            @Override
            public String name() {
                return "Encoding";
            }
        };
        boolean managedResource = true;

        IOContext testContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        testContext.setEncoding(encoding);

        assertEquals(sourceRef, testContext.getSourceReference());
        assertEquals(encoding, testContext.getEncoding());
        assertEquals(managedResource, testContext.isResourceManaged());
    }

    @Test
    public void IOContextTest_allocReadIOBuffer() {
        // Test with valid call
        byte[] buffer = context.allocReadIOBuffer();
        assertNotNull(buffer);

        // Test with minSize > buffer size
        buffer = context.allocReadIOBuffer(10);
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER.length, buffer.length);

        // Test buffer recycling
        context.releaseReadIOBuffer(buffer);
        byte[] recycledBuffer = context.allocReadIOBuffer();
        assertArrayEquals(buffer, recycledBuffer);
    }

    @Test
    public void IOContextTest_allocWriteEncodingBuffer() {
        // Test with valid call
        byte[] buffer = context.allocWriteEncodingBuffer();
        assertNotNull(buffer);

        // Test with minSize > buffer size
        buffer = context.allocWriteEncodingBuffer(10);
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER.length, buffer.length);

        // Test buffer recycling
        context.releaseWriteEncodingBuffer(buffer);
        byte[] recycledBuffer = context.allocWriteEncodingBuffer();
        assertArrayEquals(buffer, recycledBuffer);
    }

    @Test
    public void IOContextTest_BufferRelease() {
        byte[] buffer = context.allocReadIOBuffer();

        // Test null buf
        context.releaseReadIOBuffer(null);

        // Test buffer that is not owned by the context
        context.releaseReadIOBuffer(new byte[10]);

        // Test buffer that is owned by the context
        context.releaseReadIOBuffer(buffer);

        // Test buffer is properly released
        assertNull(context.getReadIOBuffer());
    }

    
}