
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class StdTypeResolverBuilderTest {

    @Test
    public void test_init_withValidArguments_returnsBuilderObject() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        StdTypeResolverBuilder result = builder.init(Id.CLASS, null);
        // Assert
        assertEquals(builder, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_init_withNullIdType_throwsIllegalArgumentException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        builder.init(null, null);
        // Assert - expect exception
    }

    @Test
    public void test_typeProperty_withValidTypeProperty_returnsBuilderObject() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        StdTypeResolverBuilder result = builder.typeProperty("type");
        // Assert
        assertEquals(builder, result);
    }

    @Test
    public void test_typeProperty_withNullTypeProperty_returnsBuilderObjectWithDefaultTypeProperty() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        StdTypeResolverBuilder result = builder.typeProperty(null);
        // Assert
        assertEquals(builder, result);
        assertEquals(Id.CLASS.getDefaultPropertyName(), builder.getTypeProperty());
    }

    @Test
    public void test_defaultImpl_withValidDefaultImpl_returnsBuilderObject() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        StdTypeResolverBuilder result = builder.defaultImpl(StdTypeResolverBuilder.class);
        // Assert
        assertEquals(builder, result);
    }

    @Test
    public void test_typeIdVisibility_withTrue_returnsBuilderObject() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        // Assert
        assertEquals(builder, result);
    }

    @Test
    public void test_getTypeProperty_withDefaultTypeProperty_returnsExpectedTypeProperty() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        String typeProperty = builder.getTypeProperty();
        // Assert
        assertEquals(Id.CLASS.getDefaultPropertyName(), typeProperty);
    }

    @Test
    public void test_isTypeIdVisible_withFalse_returnsFalse() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        boolean typeIdVisible = builder.isTypeIdVisible();
        // Assert
        assertFalse(typeIdVisible);
    }

    @Test
    public void test_buildTypeSerializer_withIdTypeNone_returnsNull() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(Id.NONE, null);
        // Act
        TypeSerializer result = builder.buildTypeSerializer(null, null, null);
        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void test_buildTypeSerializer_withInvalidIncludeAs_throwsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(Id.CLASS, null).inclusion(null);
        // Act
        builder.buildTypeSerializer(null, null, null);
        // Assert - expect exception
    }

    @Test
    public void test_buildTypeDeserializer_withIdTypeNone_returnsNull() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(Id.NONE, null);
        // Act
        TypeDeserializer result = builder.buildTypeDeserializer(null, null, null);
        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void test_buildTypeDeserializer_withInvalidIncludeAs_throwsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(Id.CLASS, null).inclusion(null);
        // Act
        builder.buildTypeDeserializer(null, null, null);
        // Assert - expect exception
    }

    @Test
    public void test_idResolver_withCustomIdResolver_returnsCustomIdResolver() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver customIdResolver = new CustomTypeIdResolver();
        builder._customIdResolver = customIdResolver;
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;
        // Act
        TypeIdResolver result = builder.idResolver(config, baseType, subtypes, forSer, forDeser);
        // Assert
        assertEquals(customIdResolver, result);
    }

    private class CustomTypeIdResolver implements TypeIdResolver {
		@Override
		public void init(JavaType baseType) {
			
		}

		@Override
		public String idFromValue(Object value) {
			return null;
		}

		@Override
		public String idFromBaseType() {
			return null;
		}

		@Override
		public String idFromValueAndType(Object value, Class<?> suggestedType) {
			return null;
		}

		@Override
		public JavaType typeFromId(DatabindContext context, String id) {
			return null;
		}

		@Override
		public String getDescForKnownTypeIds() {
			return null;
		}

		@Override
		public JsonTypeInfo.Id getMechanism() {
			return null;
		}
	}
}

