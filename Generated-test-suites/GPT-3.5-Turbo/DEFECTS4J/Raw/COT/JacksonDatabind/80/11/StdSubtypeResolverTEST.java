import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        assertNotNull(subtypeResolver._registeredSubtypes);
        assertEquals(1, subtypeResolver._registeredSubtypes.size());
        assertEquals(SubType.class, subtypeResolver._registeredSubtypes.iterator().next().getType());
    }

    @Test
    public void testRegisterSubtypesFromClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(SubType.class);
        assertNotNull(subtypeResolver._registeredSubtypes);
        assertEquals(1, subtypeResolver._registeredSubtypes.size());
        assertEquals(SubType.class, subtypeResolver._registeredSubtypes.iterator().next().getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassEmptyRegisteredSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassEmptyAnnotations() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdEmptyRegisteredSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdEmptyAnnotations() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullConfig() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(SubType.class, "SubType"));
        subtypeResolver.registerSubtypes(new NamedType(SubSubType.class, "SubSubType"));
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.getType() == SubType.class || subtype.getType() == SubSubType.class);
        }
    }

    private class SubType {}
    private class SubSubType extends SubType {}

}