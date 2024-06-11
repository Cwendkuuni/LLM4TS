import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }
    
    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {new NamedType(String.class), new NamedType(Integer.class)};
        
        subtypeResolver.registerSubtypes(types);
        
        Assert.assertEquals(2, subtypeResolver._registeredSubtypes.size());
        Assert.assertTrue(subtypeResolver._registeredSubtypes.contains(types[0]));
        Assert.assertTrue(subtypeResolver._registeredSubtypes.contains(types[1]));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedClass() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

        Assert.assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedClass() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);

        Assert.assertNotNull(subtypes);
    }

}