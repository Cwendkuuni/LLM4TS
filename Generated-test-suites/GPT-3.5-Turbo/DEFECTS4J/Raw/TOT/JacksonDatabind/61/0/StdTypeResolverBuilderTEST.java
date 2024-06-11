import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("$class", builder._typeProperty);
    }
    
    @Test
    public void testInit_NullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBuildTypeSerializer_NoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        assertNull(builder.buildTypeSerializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeSerializer_WrapperArrayType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        // Additional test cases for Wrapper Array TypeSerializer can be added
    }
    
    @Test
    public void testBuildTypeSerializer_PropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        // Additional test cases for Property TypeSerializer can be added
    }
    
    @Test
    public void testBuildTypeSerializer_WrapperObjectType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        // Additional test cases for Wrapper Object TypeSerializer can be added
    }
    
    @Test
    public void testBuildTypeSerializer_ExternalPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        // Additional test cases for External Property TypeSerializer can be added
    }
    
    @Test
    public void testBuildTypeSerializer_ExistingPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        // Additional test cases for Existing Property TypeSerializer can be added
    }
    
    @Test
    public void testBuildTypeSerializer_UnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        
        try {
            builder.buildTypeSerializer(null, null, null);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: null", e.getMessage());
        }
    }
    
    @Test
    public void testBuildTypeDeserializer_NoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeDeserializer_WrapperArrayType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        // Additional test cases for Wrapper Array TypeDeserializer can be added
    }
    
    @Test
    public void testBuildTypeDeserializer_PropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        // Additional test cases for Property TypeDeserializer can be added
    }
    
    @Test
    public void testBuildTypeDeserializer_WrapperObjectType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        // Additional test cases for Wrapper Object TypeDeserializer can be added
    }
    
    @Test
    public void testBuildTypeDeserializer_ExternalPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        // Additional test cases for External Property TypeDeserializer can be added
    }
    
    @Test
    public void testBuildTypeDeserializer_ExistingPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        // Additional test cases for Existing Property TypeDeserializer can be added
    }
    
    @Test
    public void testBuildTypeDeserializer_UnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);
        
        try {
            builder.buildTypeDeserializer(null, null, null);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: null", e.getMessage());
        }
    }
    
    // Additional test cases for other methods can be added
    
}