import com.fasterxml.jackson.databind.*;

public class JsonMappingExceptionTest {

    @org.junit.Test
    public void testGetPath() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertEquals(0, e.getPath().size());
    }

    @org.junit.Test
    public void testGetPathReference() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertEquals("", e.getPathReference());
    }

    @org.junit.Test
    public void testPrependPath() {
        JsonMappingException e = new JsonMappingException("Test");
        Object referrer = new Object();
        String fieldName = "fieldName";
        e.prependPath(referrer, fieldName);
        org.junit.Assert.assertEquals(1, e.getPath().size());
        org.junit.Assert.assertEquals(referrer, e.getPath().get(0).getFrom());
        org.junit.Assert.assertEquals(fieldName, e.getPath().get(0).getFieldName());
    }

    @org.junit.Test
    public void testGetProcessor() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertNull(e.getProcessor());
    }

    @org.junit.Test
    public void testGetLocalizedMessage() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertEquals("Test", e.getLocalizedMessage());
    }

    @org.junit.Test
    public void testGetMessage() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertEquals("Test", e.getMessage());
    }

    @org.junit.Test
    public void testToString() {
        JsonMappingException e = new JsonMappingException("Test");
        org.junit.Assert.assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Test", e.toString());
    }
}