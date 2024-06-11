import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {
    
    private ClassNameIdResolver resolver;
    
    @Before
    public void setUp() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers, so no test case needed 
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertNotNull(id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertNotNull(id);
    }
    
    @Test
    public void testTypeFromId() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "java.lang.String";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_invalidId() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.UnknownClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_exceptionHandling() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.ExceptionThrowingClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id '"+id+"' (for id type 'Id.class')", e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_deserializationContext() {
        DatabindContext context = new DatabindContext(
            new DeserializationContext(
                new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
            )
        );
        String id = "com.example.UnknownClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNull(javaType);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_unknownClassHandler() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        ClassNameIdResolver resolver = new ClassNameIdResolver(
            TypeFactory.defaultInstance().constructType(Object.class),
            TypeFactory.defaultInstance()
        ) {
            @Override
            public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver idResolver, String failureMsg) throws IOException {
                return TypeFactory.defaultInstance().constructType(Integer.class);
            }
        };
        String id = "com.example.UnknownClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
            assertEquals(Integer.class, javaType.getRawClass());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_nestedClass() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.OuterClass$InnerClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_staticNestedClass() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.OuterClass$StaticNestedClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            assertNotNull(javaType);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_nullOuterClass() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.PrivateNestedClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id '"+id+"' (for id type 'Id.class')", e.getMessage());
        }
    }
    
    @Test
    public void testTypeFromId_privateStaticNestedClass() {
        DatabindContext context = new DeserializationContext(
            new ObjectMapper(), DeserializationConfig.Defaults.defaultInstance(), null
        );
        String id = "com.example.OuterClass$PrivateStaticNestedClass";
        try {
            JavaType javaType = resolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id '"+id+"' (for id type 'Id.class')", e.getMessage());
        }
    }
    
    @Test
    public void testDescForKnownTypeIds() {
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}