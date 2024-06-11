import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {

    @Test
    public void testReferenceConstructorWithFromAndFieldName() {
        Object from = new Object();
        String fieldName = "testField";
        JsonMappingException.Reference reference = new JsonMappingException.Reference(from, fieldName);
        
        assertEquals(from, reference.getFrom());
        assertEquals(fieldName, reference.getFieldName());
    }

    @Test
    public void testReferenceConstructorWithFromAndIndex() {
        Object from = new Object();
        int index = 123;
        JsonMappingException.Reference reference = new JsonMappingException.Reference(from, index);
        
        assertEquals(from, reference.getFrom());
        assertEquals(index, reference.getIndex());
    }

    @Test
    public void testReferenceConstructorWithFromOnly() {
        Object from = new Object();
        JsonMappingException.Reference reference = new JsonMappingException.Reference(from);
        
        assertEquals(from, reference.getFrom());
    }

    @Test
    public void testWrapWithPath() {
        Throwable src = new Throwable("Test Exception");
        Object refFrom = new Object();
        String refFieldName = "testField";
        JsonMappingException.Reference ref = new JsonMappingException.Reference(refFrom, refFieldName);
        JsonMappingException jme = JsonMappingException.wrapWithPath(src, ref);
        
        assertEquals("Test Exception", jme.getMessage());
        assertTrue(jme.getPath().contains(ref));
    }

    @Test
    public void testPrependPath() {
        JsonMappingException jme = new JsonMappingException("Test Exception");
        Object refFrom = new Object();
        String refFieldName = "testField";
        JsonMappingException.Reference ref = new JsonMappingException.Reference(refFrom, refFieldName);
        
        jme.prependPath(ref);
        assertTrue(jme.getPath().contains(ref));
    }

    @Test
    public void testGetProcessor() {
        JsonMappingException jme = new JsonMappingException("Test Exception");
        Closeable processor = new Closeable() {
            @Override
            public void close() throws IOException {
                // Close
            }
        };
        
        jme._processor = processor;
        assertEquals(processor, jme.getProcessor());
    }

    @Test
    public void testGetPath() {
        JsonMappingException jme = new JsonMappingException("Test Exception");
        Object refFrom = new Object();
        String refFieldName = "testField";
        JsonMappingException.Reference ref = new JsonMappingException.Reference(refFrom, refFieldName);
        
        jme.prependPath(ref);
        assertTrue(jme.getPath().contains(ref));
    }

    @Test
    public void testGetPathReference() {
        JsonMappingException jme = new JsonMappingException("Test Exception");
        Object refFrom = new Object();
        String refFieldName = "testField";
        JsonMappingException.Reference ref = new JsonMappingException.Reference(refFrom, refFieldName);
        
        jme.prependPath(ref);
        assertEquals("java.lang.Object[testField]", jme.getPathReference());
    }
}