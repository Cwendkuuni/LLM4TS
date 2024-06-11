package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingExceptionBasic() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\"]}";
        JsonMappingException e = mapper.readValue(json, MyBean.class);
        assertNotNull(e);
        assertEquals("Unexpected token (EXPECTED_BEGIN_OBJECT but was START_ARRAY)", e.getMessage());
        List<Reference> path = e.getPath();
        assertEquals(2, path.size());
        Reference ref = path.get(0);
        assertEquals("cities", ref.getFieldName());
        assertEquals("[Ljava/lang/String;", ref.getDescription());
        Reference ref1 = path.get(1);
        assertEquals("0", ref1.getIndex());
        assertEquals("name", ref1.getFieldName());
        assertEquals("MyBean", ref1.getDescription());
    }

    @Test
    public void testJsonMappingExceptionNested() throws Exception {
        String json = "{\"name\": \"John\", \"cities\": [{\"cityName\": \"New York\"}, {\"cityName\": \"Los Angeles\"}]}";
        JsonMappingException e = mapper.readValue(json, MyBeanNested.class);
        assertNotNull(e);
        assertEquals("Unexpected token (EXPECTED_END_ARRAY but was START_OBJECT)", e.getMessage());
        List<Reference> path = e.getPath();
        assertEquals(4, path.size());
        Reference ref = path.get(0);
        assertEquals("cities", ref.getFieldName());
        assertEquals("[Lcom.fasterxml.jackson.databind.test.MyBeanNested;", ref.getDescription());
        Reference ref1 = path.get(1);
        assertEquals("1", ref1.getIndex());
        assertEquals("cityName", ref1.getFieldName());
        assertEquals("Los Angeles", ref1.getDescription());
        Reference ref2 = path.get(2);
        assertEquals("name", ref2.getFieldName());
        assertEquals("MyBeanNested", ref2.getDescription());
        Reference ref3 = path.get(3);
        assertEquals("name", ref3.getFieldName());
        assertEquals("MyBean", ref3.getDescription());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithRootCause() throws Exception {
        String json = "{\"name\": \"John\", \"cities\": [\"New York\", \"Los Angeles\"], \"age\": 30}";
        mapper.readValue(json, MyBean.class);
        throw new IOException("Unexpected IO error");
    }

    @Test
    public void testJsonMappingExceptionWithRootCauseAndPath() throws Exception {
        String json = "{\"name\": \"John\", \"cities\": [\"New York\", \"Los Angeles\"], \"age\": 30}";
        JsonMappingException e = mapper.readValue(json, MyBean.class);
        assertEquals("Unexpected token (EXPECTED_END_ARRAY but was START_OBJECT)", e.getMessage());
        List<Reference> path = e.getPath();
        assertEquals(2, path.size());
        Reference ref = path.get(0);
        assertEquals("cities", ref.getFieldName());
        assertEquals("[Ljava/lang/String;", ref.getDescription());
        Reference ref1 = path.get(1);
        assertEquals("1", ref1.getIndex());
        assertEquals("name", ref1.getFieldName());
        assertEquals("MyBean", ref1.getDescription());
        Throwable rootCause = e.getRootCause();
        assertEquals("Unexpected IO error", rootCause.getMessage());
    }

    static class MyBean {
        String name;
        int age;
        List<String> cities;
    }

    static class MyBeanNested {
        String cityName;
    }
}