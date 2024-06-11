package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private MapperConfig<?> createConfig() {
        MapperConfig<Object> config = new MapperConfig<Object>(DefaultInitTracker.NONE);
        config.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public List<NamedType> findSubtypes(AnnotatedMember member) {
                return null;
            }

            @Override
            public List<NamedType> findSubtypes(AnnotatedClass clazz) {
                return null;
            }
        });
        return config;
    }

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?>[] classTypes = { String.class, Integer.class };
        resolver.registerSubtypes(classTypes);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(createConfig(), null, baseType);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getType(), subtype.getName());
            if (subtype.getName().equals(String.class.getName())) {
                assertEquals(String.class, subtype.getType());
            } else if (subtype.getName().equals(Integer.class.getName())) {
                assertEquals(Integer.class, subtype.getType());
            }
        }

        resolver.registerSubtypes(new NamedType[] { new NamedType(String.class), new NamedType(Integer.class) });
        subtypes = resolver.collectAndResolveSubtypesByClass(createConfig(), null, baseType);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getType(), subtype.getName());
            if (subtype.getName().equals(String.class.getName())) {
                assertEquals(String.class, subtype.getType());
            } else if (subtype.getName().equals(Integer.class.getName())) {
                assertEquals(Integer.class, subtype.getType());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedMember() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = createConfig();
        Class<?> baseType = Object.class;
        Class<?> subtypeType = String.class;
        JavaType javaType = config.getTypeFactory().constructType(JavaType.Builder.natural(subtypeType)
                .withoutParameter(0)
                .withId(Id.CLASS));

        // Register subtypes by annotated member
        AnnotatedMember property = createAnnotatedMember(String.class);
        resolver.registerSubtypes(property);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());

        // Register subtypes by class
        resolver.registerSubtypes(subtypeType);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(1, subtypes.size());
        subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotatedMember() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = createConfig();
        Class<?> baseType = Object.class;
        Class<?> subtypeType = String.class;
        JavaType javaType = config.getTypeFactory().constructType(JavaType.Builder.natural(subtypeType)
                .withoutParameter(0)
                .withId(Id.CLASS));

        // Register subtypes by annotated member
        AnnotatedMember property = createAnnotatedMember(String.class);
        resolver.registerSubtypes(property);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());

        // Register subtypes by class
        resolver.registerSubtypes(subtypeType);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(1, subtypes.size());
        subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = createConfig();
        Class<?> baseType = Object.class;
        Class<?> subtypeType = String.class;
        JavaType javaType = config.getTypeFactory().constructType(JavaType.Builder.natural(subtypeType)
                .withoutParameter(0)
                .withId(Id.CLASS));

        // Register subtypes by class
        resolver.registerSubtypes(subtypeType);
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());

        // Register subtypes by annotated member
        AnnotatedClass annotatedClass = createAnnotatedClass(String.class);
        resolver.registerSubtypes(annotatedClass);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);
        assertEquals(1, subtypes.size());
        subtype = subtypes.iterator().next();
        assertEquals(subtypeType, subtype.getType());
        assertEquals(String.class.getName(), subtype.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesNullTypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByClassNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = createConfig();
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = createConfig();
        Set<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
    }

    private AnnotatedMember createAnnotatedMember(Class<?> type) {
        return new AnnotatedMember() {
            @Override
            public AnnotatedClass getAnnotatedClass() {
                return null;
            }

            @Override
            public AnnotatedConstructor<?> getAnnouncedConstructor() {
                return null;
            }

            @Override
            public AnnotatedMethod getAnnouncedMethod() {
                return null;
            }

            @Override
            public AnnotatedField getAnnouncedField() {
                return null;
            }

            @Override
            public AnnotatedParameter getAnnotatedParameter(int i) {
                return null;
            }

            @Override
            public JavaType getRawType() {
                return null;
            }

            @Override
            public TypeDescriptor getTypeDescriptor() {
                return null;
            }

            @Override
            public int getAnnotatedMemberVisibility() {
                return Modifier.PUBLIC;
            }

            @Override
            public JavaType getMemberType() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }

            @Override
            public JavaType getMemberType(MapperConfig<?> config) {
                return null;
            }

            @Override
            public AnnotatedClass getDeclaringClass() {
                return null;
            }

            @Override
            public JavaType getType(MapperConfig<?> config) {
                return config.getTypeFactory().constructType(type);
            }

            @Override
            public JavaType getType(MapperConfig<?> config, JavaType baseType) {
                return config.getTypeFactory().constructType(type);
            }

            @Override
            public JavaType getGenericType(MapperConfig<?> config, JavaType baseType) {
                return null;
            }

            @Override
            public JavaType getRawGenericType() {
                return null;
            }

            @Override
            public String getSimpleName() {
                return "testProperty";
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return false;
            }
        };
    }

    private AnnotatedClass createAnnotatedClass(Class<?> type) {
        return new AnnotatedClass() {
            @Override
            public JavaType getDeclaredType(MapperConfig<?> config, JavaType baseType) {
                return config.getTypeFactory().constructType(type);
            }

            @Override
            public JavaType getRawType() {
                return null;
            }

            @Override
            public Class<?> getRawClass() {
                return type;
            }

            @Override
            public JavaType getType(MapperConfig<?> config) {
                return config.getTypeFactory().constructType(type);
            }

            @Override
            public JavaType getType(MapperConfig<?> config, JavaType baseType) {
                return config.getTypeFactory().constructType(type);
            }

            @Override
            public JavaType getSuperClassType(MapperConfig<?> config) {
                return Object.class;
            }

            @Override
            public JavaType[] getInterfacesAndSuperTypes() {
                return new JavaType[] { Object.class };
            }

            @Override
            public JavaType[] getInterfaces() {
                return new JavaType[0];
            }

            @Override
            public AnnotatedMember getMember(String name) {
                return null;
            }

            @Override
            public Map<String, AnnotatedMember> getMembers() {
                return new HashMap<String, AnnotatedMember>();
            }

            @Override
            public AnnotatedConstructor<?> getDeclaredConstructor(Class<?>... parameterTypes) {
                return null;
            }

            @Override
            public AnnotatedConstructor<?>[] getDeclaredConstructors() {
                return new AnnotatedConstructor[0];
            }

            @Override
            public AnnotatedMethod getDeclaredMethod(String name, Class<?>... parameterTypes) {
                return null;
            }

            @Override
            public AnnotatedMethod getDeclaredMethod(String name, Class<?> parameterType,
                    Class<?>... parameterTypes) {
                return null;
            }

            @Override
            public AnnotatedMethod[] getDeclaredMethods() {
                return new AnnotatedMethod[0];
            }

            @Override
            public AnnotatedField getDeclaredField(String name) {
                return null;
            }

            @Override
            public AnnotatedField[] getDeclaredFields() {
                return new AnnotatedField[0];
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public String getSimpleName() {
                return type.getSimpleName();
            }

            @Override
            public String getName() {
                return type.getName();
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isLocalClass() {
                return false;
            }

            @Override
            public boolean isMemberClass() {
                return false;
            }

            @Override
            public boolean isEnum() {
                return false;
            }

            @Override
            public boolean isSynthetic() {
                return false;
            }

            @Override
            public boolean isDeclaredSynthetic() {
                return false;
            }

            @Override
            public boolean isEnumConstant() {
                return false;
            }

            @Override
            public boolean isAnonymousInnerClass() {
                return false;
            }

            @Override
            public boolean isMemberNameMangled() {
                return false;
            }
        };
    }

}