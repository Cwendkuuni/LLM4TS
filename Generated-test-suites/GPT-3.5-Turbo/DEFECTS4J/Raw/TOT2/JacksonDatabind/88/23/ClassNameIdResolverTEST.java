import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.Test;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        resolver.registerSubtype(Integer.class, "Integer");
        // Test not used with class name - based resolvers
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        assertEquals("java.lang.String", resolver.idFromValue("test"));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        assertEquals("java.lang.String", resolver.idFromValueAndType("test", String.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        JavaType javaType = resolver.typeFromId(new DeserializationContext(null, null, null, null, null), "java.lang.String");
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromId_WithUnknownType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        try {
            resolver.typeFromId(new DeserializationContext(null, null, null, null, null), "com.example.UnknownType");
            fail("Should throw ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected exception
        }
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        String id = resolver._idFrom("value", String.class, TypeFactory.defaultInstance());
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}