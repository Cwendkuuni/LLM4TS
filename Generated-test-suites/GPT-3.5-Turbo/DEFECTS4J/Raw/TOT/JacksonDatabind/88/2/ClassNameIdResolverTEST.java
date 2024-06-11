import static org.junit.Assert.assertEquals; 

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;
    private DatabindContext databindContext;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        databindContext = objectMapper.getDeserializationContext();
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        typeFactory = TypeFactory.defaultInstance();
        typeIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, typeIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers, nothing to test
    }

    @Test
    public void testIdFromValue() {
        String value = "Test";
        assertEquals("java.lang.String", typeIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        String value = "Test";
        Class<String> type = String.class;
        assertEquals("java.lang.String", typeIdResolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testTypeFromIdInvalid() throws IOException {
        String id = "invalid.Type";
        assertNull(typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testTypeFromIdException() throws IOException {
        String id = "java.lang.Exception";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Exception.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithException() throws IOException {
        String id = "java.lang.IllegalArgumentException";
        typeIdResolver.typeFromId(databindContext, id);
    }

    @Test
    public void testTypeFromIdNull() throws IOException {
        String id = null;
        assertNull(typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testTypeFromIdEmpty() throws IOException {
        String id = "";
        assertNull(typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(InnerClass.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }
    
    public class InnerClass {
        // Inner class for testing purposes
    }

    @Test
    public void testIdFromValueWithEnumSet() {
        EnumSet<SampleEnum> value = EnumSet.allOf(SampleEnum.class);
        String expectedId = "java.util.EnumSet<com.example.SampleEnum>";
        assertEquals(expectedId, typeIdResolver.idFromValue(value));
    }

    @Test
    public void testTypeFromIdWithEnumSet() throws IOException {
        String id = "java.util.EnumSet<com.example.SampleEnum>";
        JavaType expectedType = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, SampleEnum.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testIdFromValueWithEnumMap() {
        EnumMap<SampleEnum, String> value = new EnumMap<>(SampleEnum.class);
        String expectedId = "java.util.EnumMap<com.example.SampleEnum,java.lang.String>";
        assertEquals(expectedId, typeIdResolver.idFromValue(value));
    }

    @Test
    public void testTypeFromIdWithEnumMap() throws IOException {
        String id = "java.util.EnumMap<com.example.SampleEnum,java.lang.String>";
        JavaType expectedType = TypeFactory.defaultInstance().constructMapType(EnumMap.class, SampleEnum.class, String.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testIdFromValueWithArrayList() {
        List<String> value = new ArrayList<>();
        String expectedId = "java.util.ArrayList";
        assertEquals(expectedId, typeIdResolver.idFromValue(value));
    }

    @Test
    public void testTypeFromIdWithArrayList() throws IOException {
        String id = "java.util.ArrayList";
        JavaType expectedType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Object.class);
        assertEquals(expectedType, typeIdResolver.typeFromId(databindContext, id));
    }

    @Test
    public void testIdFromValueWithInnerClass() {
        InnerClass value = new InnerClass();
        String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass";
        assertEquals(expectedId, typeIdResolver.idFromValue(value));
    }
}

enum SampleEnum {
    VALUE1, VALUE2, VALUE3;
}