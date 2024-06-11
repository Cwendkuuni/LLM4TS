import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class StdSubtypeResolverTest {
    
    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<Object> config;
    private AnnotationIntrospector ai;
    
    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return ai;
            }
        };
        ai = new AnnotationIntrospector() {
            @Override
            public Collection<NamedType> findSubtypes(AnnotatedClass annotated) {
                return null;
            }
        };
    }
    
    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = { new NamedType(String.class), new NamedType(Integer.class) };
        subtypeResolver.registerSubtypes(types);
        LinkedHashSet<NamedType> subtypes = subtypeResolver._registeredSubtypes;
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
        assertTrue(subtypes.contains(new NamedType(Integer.class)));
    }
    
    @Test
    public void testRegisterSubtypes_withClass() {
        subtypeResolver.registerSubtypes(String.class, Integer.class);
        LinkedHashSet<NamedType> subtypes = subtypeResolver._registeredSubtypes;
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
        assertTrue(subtypes.contains(new NamedType(Integer.class)));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_withProperty() {
        AnnotatedMember property = new AnnotatedMember() {
            @Override
            public Class<?> getRawType() {
                return Object.class;
            }
        };
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, null);
        assertTrue(subtypes.isEmpty());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_withType() {
        AnnotatedClass type = new AnnotatedClass() {
            @Override
            public Class<?> getRawType() {
                return Object.class;
            }
        };
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertTrue(subtypes.isEmpty());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_withProperty() {
        AnnotatedMember property = new AnnotatedMember() {
            @Override
            public Class<?> getRawType() {
                return Object.class;
            }
        };
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, null);
        assertTrue(subtypes.isEmpty());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_withType() {
        AnnotatedClass type = new AnnotatedClass() {
            @Override
            public Class<?> getRawType() {
                return Object.class;
            }
        };
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCombiningNamedAndUnnamed() {
        Class<?> rawBase = Object.class;
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        byName.put("String", new NamedType(String.class));
        typesHandled.add(Integer.class);
        typesHandled.add(Long.class);
        Collection<NamedType> subtypes = subtypeResolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class)));
        assertTrue(subtypes.contains(new NamedType(Integer.class)));
        assertTrue(subtypes.contains(new NamedType(Long.class)));
    }

}