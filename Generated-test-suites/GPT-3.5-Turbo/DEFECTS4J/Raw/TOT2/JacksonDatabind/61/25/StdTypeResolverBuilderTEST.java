import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.type.NamedType;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = null; // mock config
        JavaType baseType = null; // mock base type
        Collection<NamedType> subtypes = null; // mock subtypes
        assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = null; // mock config
        JavaType baseType = null; // mock base type
        Collection<NamedType> subtypes = null; // mock subtypes
        assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class; // mock default impl
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
    
    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class; // mock default impl
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

}