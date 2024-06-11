import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.*;
import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        String expected = value.getClass().getName();
        String actual = resolver.idFromValue(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> type = String.class;
        String expected = value.getClass().getName() + "_" + type.getName();
        String actual = resolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = new ClassType(String.class);
        String id = "java.lang.String";
        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        JavaType actual = new ClassNameIdResolver(javaType, mapper.getTypeFactory())
                .typeFromId(mapper.getContext(), id);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithNullContext() throws IOException {
        JavaType javaType = new ClassType(String.class);
        String id = "java.lang.String";
        new ClassNameIdResolver(javaType, null).typeFromId(null, id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithEmptyId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = new ClassType(String.class);
        String id = "";
        new ClassNameIdResolver(javaType, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = new ClassType(String.class);
        String id = "invalidTypeId";
        new ClassNameIdResolver(javaType, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
    }

    @Test(expected = IOException.class)
    public void testTypeFromIdWithUnknownClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = new ClassType(String.class);
        String id = "com.unknown.UnknownClass";
        new ClassNameIdResolver(javaType, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
    }

}