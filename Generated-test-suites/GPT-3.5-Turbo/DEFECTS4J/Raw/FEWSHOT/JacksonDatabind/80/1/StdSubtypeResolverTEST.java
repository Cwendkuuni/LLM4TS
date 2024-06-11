package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {
    
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        NamedType namedType1 = new NamedType(Object.class);
        NamedType namedType2 = new NamedType(String.class, "string");
        
        subtypeResolver.registerSubtypes(namedType1, namedType2);
        
        assertNotNull(subtypeResolver._registeredSubtypes);
        assertEquals(2, subtypeResolver._registeredSubtypes.size());
        assertEquals(namedType1, subtypeResolver._registeredSubtypes.toArray()[0]);
        assertEquals(namedType2, subtypeResolver._registeredSubtypes.toArray()[1]);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StubMapperConfig config = new StubMapperConfig();
        StubAnnotationIntrospector ai = new StubAnnotationIntrospector();
        StubAnnotatedClass property = new StubAnnotatedClass();
        StubJavaType baseType = new StubJavaType(Object.class);
        
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver._registeredSubtypes.add(new NamedType(String.class));
        subtypeResolver._registeredSubtypes.add(new NamedType(Integer.class));
        
        assertNotNull(subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType));
        assertNotNull(subtypeResolver.collectAndResolveSubtypesByClass(config, property));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StubMapperConfig config = new StubMapperConfig();
        StubAnnotationIntrospector ai = new StubAnnotationIntrospector();
        StubAnnotatedMember property = new StubAnnotatedMember();
        StubJavaType baseType = new StubJavaType(Object.class);
        
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver._registeredSubtypes.add(new NamedType(String.class));
        subtypeResolver._registeredSubtypes.add(new NamedType(Integer.class));
        
        assertNotNull(subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType));
        assertNotNull(subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType));
    }
    
    class StubMapperConfig extends MapperConfig<StubMapperConfig> {
        public StubMapperConfig() {
            super(null);
        }
        
        public AnnotationIntrospector getAnnotationIntrospector() {
            return null;
        }
    }
    
    class StubAnnotationIntrospector extends AnnotationIntrospector {
        public Collection<NamedType> findSubtypes(AnnotatedClass annotatedClass) {
            return null;
        }
        
        public String findTypeName(AnnotatedClass annotatedClass) {
            return null;
        }
    }
    
    class StubAnnotatedClass extends AnnotatedClass {
        public StubAnnotatedClass() {
            super(null);
        }
        
        public Class<?> getRawType() {
            
        }
    }
    
    class StubAnnotatedMember extends AnnotatedMember {
        public StubAnnotatedMember() {
            super(null);
        }
        
        public JavaType getType() {
            
        }
    }
    
    class StubJavaType extends JavaType {
        public StubJavaType(Object object) {
            super(null, null);
        }
        
        public Class<?> getRawClass() {
            
        }
    }
}