package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.core.TypeParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for CollectionLikeType class
 */
public class CollectionLikeTypeTest {

    @Test
    public void testConstructor() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType());
        assertEquals("CollectionOfInt", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType collectionType = CollectionLikeType.construct(javaType.getRawClass(),
                javaType.getElementType());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType,
                javaType.getElementType());
        assertEquals("CollectionOfString", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        JavaType expected = JavaType.of(Integer.class);
        JavaType actual = collectionLikeType.getContentType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        String expected = "[collection-like type; class java.util.Collection, contains class java.lang.Integer]";
        String actual = collectionLikeType.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        CollectionLikeType.construct(javaType.getRawClass(), javaType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        CollectionLikeType.upgradeFrom(javaType, javaType.getElementType());
    }

    @Test
    public void testNarrow() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        JavaType narrowType = collectionLikeType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, narrowType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        JavaType newElementType = JavaType.of(Long.class);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newElementType);
        JavaType expected = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, newElementType);
        JavaType actual = newCollectionLikeType.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        TypeDeserializer handler = new TypeDeserializer() {
            @Override
            public JavaType getType(DeserializationContext c, BeanProperty property, TypeDeserializer valueDeserializer)
                    throws IOException, JsonProcessingException {
                return JavaType.of(Long.class);
            }
        };
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
        JavaType expected = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Long.class));
        JavaType actual = newCollectionLikeType.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        TypeDeserializer handler = new TypeDeserializer() {
            @Override
            public JavaType getType(DeserializationContext c, BeanProperty property, TypeDeserializer valueDeserializer)
                    throws IOException, JsonProcessingException {
                return JavaType.of(Long.class);
            }
        };
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
        JavaType expected = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Long.class));
        JavaType actual = newCollectionLikeType.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        TypeDeserializer handler = new TypeDeserializer() {
            @Override
            public Object deserializeWithType(DeserializationContext c, BeanProperty property, TypeDeserializer valueDeserializer, TypeDeserializer typeDeserializer)
                    throws IOException, JsonProcessingException {
                return 1L;
            }
        };
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
        Object expected = 1L;
        Object actual = newCollectionLikeType.getValueHandler().deserializeWithType(null, null, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        TypeDeserializer handler = new TypeDeserializer() {
            @Override
            public Object deserializeWithType(DeserializationContext c, BeanProperty property, TypeDeserializer valueDeserializer, TypeDeserializer typeDeserializer)
                    throws IOException, JsonProcessingException {
                return 1L;
            }
        };
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
        Object expected = 1L;
        Object actual = newCollectionLikeType.getValueHandler().deserializeWithType(null, null, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Collection.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        assertTrue(newCollectionLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        JavaType refinedType = collectionLikeType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);
        JavaType expected = TypeFactory.defaultInstance()
                .constructParametricType(LinkedList.class, JavaType.of(Integer.class));
        JavaType actual = refinedType.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType1.getElementType(), null, null, false);
        JavaType javaType2 = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType2.getElementType(), null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testHashCode() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType.getElementType(), null, null, false);
        JavaType javaType2 = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawClass(),
                TypeBindings.emptyBindings(), null, null, javaType2.getElementType(), null, null, false);
        int expectedHashCode1 = collectionLikeType1.hashCode();
        int expectedHashCode2 = collectionLikeType2.hashCode();
        int actualHashCode1 = collectionLikeType1.hashCode();
        int actualHashCode2 = collectionLikeType2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
        assertNotSame(expectedHashCode1, expectedHashCode2);
    }
}