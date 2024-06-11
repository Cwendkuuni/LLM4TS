
import org.junit.*;
import java.lang.reflect.Modifier;
import java.util.*;

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
        NamedType namedType = new NamedType(Object.class);
        subtypeResolver.registerSubtypes(namedType);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertTrue(subtypes.contains(namedType));
    }

    @Test
    public void testRegisterSubtypesArray() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        Class<?>[] classArray = {Object.class};
        subtypeResolver.registerSubtypes(classArray);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertTrue(subtypes.contains(new NamedType(Object.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;        
        AnnotatedClass type = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertNotNull(subtypes);
    }
}
