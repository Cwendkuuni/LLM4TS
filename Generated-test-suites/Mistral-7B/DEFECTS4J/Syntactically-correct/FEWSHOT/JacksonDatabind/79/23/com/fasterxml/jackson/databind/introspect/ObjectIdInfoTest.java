package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo_PropertyName_Scope_Generator() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);

        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertSame(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfo_PropertyName_Scope_Generator_AlwaysAsId_Resolver() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true, resolver);

        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertSame(resolver, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfo_String_Scope_Generator() {
        String propName = "id";
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);

        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("id", propertyName.getId());
        assertEquals("id", propertyName.getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertSame(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfo_String_Scope_Generator_AlwaysAsId_Resolver() {
        String propName = "id";
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true, resolver);

        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("id", propertyName.getId());
        assertEquals("id", propertyName.getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertSame(resolver, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfo_PropertyName_Scope_Generator_AlwaysAsId() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true);

        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertSame(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfo_PropertyName_Scope_Generator_AlwaysAsId_Resolver() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true, resolver);

        assertEquals(propName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertEquals(CustomObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfo_NullPropertyName() {
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        new ObjectIdInfo(null, scope, generator);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfo_NullScope() {
        PropertyName propName = new PropertyName("id");
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        new ObjectIdInfo(propName, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfo_NullGenerator() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;

        new ObjectIdInfo(propName, scope, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfo_NullResolver() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        new ObjectIdInfo(propName, scope, generator, true, null);
    }

    @Test
    public void testObjectIdInfo_ToString() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);

        String toString = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityHashCodeObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, toString);
    }

    @Test
    public void testObjectIdInfo_ToString_AlwaysAsId() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<IdentityHashCodeObjectIdGenerator> generator = IdentityHashCodeObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, true);

        String toString = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityHashCodeObjectIdGenerator, alwaysAsId=true";
        assertEquals(expected, toString);
    }

    @Test
    public void testObjectIdInfo_CompareTo() {
        PropertyName propName1 = new PropertyName("id1");
        PropertyName propName2 = new PropertyName("id2");

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propName1, Object.class, IdentityHashCodeObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propName2, Object.class, IdentityHashCodeObjectIdGenerator.class);

        int compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(-1, compareTo);

        objectIdInfo1 = new ObjectIdInfo(propName2, Object.class, IdentityHashCodeObjectIdGenerator.class);
        compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(0, compareTo);

        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propName1, Object.class, IdentityHashCodeObjectIdGenerator.class, true);
        compareTo = objectIdInfo1.compareTo(objectIdInfo3);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testObjectIdInfo_CompareTo_SameProperties() {
        PropertyName propName = new PropertyName("id");

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propName, Object.class, IdentityHashCodeObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propName, Object.class, IdentityHashCodeObjectIdGenerator.class);

        int compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(0, compareTo);
    }

    @Test
    public void testObjectIdInfo_CompareTo_SameProperties_AlwaysAsId() {
        PropertyName propName = new PropertyName("id");

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propName, Object.class, IdentityHashCodeObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propName, Object.class, IdentityHashCodeObjectIdGenerator.class, true);

        int compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testObjectIdInfo_CompareTo_DifferentProperties() {
        PropertyName propName1 = new PropertyName("id1");
        PropertyName propName2 = new PropertyName("id2");

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propName1, Object.class, IdentityHashCodeObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propName2, Object.class, IdentityHashCodeObjectIdGenerator.class);

        int compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testObjectIdInfo_CompareTo_DifferentProperties_AlwaysAsId() {
        PropertyName propName1 = new PropertyName("id1");
        PropertyName propName2 = new PropertyName("id2");

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propName1, Object.class, IdentityHashCodeObjectIdGenerator.class, true);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propName2, Object.class, IdentityHashCodeObjectIdGenerator.class, true);

        int compareTo = objectIdInfo1.compareTo(objectIdInfo2);
        assertEquals(0, compareTo);
    }

    @Test
    public void testObjectIdInfo_CompareTo_Null() {
        PropertyName propName = new PropertyName("id");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, Object.class, IdentityHashCodeObjectIdGenerator.class);

        int compareTo = objectIdInfo.compareTo(null);
        assertNull(compareTo);
    }

    static class CustomObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdGenerator idGenerator(SerializableCarrier carrier) {
            return null;
        }

        @Override
        public ObjectId idFromString(String id) {
            return null;
        }

        @Override
        public String idToString(ObjectId id) {
            return null;
        }
    }

    static class IdentityHashCodeObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectId generateId(SerializableCarrier carrier, Object bean) {
            return new ObjectId() {
                @Override
                public String toString() {
                    return Integer.toHexString(bean.hashCode());
                }
            };
        }
    }
}