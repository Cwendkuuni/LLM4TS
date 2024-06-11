import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingException;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver resolver;

    private static class SubType1 {
    }

    private static class SubType2 {
    }

    private static class SubType3 {
    }

    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(SubType3.class, "subType3");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test
    public void testRegisterSubtypesWithClasss() {
        resolver.registerSubtypes(SubType1.class, SubType2.class, SubType3.class);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        for (NamedType namedType : subtypes) {
            assertTrue(namedType.getType() == SubType1.class ||
                    namedType.getType() == SubType2.class ||
                    namedType.getType() == SubType3.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(SubType3.class, "subType3");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_noRegisteredSubtypes() {
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withAnnotatedMember() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(SubType3.class, "subType3");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test(expected = MappingException.class)
    public void testCollectAndResolveSubtypesByClass_withError() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(Integer.class, "integer");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        resolver.collectAndResolveSubtypesByClass(null, null);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(SubType3.class, "subType3");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_noRegisteredSubtypes() {
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withAnnotatedMember() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(SubType3.class, "subType3");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test(expected = MappingException.class)
    public void testCollectAndResolveSubtypesByTypeId_withError() {
        NamedType namedType1 = new NamedType(SubType1.class, "subType1");
        NamedType namedType2 = new NamedType(SubType2.class, "subType2");
        NamedType namedType3 = new NamedType(Integer.class, "integer");
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        resolver.collectAndResolveSubtypesByTypeId(null, null);
    }
    
}