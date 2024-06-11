import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolverTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdTypeResolverBuilderTest {
    private StdTypeResolverBuilder typeResolverBuilder;

    @Before
    public void setUp() {
        typeResolverBuilder = new StdTypeResolverBuilder();
    }

    // Test case for init method

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType() {
        typeResolverBuilder.init(null, null);
    }

    // Test case for buildTypeSerializer method

    @Test
    public void testBuildTypeSerializer_IdTypeNone() {
        typeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(typeResolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializer_IncludeAsWrapperArray() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY)
                .typeProperty("typeId");
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_IncludeAsProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeId");
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_IncludeAsWrapperObject() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("typeId");
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_IncludeAsExternalProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY)
                .typeProperty("typeId");
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExternalTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_IncludeAsExistingProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXISTING_PROPERTY)
                .typeProperty("typeId");
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExistingPropertyTypeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializer_UnknownIncludeType() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(null);
        typeResolverBuilder.buildTypeSerializer(null, null, null);
    }

    // Test case for buildTypeDeserializer method

    @Test
    public void testBuildTypeDeserializer_IdTypeNone() {
        typeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(typeResolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer_IncludeAsWrapperArray() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY)
                .typeProperty("typeId");
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_IncludeAsProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("typeId");
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_IncludeAsWrapperObject() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("typeId");
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_IncludeAsExternalProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY)
                .typeProperty("typeId");
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsExternalTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_IncludeAsExistingProperty() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXISTING_PROPERTY)
                .typeProperty("typeId");
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializer_UnknownIncludeType() {
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(null);
        typeResolverBuilder.buildTypeDeserializer(null, null, null);
    }

    // Test case for inclusion method

    @Test(expected = IllegalArgumentException.class)
    public void testInclusion_NullIncludeType() {
        typeResolverBuilder.inclusion(null);
    }

    // Test case for typeProperty method

    @Test
    public void testTypeProperty_NullTypeIdPropName() {
        typeResolverBuilder.typeProperty(null);
        assertEquals(typeResolverBuilder.getDefaultTypeIdPropName(), typeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testTypeProperty_EmptyTypeIdPropName() {
        typeResolverBuilder.typeProperty("");
        assertEquals(typeResolverBuilder.getDefaultTypeIdPropName(), typeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testTypeProperty_ValidTypeIdPropName() {
        String typeIdPropName = "customType";
        typeResolverBuilder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, typeResolverBuilder.getTypeProperty());
    }

    // Test case for defaultImpl method

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        typeResolverBuilder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, typeResolverBuilder.getDefaultImpl());
    }

    // Test case for typeIdVisibility method

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        typeResolverBuilder.typeIdVisibility(isVisible);
        assertEquals(isVisible, typeResolverBuilder.isTypeIdVisible());
    }

}